package net.danielpark.library.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.support.annotation.RawRes;

import net.danielpark.library.log.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Daniel Park on 2017. 12. 2..
 */

@TargetApi(16)
public final class AudioStreamPlayer {
    private final String TAG = AudioStreamPlayer.class.getSimpleName();

    private Context context;

    private File mAudioFile;

    private MediaExtractor mExtractor = null;
    private MediaCodec mMediaCodec = null;
    private AudioTrack mAudioTrack = null;

    private int mInputBufIndex = 0;

    private State mState = State.Stopped;

    private boolean isForceStop = false;
    private volatile boolean isPause = false;
    private boolean isSeek = false;
    private long mSeekTimeMilliSec = 0;
    private int mPlayingTime   = 0;
    private boolean isReleased = false;

    private ArrayList<byte[]> mArray = new ArrayList<>();

    private AudioStreamPlayerListener mListener;

    public AudioStreamPlayer(Context context) {
        this.context = context;
    }

    public AudioStreamPlayer init(@RawRes int rawId) {
        InputStream in = context.getResources().openRawResource(rawId);
        File audioFile = new File(context.getExternalCacheDir() , "temp_audio.mp3");
        copyInputStreamToFile(in, audioFile);

        mAudioFile = audioFile;
        return this;
    }

    /**
     * It might be request {@link android.Manifest.permission#READ_EXTERNAL_STORAGE}
     * @param audioFile
     */
    public AudioStreamPlayer init(File audioFile) {
        mAudioFile = audioFile;
        return this;
    }

    public void play(long seekTo) {
        mState = State.Prepare;
        isForceStop = false;

        if (mListener != null)
            mListener.onAudioStreamPlayerBuffering(this);

        if (seekTo > 0) {
            seekTo(seekTo);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                decodeLoop();
            }
        }).start();
    }

    public void pause() {
        isPause = true;
    }

    public void resume() {
        isPause = false;

        if (mListener != null)
            mListener.onAudioStreamPlayerResume(this);
    }

    public void stop() {
        isForceStop = true;
    }


    public State getPlayerState() {
        return mState;
    }

    private void decodeLoop() {
        ByteBuffer[] codecInputBuffers;
        ByteBuffer[] codecOutputBuffers;

        if (mExtractor == null)
            mExtractor = new MediaExtractor();

        try {
            mExtractor.setDataSource(mAudioFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();

            if (mListener != null)
                mListener.onAudioStreamPlayerError(this);

            return;
        }

        MediaFormat format = mExtractor.getTrackFormat(0);
        int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        String mime = format.getString(MediaFormat.KEY_MIME);
        long duration = format.getLong(MediaFormat.KEY_DURATION);
        long totalMilliseconds = duration / 1000;

        int totalSec = (int) (duration / 1000 / 1000);
        int min = totalSec / 60;
        int sec = totalSec % 60;

        if (mListener != null)
            mListener.onAudioStreamPlayerDuration(totalMilliseconds);

        Logger.d(TAG, "Time = " + min + " : " + sec);
        Logger.d(TAG, "Duration = " + duration);

        try {
            mMediaCodec = MediaCodec.createDecoderByType(mime);
        } catch (IOException e) {
            e.printStackTrace();

            if (mListener != null)
                mListener.onAudioStreamPlayerError(this);

            return;
        }

        mMediaCodec.configure(format, null, null, 0);
        mMediaCodec.start();

        codecInputBuffers = mMediaCodec.getInputBuffers();
        codecOutputBuffers = mMediaCodec.getOutputBuffers();

        int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

        Logger.i(TAG, "MIME = " + mime);
        Logger.i(TAG, "Sample Rate = " + sampleRate);

        int channelConfig = (channelCount == 1)
                ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;

        int bufferSizeInBytes = AudioTrack.getMinBufferSize(
                sampleRate, channelConfig, AudioFormat.ENCODING_PCM_16BIT
        );
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);

        mAudioTrack.play();
        mExtractor.selectTrack(0);

        final long kTimeOutUs = 10000;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        boolean sawInputEOS = false;
        int noOutputCounter = 0;
        int noOutputCounterLimit = 50;

        while (!sawInputEOS && noOutputCounter < noOutputCounterLimit && !isForceStop) {

            if (!sawInputEOS) {

                if (isPause) {
                    if (mState != State.Pause) {
                        mState = State.Pause;

                        if (mListener != null)
                            mListener.onAudioStreamPlayerPause(this);
                    }
                    continue;
                }

                noOutputCounter++;

                if (isSeek) {
                    mExtractor.seekTo(
                            mSeekTimeMilliSec * 1000, MediaExtractor.SEEK_TO_CLOSEST_SYNC
                    );
                    isSeek = false;
                }

                mInputBufIndex = mMediaCodec.dequeueInputBuffer(kTimeOutUs);

                if (mInputBufIndex >= 0) {
                    ByteBuffer dstBuf = codecInputBuffers[mInputBufIndex];

                    int sampleSize = mExtractor.readSampleData(dstBuf, 0);

                    long presentationTimeUs = 0;

                    if (sampleSize < 0) {
                        sawInputEOS = true;
                        sampleSize = 0;
                    }
                    else {
                        presentationTimeUs = mExtractor.getSampleTime();

                        mPlayingTime = (int) (presentationTimeUs / 1000);

                        if (mListener != null)
                            mListener.onAudioStreamPlayerCurrentTime(mPlayingTime);
                    }

                    mMediaCodec.queueInputBuffer(
                            mInputBufIndex, 0, sampleSize, presentationTimeUs,
                            sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0
                    );

                    if (!sawInputEOS)
                        mExtractor.advance();
                }
            }

            int res = mMediaCodec.dequeueOutputBuffer(info, kTimeOutUs);

            if (res >= 0) {

                if (info.size > 0) {
                    noOutputCounter = 0;
                }

                int outputBufIndex = res;
                ByteBuffer buf = codecOutputBuffers[outputBufIndex];

                final byte[] chunk = new byte[info.size];
                buf.get(chunk);
                buf.clear();

                if (chunk.length > 0) {

                    if (mListener != null)
                        mListener.onAudioStreamPlayerAnalyze(chunk);

                    mArray.add(chunk);

//                    mAudioTrack.write(chunk, 0, chunk.length);

                    if (mState != State.Playing) {
                        if (mListener != null)
                            mListener.onAudioStreamPlayerStart(this);
                    }

                    mState = State.Playing;
                }

                mMediaCodec.releaseOutputBuffer(outputBufIndex, false);
            }
            else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                codecOutputBuffers = mMediaCodec.getOutputBuffers();

                Logger.d(TAG, "output buffers have changed.");
            }
            else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                MediaFormat oformat = mMediaCodec.getOutputFormat();

                Logger.d(TAG, "output format has changed to " + oformat);
            }
            else {
                Logger.d(TAG, "dequeueOutputBuffer returned " + res);
            }
        }

        if (mListener != null)
            mListener.onAudioStreamPlayerAnalyzeComplete();

        // After analyzing all mp3 file, time to play by buffer!
        // TODO: Do we analyze mp3 file asynchronously? Come on!!!
        while (mArray.size() > 0) {
            if (isReleased) break;

            byte[] chunk = mArray.remove(0);

            if (mListener != null)
                mListener.onAudioStreamPlayerNarrate(chunk);

            mAudioTrack.write(chunk, 0, chunk.length);
        }

        releaseResources(true);

        mState = State.Stopped;
        isForceStop = true;

        if (noOutputCounter >= noOutputCounterLimit) {
            if (mListener != null)
                mListener.onAudioStreamPlayerError(this);
        }
        else {
            if (mListener != null)
                mListener.onAudioStreamPlayerStop(this);
        }
    }

    private void seekTo(long seekTo) {
        isSeek = true;
        mSeekTimeMilliSec = seekTo;
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        Logger.d(TAG, "start copying mp3 file to " + file.getAbsolutePath());
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if (out != null) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Logger.d(TAG, "copied mp3 file");
    }

    private void releaseResources(boolean isReleased) {
        if (mExtractor != null) {
            mExtractor.release();
            mExtractor = null;
        }

        if (mMediaCodec != null) {
            if (isReleased) {
                mMediaCodec.stop();
                mMediaCodec.release();
                mMediaCodec = null;
            }
        }

        if (mAudioTrack != null) {
            mAudioTrack.flush();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    public enum State {
        Stopped, Prepare, Buffering, Playing, Pause
    }

    public void setAudioStreamPlayerListener(AudioStreamPlayerListener listener) {
        this.mListener = listener;
    }

    public interface AudioStreamPlayerListener {

        void onAudioStreamPlayerStart(AudioStreamPlayer player);

        void onAudioStreamPlayerPause(AudioStreamPlayer player);

        void onAudioStreamPlayerResume(AudioStreamPlayer player);

        void onAudioStreamPlayerStop(AudioStreamPlayer player);

        void onAudioStreamPlayerError(AudioStreamPlayer player);

        void onAudioStreamPlayerBuffering(AudioStreamPlayer player);

        void onAudioStreamPlayerDuration(long milliseconds);

        void onAudioStreamPlayerCurrentTime(long milliseconds);

        void onAudioStreamPlayerAnalyze(byte[] bytes);

        void onAudioStreamPlayerAnalyzeComplete();

        void onAudioStreamPlayerNarrate(byte[] bytes);
    }
}
