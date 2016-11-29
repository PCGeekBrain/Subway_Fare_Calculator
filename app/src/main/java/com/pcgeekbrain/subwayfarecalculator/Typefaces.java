package com.pcgeekbrain.subwayfarecalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by Mendel on 11/28/2016.
 * Carbon copied from https://code.google.com/p/android/issues/detail?id=9904#c7
 * Supposed to handle memory leak from making multiple calls to asset file
 * Comments are me trying to figure it out
 */

class Typefaces {
    private static final String TAG = "TypeFaces";
    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    static Typeface get(Context c, String assetPath){
        synchronized (cache){
            if (!cache.containsKey(assetPath)){
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e){
                    Log.e(TAG, "Could not get Typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}
