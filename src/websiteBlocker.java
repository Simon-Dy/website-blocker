import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class websiteBlocker extends JFrame {

    private JTextArea textArea;

    public websiteBlocker() {
        // Setzt die Grundkonfiguration für das JFrame
        setTitle("Text Editor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erstellt eine JTextArea für den Text
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Erstellt einen JButton zum Speichern des Textes
        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speichern();
            }
        });

        // Fügt die Komponenten zum Layout hinzu
        getContentPane().add(scrollPane, "Center");
        getContentPane().add(saveButton, "South");
    }

    private void speichern() {
    	 String filePath = "../website-blocker/Blacklist.txt";
        // Zeigt einen Dialog zum Speichern an
        //JFileChooser fileChooser = new JFileChooser();
        //int result = fileChooser.showSaveDialog(this);

        // Wenn der Benutzer auf "Speichern" klickt
        if (filePath != null) {
            try {
                // Erstellt eine BufferedWriter, um in die Datei zu schreiben
            	//BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                // Schreibt den Text aus der JTextArea in die Datei
                writer.write(textArea.getText());
                writer.newLine();
                // Schließt den Writer
                writer.close();
                JOptionPane.showMessageDialog(this, "Text erfolgreich gespeichert.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Datei.");
            }
        }
    }

    public static void main(String[] args) {
        // Erstellt eine Instanz der GUI und zeigt sie an
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new websiteBlocker().setVisible(true);
            }
        });
    }
}