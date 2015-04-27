package com.example.ziad.algorithmicmusicplayer;

/**
 * Created by ziad on 22/04/15.
 */
public class Instrument {

    private String bar;
    private String name;
    private String path_to_notes;
    private String path_to_list1;
    private String path_to_list2;
    private String[] path_to_lists = new String[2];

    public Instrument(String name){

        this.name= name;
//        path_to_list1 = "/storage/emulated/legacy/Audio/"+name+"1.txt";
 //       path_to_list2 = "/storage/emulated/legacy/Audio/"+name+"2.txt";
  //      path_to_lists[0] = path_to_list1;
   //     path_to_lists[1] = path_to_list2;
    }

    public String getBar(){
        return bar;
    }
    public String getNotesPath(){
        return path_to_notes;
    }
    public void setBar(String bar){
        this.bar= bar;
    }
    public void setPath_to_notes(String path_to_notes){
        this.path_to_notes =path_to_notes;
    }
    public String toString(){
        return "Bar: "+bar +"\nNotes Path: "+path_to_notes;
    }
    public String[] getPath_to_lists(){
        return path_to_lists;
    }
    public String getName(){
        return name;
    }
    public void setListsPath(String path){
        path_to_list1 = path+name+"1.txt";
        path_to_list2 = path+name+"2.txt";
        path_to_lists[0] = path_to_list1;
        path_to_lists[1] = path_to_list2;


    }
}