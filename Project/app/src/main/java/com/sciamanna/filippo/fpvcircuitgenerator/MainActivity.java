package com.sciamanna.filippo.fpvcircuitgenerator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.travijuu.numberpicker.library.NumberPicker;


import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private NumberPicker turns_picker;
    private SeekBar spykeness;
    private SeekBar irregularity;
    private ImageButton previous;
    private ImageButton next;
    private ImageView circuit;
    private ArrayList<Path> pathArrayList;
    private int current_circuit;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //load menu using MenuInflater
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.help)
        {
            //Create an alert dialog that contains all the info
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Help");
            alertDialog.setMessage(getResources().getString(R.string.help_message));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //Load a custom layout for the action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.custom_titlebar_layout, null);

        actionBar.setCustomView(customNav, lp1);

        //Create various object
        turns_picker=findViewById(R.id.turns_number);
        spykeness=findViewById(R.id.spyke_seek);
        irregularity=findViewById(R.id.irregular_seek);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        circuit =findViewById(R.id.circuit);

        //Set a blank bitmap inside the imageview
        Bitmap bitmap = Bitmap.createBitmap(377, 314, Bitmap.Config.ARGB_8888);
        circuit.setImageBitmap(bitmap);

        //Set various options
        turns_picker.setMin(3);
        turns_picker.setMax(50);
        turns_picker.setValue(3);

        spykeness.setMax(10);
        spykeness.setProgress(0);

        irregularity.setMax(10);
        spykeness.setProgress(0);
        pathArrayList=new ArrayList<>();

        next.setActivated(false);
        previous.setActivated(false);

        current_circuit=0;
    }

    /**
     * On click on generate button the method launch an AsyncTask that generate and draws a racing track
     * @param view circuit passed by the caller
     */
    public void generateTrack(View view) {

            new AsyncCreation(this,turns_picker.getValue(),189,157,spykeness.getProgress(),irregularity.getProgress()/10,75).execute();
    }

    /**
     * Adds the last path generated to the list of generated path. The list can contain a maximum of 50 paths
     * @param path path to be added
     */
    public void addPath(Path path)
    {
        if(pathArrayList.size()==50)
            pathArrayList.remove(0);
        pathArrayList.add(path);
        current_circuit=pathArrayList.size()-1;
        previous.setActivated(true);
        next.setActivated(false);
    }

    /**
     * Draws the selected path inside the ImageView
     * @param path path to be drawn
     */
    private void drawPath(Path path)
    {
        Bitmap bitmap = Bitmap.createBitmap(377, 314, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPath(path,paint);
        circuit.setImageBitmap(bitmap);
    }

    /**
     * Draws the previous circuit, if it exists
     * @param view view that calls the method
     */
    public void previousCircuit(View view) {

        if(current_circuit!=0)
        {

            current_circuit-=1;
            drawPath(pathArrayList.get(current_circuit));
        }
        if(current_circuit==0)
            previous.setActivated(false);
        else
            previous.setActivated(true);
    }
    /**
     * Draws the next circuit, if it exists
     * @param view view that calls the method
     */
    public void nextCircuit(View view) {

        if(current_circuit+1<pathArrayList.size())
        {
            current_circuit+=1;
            drawPath(pathArrayList.get(current_circuit));
        }
        if(current_circuit==pathArrayList.size()-1)
            next.setActivated(false);
        else
            next.setActivated(true);
    }
}
