import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox longitudComboBox;
    private JComboBox escalaComboBox;
    private JButton escucharButton;
    private JButton recomendarButton;
    private JButton reiniciarButton;
    private JButton btn4;
    private JButton btn3;
    private JButton btn2;
    private JButton btn1;
    private JButton btn5;
    private JButton btn6;
    private JButton btn8;
    private JButton btn9;
    private JButton btn10;
    private JButton btn11;
    private JButton btn12;
    private JButton btn13;
    private JLabel cantusLabel;
    private JButton btn7;
    private JButton btn;
    private JButton btn14;
    private JButton btn15;
    private JLabel notaSeleccionada;

    public MainFrame(){
        setContentPane(mainPanel);
        setTitle("Cantus firmus - Sistema experto");
        setSize(900, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        recomendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //tfname.getText
                //String notes = "6D, 6C#, 6E, 6G";
                longitudComboBox.setEnabled(false);
                escalaComboBox.setEnabled(false);
                Cantus cantus=new Cantus();
                cantusLabel.setText(cantusLabel.getText() + " " + notaSeleccionada.getText());

                currentCantusFirmus.add(cantus.id(notaSeleccionada.getText() ));

                String value = escalaComboBox.getSelectedItem().toString();
                //System.out.println(value);

                ArrayList<Integer> ScaleNoteNumbers = cantus.GetScaleNotes(value);
                //System.out.println("ScaleNotes: " + ScaleNoteNumbers);
                ArrayList<String> ScaleNoteNames =  cantus.GetNoteNames(ScaleNoteNumbers);
                //System.out.println("ScaleNotes: " + ScaleNoteNames );

                int cantusSize = Integer.parseInt(longitudComboBox.getSelectedItem().toString());;
                //System.out.println(cantusSize);
                ArrayList<String> input = new ArrayList<String>(Arrays.asList(notaSeleccionada.getText()));
                System.out.println(input);
                //nota inicial mostrada a escoger, nota final por recomendacion
                //System.out.println("Cantus: " + input);
                // selected c major scale, transposed to c major then back to original

                ArrayList<Integer> NumericInput = new ArrayList<Integer>();
                for (int i = 0; i < input.size(); i++) {
                    NumericInput.add(cantus.id(input.get(i) ));
                }

                ArrayList<Integer> Recommendation = cantus.Recommend(ScaleNoteNumbers, currentCantusFirmus, ScaleNoteNumbers, cantusSize);
                System.out.println("Recommendation: " + Recommendation);
                notaSeleccionada.setText("");

                ArrayList<String> RecommendationNoteNames =  cantus.GetNoteNames(Recommendation);
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

                if (cantusSize == currentCantusFirmus.size()) {
                    for (int i = 0; i < JButtons.size(); i++) {
                        JButtons.get(i).setEnabled(false);
                    }
                    recomendarButton.setEnabled(false);
                }

            }
        });

        reiniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notaSeleccionada.setText("");
                cantusLabel.setText("");
                currentCantusFirmus = new ArrayList<Integer>();
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


            }
        });

        escucharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Cantus cts=new Cantus();
                ArrayList<String> snnames =  cts.GetNoteNames(currentCantusFirmus);
                System.out.println( snnames);
                Synth sth = new Synth(snnames);
            }
        });

        escalaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String.valueOf(Math.random())
                //ArrayList<String> notes = new ArrayList<String>(Arrays.asList("C","D","E","F","G","A","B","C","D","E","F","G","Z", "X", "Y"));
                String value = escalaComboBox.getSelectedItem().toString();
                System.out.println(value);
                Cantus cantus=new Cantus();
                ArrayList<Integer> ScaleNoteNumbers = cantus.GetScaleNotes(value);
                System.out.println("ScaleNotes: " + ScaleNoteNumbers);
                ArrayList<String> ScaleNoteNames =  cantus.GetNoteNames(ScaleNoteNumbers);
                System.out.println("ScaleNotes: " + ScaleNoteNames );

                ArrayList<JButton> JButtons = new ArrayList<JButton>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15));

                for (int i = 0; i < JButtons.size(); i++) {
                    JButtons.get(i).setText(ScaleNoteNames.get(i));
                }

                //btn3.setEnabled(true);
            }
        });
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn1.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn1.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn2.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn2.getText());
            }
        });
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn3.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn3.getText());
            }
        });
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn4.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn4.getText());
            }
        });
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn5.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn5.getText());
            }
        });
        btn6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn6.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn6.getText());
            }
        });
        btn7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn7.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn7.getText());
            }
        });
        btn8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn8.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn8.getText());
                recomendarButton.setEnabled(true);
            }
        });
        btn9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn9.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn9.getText());
            }
        });
        btn10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn10.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn10.getText());
            }
        });
        btn11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn11.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn11.getText());
            }
        });
        btn12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn12.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn12.getText());
            }
        });
        btn13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn13.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn13.getText());
            }
        });
        btn14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn14.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn14.getText());
            }
        });
        btn15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList(btn15.getText()));
                Synth sth = new Synth(notes);

                notaSeleccionada.setText(btn15.getText());
                recomendarButton.setEnabled(true);
            }
        });

    }

    public static ArrayList<Integer> currentCantusFirmus = new ArrayList<Integer>();

    public static void main(String[] args) {
        MainFrame CantusFrame = new MainFrame();
        for (int i = 8; i <= 16; i++) {
            CantusFrame.longitudComboBox.addItem(i);
        }
        ArrayList<String> notes = new ArrayList<String>(Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"));
        for (int i = 0; i < notes.size(); i++) {
            CantusFrame.escalaComboBox.addItem(notes.get(i));
        }



        //Object selectedScale = CantusFrame.escalaComboBox.getSelectedObjects();

        /*
        Cantus cantus=new Cantus();
        ArrayList<Integer> ScaleNoteNumbers = cantus.GetScaleNotes(selectedScale.toString());
        System.out.println("ScaleNotes: " + ScaleNoteNumbers);

        ArrayList<String> ScaleNoteNames =  cantus.GetNoteNames(ScaleNoteNumbers);
        System.out.println("ScaleNotes: " + ScaleNoteNames );
        */
    }

}
