import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainFrame extends JFrame { // Clase MainFrame
    private JPanel mainPanel; // Panel principal
    private JComboBox longitudComboBox, escalaComboBox; // ComboBoxes para seleccionar longitud y escala
    private JButton escucharButton, recomendarButton, reiniciarButton; // Botones para escuchar, recomendar y reiniciar
    private JLabel cantusLabel, notaSeleccionada; // Etiquetas para mostrar cantus y nota seleccionada
    private JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12, btn13, btn14, btn15; // Botones para seleccionar notas
    private JButton btn; // Boton deshabilitado por el diseño de la UI
    public static ArrayList<Integer> cantusFirmusActual = new ArrayList<Integer>(); // ArrayList para cantus firmus actual

    public MainFrame() { // Constructor
        setContentPane(mainPanel); // Seteo el panel principal
        setTitle("Cantus firmus - Sistema experto"); // Seteo el titulo
        setSize(900, 300); // Seteo el tamaño de la ventana
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Seteo la operacion de cierre
        setVisible(true); // Seteo la visibilidad de la ventana

        recomendarButton.addActionListener(new ActionListener() { // Evento para recomendar
            @Override // Override
            public void actionPerformed(ActionEvent e) { // Metodo para recomendar
                if (Objects.equals(notaSeleccionada.getText(), "")) { return; } // Si no hay nota seleccionada, no hago nada

                longitudComboBox.setEnabled(false); // Deshabilito la longitud
                escalaComboBox.setEnabled(false); // Deshabilito la escala
                Cantus cantus=new Cantus(); // Creo un objeto de la clase Cantus
                cantusLabel.setText(cantusLabel.getText() + " " + notaSeleccionada.getText()); // Agrego la nota seleccionada al cantus
                cantusFirmusActual.add(cantus.id(notaSeleccionada.getText() )); // Agrego la nota seleccionada al cantus firmus actual
                String value = escalaComboBox.getSelectedItem().toString(); // Obtengo el valor de la escala
                ArrayList<Integer> ScaleNoteNumbers = cantus.obtenerNumeros(value); // Obtengo los numeros de las notas de la escala
                int cantusSize = Integer.parseInt(longitudComboBox.getSelectedItem().toString());; // Obtengo la longitud del cantus
                ArrayList<String> input = new ArrayList<String>(Arrays.asList(notaSeleccionada.getText())); // Creo un arraylist con la nota seleccionada
                System.out.println(input); // Imprimo la nota seleccionada

                ArrayList<Integer> NumericInput = new ArrayList<Integer>(); // Creo un arraylist para las notas numericas
                for (int i = 0; i < input.size(); i++) { // Recorro el arraylist de la nota seleccionada
                    NumericInput.add(cantus.id(input.get(i) )); // Agrego el numero de la nota al arraylist de notas numericas
                }

                ArrayList<Integer> Recommendation = cantus.recomendar(ScaleNoteNumbers, cantusFirmusActual, ScaleNoteNumbers, cantusSize); // Obtengo la recomendacion
                System.out.println("Recommendation: " + Recommendation); // Imprimo la recomendacion
                notaSeleccionada.setText(""); // Seteo la nota seleccionada en vacio

                ArrayList<String> RecommendationNoteNames =  cantus.obtenerNombres(Recommendation); // Obtengo los nombres de las notas de la recomendacion
                System.out.println("RecommendationNoteNames: " + RecommendationNoteNames); // Imprimo las notas de la recomendacion
                ArrayList<JButton> JButtons = new ArrayList<JButton>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15)); // Creo un arraylist con los botones

                for (int i = 0; i < JButtons.size(); i++) { // Recorro el arraylist de los botones
                    JButtons.get(i).setEnabled(false); // Deshabilito los botones
                    for (int j = 0; j < RecommendationNoteNames.size(); j++){ // Recorro el arraylist de las notas de la recomendacion
                        if (Objects.equals(JButtons.get(i).getText(), RecommendationNoteNames.get(j))) { // Si el texto del boton es igual a la nota de la recomendacion
                            JButtons.get(i).setEnabled(true); // Habilito el boton
                        }
                    }
                }

                if (cantusSize == cantusFirmusActual.size()) { // Si la longitud del cantus firmus actual es igual a la longitud del cantus
                    for (int i = 0; i < JButtons.size(); i++) { // Recorro el arraylist de los botones
                        JButtons.get(i).setEnabled(false); // Deshabilito los botones
                    }
                    recomendarButton.setEnabled(false); // Deshabilito el boton de recomendar
                }

                escucharButton.setEnabled(true); // Habilito el boton de escuchar
                recomendarButton.setEnabled(false); // Deshabilito el boton de recomendar

            }
        });
        reiniciarButton.addActionListener(new ActionListener() { // Evento para reiniciar
            @Override // Override
            public void actionPerformed(ActionEvent e) { // Metodo para reiniciar
                notaSeleccionada.setText(""); // Seteo la nota seleccionada en vacio
                cantusLabel.setText(""); // Seteo el cantus en vacio
                cantusFirmusActual = new ArrayList<Integer>(); // Seteo el cantus firmus actual en vacio
                ArrayList<JButton> JButtons = new ArrayList<JButton>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15)); // Creo un arraylist con los botones
                for (int i = 0; i < JButtons.size(); i++) { // Recorro el arraylist de los botones
                    JButtons.get(i).setEnabled(false); // Deshabilito los botones
                }
                btn1.setEnabled(true); // Habilito el boton 1
                btn8.setEnabled(true); // Habilito el boton 8
                btn15.setEnabled(true); // Habilito el boton 15
                recomendarButton.setEnabled(false); // Deshabilito el boton de recomendar
                longitudComboBox.setEnabled(true); // Habilito la longitud
                escalaComboBox.setEnabled(true); // Habilito la escala
                escucharButton.setEnabled(false); // Deshabilito el boton de escuchar

            }
        });
        escucharButton.addActionListener(new ActionListener() { // Evento para escuchar
            @Override // Override
            public void actionPerformed(ActionEvent e) { // Metodo para escuchar
                ArrayList<String> snnames =  Cantus.obtenerNombres(cantusFirmusActual); // Obtengo los nombres de las notas del cantus firmus actual
                System.out.println( snnames); // Imprimo los nombres de las notas del cantus firmus actual
                Cantus.tocar(snnames); // Toco las notas del cantus firmus actual
            }
        });
        escalaComboBox.addActionListener(new ActionListener() { // Evento para cambiar la escala
            @Override // Override
            public void actionPerformed(ActionEvent e) { // Metodo para cambiar la escala
                String value = escalaComboBox.getSelectedItem().toString(); // Obtengo el valor de la escala
                System.out.println(value);  // Imprimo el valor de la escala
                ArrayList<Integer> ScaleNoteNumbers = Cantus.obtenerNumeros(value); // Obtengo los numeros de las notas de la escala
                System.out.println("ScaleNotes: " + ScaleNoteNumbers); // Imprimo los numeros de las notas de la escala
                ArrayList<String> ScaleNoteNames =  Cantus.obtenerNombres(ScaleNoteNumbers); // Obtengo los nombres de las notas de la escala
                System.out.println("ScaleNotes: " + ScaleNoteNames ); // Imprimo los nombres de las notas de la escala

                ArrayList<JButton> JButtons = new ArrayList<JButton>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15)); // Creo un arraylist con los botones

                for (int i = 0; i < JButtons.size(); i++) { // Recorro el arraylist de los botones
                    JButtons.get(i).setText(ScaleNoteNames.get(i)); // Seteo el texto del boton con el nombre de la nota de la escala
                }
            }
        });
        btn1.addActionListener(new ActionListener() { // Evento para el boton
            @Override // Override
            public void actionPerformed(ActionEvent e) { // Metodo para el boton
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn1.getText())); // Creo un arraylist con la nota
                Cantus.tocar(notes); // Toco la nota
                notaSeleccionada.setText(btn1.getText()); // Seteo la nota seleccionada con la nota
                recomendarButton.setEnabled(true); // Habilito el boton de recomendar
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn2.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn2.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn3.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn3.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn4.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn4.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn5.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn5.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn6.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn6.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn7.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn7.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn8.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn8.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn9.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn9.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn10.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn10.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn11.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn11.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn12.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn12.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn13.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn13.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn14.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn14.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn15.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn15.getText());
                recomendarButton.setEnabled(true);
            }
        });
    }

    public static void LlenarComboBoxes(MainFrame obj) { // Metodo para llenar los comboboxes
        for (int i = 8; i <= 16; i++) { // Se recorren los comboboxes
            obj.longitudComboBox.addItem(i); // Se agregan los valores de 8 a 16
        }

        for (int i = 0; i < Cantus.nomenclatura.size(); i++) { // Se recorren las nomenclaturas
            obj.escalaComboBox.addItem(Cantus.nomenclatura.get(i)); // Se agregan las nomenclaturas
        }
    }
}
