import jdk.swing.interop.SwingInterOpUtils;

import java.util.*;
public class Cantus {
    /*
NonRepetedNotes C
NonLeapsLargerThanOctaveNotes A

NonDissonantLeapsNotes A
HasUniqueClimaxNotes C

BetweenTwoAndFourLeapsNotes R

NoLongRunsNotes R
LargerLeapsFollowedByChangeOfDirectionNotes R


FinalNoteApproachedByStepNotes B D si es antepen no es B // A
LeadingNoteGoesToTonicNotes C penu // C
LastNoteIsTonic ult y primera C

*desactivar de inicio las no tonicas
------
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


    public ArrayList<Integer> GetScaleNotes(String note) { //C
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

    ArrayList<Integer> GetAbsoluteIntervals(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);
        for (int i = 0; i < scale.size(); i++) {
            filteredN.add(scale.get(i)-last);
        }
        return filteredN;
    }

    // "R U L E S"
    ArrayList<Integer> NoRepetition(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);
        for (int i = 0; i < scale.size(); i++) {
            if (scale.get(i) != last) {
                filteredC.add(scale.get(i));
            }
        }
        return filteredC;
    }

    ArrayList<Integer> NoLeapsLargerThanOctave(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> AbsoluteIntervals = GetAbsoluteIntervals(scale,cantus);
        for (int i = 0; i < scale.size(); i++) {
            if (Math.abs(AbsoluteIntervals.get(i)) <= 12) {
                filteredC.add(scale.get(i));
            }
        }
        return filteredC;
    }

    ArrayList<Integer> NoDissonantLeaps(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> AbsoluteIntervals = GetAbsoluteIntervals(scale,cantus);
        for (int i = 0; i < scale.size(); i++) {
            if (Math.abs(AbsoluteIntervals.get(i)) == 11 || Math.abs(AbsoluteIntervals.get(i)) == 10 || Math.abs(AbsoluteIntervals.get(i)) == 6) {
                continue;
            } else {
                filteredC.add(scale.get(i));
            }
        }
        return filteredC;
    }

    //has_climax
    ArrayList<Integer> HasUniqueClimax(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
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

    //between_two_and_four_leaps
    ArrayList<Integer> BetweenTwoAndFourLeaps(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = GetRelativeIntervals(cantus);
        long count = RelativeIntervals.stream().filter(interv -> Math.abs(interv) >= 3).count();
        ArrayList<Integer> AbsoluteIntervals = GetAbsoluteIntervals(scale,cantus);
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

    int CheckLongRun(ArrayList<Integer> intervals) {
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

    //no_long_runs
    ArrayList<Integer> NoLongRuns(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = GetRelativeIntervals(cantus);

        if (cantus.size() >= 4) {
            int result = CheckLongRun(RelativeIntervals);
            //positivos
            if (result == 0) {
                for (int i = 0; i < scale.size(); i++) {
                    if (scale.get(i) < cantus.get(cantus.size() - 1)) { //cambia de direccion
                        filteredC.add(scale.get(i));
                    }
                }
            //negativos
            }else if (result == 1){
                for (int i = 0; i < scale.size(); i++) {
                    if (scale.get(i) > cantus.get(cantus.size() - 1)) { //cambia de direccion
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

    //larger_leaps_followed_by_change_of_direction
    ArrayList<Integer> LargerLeapsFollowedByChangeOfDirection(ArrayList<Integer> scale, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        ArrayList<Integer> RelativeIntervals = GetRelativeIntervals(cantus);

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
    //changes_direction_several_times
    //no_note_repeated_too_often

    int ClosestTonic(ArrayList<Integer> cantus, ArrayList<Integer> snn) {
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
    //FinalNoteApproachedByStepNotes B D si es antepen no es B // A
    //final_note_approached_by_step
    ArrayList<Integer> FinalNoteApproachedByStep(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn, int cantusSize) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int closestTonic = ClosestTonic( cantus, snn);
        System.out.println(closestTonic );
        if (cantusSize - 3 == cantus.size()) {
            //antepentul
            for (int i = 0; i < snn.size(); i++) {
                if (snn.get(i) < closestTonic - 2 || snn.get(i) > closestTonic + 2) {
                    filteredC.add(snn.get(i));
                }
            }

        } else if (cantusSize - 2 == cantus.size())
        {
            System.out.println(snn);
            //penul
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



    //leading_note_goes_to_tonic
    ArrayList<Integer> LeadingNoteGoesToTonic(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);

        if (last == snn.get(6) || last == snn.get(13)) {

            filteredC.add(ClosestTonic( cantus, snn));
        } else {
            filteredC = scale;
        }
        return filteredC;
    }
    //no_more_than_two_consecutive_leaps_in_same_direction
    //no_same_two_intervals_in_a_row
    //no_noodling

    //no_unresolved_melodic_tension
    //no_sequences
    ArrayList<Integer> LastNoteIsTonic(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn, int cantusSize)
    {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        if (cantusSize - 1 == cantus.size())
        {
            filteredC.add(ClosestTonic( cantus, snn));
        } else {
            filteredC = scale;
        }
        return filteredC;
    }

    public ArrayList<Integer> majorIntervals = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24));

    public ArrayList<Integer> cMajorNotes = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19));

    public int cantusSize = 8;
    //Scale cMajor = new Scale(0, majorIntervals);
    public List<String> notes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
    public int id(String note)
    {
        int octave = Integer.parseInt(note.substring(0, 1));
        return notes.indexOf(note.substring(1)) + 12 * octave + 12;
    }
    public String note(int id)
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

    public ArrayList<Integer> Recommend(ArrayList<Integer> scale, ArrayList<Integer> NumericInput, ArrayList<Integer> ScaleNoteNumbers, int cantusSize)
    {
        ArrayList<Integer> NonRepetedNotes = NoRepetition(ScaleNoteNumbers,NumericInput);
        System.out.println("NonRepetedNotes: " + NonRepetedNotes);

        ArrayList<Integer> NonLeapsLargerThanOctaveNotes = NoLeapsLargerThanOctave(NonRepetedNotes, NumericInput);
        System.out.println("NonLeapsLargerThanOctaveNotes: " + NonLeapsLargerThanOctaveNotes);

        ArrayList<Integer> NonDissonantLeapsNotes = NoDissonantLeaps(NonLeapsLargerThanOctaveNotes, NumericInput);
        System.out.println("NonDissonantLeapsNotes: " + NonDissonantLeapsNotes);

        ArrayList<Integer> HasUniqueClimaxNotes = HasUniqueClimax(NonDissonantLeapsNotes, NumericInput);
        System.out.println("HasUniqueClimaxNotes: " + HasUniqueClimaxNotes);

        ArrayList<Integer> BetweenTwoAndFourLeapsNotes = BetweenTwoAndFourLeaps(HasUniqueClimaxNotes, NumericInput);
        System.out.println("BetweenTwoAndFourLeapsNotes: " + BetweenTwoAndFourLeapsNotes);

        ArrayList<Integer> NoLongRunsNotes = NoLongRuns(BetweenTwoAndFourLeapsNotes, NumericInput);
        System.out.println("NoLongRunsNotes: " + NoLongRunsNotes);

        ArrayList<Integer> LargerLeapsFollowedByChangeOfDirectionNotes = LargerLeapsFollowedByChangeOfDirection(NoLongRunsNotes, NumericInput);
        System.out.println("LargerLeapsFollowedByChangeOfDirectionNotes: " + LargerLeapsFollowedByChangeOfDirectionNotes);

        ArrayList<Integer> FinalNoteApproachedByStepNotes = FinalNoteApproachedByStep(NoLongRunsNotes, NumericInput, ScaleNoteNumbers, cantusSize);
        System.out.println("FinalNoteApproachedByStepNotes: " + FinalNoteApproachedByStepNotes);

        ArrayList<Integer> LeadingNoteGoesToTonicNotes = LeadingNoteGoesToTonic(FinalNoteApproachedByStepNotes, NumericInput, ScaleNoteNumbers);
        System.out.println("LeadingNoteGoesToTonicNotes: " + LeadingNoteGoesToTonicNotes);

        ArrayList<Integer> LastNoteIsTonicNotes = LastNoteIsTonic(LeadingNoteGoesToTonicNotes, NumericInput, ScaleNoteNumbers, cantusSize);
        System.out.println("LastNoteIsTonicNotes: " + LastNoteIsTonicNotes);

        return LastNoteIsTonicNotes;
    }

    public static void main(String[] args) {
        //int[] input = {60, 62, 64, 60};

        int cantusSize = 8;
        String cantusScale = "C";
        ArrayList<String> input = new ArrayList<String>(Arrays.asList("4C","4D"));
        Cantus cnts = new Cantus();
        //nota inicial mostrada a escoger, nota final por recomendacion
        System.out.println("Cantus: " + input);
        // selected c major scale, transposed to c major then back to original
        ArrayList<Integer> ScaleNoteNumbers = cnts.GetScaleNotes(cantusScale);
        System.out.println("ScaleNotes: " + ScaleNoteNumbers);

        ArrayList<String> ScaleNoteNames =  cnts.GetNoteNames(ScaleNoteNumbers);
        System.out.println("ScaleNotes: " + ScaleNoteNames );

        ArrayList<Integer> NumericInput = new ArrayList<Integer>();
        for (int i = 0; i < input.size(); i++) {
            NumericInput.add(cnts.id(input.get(i) ));
        }

        ArrayList<Integer> RelativeIntervals = cnts.GetRelativeIntervals(NumericInput);
        System.out.println("RelativeIntervals: " + RelativeIntervals);

        ArrayList<Integer> AbsoluteIntervals = cnts.GetAbsoluteIntervals(ScaleNoteNumbers,NumericInput);
        System.out.println("AbsoluteIntervals: " + AbsoluteIntervals);

        ArrayList<Integer> Recommendation = cnts.Recommend(ScaleNoteNumbers, NumericInput, ScaleNoteNumbers, cantusSize);
        System.out.println("Recommendation: " + Recommendation);
        /*
        ArrayList<Integer> NonRepetedNotes = cnts.NoRepetition(ScaleNoteNumbers,NumericInput);
        System.out.println("NonRepetedNotes: " + NonRepetedNotes);

        ArrayList<Integer> NonLeapsLargerThanOctaveNotes = cnts.NoLeapsLargerThanOctave(NonRepetedNotes, NumericInput);
        System.out.println("NonLeapsLargerThanOctaveNotes: " + NonLeapsLargerThanOctaveNotes);

        ArrayList<Integer> NonDissonantLeapsNotes = cnts.NoDissonantLeaps(NonLeapsLargerThanOctaveNotes, NumericInput);
        System.out.println("NonDissonantLeapsNotes: " + NonDissonantLeapsNotes);

        ArrayList<Integer> HasUniqueClimaxNotes = cnts.HasUniqueClimax(NonDissonantLeapsNotes, NumericInput);
        System.out.println("HasUniqueClimaxNotes: " + HasUniqueClimaxNotes);

        ArrayList<Integer> BetweenTwoAndFourLeapsNotes = cnts.BetweenTwoAndFourLeaps(HasUniqueClimaxNotes, NumericInput);
        System.out.println("BetweenTwoAndFourLeapsNotes: " + BetweenTwoAndFourLeapsNotes);

        ArrayList<Integer> NoLongRunsNotes = cnts.NoLongRuns(BetweenTwoAndFourLeapsNotes, NumericInput);
        System.out.println("NoLongRunsNotes: " + NoLongRunsNotes);

        ArrayList<Integer> LargerLeapsFollowedByChangeOfDirectionNotes = cnts.LargerLeapsFollowedByChangeOfDirection(NoLongRunsNotes, NumericInput);
        System.out.println("LargerLeapsFollowedByChangeOfDirectionNotes: " + LargerLeapsFollowedByChangeOfDirectionNotes);

        ArrayList<Integer> FinalNoteApproachedByStepNotes = cnts.FinalNoteApproachedByStep(NoLongRunsNotes, NumericInput, ScaleNoteNumbers, cantusSize);
        System.out.println("FinalNoteApproachedByStepNotes: " + FinalNoteApproachedByStepNotes);

        ArrayList<Integer> LeadingNoteGoesToTonicNotes = cnts.LeadingNoteGoesToTonic(FinalNoteApproachedByStepNotes, NumericInput, ScaleNoteNumbers);
        System.out.println("LeadingNoteGoesToTonicNotes: " + LeadingNoteGoesToTonicNotes);

        ArrayList<Integer> LastNoteIsTonicNotes = cnts.LastNoteIsTonic(LeadingNoteGoesToTonicNotes, NumericInput, ScaleNoteNumbers, cantusSize);
        System.out.println("LastNoteIsTonicNotes: " + LastNoteIsTonicNotes);

        */
    }
}
