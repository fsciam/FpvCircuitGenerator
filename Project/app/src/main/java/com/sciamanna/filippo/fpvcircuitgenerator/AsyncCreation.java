package com.sciamanna.filippo.fpvcircuitgenerator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.jjoe64.graphview.series.DataPoint;

import java.lang.ref.WeakReference;
import java.util.Random;

import static java.lang.Math.abs;


/**
 * This Async class generates and draws a random circuit
 */

public class AsyncCreation extends AsyncTask<Void,Void,Path> {

    private final int center_y;
    private final int center_x;
    private final float radius;
    private double spyke;
    private double irregularity;
    private WeakReference<Activity> activity;
    private DataPoint[] graph_point;
    private int turns;
    private boolean intersection;


    public AsyncCreation(Activity activity, int turns, int center_x,int center_y,double spyke, double irregularity,float radius) {
        this.activity = new WeakReference<>(activity);
        this.turns =turns;
        this.center_x=center_x;
        this.center_y=center_y;
        this.spyke =spyke;
        this.irregularity=irregularity;
        graph_point = new DataPoint[turns];
        this.radius=radius;

    }


    @Override
    protected Path doInBackground(Void... voids) {

        Path path=new Path();
        irregularity = clip( irregularity, 0,1 ) * 2*Math.PI / (float)turns;
        spyke = clip(spyke, 0,1 ) * radius;
        double angleSteps[]=new double[turns];
        double lower = (2*Math.PI / turns) - irregularity;
        double upper = (2*Math.PI / turns) + irregularity;
        System.out.println("lower "+lower+" upper "+upper);
        double sum = 0;
        Random random=new Random();
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
            double r_i = clip( random.nextGaussian()* spyke +radius, 30,2*radius);
            double x = center_x + r_i*Math.cos(angle);
            double y = 351.0-(center_y + r_i*Math.sin(angle));
            if(i==0)
                path.moveTo((float)x,(float)y);
            else
                path.lineTo((float)x,(float)y);

            angle = angle + angleSteps[i];
        }
        path.close();
        return path;
    }

    private double clip(double x, double min, double max) {
        if( min > max )   return x;
        else if( x < min )  return min;
        else if( x > max ) return max;
     else           return x;
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
}
