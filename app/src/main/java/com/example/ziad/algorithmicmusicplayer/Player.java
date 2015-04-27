package com.example.ziad.algorithmicmusicplayer;
import android.content.Context;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by ziad on 22/04/15.
 */
public class Player {

    private Context context;
    private FFmpeg ffmpeg;
    private ArrayList<Instrument> instruments = new ArrayList<Instrument>();

    public Player(Context context){
        this.context = context;
        setupFFmpeg();
    }

    public void addInstrument(Instrument instrument){
        if(instrument!=null) instruments.add(instrument);
    }

    public ArrayList<Instrument> getInstruments(){
        return instruments;
    }

    public String produceMusic(String path_output){
        if(path_output==null || path_output.isEmpty() ) return "Invalid Path";
        if(instruments.isEmpty()) return "No instruments are added";

        //Create temporary Wav files for each instrument
        String[] temp_output_file_path = new String[instruments.size()];
        for(int i=0; i<instruments.size();i++){
            String temp = context.getCacheDir()+"temp"+i+".wav";
            temp_output_file_path[i] = temp;
            create_instrument_wav_file(instruments.get(i), temp);
        }
        //Merge All the temporary Wav files
        merge_wave_files(temp_output_file_path,path_output);

        //Clear temporary files
        CacheManager.trimCache(context);
        return "Done";
    }
    private void create_instrument_wav_file(Instrument instrument, String output_file_path){
        //Create temporary List files
        instrument.setListsPath(context.getCacheDir()+"");

        //Create 2 temporary Wav files
        String f1 = context.getCacheDir()+instrument.getName()+"temp1.wav";
        String f2 = context.getCacheDir()+instrument.getName()+"temp2.wav";
        String[] temp_files = {f1,f2};

        String bar = instrument.getBar();
        String path_to_notes = instrument.getNotesPath();
        String[] path_lists = instrument.getPath_to_lists();
        String s1 = splitBar(bar);
        String[] s2 = s1.split("--");

        printMusicalStringIntoFile(s2[0], path_to_notes, path_lists[0]);
        concatenate_wave_files(path_lists[0], f1);
        printMusicalStringIntoFile(s2[1], path_to_notes, path_lists[1]);
        concatenate_wave_files(path_lists[1], f2);

        merge_wave_files(temp_files,output_file_path);
    }

    private String printMusicalStringIntoFile(String bar, String path_to_notes ,String path_list){
        PrintWriter out = null;
        try {
            out = new PrintWriter(path_list);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] notes = bar.split(" ");
        String temp="";
        for(int i=0;i<notes.length;i++){
            out.println("file '"+path_to_notes+notes[i]+".wav'");
        }
        out.flush();
        out.close();
        return temp;
    }


    private String splitBar(String bar){
        String temp1="";
        String temp2="";
        String[] notes = bar.split(" ");

        for(int i=0; i<notes.length;i++){
            if(i%2==0){
                if(i==notes.length-1) temp1 +=notes[i];
                else temp1+=notes[i]+" r"+notes[i+1].charAt(notes[i+1].length()-1)+" ";
            }else{
                if(i==notes.length-1) temp2 +=notes[i];
                else temp2+=notes[i]+" r"+notes[i+1].charAt(notes[i+1].length()-1)+" ";
            }
        }
        //add first rest note
        temp2= "r"+notes[0].charAt(notes[0].length()-1)+" "+temp2;
        return temp1 + "--"+ temp2;
    }
    private void concatenate_wave_files(String path_list, String output_path){
        String cmd_notes = "-f concat -i "+path_list+" -y "+output_path;
        ffmpegCmd(cmd_notes);
    }

    private void merge_wave_files(String[] files, String output_path){
        String temp = "";
        int num_files = files.length;
        for(int i=0; i<files.length;i++){
            temp+="-i "+files[i]+" ";
        }
        String cmd_merge= temp+" -filter_complex amix=inputs="+num_files+":duration=first:dropout_transition=3 -y "+output_path;
        ffmpegCmd(cmd_merge);
    }


    private void ffmpegCmd(String cmd) {
        ffmpeg = FFmpeg.getInstance(context);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onProgress(String message) {
                }

                @Override
                public void onFailure(String message) {

                }

                @Override
                public void onSuccess(String message) {
                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running

        }

    }
    private void setupFFmpeg(){
        ffmpeg = FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }

}