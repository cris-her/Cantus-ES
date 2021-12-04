import java.util.Arrays;
import java.util.List;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;
import java.util.*;

public class Cantus { // Clase cantus
    private static MidiChannel[] channels; // Array de canales MIDI
    private static int instrumento = 0; // de 0 - 9 (0 es piano)
    private static int volumen = 80; // de 0 - 127
    public static ArrayList<Integer> intervalos = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24)); // Array de intervalos
    public static List<String> nomenclatura = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"); // Array de nomenclatura
    public static int id(String note) { // Funcion que devuelve el id de la nota
        int octave = Integer.parseInt(note.substring(0, 1)); // Obtenemos la octava
        return nomenclatura.indexOf(note.substring(1)) + 12 * octave + 12; // Devolvemos el id
    }
    public static String note(int id) { // Funcion que devuelve la nota
        int octave = (id - 12) / 12; // Obtenemos la octava
        String note = nomenclatura.get( (id - 12) % 12 ); // Obtenemos la nota
        return octave + note; // Devolvemos la nota en formato octava + nota
    }
    public static ArrayList<Integer> obtenerNumeros(String note) { // Funcion que devuelve los numeros de la nota
        ArrayList<Integer> filteredN = new ArrayList<Integer>(); // Array de numeros
        int startNote = id(4+note); // Obtenemos el id de la nota
        for (int i = 0; i < intervalos.size(); i++) { // Recorremos el array de intervalos
                filteredN.add(intervalos.get(i)+startNote); // Añadimos el numero al array de numeros
        }
        return filteredN; // Devolvemos el array de numeros
    }
    public static ArrayList<String> obtenerNombres(ArrayList<Integer> noteNumbers) { // Funcion que devuelve los nombres de las notas
        ArrayList<String> ScaleNoteNames = new ArrayList<String>(); // Array de nombres
        for (int i = 0; i < noteNumbers.size(); i++) { // Recorremos el array de numeros
            ScaleNoteNames.add(note(noteNumbers.get(i))); // Añadimos el nombre de la nota al array de nombres
        }
        return ScaleNoteNames; // Devolvemos el array de nombres
    }
    private ArrayList<Integer> intervalosRelativos(ArrayList<Integer> intervals) { // Funcion que devuelve los intervalos relativos
        ArrayList<Integer> filteredN = new ArrayList<Integer>(); // Array de intervalos relativos
        for (int i = 0; i < intervals.size(); i++) { // Recorremos el array de intervalos
            if (i == 0) { // Si es el primer intervalo (0)
                filteredN.add(0); // Añadimos el intervalo relativo 
            } else { // Si no
                filteredN.add(intervals.get(i)-intervals.get(i-1)); // Añadimos el intervalo relativo
            }
        }
        return filteredN; // Devolvemos el array de intervalos relativos
    }
    private ArrayList<Integer> intervalosAbsolutos(ArrayList<Integer> scale, ArrayList<Integer> cantus) { // Funcion que devuelve los intervalos absolutos
        ArrayList<Integer> filteredN = new ArrayList<Integer>(); // Array de intervalos absolutos
        int last = cantus.get(cantus.size() - 1); // Obtenemos el ultimo numero del cantus
        for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
            filteredN.add(scale.get(i)-last); // Añadimos el intervalo absoluto
        }
        return filteredN; // Devolvemos el array de intervalos absolutos
    }
    public static void sonar(String note, int duration) throws InterruptedException { // Funcion que sona la nota
        channels[instrumento].noteOn(id(note), volumen); // Sonamos la nota
        Thread.sleep( duration ); // Esperamos durante la duracion de la nota
        channels[instrumento].noteOff(id(note)); // Detenemos la nota
    }
    public static void tocar(ArrayList<String> notesToPlay) { // Funcion que toca las notas
        try { // Intentamos
            Synthesizer synth = MidiSystem.getSynthesizer(); // Obtenemos el sintetizador
            synth.open(); // Abrimos el sintetizador
            channels = synth.getChannels(); // Obtenemos los canales MIDI
            for (int i = 0; i < notesToPlay.size(); i++) { // Recorremos el array de notas
                sonar(notesToPlay.get(i),  1000); // Sonamos la nota
            }
            synth.close(); // Cerramos el sintetizador
        }
        catch (Exception e) { // Si hay error
            throw new RuntimeException(e); // Lanzamos una excepcion
        }

    }

    // "R E G L A S"
    private ArrayList<Integer> noRepeticion(ArrayList<Integer> scale, ArrayList<Integer> cantus) { // Funcion que devuelve los intervalos sin repeticion
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de intervalos sin repeticion
        int last = cantus.get(cantus.size() - 1); // Obtenemos el ultimo numero del cantus
        for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
            if (scale.get(i) != last) { // Si no es el ultimo numero
                filteredC.add(scale.get(i)); // Añadimos el intervalo sin repeticion
            }
        }
        return filteredC; // Devolvemos el array de intervalos sin repeticion
    }
    private ArrayList<Integer> noIntervalosCompuestos(ArrayList<Integer> scale, ArrayList<Integer> cantus) { // Funcion que devuelve los intervalos sin compuestos
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de intervalos sin compuestos
        ArrayList<Integer> AbsoluteIntervals = intervalosAbsolutos(scale,cantus); // Obtenemos los intervalos absolutos
        for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
            if (Math.abs(AbsoluteIntervals.get(i)) <= 12) { // Si no es un intervalo compuesto
                filteredC.add(scale.get(i)); // Añadimos el intervalo sin compuesto
            }
        }
        return filteredC; // Devolvemos el array de intervalos sin compuestos
    }
    private ArrayList<Integer> noSaltosDisonantes(ArrayList<Integer> scale, ArrayList<Integer> cantus) { // Funcion que devuelve los intervalos sin saltos disonantes
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de intervalos sin saltos disonantes
        ArrayList<Integer> AbsoluteIntervals = intervalosAbsolutos(scale,cantus); // Obtenemos los intervalos absolutos
        for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
            if (Math.abs(AbsoluteIntervals.get(i)) == 11 || Math.abs(AbsoluteIntervals.get(i)) == 10 || Math.abs(AbsoluteIntervals.get(i)) == 6) { // Si no es un intervalo de saltos disonantes
                continue; // Continuamos
            } else { // Si no
                filteredC.add(scale.get(i)); // Añadimos el intervalo sin saltos disonantes
            }
        }
        return filteredC; // Devolvemos el array de intervalos sin saltos disonantes
    }
    private ArrayList<Integer> tieneClimaxUnico(ArrayList<Integer> scale, ArrayList<Integer> cantus) { // Funcion que devuelve los intervalos con climax unico
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de intervalos con climax unico
        Integer max = Collections.max(cantus); // Obtenemos el maximo del cantus
        System.out.println("Max: " + max); // Mostramos el maximo del cantus
        for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
            if (scale.get(i) != max) { // Si no es el maximo del cantus
                filteredC.add(scale.get(i)); // Añadimos el intervalo con climax unico
            }
        }
        return filteredC; // Devolvemos el array de intervalos con climax unico
    }
    private ArrayList<Integer> entreDosYCuatroSaltos(ArrayList<Integer> scale, ArrayList<Integer> cantus) { // Funcion que devuelve los intervalos entre dos y cuatro saltos
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de intervalos entre dos y cuatro saltos
        ArrayList<Integer> RelativeIntervals = intervalosRelativos(cantus); // Obtenemos los intervalos relativos
        long count = RelativeIntervals.stream().filter(interv -> Math.abs(interv) >= 3).count(); // Contamos los intervalos que cumplen la condicion
        ArrayList<Integer> AbsoluteIntervals = intervalosAbsolutos(scale,cantus); // Obtenemos los intervalos absolutos
        System.out.println("count: " + count); // Mostramos el numero de intervalos que cumplen la condicion

        if (count == 3) { // Si hay tres intervalos que cumplen la condicion
            for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
                if (Math.abs(AbsoluteIntervals.get(i)) <= 2) { // Si no es un intervalo entre dos y cuatro saltos
                    filteredC.add(scale.get(i)); // Añadimos el intervalo entre dos y cuatro saltos
                }
            }
        } else { // Si no
            filteredC = scale; // Devolvemos el array de notas
        }

        return filteredC; // Devolvemos el array de intervalos entre dos y cuatro saltos
    }
    private int checkLongRun(ArrayList<Integer> intervals) { // Funcion que devuelve el numero de intervalos que cumplen la condicion
        System.out.println(intervals); // Mostramos el array de intervalos
        ArrayList<Integer> myLast4Intervals = new ArrayList<Integer>( intervals.subList(intervals.size()-4, intervals.size()) ); // Obtenemos los ultimos 4 intervalos
        long countP = myLast4Intervals.stream().filter(interv -> interv >= 1 && interv <= 2).count(); // Contamos los intervalos que cumplen la condicion
        System.out.println(countP); // Mostramos el numero de intervalos que cumplen la condicion
        long countN = myLast4Intervals.stream().filter(interv -> interv <= -1 && interv >= -2).count(); // Contamos los intervalos que cumplen la condicion
        System.out.println(countN); // Mostramos el numero de intervalos que cumplen la condicion

        if (countP == 3) // Si hay tres intervalos que cumplen la condicion
        {
            return 0; // Devolvemos 0
        }
        else if (countN == 3) // Si hay tres intervalos que cumplen la condicion
        {
            return 1; // Devolvemos 1
        }
        else // Si no
        {
            return 2; // Devolvemos 2
        }
    }
    private ArrayList<Integer> noLongRuns(ArrayList<Integer> scale, ArrayList<Integer> cantus) { // Funcion que devuelve los intervalos sin long runs
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de intervalos sin long runs
        ArrayList<Integer> RelativeIntervals = intervalosRelativos(cantus); // Obtenemos los intervalos relativos

        if (cantus.size() >= 4) { // Si hay mas de 4 notas
            int result = checkLongRun(RelativeIntervals); // Obtenemos el resultado de la funcion checkLongRun

            if (result == 0) { // Si el resultado es 0
                for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
                    if (scale.get(i) < cantus.get(cantus.size() - 1)) { // Si la nota es menor que la ultima nota del cantus
                        filteredC.add(scale.get(i)); // Añadimos la nota
                    }
                }

            }else if (result == 1){ // Si el resultado es 1
                for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
                    if (scale.get(i) > cantus.get(cantus.size() - 1)) { // Si la nota es mayor que la ultima nota del cantus
                        filteredC.add(scale.get(i)); // Añadimos la nota
                    }
                }
            } else { // Si el resultado es 2
                filteredC = scale; // Devolvemos el array de notas
            }
        } else { // Si no
            filteredC = scale; // Devolvemos el array de notas
        }

        return filteredC; // Devolvemos el array de intervalos sin long runs
    }
    private ArrayList<Integer> saltoYCambioDeDireccion(ArrayList<Integer> scale, ArrayList<Integer> cantus) { // Funcion que devuelve los intervalos con salto y cambio de direccion
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de intervalos con salto y cambio de direccion
        ArrayList<Integer> RelativeIntervals = intervalosRelativos(cantus); // Obtenemos los intervalos relativos

        if (RelativeIntervals.get(RelativeIntervals.size()-1) >= 3) { // Si el ultimo intervalo es mayor o igual a 3
            for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
                if (scale.get(i) < cantus.get(cantus.size() - 1) && scale.get(i) >= cantus.get(cantus.size() - 1) - 2) { // Si la nota es menor que la ultima nota del cantus y es mayor o igual a la ultima nota del cantus - 2
                    filteredC.add(scale.get(i)); // Añadimos la nota
                }
            }
        }else if (RelativeIntervals.get(RelativeIntervals.size()-1) <= -3){ // Si el ultimo intervalo es menor o igual a -3
            for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
                if (scale.get(i) > cantus.get(cantus.size() - 1) && scale.get(i) <= cantus.get(cantus.size() - 1) + 2) { // Si la nota es mayor que la ultima nota del cantus y es menor o igual a la ultima nota del cantus + 2
                    filteredC.add(scale.get(i)); // Añadimos la nota
                }
            }
        } else { // Si no
            filteredC = scale; // Devolvemos el array de notas
        }

        return filteredC; // Devolvemos el array de intervalos con salto y cambio de direccion
    }
    private int tonicaMasCercana(ArrayList<Integer> cantus, ArrayList<Integer> snn) { // Funcion que devuelve la nota mas cercana a la tonica
        ArrayList<Integer> aux = new ArrayList<Integer>(); // Array auxiliar
        aux.add(Math.abs( snn.get(0) - cantus.get(cantus.size()-1))); // Añadimos el valor absoluto del primer intervalo
        aux.add(Math.abs( snn.get(7) - cantus.get(cantus.size()-1))); // Añadimos el valor absoluto del ultimo intervalo
        aux.add(Math.abs( snn.get(14) - cantus.get(cantus.size()-1))); // Añadimos el valor absoluto del ultimo intervalo
        Integer min = Collections.min(aux); // Obtenemos el minimo del array auxiliar
        int tonic = aux.indexOf(min); // Obtenemos el indice del minimo del array auxiliar
        if (tonic == 0) // Si el indice es 0
        {
            return snn.get(0); // Devolvemos la nota mas cercana a la tonica
        } else if (tonic == 1) // Si el indice es 1
        {
            return snn.get(7); // Devolvemos la nota mas cercana a la tonica
        }
        else // Si el indice es 2
        {
            return snn.get(14); // Devolvemos la nota mas cercana a la tonica
        }
    }
    private ArrayList<Integer> FinalNoteApproachedByStep(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn, int cantusSize) { // Funcion que devuelve las notas que se acercan a la ultima nota del cantus
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de notas que se acercan a la ultima nota del cantus
        int closestTonic = tonicaMasCercana( cantus, snn); // Obtenemos la nota mas cercana a la tonica
        System.out.println(closestTonic ); // Imprimimos la nota mas cercana a la tonica
        if (cantusSize - 3 == cantus.size()) { // Si el tamaño del cantus es 3

            for (int i = 0; i < scale.size(); i++) { // Recorremos el array de notas
                if (scale.get(i) != snn.get(1) && scale.get(i) != snn.get(6) && scale.get(i) != snn.get(13)) { // Si la nota no es igual a la nota mas cercana a la tonica
                    filteredC.add(scale.get(i)); // Añadimos la nota
                }
            }

        } else if (cantusSize - 2 == cantus.size()) // Si el tamaño del cantus es 2
        {
            System.out.println(snn); // Imprimimos el array de notas

            for (int i = 0; i < snn.size(); i++) { // Recorremos el array de notas
                if (snn.get(i) >= closestTonic - 2 && snn.get(i) <= closestTonic + 2 && snn.get(i) != closestTonic) { // Si la nota es mayor o igual que la nota mas cercana a la tonica - 2 y menor o igual que la nota mas cercana a la tonica + 2 y es diferente a la nota mas cercana a la tonica
                    filteredC.add(snn.get(i)); // Añadimos la nota
                }
            }

        }
        else { // Si el tamaño del cantus es 1
            filteredC = scale; // Devolvemos el array de notas
        }
        return filteredC; // Devolvemos el array de notas que se acercan a la ultima nota del cantus
    }
    private ArrayList<Integer> sensibleVaALaTonica(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn) { // Funcion que devuelve las notas que son sensible a la tonica
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de notas que son sensible a la tonica
        int last = cantus.get(cantus.size() - 1); // Obtenemos la ultima nota del cantus

        if (last == snn.get(6) || last == snn.get(13)) { // Si la ultima nota del cantus es igual a la nota mas cercana a la tonica

            filteredC.add(tonicaMasCercana( cantus, snn)); // Añadimos la nota mas cercana a la tonica
        } else {
            filteredC = scale; // Devolvemos el array de notas
        }
        return filteredC; // Devolvemos el array de notas que son sensible a la tonica
    }
    private ArrayList<Integer> ultimaEsTonica(ArrayList<Integer> scale, ArrayList<Integer> cantus, ArrayList<Integer> snn, int cantusSize) { // Funcion que devuelve las notas que son la ultima es la tonica
        ArrayList<Integer> filteredC = new ArrayList<Integer>(); // Array de notas que son la ultima es la tonica
        if (cantusSize - 1 == cantus.size()) // Si el tamaño del cantus es 1
        {
            filteredC.add(tonicaMasCercana( cantus, snn)); // Añadimos la nota mas cercana a la tonica
        } else { // Si el tamaño del cantus es mayor que 1
            filteredC = scale; // Devolvemos el array de notas
        }
        return filteredC; // Devolvemos el array de notas que son la ultima es la tonica
    }

    public ArrayList<Integer> recomendar(ArrayList<Integer> scale, ArrayList<Integer> NumericInput, ArrayList<Integer> ScaleNoteNumbers, int cantusSize) { // Funcion que devuelve las notas que se acercan a la ultima nota del cantus
        ArrayList<Integer> NonRepetedNotes = noRepeticion(ScaleNoteNumbers,NumericInput); // Array de notas que no se repiten
        System.out.println("NonRepetedNotes: " + NonRepetedNotes); // Imprimimos el array de notas que no se repiten

        ArrayList<Integer> NonLeapsLargerThanOctaveNotes = noIntervalosCompuestos(NonRepetedNotes, NumericInput); // Array de notas que no sean intervalos compuestos
        System.out.println("NonLeapsLargerThanOctaveNotes: " + NonLeapsLargerThanOctaveNotes); // Imprimimos el array de notas que no sean intervalos compuestos

        ArrayList<Integer> NonDissonantLeapsNotes = noSaltosDisonantes(NonLeapsLargerThanOctaveNotes, NumericInput); // Array de notas que no sean saltos disonantes
        System.out.println("NonDissonantLeapsNotes: " + NonDissonantLeapsNotes); // Imprimimos el array de notas que no sean saltos disonantes

        ArrayList<Integer> HasUniqueClimaxNotes = tieneClimaxUnico(NonDissonantLeapsNotes, NumericInput); // Array de notas que tiene climax unico
        System.out.println("HasUniqueClimaxNotes: " + HasUniqueClimaxNotes); // Imprimimos el array de notas que tiene climax unico

        ArrayList<Integer> BetweenTwoAndFourLeapsNotes = entreDosYCuatroSaltos(HasUniqueClimaxNotes, NumericInput); // Array de notas que estan entre dos y cuatro saltos
        System.out.println("BetweenTwoAndFourLeapsNotes: " + BetweenTwoAndFourLeapsNotes); // Imprimimos el array de notas que estan entre dos y cuatro saltos

        ArrayList<Integer> NoLongRunsNotes = noLongRuns(BetweenTwoAndFourLeapsNotes, NumericInput); // Array de notas que no tienen long runs
        System.out.println("NoLongRunsNotes: " + NoLongRunsNotes); // Imprimimos el array de notas que no tienen long runs

        ArrayList<Integer> LargerLeapsFollowedByChangeOfDirectionNotes = saltoYCambioDeDireccion(NoLongRunsNotes, NumericInput); // Array de notas que tienen saltos y cambios de direccion
        System.out.println("LargerLeapsFollowedByChangeOfDirectionNotes: " + LargerLeapsFollowedByChangeOfDirectionNotes); // Imprimimos el array de notas que tienen saltos y cambios de direccion

        ArrayList<Integer> FinalNoteApproachedByStepNotes = FinalNoteApproachedByStep(LargerLeapsFollowedByChangeOfDirectionNotes, NumericInput, ScaleNoteNumbers, cantusSize); // Array de notas que se acercan a la ultima nota del cantus
        System.out.println("FinalNoteApproachedByStepNotes: " + FinalNoteApproachedByStepNotes); // Imprimimos el array de notas que se acercan a la ultima nota del cantus

        ArrayList<Integer> LeadingNoteGoesToTonicNotes = sensibleVaALaTonica(FinalNoteApproachedByStepNotes, NumericInput, ScaleNoteNumbers); // Array de notas que son sensible a la tonica
        System.out.println("LeadingNoteGoesToTonicNotes: " + LeadingNoteGoesToTonicNotes); // Imprimimos el array de notas que son sensible a la tonica

        ArrayList<Integer> LastNoteIsTonicNotes = ultimaEsTonica(LeadingNoteGoesToTonicNotes, NumericInput, ScaleNoteNumbers, cantusSize); // Array de notas que son la ultima es la tonica
        System.out.println("LastNoteIsTonicNotes: " + LastNoteIsTonicNotes); // Imprimimos el array de notas que son la ultima es la tonica

        return LastNoteIsTonicNotes; // Devolvemos el array de notas que son la ultima es la tonica

    }
}
