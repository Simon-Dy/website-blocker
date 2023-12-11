import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class websiteBlocker extends JFrame {

    private JTextArea textArea;
    
    String filePath = "../website-blocker/hosts";
    String filePathBackup = "../website-blocker/hosts_backup";
	String filePathSave = "../website-blocker/Blacklist.txt";
	
    public websiteBlocker() {
        // Setzt die Grundkonfiguration f�r das JFrame
        setTitle("Website-Blocker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erstellt eine JTextArea f�r den Text
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton saveButton = new JButton("Block Website");
        JButton manageButton = new JButton("Show blocked Websites");
        JButton backButton = new JButton("Back");
        backButton.setVisible(false);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speichern();
            }
        });

        manageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	saveButton.setVisible(false);
            	manageButton.setVisible(false);
            	backButton.setVisible(true);
            	textArea.setEditable(false);
            	textArea.setText(getFile(filePathSave));
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	saveButton.setVisible(true);
            	manageButton.setVisible(true);
            	backButton.setVisible(false);
            	textArea.setEditable(true);
            	textArea.setText("");
            }
        });

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // JTextArea: nimmt mehrere Zellen im Gitter in Anspruch (ganze Spalte)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Zwei Zellen in der Breite
        gbc.gridheight = 1; // Eine Zelle in der H�he
        gbc.fill = GridBagConstraints.BOTH; // F�llt sowohl horizontal als auch vertikal
        gbc.weightx = 1.0; // Gewichtung in horizontaler Richtung
        gbc.weighty = 8.0; // Gewichtung in vertikaler Richtung
        getContentPane().add(scrollPane, gbc);

        // Clear Button: nimmt eine Zelle im Gitter ein
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Eine Zelle in der Breite
        gbc.gridheight = 1; // Eine Zelle in der H�he
        gbc.fill = GridBagConstraints.BOTH; // F�llt horizontal
        gbc.weightx = 1.0; // Keine Gewichtung in horizontaler Richtung
        gbc.weighty = 4.0; // Keine Gewichtung in vertikaler Richtung
        getContentPane().add(saveButton, gbc);

        // Additional Button: nimmt eine Zelle im Gitter ein
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Eine Zelle in der Breite
        gbc.gridheight = 1; // Eine Zelle in der H�he
        gbc.fill = GridBagConstraints.BOTH; // F�llt horizontal
        gbc.weightx = 1.0; // Keine Gewichtung in horizontaler Richtung
        gbc.weighty = 1.0; // Keine Gewichtung in vertikaler Richtung
        getContentPane().add(manageButton, gbc);
        
        // Additional Button: nimmt eine Zelle im Gitter ein
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Eine Zelle in der Breite
        gbc.gridheight = 1; // Eine Zelle in der H�he
        gbc.fill = GridBagConstraints.BOTH; // F�llt horizontal
        gbc.weightx = 1.0; // Keine Gewichtung in horizontaler Richtung
        gbc.weighty = 1.0; // Keine Gewichtung in vertikaler Richtung
        getContentPane().add(backButton, gbc);
        
        checkForBackup();
    }
    
    // Wenn der Benutzer auf "Speichern" klickt
    private void speichern() {
        // Zeigt einen Dialog zum Speichern an
        //JFileChooser fileChooser = new JFileChooser();
        //int result = fileChooser.showSaveDialog(this);
         
    	 boolean positionFound = false;
    	 long position = 0;
    	 String searchWord = "#Website-Blacklist";
    	 
         try {
             // �ffnet die Datei zum Lesen
        	 RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");

             String line;
             int lineNumber = 0;

             // Durchsucht jede Zeile nach dem gesuchten Wort
             while ((line = randomAccessFile.readLine()) != null) {
                 lineNumber++;

                 // �berpr�ft, ob das gesuchte Wort in der Zeile enthalten ist
                 if (line.contains(searchWord)) {
                     //System.out.println("Das Wort '" + searchWord + "' wurde in Zeile " + lineNumber + " gefunden.");
                     // Hier k�nnen Sie weitere Aktionen durchf�hren, z.B. die Position des Wortes in der Zeile anzeigen
                     position = randomAccessFile.getFilePointer();
                     positionFound = true;
                     //System.out.println("Position des Wortes: " + position);
                 }
             }
             // Schlie�t den BufferedReader
             randomAccessFile.close();
             
             if (!positionFound) {
            	 BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                 writer.write("\n" + searchWord);
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
	                writer.write("127.0.0.1 " + textArea.getText());
	                writer.newLine();
	                writer.close();
	                
	                putToSave(textArea.getText());
	                JOptionPane.showMessageDialog(this, "Text erfolgreich gespeichert.");
	                textArea.setText("");
	            } catch (IOException e) {
	                e.printStackTrace();
	                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Datei.");
	            }
        	} 
        	else {
        		try {
                    // RandomAccessFile um an eine bestimmte Stelle zu schreiben
                    RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
                    
                    // sichere die restliche Datei hinter der Baustelle um keine Inhalte durch �berschreiben zu verlieren
                    long fileSize = randomAccessFile.length();
                    long remainingFileSize = fileSize - position;
                    byte[] remainingBytes = new byte[(int) remainingFileSize];
                    randomAccessFile.seek(position);
                    randomAccessFile.readFully(remainingBytes);
                    System.out.println(remainingBytes[0]);
                    
                    // schreibe den neuen Inhalt
                    randomAccessFile.seek(position);
                    randomAccessFile.writeBytes("127.0.0.1 " + textArea.getText());
                    randomAccessFile.writeBytes(System.lineSeparator());
                    
                    // f�ge die restliche Datei wieder an
                    // randomAccessFile.seek(randomAccessFile.length());
                    randomAccessFile.write(remainingBytes);
                    randomAccessFile.close();
                    
                    putToSave(textArea.getText());
                    JOptionPane.showMessageDialog(this, "Text erfolgreich gespeichert.");
                    textArea.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Fehler beim Schreiben an eine bestimmte Stelle in der Datei.");
                }
        	}
        }
    }

    public void putToSave(String text) {
    	try {
            // Erstellt eine BufferedWriter, um ans Ende der Datei zu schreiben
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePathSave, true));
            writer.write(text);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Datei.");
        }
    }
    
    public String getFile(String fp) {
    	try {
            // Liest den gesamten Inhalt der Datei als String
            Path path = Paths.get(fp);
            byte[] fileBytes = Files.readAllBytes(path);
            String fileContent = new String(fileBytes);

            // Verarbeitet den String (hier wird er einfach ausgegeben)
            System.out.println("Inhalt der Datei:");
            System.out.println(fileContent);
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Fehler beim Lesen der Datei.");
        }
    	return "";
    }
    
    public void checkForBackup() {
    	
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