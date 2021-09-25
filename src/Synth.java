import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;

/**
 * A little example showing how to play a tune in Java.
 *
 * Inputs are not sanitized or checked, this is just to show how simple it is.
 *
 * @author Peter
 */
public class Synth {

    private List<String> notes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
    private MidiChannel[] channels;
    private int INSTRUMENT = 0; // 0 is a piano, 9 is percussion, other channels are for other instruments
    private int VOLUME = 80; // between 0 et 127

    public void play(String note, int duration) throws InterruptedException
    {
        // * start playing a note
        channels[INSTRUMENT].noteOn(id(note), VOLUME );
        // * wait
        Thread.sleep( duration );
        // * stop playing a note
        channels[INSTRUMENT].noteOff(id(note));
    }

    private void rest(int duration) throws InterruptedException
    {
        Thread.sleep(duration);
    }

    private int id(String note)
    {
        int octave = Integer.parseInt(note.substring(0, 1));
        return notes.indexOf(note.substring(1)) + 12 * octave + 12;
    }

    public Synth(ArrayList<String> notesToPlay)
    {
        try {
            // * Open a synthesizer
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();
            for (int i = 0; i < notesToPlay.size(); i++) {
                play(notesToPlay.get(i),  1000);
            }
            synth.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void main( String[] args ) {
        ArrayList<String> notes = new ArrayList<String>(Arrays.asList("6D","6C#","6E","6G"));
        Synth sth = new Synth(notes);
    }
}