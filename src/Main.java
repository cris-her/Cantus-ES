import java.util.*;
class Main {
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
    static int[] chromaticScale = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    public static int getPitchClass(int pitch)
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

    public class Scale
    {
        public int root;
        public int[] intervals;

        public Scale(int root, int[] intervals)
        {
            this.root = root;
            this.intervals = intervals;
        }
    }

    static ArrayList<Integer> GetScaleNotes(ArrayList<Integer> intervals) {
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        for (int i = 0; i < chromaticScale.length; i++) {
            if (intervals.contains(i)) {
                filteredN.add(chromaticScale[i]);
            }
        }
        return filteredN;
    }

    static ArrayList<Integer> GetRelativeIntervals(ArrayList<Integer> intervals) {
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        for (int i = 0; i < intervals.size(); i++) {
            if (i == 0) {
                filteredN.add(intervals.get(i));
            } else {
                filteredN.add(intervals.get(i)-intervals.get(i-1));
            }
        }
        return filteredN;
    }

    static ArrayList<Integer> GetAbsoluteIntervals(ArrayList<Integer> intervals, ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredN = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);
        for (int i = 0; i < intervals.size(); i++) {
            filteredN.add(intervals.get(i)-intervals.get(intervals.indexOf(last)));
        }
        return filteredN;
    }

    static ArrayList<Integer> NoRepetition(ArrayList<Integer> cantus) {
        ArrayList<Integer> filteredC = new ArrayList<Integer>();
        int last = cantus.get(cantus.size() - 1);
        for (int i = 0; i < cMajorNotes.size(); i++) {
            if (cMajorNotes.get(i) != last) {
                filteredC.add(cMajorNotes.get(i));
            }
        }
        return filteredC;
    }

    static ArrayList<Integer> NoDissonantLeaps(ArrayList<Integer> cantus) {
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

    public static ArrayList<Integer> majorIntervals = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19));

    public static ArrayList<Integer> cMajorNotes = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19));

    public static int[] majorInt = {0, 2, 4, 5, 7, 9, 11};
    //Scale cMajor = new Scale(0, majorIntervals);

    public static void main(String[] args) {
        //int[] input = {60, 62, 64, 60};
        ArrayList<Integer> input = new ArrayList<Integer>(Arrays.asList(0, 5, 0));

        // selected c major scale, transposed to c major then back to original
        ArrayList<Integer> ScaleNotes = GetScaleNotes(majorIntervals);
        System.out.println("ScaleNotes: " + ScaleNotes);
        System.out.println("ScaleNotes:  C  D  E  F  G  A  B   C   D   E   F   G");

        ArrayList<Integer> actualIntervals = GetRelativeIntervals(input);
        System.out.println("actualIntervals: " + actualIntervals);

        ArrayList<Integer> NonRepetedNotes = NoRepetition(input);
        System.out.println("NonRepetedNotes: " + NonRepetedNotes);

        ArrayList<Integer> AbsoluteIntervals = GetAbsoluteIntervals(cMajorNotes,input);
        System.out.println("AbsoluteIntervals: " + AbsoluteIntervals);

        ArrayList<Integer> NonDissonantLeapsNotes = NoDissonantLeaps(input);
        System.out.println("NonDissonantLeapsNotes: " + NonDissonantLeapsNotes);
    }
}
