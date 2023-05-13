package Views;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Console extends JDialog {
    private JTextArea consoleTextArea;
    public JTextField textField;
    Color fieldsColor = new Color(134, 134, 134);
    Color bgColor = new Color(54, 54, 54);
    Color textColor = new Color(178, 178, 178);
    public Console(){

        super((Dialog)null);

        this.setTitle("Console");
        this.setSize(new Dimension(600,600));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);

        consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);
        JScrollPane consoleTextPane = new JScrollPane(consoleTextArea);
        consoleTextArea.setBackground(bgColor);
        consoleTextArea.setForeground(textColor);
        consoleTextArea.setBorder(null);
        consoleTextPane.setBorder(null);

        textField = new JTextField();
        textField.setBackground(fieldsColor);
        textField.setBorder(null);
        this.add(consoleTextPane, BorderLayout.CENTER);
        this.add(textField, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void addText(String text){
        consoleTextArea.append(text);
    }
    public void addText(InputStream inputStream){
        byte[] bytes;
        try {
            bytes = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        consoleTextArea.append(new String(bytes, StandardCharsets.UTF_8));

    }

}
