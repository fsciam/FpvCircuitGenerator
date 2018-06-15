package com.sciamanna.filippo.fpvcircuitgenerator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.widget.ImageView;



import java.lang.ref.WeakReference;
import java.util.Random;




/**
 * This Async class generates and draws a random circuit
 */

public class AsyncCreation extends AsyncTask<Void,Void,Path> {

    private final int cY;
    private final int cX;
    private final float averageRadius;
    private double spykeness;
    private double irregularity;
    private WeakReference<Activity> activity;
    private int turns;


    /**
     * Constructor
     * @param activity activity that launch the async task
     * @param turns number of turns
     * @param cX x coordinate of the center
     * @param cY y coordinate of the center
     * @param spykeness parameter that express narrow turns density
     * @param irregularity parameter that express how much segments' length has to be irregular
     * @param averageRadius average distance from the center
     */
    public AsyncCreation(Activity activity, int turns, int cX, int cY, double spykeness, double irregularity, float averageRadius) {
        this.activity = new WeakReference<>(activity);
        this.turns =turns;
        this.cX = cX;
        this.cY = cY;
        this.spykeness =spykeness;
        this.irregularity=irregularity;
        this.averageRadius = averageRadius;

    }


    @Override
    protected Path doInBackground(Void... voids) {

        Path path=new Path();
        irregularity = clip( irregularity, 0,1 ) * 2*Math.PI / (float)turns;
        spykeness = clip(spykeness, 0,10 ) * averageRadius;

        double lower = (2*Math.PI / turns) - irregularity;
        double upper = (2*Math.PI / turns) + irregularity;

        double sum = 0;
        Random random=new Random();

        double[]  angleSteps=new double[turns];
        for(int i=0;i<turns;i++)
        {
            double tmp = random.nextDouble()*(upper - lower+ 1) + lower;
            angleSteps[i]= tmp;
            sum = sum + tmp;
        }

        double k = sum / (2*Math.PI);
        for(int i=0;i<turns;i++)
        {

            angleSteps[i]= angleSteps[i]/k;

        }

        double angle = random.nextDouble()*2*Math.PI;
        for(int i=0;i<turns;i++)
        {
            double randomRadius = clip( random.nextGaussian()* spykeness + averageRadius, 10,2* averageRadius);
            double x = cX + randomRadius*Math.cos(angle);
            double y = 314.0-(cY + randomRadius*Math.sin(angle));
            if(i==0)
                path.moveTo((float)x,(float)y);
            else
                path.lineTo((float)x,(float)y);

            angle = angle + angleSteps[i];
        }
        path.close();
        return path;
    }



    @Override
    protected void onPostExecute(Path path) {
        if(activity.get() instanceof MainActivity)
        {
            ((MainActivity) activity.get()).addPath(path);
        }
        ImageView graph=activity.get().findViewById(R.id.circuit);

        Bitmap bitmap = Bitmap.createBitmap(377, 314, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPath(path,paint);
        graph.setImageBitmap(bitmap);



    }

    /**
     * This method allow to bound a variable x
     * @param x variable
     * @param min minimum
     * @param max maximum
     * @return x if min<=x<=max, max if x>max and min if x<min
     */
    private double clip(double x, double min, double max) {
        if( min > max )
            return x;
        else if( x < min )
            return min;
        else if( x > max )
            return max;
        else
            return x;
    }
}
