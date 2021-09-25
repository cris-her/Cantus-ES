import java.util.*;
public class Cantus {
    /*
Soprano do 4 - sol 5/do 6
Min 8 max 16
No 7ma, aum o dim
Un solo punto algido
2 saltos > 4ta, 3 en total, ni seguidos + cambio de sentido excepto 3ra
Resolver sensible [menor melodi
No repetir ni dominar, equilibrio
Finalizar por grado conjunto

if < 10 = 4 else 5
Descarta previa nota mas alta
*/
    int[] chromaticScale = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    public int getPitchClass(int pitch)
    {
        if (pitch >= 12) {
            return pitch % 12;
        } else {
            return pitch;
        }
    }

    public boolean samePitchClass(int pitch1, int pitch2)
    {
        return getPitchClass(pitch1) == getPitchClass(pitch2);
    }


    ArrayList<Integer> GetScaleNotes(String note) { //C
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        int startNote = id(4+note); //60
        for (int i = 0; i < majorIntervals.size(); i++) { //0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24
                filteredN.add(majorIntervals.get(i)+startNote);
        }
        return filteredN;
    }

    ArrayList<Integer> GetRelativeIntervals(ArrayList<Integer> intervals) {
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

    ArrayList<Integer> GetAbsoluteIntervals(ArrayList<Integer> intervals, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);
        for (int i = 0; i < intervals.size(); i++) {
            filteredN.add(intervals.get(i)-intervals.get(intervals.indexOf(last)));
        }
        return filteredN;
    }

    // "R U L E S"
    ArrayList<Integer> NoRepetition(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);
        for (int i = 0; i < cMajorNotes.size(); i++) {
            if (cMajorNotes.get(i) != last) {
                filteredC.add(cMajorNotes.get(i));
            }
        }
        return filteredC;
    }

    ArrayList<Integer> NoLeapsLargerThanOctave(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> AbsoluteIntervals = GetAbsoluteIntervals(cMajorNotes,cantus);
        for (int i = 0; i < cMajorNotes.size(); i++) {
            if (Math.abs(AbsoluteIntervals.get(i)) <= 12) {
                filteredC.add(cMajorNotes.get(i));
            }
        }
        return filteredC;
    }

    ArrayList<Integer> NoDissonantLeaps(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> AbsoluteIntervals = GetAbsoluteIntervals(cMajorNotes,cantus);
        for (int i = 0; i < cMajorNotes.size(); i++) {
            if (Math.abs(AbsoluteIntervals.get(i)) == 11) {
                continue;
            } else if (Math.abs(AbsoluteIntervals.get(i)) == 6) {
                continue;
            } else {
                filteredC.add(cMajorNotes.get(i));
            }
        }
        return filteredC;
    }

    //between_two_and_four_leaps
    ArrayList<Integer> BetweenTwoAndFourLeaps(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = GetRelativeIntervals(cantus);
        long count = RelativeIntervals.stream().filter(c -> Math.abs(c) > 4).count();
        ArrayList<Integer> AbsoluteIntervals = GetAbsoluteIntervals(cMajorNotes,cantus);
        System.out.println("Count: " + count);

        if (count > 2) {
            for (int i = 0; i < cMajorNotes.size(); i++) {
                if (Math.abs(AbsoluteIntervals.get(i)) < 3) {
                    filteredC.add(cMajorNotes.get(i));
                }
            }
        } else if (count == 2){
            for (int i = 0; i < cMajorNotes.size(); i++) {
                if (Math.abs(AbsoluteIntervals.get(i)) <= 3) {
                    filteredC.add(cMajorNotes.get(i));
                }
            }
        } else {
            filteredC = cMajorNotes;
        }

        return filteredC;
    }

    //has_climax
    ArrayList<Integer> HasUniqueClimax(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        Integer max = Collections.max(cantus);
        System.out.println("Max: " + max);
        for (int i = 0; i < cMajorNotes.size(); i++) {
            if (cMajorNotes.get(i) != max) {
                filteredC.add(cMajorNotes.get(i));
            }
        }
        return filteredC;
    }
    //changes_direction_several_times
    //no_note_repeated_too_often
    //final_note_approached_by_step
    ArrayList<Integer> FinalNoteApproachedByStep(ArrayList<Integer> cantus, int lastNote) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();

        if (cantusSize - 2 == cantus.size()) {
            if (lastNote == 0) {
                filteredC.add(2);
            } else {
                filteredC.add(11);
                filteredC.add(14);
            }
        } else {
            filteredC = cMajorNotes;
        }
        return filteredC;
    }

    //larger_leaps_followed_by_change_of_direction
    ArrayList<Integer> LargerLeapsFollowedByChangeOfDirection(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = GetRelativeIntervals(cantus);

        if (RelativeIntervals.get(RelativeIntervals.size()-1) > 4) {
            for (int i = 0; i < cMajorNotes.size(); i++) {
                if (cMajorNotes.get(i) < cantus.get(cantus.size() - 1)) {
                    filteredC.add(cMajorNotes.get(i));
                }
            }
        }else if (RelativeIntervals.get(RelativeIntervals.size()-1) < -4){
            for (int i = 0; i < cMajorNotes.size(); i++) {
                if (cMajorNotes.get(i) > cantus.get(cantus.size() - 1)) {
                    filteredC.add(cMajorNotes.get(i));
                }
            }
        } else {
            filteredC = cMajorNotes;
        }

        return filteredC;
    }

    //leading_note_goes_to_tonic
    ArrayList<Integer> LeadingNoteGoesToTonic(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);

        if (last == 11) {
            filteredC.add(12);
        } else {
            filteredC = cMajorNotes;
        }
        return filteredC;
    }
    //no_more_than_two_consecutive_leaps_in_same_direction
    //no_same_two_intervals_in_a_row
    //no_noodling
    //no_long_runs
    ArrayList<Integer> NoLongRuns(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = GetRelativeIntervals(cantus);
        if (cantus.size() >= 5) {
            if (RelativeIntervals.get(RelativeIntervals.size()-1) >= 0 &&  RelativeIntervals.get(RelativeIntervals.size()-2) >= 0 && RelativeIntervals.get(RelativeIntervals.size()-3) >= 0 &&  RelativeIntervals.get(RelativeIntervals.size()-4) >= 0 &&  RelativeIntervals.get(RelativeIntervals.size()-5) >= 0 ) {
                for (int i = 0; i < cMajorNotes.size(); i++) {
                    if (cMajorNotes.get(i) < cantus.get(cantus.size() - 1)) {
                        filteredC.add(cMajorNotes.get(i));
                    }
                }
            }else if (RelativeIntervals.get(RelativeIntervals.size()-1) <= 0 &&  RelativeIntervals.get(RelativeIntervals.size()-2) <= 0 && RelativeIntervals.get(RelativeIntervals.size()-3) <= 0 &&  RelativeIntervals.get(RelativeIntervals.size()-4) <= 0 &&  RelativeIntervals.get(RelativeIntervals.size()-5) <= 0 ){
                for (int i = 0; i < cMajorNotes.size(); i++) {
                    if (cMajorNotes.get(i) > cantus.get(cantus.size() - 1)) {
                        filteredC.add(cMajorNotes.get(i));
                    }
                }
            } else {
                filteredC = cMajorNotes;
            }
        } else {
            filteredC = cMajorNotes;
        }

        return filteredC;
    }
    //no_unresolved_melodic_tension
    //no_sequences

    public ArrayList<Integer> majorIntervals = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24));

    public ArrayList<Integer> cMajorNotes = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19));

    public int cantusSize = 8;
    //Scale cMajor = new Scale(0, majorIntervals);
    private List<String> notes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
    private int id(String note)
    {
        int octave = Integer.parseInt(note.substring(0, 1));
        return notes.indexOf(note.substring(1)) + 12 * octave + 12;
    }
    private String note(int id)
    {
        int octave = (id - 12) / 12;
        String note = notes.get( (id - 12) % 12 );
        return octave + note;
    }

    public ArrayList<String> GetNoteNames(ArrayList<Integer> noteNumbers)
    {
        ArrayList<String> ScaleNoteNames = new ArrayList<String>();
        for (int i = 0; i < noteNumbers.size(); i++) {
            ScaleNoteNames.add(note(noteNumbers.get(i)));
        }
        return ScaleNoteNames;
    }

    public static void main(String[] args) {
        //int[] input = {60, 62, 64, 60};

        int cantusSize = 8;
        String cantusScale = "F";
        ArrayList<String> input = new ArrayList<String>(Arrays.asList("4C", "4D", "4E"));
        Cantus cnts = new Cantus();
        //nota inicial mostrada a escoger, nota final por recomendacion
        System.out.println("Cantus: " + input);
        // selected c major scale, transposed to c major then back to original
        ArrayList<Integer> ScaleNoteNumbers = cnts.GetScaleNotes(cantusScale);
        System.out.println("ScaleNotes: " + ScaleNoteNumbers);

        ArrayList<String> ScaleNoteNames =  cnts.GetNoteNames(ScaleNoteNumbers);
        System.out.println("ScaleNotes: " + ScaleNoteNames );
/*
        ArrayList<Integer> actualIntervals = cnts.GetRelativeIntervals(input);
        System.out.println("actualIntervals: " + actualIntervals);

        ArrayList<Integer> NonRepetedNotes = cnts.NoRepetition(input);
        System.out.println("NonRepetedNotes: " + NonRepetedNotes);

        ArrayList<Integer> AbsoluteIntervals = cnts.GetAbsoluteIntervals(cnts.cMajorNotes,input);
        System.out.println("AbsoluteIntervals: " + AbsoluteIntervals);

        ArrayList<Integer> NonDissonantLeapsNotes = cnts.NoDissonantLeaps(input);
        System.out.println("NonDissonantLeapsNotes: " + NonDissonantLeapsNotes);

        ArrayList<Integer> NonLeapsLargerThanOctaveNotes = cnts.NoLeapsLargerThanOctave(input);
        System.out.println("NonLeapsLargerThanOctaveNotes: " + NonLeapsLargerThanOctaveNotes);

        ArrayList<Integer> LeadingNoteGoesToTonicNotes = cnts.LeadingNoteGoesToTonic(input);
        System.out.println("LeadingNoteGoesToTonicNotes: " + LeadingNoteGoesToTonicNotes);

        ArrayList<Integer> FinalNoteApproachedByStepNotes = cnts.FinalNoteApproachedByStep(input, 0);
        System.out.println("FinalNoteApproachedByStepNotes: " + FinalNoteApproachedByStepNotes);

        ArrayList<Integer> HasUniqueClimaxNotes = cnts.HasUniqueClimax(input);
        System.out.println("HasUniqueClimaxNotes: " + HasUniqueClimaxNotes);

        ArrayList<Integer> LargerLeapsFollowedByChangeOfDirectionNotes = cnts.LargerLeapsFollowedByChangeOfDirection(input);
        System.out.println("LargerLeapsFollowedByChangeOfDirectionNotes: " + LargerLeapsFollowedByChangeOfDirectionNotes);

        ArrayList<Integer> BetweenTwoAndFourLeapsNotes = cnts.BetweenTwoAndFourLeaps(input);
        System.out.println("BetweenTwoAndFourLeapsNotes: " + BetweenTwoAndFourLeapsNotes);

        ArrayList<Integer> NoLongRunsNotes = cnts.NoLongRuns(input);
        System.out.println("NoLongRunsNotes: " + NoLongRunsNotes);

 */
    }
}
