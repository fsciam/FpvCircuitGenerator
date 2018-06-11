package com.sciamanna.filippo.fpvcircuitgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;
import android.widget.Toast;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity {

    private int gates=0;
    private int flags=0;
    private NumberPicker gates_picker;
    private NumberPicker flags_picker;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //load menu using MenuInflater
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.settings)
            this.startActivity(new Intent(this,SettingsActivity.class));

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gates_picker=findViewById(R.id.gates_number);
        flags_picker=findViewById(R.id.flags_number);


        gates_picker.setMin(0);
        gates_picker.setMax(10);

        flags_picker.setMin(3);
        flags_picker.setMax(10);
        flags_picker.setValue(3);


        flags_picker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                flags=value;
            }
        });

        gates_picker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                gates=value;
            }
        });

        gates=gates_picker.getValue();
        flags=flags_picker.getValue();
    }

    /**
     * On click on generate button the method launch an AsyncTask that generate and draws a racing track
     * @param view view passed by the caller
     */
    public void generateTrack(View view) {


            Toast.makeText(this,"gates:"+gates+"\nflags:"+flags,Toast.LENGTH_LONG).show();
            new AsyncCreation(this,flags,gates).execute();
    }
}
