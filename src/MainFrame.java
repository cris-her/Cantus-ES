import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

public class MainFrame extends JFrame {
    private JComboBox notaFinalComboBox;
    private JPanel mainPanel;
    private JComboBox longitudComboBox;
    private JComboBox escalaComboBox;
    private JComboBox notaInicialComboBox;
    private JButton escucharButton;
    private JButton recomendarButton;
    private JButton reiniciarButton;
    private JButton btn4;
    private JButton btn3;
    private JButton btn2;
    private JButton btn1;
    private JButton btn5;
    private JButton btn6;
    private JButton btn7;
    private JButton btn8;
    private JButton btn9;
    private JButton btn10;
    private JButton btn11;
    private JButton btn12;
    private JLabel cantusLabel;

    public MainFrame(){
        setContentPane(mainPanel);
        setTitle("Cantus firmus - Sistema experto");
        setSize(650, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        recomendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //tfname.getText
                String notes = "6D, 6C#, 6E, 6G";
                cantusLabel.setText(notes);
            }
        });

        reiniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantusLabel.setText("");
            }
        });

        escucharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList("6D","6C#","6E","6G"));
                Synth sth = new Synth(notes);
            }
        });

        escalaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String.valueOf(Math.random())
                ArrayList<String> notes = new ArrayList<String>(Arrays.asList("C","D","E","F","G","A","B","C","D","E","F","G"));
                ArrayList<JButton> JButtons = new ArrayList<JButton>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12));

                for (int i = 0; i < JButtons.size(); i++) {
                    JButtons.get(i).setText(notes.get(i));
                }

            }
        });
    }

    public static void main(String[] args) {
        MainFrame CantusFrame = new MainFrame();
        for (int i = 8; i <= 16; i++) {
            CantusFrame.longitudComboBox.addItem(i);
        }
        ArrayList<String> notes = new ArrayList<String>(Arrays.asList("C","D","E","F","G","A","B"));
        for (int i = 0; i < notes.size(); i++) {
            CantusFrame.escalaComboBox.addItem(notes.get(i));
            CantusFrame.notaInicialComboBox.addItem(notes.get(i));
            CantusFrame.notaFinalComboBox.addItem(notes.get(i));
        }
        //notaInicialComboBox.getSelectedObjects()
    }

}
