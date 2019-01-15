package com.example.ziad.algorithmicmusicplayer;

public interface MusicGeneratingCallback {

    void onError(String message);

    void onSuccess(String message);
}
