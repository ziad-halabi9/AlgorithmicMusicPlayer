package com.example.ziad.algorithmicmusicplayer;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

/**
 * Created by ziad on 22/04/15.
 */
public class Player {

    private Context mContext;
    private FFmpeg mFFmpeg;
    private ArrayList<Instrument> mInstruments = new ArrayList<Instrument>();

    public Player(Context context) {
        this.mContext = context;
        setupFFmpeg();
    }

    public void addInstrument(Instrument instrument) {
        if (instrument != null) mInstruments.add(instrument);
    }

    public ArrayList<Instrument> getInstruments() {
        return mInstruments;
    }

    public void produceMusic(String path_output, MusicGeneratingCallback callback) {
        if (path_output == null || path_output.isEmpty()) {
            if (callback != null) {
                callback.onError("Invalid Path");
            }
            return;
        }
        if (mInstruments.isEmpty()) {
            if (callback != null) {
                callback.onError("No mInstruments are added");
            }
            return;
        }

        ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<>();
        String[] temp_output_file_path = new String[mInstruments.size()];
        for (int i = 0; i < mInstruments.size(); i++) {
            String temp = mContext.getCacheDir() + File.separator + "temp" + i + ".wav";
            temp_output_file_path[i] = temp;
            commands.addAll(getCreateInstrumentWavFileCmd(mInstruments.get(i), temp));
        }

        String cmdMergeAll = getMergeWavFiles(temp_output_file_path, path_output);
        commands.add(cmdMergeAll);

        executeAllCmds(commands, callback);
    }

    private void executeAllCmds(final ConcurrentLinkedQueue<String> cmds, final MusicGeneratingCallback callback) {
        final ExecuteBinaryResponseHandler listener = new ExecuteBinaryResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onProgress(String message) {
            }

            @Override
            public void onFailure(String message) {
                if (callback != null) {
                    callback.onError(message);
                }
            }

            @Override
            public void onSuccess(String message) {
                if (!cmds.isEmpty()) {
                    ffmpegCmd(cmds.poll(), this);
                } else {
                    if (callback != null) {
                        callback.onSuccess(message);
                    }
                    //clear temporary files
                    CacheManager.trimCache(mContext);
                }
            }

            @Override
            public void onFinish() {
            }
        };
        ffmpegCmd(cmds.poll(), listener);
    }

    private ArrayList<String> getCreateInstrumentWavFileCmd(Instrument instrument, String output_file_path) {
        //Create temporary List files
        instrument.setListsPath(mContext.getCacheDir().getAbsolutePath() + File.separator);

        //Create 2 temporary Wav files
        String f1 = mContext.getCacheDir() + File.separator + "temp1" + instrument.getName() + ".wav";
        String f2 = mContext.getCacheDir() + File.separator + "temp2" + instrument.getName() + ".wav";

        String[] temp_files = {f1, f2};

        String bar = instrument.getBar();
        String path_to_notes = instrument.getNotesPath();
        String[] path_lists = instrument.getPath_to_lists();
        String s1 = splitBar(bar);
        String[] s2 = s1.split("--");

        printMusicalStringIntoFile(s2[0], path_to_notes, path_lists[0]);
        printMusicalStringIntoFile(s2[1], path_to_notes, path_lists[1]);

        String cmdConcat1 = getConcatenateWavFilesCmnd(path_lists[0], f1);
        String cmdConcat2 = getConcatenateWavFilesCmnd(path_lists[1], f2);
        String cmdMerge = getMergeWavFiles(temp_files, output_file_path);

        ArrayList<String> cmds = new ArrayList<>();
        cmds.add(cmdConcat1);
        cmds.add(cmdConcat2);
        cmds.add(cmdMerge);
        return cmds;
    }

    private void printMusicalStringIntoFile(String bar, String path_to_notes, String path_list) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(path_list);
            String[] notes = bar.split(" ");
            for (int i = 0; i < notes.length; i++) {
                out.println("file '" + path_to_notes + notes[i] + ".wav'");
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }


    private String splitBar(String bar) {
        String temp1 = "";
        String temp2 = "";
        String[] notes = bar.split(" ");

        for (int i = 0; i < notes.length; i++) {
            if (i % 2 == 0) {
                if (i == notes.length - 1) temp1 += notes[i];
                else
                    temp1 += notes[i] + " r" + notes[i + 1].charAt(notes[i + 1].length() - 1) + " ";
            } else {
                if (i == notes.length - 1) temp2 += notes[i];
                else
                    temp2 += notes[i] + " r" + notes[i + 1].charAt(notes[i + 1].length() - 1) + " ";
            }
        }
        //add first rest note
        temp2 = "r" + notes[0].charAt(notes[0].length() - 1) + " " + temp2;
        return temp1 + "--" + temp2;
    }

    private String getConcatenateWavFilesCmnd(String path_list, String output_path) {
        return "-f concat -safe 0 -i " + path_list + " -y " + output_path;
    }

    private String getMergeWavFiles(String[] files, String output_path) {
        StringBuilder temp = new StringBuilder();
        int num_files = files.length;
        for (int i = 0; i < files.length; i++) {
            temp.append("-i ").append(files[i]).append(" ");
        }
        return temp.append("-filter_complex amix=inputs=")
                .append(num_files)
                .append(":duration=first:dropout_transition=3 -y ")
                .append(output_path)
                .toString();
    }


    private void ffmpegCmd(final String cmd, ExecuteBinaryResponseHandler listener) {
        mFFmpeg = FFmpeg.getInstance(mContext);
        mFFmpeg.execute(cmd.split(" "), listener);
    }

    private void setupFFmpeg() {
        mFFmpeg = FFmpeg.getInstance(mContext);
    }
}