package com.example.ziad.algorithmicmusicplayer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path_audio = "/storage/emulated/legacy/Audio/";
        String path_piano_notes = path_audio+"pianonotes_notloud/";

        Instrument piano_right = new Instrument("piano_right");
        piano_right.setPath_to_notes(path_piano_notes);
        piano_right.setBar("A5i C6i D6h rq A6i C6i G6i F6i E6i A5i D6i C6i A5i C6i D6h rq A6i C6i G6i F6i E6i A5i D6i C6i A5i C6i D6i A5i C6i D6i C6i A5i A6i C6i G6i F6i E6i A5i D6i C6i D5i F5i A5i F5i A5i D6i A5i D6i F6i D6i F6i D7w");

        Instrument piano_left = new Instrument("piano_left");
        piano_left.setBar("D4w F4h C4h D4w A#3h A3h D4w F4h C4h D4w");
        piano_left.setPath_to_notes(path_piano_notes);

        Player p = new Player(this.getApplicationContext());
        p.addInstrument(piano_right);
        p.addInstrument(piano_left);

        //Create a Wav File of the song
        p.produceMusic(path_audio+"song.wav");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
