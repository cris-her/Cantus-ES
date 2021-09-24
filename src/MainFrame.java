import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

public class MainFrame extends JFrame {
    private JComboBox comboBox1;
    private JPanel mainPanel;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JButton escucharButton;
    private JButton recomendarButton;
    private JButton reiniciarButton;
    private JButton button5;
    private JButton button6;
    private JButton button4;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button14;
    private JButton button15;
    private JLabel notesLabel;

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
                String notes = "C5 D5 A5 G5 E5 F5 D5 C5";
                notesLabel.setText(notes);
            }
        });

        reiniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notesLabel.setText("");
            }
        });

        escucharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        MainFrame CantusFrame = new MainFrame();
    }

}
