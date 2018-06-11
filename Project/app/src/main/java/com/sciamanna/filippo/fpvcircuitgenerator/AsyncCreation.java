package com.sciamanna.filippo.fpvcircuitgenerator;

import android.app.Activity;
import android.graphics.Path;
import android.os.AsyncTask;

/**
 * This Async class generates and draws a random circuit
 */

public class AsyncCreation extends AsyncTask<Void,Void,Path> {

    private Activity activity;
    private int flags;
    private int gates;

    public AsyncCreation(Activity activity, int flags, int gates) {
        this.activity = activity;
        this.flags = flags;
        this.gates = gates;
    }

    @Override
    protected Path doInBackground(Void... voids) {

        return null;
    }
}
