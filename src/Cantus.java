import java.util.Arrays;
import java.util.List;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;
import java.util.*;

public class Cantus {
    private static MidiChannel[] channels;
    private static int instrumento = 0; // de 0 - 9
    private static int volumen = 80; // de 0 - 127
    public static ArrayList<Integer> intervalos = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24));
    public static List<String> nomenclatura = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
    public static int id(String note) {
        int octave = Integer.parseInt(note.substring(0, 1));
        return nomenclatura.indexOf(note.substring(1)) + 12 * octave + 12;
    }
    public static String note(int id) {
        int octave = (id - 12) / 12;
        String note = nomenclatura.get( (id - 12) % 12 );
        return octave + note;
    }
    public static ArrayList<Integer> obtenerNumeros(String note) {
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        int startNote = id(4+note);
        for (int i = 0; i < intervalos.size(); i++) {
                filteredN.add(intervalos.get(i)+startNote);
        }
        return filteredN;
    }
    public static ArrayList<String> obtenerNombres(ArrayList<Integer> noteNumbers) {
        ArrayList<String> ScaleNoteNames = new ArrayList<String>();
        for (int i = 0; i < noteNumbers.size(); i++) {
            ScaleNoteNames.add(note(noteNumbers.get(i)));
        }
        return ScaleNoteNames;
    }
    private ArrayList<Integer> intervalosRelativos(ArrayList<Integer> intervals) {
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        for (int i = 0; i < intervals.size(); i++) {
            if (i == 0) {
                filteredN.add(0);
            } else {
                filteredN.add(intervals.get(i)-intervals.get(i-1));
            }
        }
        return filteredN;
    }
    private ArrayList<Integer> intervalosAbsolutos(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);
        for (int i = 0; i < scale.size(); i++) {
            filteredN.add(scale.get(i)-last);
        }
        return filteredN;
    }
    public static void sonar(String note, int duration) throws InterruptedException {
        channels[instrumento].noteOn(id(note), volumen);
        Thread.sleep( duration );
        channels[instrumento].noteOff(id(note));
    }
    public static void tocar(ArrayList<String> notesToPlay) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();
            for (int i = 0; i < notesToPlay.size(); i++) {
                sonar(notesToPlay.get(i),  1000);
            }
            synth.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // "R E G L A S"
    private ArrayList<Integer> noRepeticion(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);
        for (int i = 0; i < scale.size(); i++) {
            if (scale.get(i) != last) {
                filteredC.add(scale.get(i));
            }
        }
        return filteredC;
    }
    private ArrayList<Integer> noIntervalosCompuestos(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> AbsoluteIntervals = intervalosAbsolutos(scale,cantus);
        for (int i = 0; i < scale.size(); i++) {
            if (Math.abs(AbsoluteIntervals.get(i)) <= 12) {
                filteredC.add(scale.get(i));
            }
        }
        return filteredC;
    }
    private ArrayList<Integer> noSaltosDisonantes(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> AbsoluteIntervals = intervalosAbsolutos(scale,cantus);
        for (int i = 0; i < scale.size(); i++) {
            if (Math.abs(AbsoluteIntervals.get(i)) == 11 || Math.abs(AbsoluteIntervals.get(i)) == 10 || Math.abs(AbsoluteIntervals.get(i)) == 6) {
                continue;
            } else {
                filteredC.add(scale.get(i));
            }
        }
        return filteredC;
    }
    private ArrayList<Integer> tieneClimaxUnico(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        Integer max = Collections.max(cantus);
        System.out.println("Max: " + max);
        for (int i = 0; i < scale.size(); i++) {
            if (scale.get(i) != max) {
                filteredC.add(scale.get(i));
            }
        }
        return filteredC;
    }
    private ArrayList<Integer> entreDosYCuatroSaltos(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = intervalosRelativos(cantus);
        long count = RelativeIntervals.stream().filter(interv -> Math.abs(interv) >= 3).count();
        ArrayList<Integer> AbsoluteIntervals = intervalosAbsolutos(scale,cantus);
        System.out.println("count: " + count);

        if (count == 3) {
            for (int i = 0; i < scale.size(); i++) {
                if (Math.abs(AbsoluteIntervals.get(i)) <= 2) {
                    filteredC.add(scale.get(i));
                }
            }
        } else {
            filteredC = scale;
        }

        return filteredC;
    }
    private int checkLongRun(ArrayList<Integer> intervals) {
        System.out.println(intervals);
        ArrayList<Integer> myLast4Intervals = new ArrayList<Integer>( intervals.subList(intervals.size()-4, intervals.size()) );
        long countP = myLast4Intervals.stream().filter(interv -> interv >= 1 && interv <= 2).count();
        System.out.println(countP);
        long countN = myLast4Intervals.stream().filter(interv -> interv <= -1 && interv >= -2).count();
        System.out.println(countN);

        if (countP == 3)
        {
            return 0;
        }
        else if (countN == 3)
        {
            return 1;
        }
        else
        {
            return 2;
        }

    }
    private ArrayList<Integer> noLongRuns(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = intervalosRelativos(cantus);

        if (cantus.size() >= 4) {
            int result = checkLongRun(RelativeIntervals);

            if (result == 0) {
                for (int i = 0; i < scale.size(); i++) {
                    if (scale.get(i) < cantus.get(cantus.size() - 1)) {
                        filteredC.add(scale.get(i));
                    }
                }

            }else if (result == 1){
                for (int i = 0; i < scale.size(); i++) {
                    if (scale.get(i) > cantus.get(cantus.size() - 1)) {
                        filteredC.add(scale.get(i));
                    }
                }
            } else {
                filteredC = scale;
            }
        } else {
            filteredC = scale;
        }

        return filteredC;
    }
    private ArrayList<Integer> saltoYCambioDeDireccion(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = intervalosRelativos(cantus);

        if (RelativeIntervals.get(RelativeIntervals.size()-1) >= 3) {
            for (int i = 0; i < scale.size(); i++) {
                if (scale.get(i) < cantus.get(cantus.size() - 1) && scale.get(i) >= cantus.get(cantus.size() - 1) - 2) {
                    filteredC.add(scale.get(i));
                }
            }
        }else if (RelativeIntervals.get(RelativeIntervals.size()-1) <= -3){
            for (int i = 0; i < scale.size(); i++) {
                if (scale.get(i) > cantus.get(cantus.size() - 1) && scale.get(i) <= cantus.get(cantus.size() - 1) + 2) {
                    filteredC.add(scale.get(i));
                }
            }
        } else {
            filteredC = scale;
        }

        return filteredC;
    }
    private int tonicaMasCercana(ArrayList<Integer> cantus, ArrayList<Integer> snn) {
        ArrayList<Integer> aux = new ArrayList<Integer>();
        aux.add(Math.abs( snn.get(0) - cantus.get(cantus.size()-1)));
        aux.add(Math.abs( snn.get(7) - cantus.get(cantus.size()-1)));
        aux.add(Math.abs( snn.get(14) - cantus.get(cantus.size()-1)));
        Integer min = Collections.min(aux);
        int tonic = aux.indexOf(min);
        if (tonic == 0)
        {
            return snn.get(0);
        } else if (tonic == 1)
        {
            return snn.get(7);
        }
        else
        {
            return snn.get(14);
        }
    }
    private ArrayList<Integer> FinalNoteApproachedByStep(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn, int cantusSize) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int closestTonic = tonicaMasCercana( cantus, snn);
        System.out.println(closestTonic );
        if (cantusSize - 3 == cantus.size()) {

            for (int i = 0; i < scale.size(); i++) {
                if (scale.get(i) != snn.get(1) && scale.get(i) != snn.get(6) && scale.get(i) != snn.get(13)) {
                    filteredC.add(scale.get(i));
                }
            }

        } else if (cantusSize - 2 == cantus.size())
        {
            System.out.println(snn);

            for (int i = 0; i < snn.size(); i++) {
                if (snn.get(i) >= closestTonic - 2 && snn.get(i) <= closestTonic + 2 && snn.get(i) != closestTonic) {
                    filteredC.add(snn.get(i));
                }
            }

        }
        else {
            filteredC = scale;
        }
        return filteredC;
    }
    private ArrayList<Integer> sensibleVaALaTonica(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);

        if (last == snn.get(6) || last == snn.get(13)) {

            filteredC.add(tonicaMasCercana( cantus, snn));
        } else {
            filteredC = scale;
        }
        return filteredC;
    }
    private ArrayList<Integer> ultimaEsTonica(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn, int cantusSize) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        if (cantusSize - 1 == cantus.size())
        {
            filteredC.add(tonicaMasCercana( cantus, snn));
        } else {
            filteredC = scale;
        }
        return filteredC;
    }

    public ArrayList<Integer> recomendar(ArrayList<Integer> scale, ArrayList<Integer> NumericInput, ArrayList<Integer> ScaleNoteNumbers, int cantusSize) {
        ArrayList<Integer> NonRepetedNotes = noRepeticion(ScaleNoteNumbers,NumericInput);
        System.out.println("NonRepetedNotes: " + NonRepetedNotes);

        ArrayList<Integer> NonLeapsLargerThanOctaveNotes = noIntervalosCompuestos(NonRepetedNotes, NumericInput);
        System.out.println("NonLeapsLargerThanOctaveNotes: " + NonLeapsLargerThanOctaveNotes);

        ArrayList<Integer> NonDissonantLeapsNotes = noSaltosDisonantes(NonLeapsLargerThanOctaveNotes, NumericInput);
        System.out.println("NonDissonantLeapsNotes: " + NonDissonantLeapsNotes);

        ArrayList<Integer> HasUniqueClimaxNotes = tieneClimaxUnico(NonDissonantLeapsNotes, NumericInput);
        System.out.println("HasUniqueClimaxNotes: " + HasUniqueClimaxNotes);

        ArrayList<Integer> BetweenTwoAndFourLeapsNotes = entreDosYCuatroSaltos(HasUniqueClimaxNotes, NumericInput);
        System.out.println("BetweenTwoAndFourLeapsNotes: " + BetweenTwoAndFourLeapsNotes);

        ArrayList<Integer> NoLongRunsNotes = noLongRuns(BetweenTwoAndFourLeapsNotes, NumericInput);
        System.out.println("NoLongRunsNotes: " + NoLongRunsNotes);

        ArrayList<Integer> LargerLeapsFollowedByChangeOfDirectionNotes = saltoYCambioDeDireccion(NoLongRunsNotes, NumericInput);
        System.out.println("LargerLeapsFollowedByChangeOfDirectionNotes: " + LargerLeapsFollowedByChangeOfDirectionNotes);

        ArrayList<Integer> FinalNoteApproachedByStepNotes = FinalNoteApproachedByStep(LargerLeapsFollowedByChangeOfDirectionNotes, NumericInput, ScaleNoteNumbers, cantusSize);
        System.out.println("FinalNoteApproachedByStepNotes: " + FinalNoteApproachedByStepNotes);

        ArrayList<Integer> LeadingNoteGoesToTonicNotes = sensibleVaALaTonica(FinalNoteApproachedByStepNotes, NumericInput, ScaleNoteNumbers);
        System.out.println("LeadingNoteGoesToTonicNotes: " + LeadingNoteGoesToTonicNotes);

        ArrayList<Integer> LastNoteIsTonicNotes = ultimaEsTonica(LeadingNoteGoesToTonicNotes, NumericInput, ScaleNoteNumbers, cantusSize);
        System.out.println("LastNoteIsTonicNotes: " + LastNoteIsTonicNotes);

        return LastNoteIsTonicNotes;

    }
}
