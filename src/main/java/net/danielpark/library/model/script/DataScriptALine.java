package net.danielpark.library.model.script;

import java.util.ArrayList;

/**
 * Created by Daniel Park on 2017. 12. 14..
 */

public class DataScriptALine {
    public final String Script;
    public final long StartOffset;
    public final long TrimmedEndOffset;
    public final long EndOffset;

    public int[] mSpaceIndices;
    public DataScriptALine( String script, long startOffset, long trimedEndOffset, long endOffset ) {
        Script      = script;
        StartOffset = startOffset;
        TrimmedEndOffset    = trimedEndOffset;
        EndOffset   = endOffset;

        ArrayList<Integer> spaces = new ArrayList<>();
        for( int i=0; i<Script.length(); i++ ) {
            if( Script.charAt(i) == ' ' ) {
                spaces.add(i);
            }
        }
        mSpaceIndices = new int[spaces.size()];
        for( int i=0; i<spaces.size(); i++ ) {
            mSpaceIndices[i] = spaces.get(i);
        }
    }

    public int[] findNextSpaceIndexes( int minIndex ) {
        int index;
        for( int i=0; i<mSpaceIndices.length; i++ ) {
            index = mSpaceIndices[i];
            if( index > minIndex ) {
                if( i > 0 ) {
                    return new int[]{ mSpaceIndices[i-1], index};
                }
                else {
                    return new int[]{0, index};
                }
            }
        }

        if( mSpaceIndices.length > 0 ) {
            return new int[]{ mSpaceIndices[mSpaceIndices.length-1], Script.length() };
        }
        else {
            return new int[]{0, Script.length()};
        }
    }

    public float getRate( float currentOffset ) {
        return (currentOffset-StartOffset) / (float)(EndOffset-StartOffset);
    }
}
