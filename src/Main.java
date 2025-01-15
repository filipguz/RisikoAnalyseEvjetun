import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends JFrame {

    private JComboBox<String> skadeTypeComboBox;
    private JSpinner sannsynlighetSpinner;
    private JSpinner konsekvensSpinner;
    private JLabel risikoLabel;
    private JLabel vurderingLabel;
    private JTextField aktivitetField;
    private JTextField risikoMoment;
    private JLabel risikoFargeLabel;

    public Main() {
        setTitle("Risikoanalyse for Leirskole");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new GridLayout(8, 2, 10, 10));
        ImageIcon appIcon = new ImageIcon("logo.png"); // Bytt ut med din ikonfil
        setIconImage(appIcon.getImage());

        // GUI-komponenter
        add(new JLabel("Skriv inn aktivitet:"));
        aktivitetField = new JTextField();
        add(aktivitetField);

        add(new JLabel("Skriv inn risikomoment"));
        risikoMoment = new JTextField();
        add(risikoMoment);

        add(new JLabel("Velg type skade:"));
        skadeTypeComboBox = new JComboBox<>(new String[]{"Personskade (P)", "Miljøskade (M)", "Materiellskade (MP)"});
        add(skadeTypeComboBox);

        add(new JLabel("Sannsynlighet (0-5):"));
        sannsynlighetSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        add(sannsynlighetSpinner);

        add(new JLabel("Konsekvens (0-5):"));
        konsekvensSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        add(konsekvensSpinner);

        JButton beregnButton = new JButton("Beregn risiko");
        add(beregnButton);

        JButton lagreKnapp = new JButton("Lagre risikoanalyse");
        add (lagreKnapp);
        // JButton lagreButton = new JButton("Lagre risikoanalyse");
       // add(lagreButton);

        risikoFargeLabel = new JLabel(" ", SwingConstants.CENTER);  // Setter grafisk farge
        risikoFargeLabel.setOpaque(true);
        add(new JLabel("Risiko farge:"));
        add(risikoFargeLabel);

        risikoLabel = new JLabel("Risiko: ");
        add(risikoLabel);

        vurderingLabel = new JLabel("Vurdering: ");
        add(vurderingLabel);

        // Logikk for beregning
        beregnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sannsynlighet = (int) sannsynlighetSpinner.getValue();
                int konsekvens = (int) konsekvensSpinner.getValue();
                int risiko = sannsynlighet * konsekvens;

                risikoLabel.setText("Risiko: " + risiko);
                vurderingLabel.setText("Vurdering: " + vurderRisiko(risiko));

                // Sett farge basert på risiko
                risikoFargeLabel.setBackground(bestemRisikoFarge(risiko));
            }
        });



        // logikk for lagring av Risikoanalysen
        lagreKnapp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aktivitet = aktivitetField.getText();
                String skadeType = (String) skadeTypeComboBox.getSelectedItem();
                int sannsynlighet = (int) sannsynlighetSpinner.getValue();
                int konsekvens = (int) konsekvensSpinner.getValue();
                int risiko = sannsynlighet * konsekvens;
                String vurdering = vurderRisiko(risiko);

                String data = "Risikoanalyse\n" +
                        "Aktivitet: " + aktivitet + "\n" +
                        "Type skade: " + skadeType + "\n" +
                        "Sannsynlighet: " + sannsynlighet + "\n" +
                        "Konsekvens: " + konsekvens + "\n" +
                        "Risiko: " + risiko + "\n" +
                        "Vurdering: " + vurdering + "\n";


                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Velg hvor du vil lagre risikoanalysen");
                int userSelection = fileChooser.showSaveDialog(Main.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        writer.write(data);
                        JOptionPane.showMessageDialog(Main.this, "Risikoanalyse lagret!", "Suksess", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Main.this, "Kunne ikke lagre risikoanalysen!", "Feil", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        setLocationRelativeTo(null); // Sentrer vinduet
        setVisible(true);

    }

    private String vurderRisiko(int risiko) {
        if (risiko == 0) {
            return "Ingen risiko (ubetydelig)";
        } else if (risiko <= 5) {
            return "Lav risiko";
        } else if (risiko <= 15) {
            return "Moderat risiko";
        } else if (risiko <= 25) {
            return "Høy risiko";
        } else {
            return "Svært høy risiko (kritisk)";
        }
    }

    private Color bestemRisikoFarge(int risiko) {
        if (risiko == 0) {
            return Color.WHITE; // Ingen risiko
        } else if (risiko <= 5) {
            return Color.GREEN; // Lav risiko
        } else if (risiko <= 15) {
            return Color.YELLOW; // Moderat risiko
        } else if (risiko <= 25) {
            return Color.RED; // Høy risiko
        } else {
            return Color.RED; // Svært høy risiko
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}