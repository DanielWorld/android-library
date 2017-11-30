package net.danielpark.library.util;

import android.graphics.Bitmap;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import net.danielpark.library.log.Logger;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Daniel Park on 2017. 11. 15..
 */

public final class BitmapCalc {
    private static final String TAG = BitmapCalc.class.getSimpleName();

    @NonNull
    private final ArrayBlockingQueue<Bitmap> queue;

    @NonNull
    private final Executor backgroundExecutor;

    private Disposable disposable;

    public BitmapCalc() {
        queue = new ArrayBlockingQueue<Bitmap>(1, true);
        backgroundExecutor = Executors.newCachedThreadPool();
    }

    /**
     * Get most used pixels from bitmap
     * <p>TODO: Need to improve performance.</p>
     *
     * @param originalBitmap    original bitmap to get most used pixels
     * @param numbers           get number's pixels
     * @param listener          listener
     */
    @MainThread
    public void getColors(Bitmap originalBitmap, final int numbers, final OnBitmapCalcListener listener) {

        synchronized (queue) {
            // Check if executes same task several times.
            if (!queue.isEmpty() && queue.peek() != originalBitmap) {
                queue.clear();
            }

            if (queue.isEmpty()) {
                // execute task !
                queue.offer(originalBitmap);

                if (disposable != null && !disposable.isDisposed())
                    disposable.dispose();

                disposable = (Single.create(new SingleOnSubscribe<Model>() {
                    @Override
                    public void subscribe(SingleEmitter<Model> e) throws Exception {
                        if (e.isDisposed()) return;

                        long taskDuration = System.currentTimeMillis();
                        Logger.d(TAG, Thread.currentThread() + " Start: " + DateUtil.getDate(taskDuration));

                        Bitmap targetBitmap = queue.peek();

                        // TODO: Too much overhead, calculation should be other thread
                        int oriWidth = targetBitmap.getWidth();
                        int oriHeight = targetBitmap.getHeight();

                        Bitmap newB;
                        // Daniel (2017-11-15 16:59:53) : To reduce calculation duration, reduce bitmap size temporary.
                        if (oriWidth > 300 || oriHeight > 300) {
                            int maxSize = Math.max(oriWidth, oriHeight);

                            oriWidth = 300 * oriWidth / maxSize;
                            oriHeight = 300 * oriHeight / maxSize;

                            newB = Bitmap.createScaledBitmap(targetBitmap, oriWidth, oriHeight, false);
                        }
                        else {
                            newB = targetBitmap.copy(targetBitmap.getConfig(), true);
                        }

                        List<int[]> result = ColorThief.compute(newB, numbers);

                        if (e.isDisposed()) return;

                        Logger.v(TAG, Thread.currentThread() + " End: " + DateUtil.getDate());
                        Logger.i(TAG, Thread.currentThread() + " Duration: " + (System.currentTimeMillis() - taskDuration));
                        Logger.i(TAG, Thread.currentThread() + " reduced size: " + oriWidth + " / " + oriHeight);

                        if (!e.isDisposed())
                            e.onSuccess(new Model(result));

                        queue.poll();
                    }
                })
                        .subscribeOn(Schedulers.from(backgroundExecutor))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Model>() {
                            @Override
                            public void accept(Model model) throws Exception {
                                if (listener != null)
                                    listener.onSuccess(model);
                            }
                        })
                );

            } else {
                // Failed to execute.
                if (listener != null)
                    listener.onFailure();
            }
        }
    }

    public static class Model {
        public final List<int[]> colors;

        public Model(List<int[]> colors) {
            this.colors = colors;
        }
    }

    public interface OnBitmapCalcListener {
        void onSuccess(@NonNull Model model);

        void onFailure();
    }
}
