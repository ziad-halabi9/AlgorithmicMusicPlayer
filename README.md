# AlgorithmicMusicPlayer


This project presents a method to convert Musical Strings into a WAV file on Android. It was build since MIDI is not supported by Android.
If you want to use AlgorithmicMusicPlayer, you need to have a collection of musical Notes stored as WAV files. The App will use FFmpeg Library to create a WAV file for your input.
The Musical String is based on JFugue's representation of musical String:
- Notes are separated by a space
- Note is represented by its pitch, octave and duration.
- Pitch can be: A A# B C C# D D# E F F# G G#
- Octave is a number between [0, 8]
- Duration can be: w (whole note), h (half), q (quarter), i (eigth) and s (sixteenth)
- Rest Notes are: rw, rh, rq, ri and rs.

You can download sample piano notes that I trimmed for this project: http://www.filedropper.com/pianonotes

The following example demonstrates how to use AlgorithmicMusicPlayer in your Android App.

```java

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path_audio = "/storage/emulated/legacy/Audio/";
        String path_piano_notes = path_audio+"pianonotes/";
    
        // Create First Instrument
        Instrument piano_right = new Instrument("piano_right");
        piano_right.setPath_to_notes(path_piano_notes);
        piano_right.setBar("A5i C6i D6h rq A6i C6i G6i F6i E6i A5i D6i C6i A5i C6i D6h rq A6i C6i G6i F6i E6i A5i D6i C6i A5i C6i D6i A5i C6i D6i C6i A5i A6i C6i G6i F6i E6i A5i D6i C6i D5i F5i A5i F5i A5i D6i A5i D6i F6i D6i F6i D7w");

        // Create Second Instrument
        Instrument piano_left = new Instrument("piano_left");
        piano_left.setBar("D4w F4h C4h D4w A#3h A3h D4w F4h C4h D4w");
        piano_left.setPath_to_notes(path_piano_notes);

        //Add Instruments to a Player    
        Player p = new Player(this.getApplicationContext());
        p.addInstrument(piano_right);
        p.addInstrument(piano_left);

        //Create a Wav File of the song
        p.produceMusic(path_audio+"song.wav");
}
```
Reference:

- Check JFugue Library (http://www.jfugue.org/) that is a great library for algorithmic music composition. It is written in JAVA and uses javax.sound which is not supported on Android. This library has been developped in order to overcome this limitation.
- Thanks to hiteshsondhi88 for publishing ffmpeg-android library (https://github.com/hiteshsondhi88/ffmpeg-android) 

