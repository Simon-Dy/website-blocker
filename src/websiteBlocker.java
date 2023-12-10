import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

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
    
    // Wenn der Benutzer auf "Speichern" klickt
    private void speichern() {
    	String filePath = "../website-blocker/hosts.txt";
    	String filePathSave = "../website-blocker/Blacklist.txt";
    	
        // Zeigt einen Dialog zum Speichern an
        //JFileChooser fileChooser = new JFileChooser();
        //int result = fileChooser.showSaveDialog(this);
         
    	 boolean positionFound = false;
    	 long position = 0;
    	 String searchWord = "#Website-Blacklist";
    	 
         try {
             // Öffnet die Datei zum Lesen
        	 RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");

             String line;
             int lineNumber = 0;

             // Durchsucht jede Zeile nach dem gesuchten Wort
             while ((line = randomAccessFile.readLine()) != null) {
                 lineNumber++;

                 // Überprüft, ob das gesuchte Wort in der Zeile enthalten ist
                 if (line.contains(searchWord)) {
                     //System.out.println("Das Wort '" + searchWord + "' wurde in Zeile " + lineNumber + " gefunden.");
                     // Hier können Sie weitere Aktionen durchführen, z.B. die Position des Wortes in der Zeile anzeigen
                     position = randomAccessFile.getFilePointer();
                     positionFound = true;
                     //System.out.println("Position des Wortes: " + position);
                 }
             }
             // Schließt den BufferedReader
             randomAccessFile.close();
             
             if (!positionFound) {
            	 BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                 writer.write(searchWord);
                 writer.newLine();
                 writer.close();
                 positionFound = true;
             }
         } catch (IOException e) {
             e.printStackTrace();
             System.err.println("Fehler beim Lesen der Datei.");
         }
        
        if (filePath != null && positionFound) {
        	if(position == 0) {
        		try {
	                // Erstellt eine BufferedWriter, um ans Ende der Datei zu schreiben
	                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
	                writer.write(textArea.getText());
	                writer.newLine();
	                writer.close();
	                
	                JOptionPane.showMessageDialog(this, "Text erfolgreich gespeichert.");
	            } catch (IOException e) {
	                e.printStackTrace();
	                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Datei.");
	            }
        	} 
        	else {
        		try {
                    // RandomAccessFile um an eine bestimmte Stelle zu schreiben
                    RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
                    
                    // sichere die restliche Datei hinter der Baustelle um keine Inhalte durch überschreiben zu verlieren
                    long fileSize = randomAccessFile.length();
                    long remainingFileSize = fileSize - position;
                    byte[] remainingBytes = new byte[(int) remainingFileSize];
                    randomAccessFile.seek(position);
                    randomAccessFile.readFully(remainingBytes);
                    System.out.println(remainingBytes[0]);
                    
                    // schreibe den neuen Inhalt
                    randomAccessFile.seek(position);
                    randomAccessFile.writeBytes(textArea.getText());
                    randomAccessFile.writeBytes(System.lineSeparator());
                    
                    // füge die restliche Datei wieder an
                    // randomAccessFile.seek(randomAccessFile.length());
                    randomAccessFile.write(remainingBytes);
                    randomAccessFile.close();
                    JOptionPane.showMessageDialog(this, "Text erfolgreich gespeichert.");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Fehler beim Schreiben an eine bestimmte Stelle in der Datei.");
                }
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