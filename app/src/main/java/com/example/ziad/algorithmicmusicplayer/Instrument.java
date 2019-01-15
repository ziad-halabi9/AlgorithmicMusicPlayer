package com.example.ziad.algorithmicmusicplayer;

import java.io.File;

/**
 * Created by ziad on 22/04/15.
 */
public class Instrument {

    private String mBar;
    private String mName;
    private String mPathToNotes;
    private String mPathToList1;
    private String mPathToList2;
    private String[] mPathToLists = new String[2];

    public Instrument(String name) {
        this.mName = name;
    }

    public String getBar() {
        return mBar;
    }

    public String getNotesPath() {
        return mPathToNotes;
    }

    public void setBar(String mBar) {
        this.mBar = mBar;
    }

    public void setPath_to_notes(String path_to_notes) {
        this.mPathToNotes = path_to_notes + File.separator;
    }

    @Override
    public String toString() {
        return "Bar: " + mBar + "\nNotes Path: " + mPathToNotes;
    }

    public String[] getPath_to_lists() {
        return mPathToLists;
    }

    public String getName() {
        return mName;
    }

    public void setListsPath(String path) {
        mPathToList1 = path + mName + "1.txt";
        mPathToList2 = path + mName + "2.txt";
        mPathToLists[0] = mPathToList1;
        mPathToLists[1] = mPathToList2;
    }
}