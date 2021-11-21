import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox longitudComboBox, escalaComboBox;
    private JButton escucharButton, recomendarButton, reiniciarButton;
    private JLabel cantusLabel, notaSeleccionada;
    private JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12, btn13, btn14, btn15;
    private JButton btn;
    public static ArrayList<Integer> cantusFirmusActual = new ArrayList<Integer>();

    public MainFrame() {
        setContentPane(mainPanel);
        setTitle("Cantus firmus - Sistema experto");
        setSize(900, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        recomendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(notaSeleccionada.getText(), "")) { return; }

                longitudComboBox.setEnabled(false);
                escalaComboBox.setEnabled(false);
                Cantus cantus=new Cantus();
                cantusLabel.setText(cantusLabel.getText() + " " + notaSeleccionada.getText());
                cantusFirmusActual.add(cantus.id(notaSeleccionada.getText() ));
                String value = escalaComboBox.getSelectedItem().toString();
                ArrayList<Integer> ScaleNoteNumbers = cantus.obtenerNumeros(value);
                int cantusSize = Integer.parseInt(longitudComboBox.getSelectedItem().toString());;
                ArrayList<String> input = new ArrayList<String>(Arrays.asList(notaSeleccionada.getText()));
                System.out.println(input);

                ArrayList<Integer> NumericInput = new ArrayList<Integer>();
                for (int i = 0; i < input.size(); i++) {
                    NumericInput.add(cantus.id(input.get(i) ));
                }

                ArrayList<Integer> Recommendation = cantus.recomendar(ScaleNoteNumbers, cantusFirmusActual, ScaleNoteNumbers, cantusSize);
                System.out.println("Recommendation: " + Recommendation);
                notaSeleccionada.setText("");

                ArrayList<String> RecommendationNoteNames =  cantus.obtenerNombres(Recommendation);
                System.out.println("RecommendationNoteNames: " + RecommendationNoteNames);
                ArrayList<JButton> JButtons = new ArrayList<JButton>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15));

                for (int i = 0; i < JButtons.size(); i++) {
                    JButtons.get(i).setEnabled(false);
                    for (int j = 0; j < RecommendationNoteNames.size(); j++){
                        if (Objects.equals(JButtons.get(i).getText(), RecommendationNoteNames.get(j))) {
                            JButtons.get(i).setEnabled(true);
                        }
                    }
                }

                if (cantusSize == cantusFirmusActual.size()) {
                    for (int i = 0; i < JButtons.size(); i++) {
                        JButtons.get(i).setEnabled(false);
                    }
                    recomendarButton.setEnabled(false);
                }

                escucharButton.setEnabled(true);
                recomendarButton.setEnabled(false);

            }
        });
        reiniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notaSeleccionada.setText("");
                cantusLabel.setText("");
                cantusFirmusActual = new ArrayList<Integer>();
                ArrayList<JButton> JButtons = new ArrayList<JButton>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15));
                for (int i = 0; i < JButtons.size(); i++) {
                    JButtons.get(i).setEnabled(false);
                }
                btn1.setEnabled(true);
                btn8.setEnabled(true);
                btn15.setEnabled(true);
                recomendarButton.setEnabled(false);
                longitudComboBox.setEnabled(true);
                escalaComboBox.setEnabled(true);
                escucharButton.setEnabled(false);

            }
        });
        escucharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> snnames =  Cantus.obtenerNombres(cantusFirmusActual);
                System.out.println( snnames);
                Cantus.tocar(snnames);
            }
        });
        escalaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = escalaComboBox.getSelectedItem().toString();
                System.out.println(value);
                ArrayList<Integer> ScaleNoteNumbers = Cantus.obtenerNumeros(value);
                System.out.println("ScaleNotes: " + ScaleNoteNumbers);
                ArrayList<String> ScaleNoteNames =  Cantus.obtenerNombres(ScaleNoteNumbers);
                System.out.println("ScaleNotes: " + ScaleNoteNames );

                ArrayList<JButton> JButtons = new ArrayList<JButton>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15));

                for (int i = 0; i < JButtons.size(); i++) {
                    JButtons.get(i).setText(ScaleNoteNames.get(i));
                }
            }
        });
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn1.getText()));
                Cantus.tocar(notes);
                notaSeleccionada.setText(btn1.getText());
                recomendarButton.setEnabled(true);
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

    public static void LlenarComboBoxes(MainFrame obj) {
        for (int i = 8; i <= 16; i++) {
            obj.longitudComboBox.addItem(i);
        }

        for (int i = 0; i < Cantus.nomenclatura.size(); i++) {
            obj.escalaComboBox.addItem(Cantus.nomenclatura.get(i));
        }
    }
}
