package Controllers;

import Models.ConsoleModel;
import Views.Console;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class ConsoleController {

    ArrayList<Integer> lengths = new ArrayList<>();
    int lastStringIndex = 0;
    int stringsNavigator;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    ConsoleController(Console consoleView, ConsoleModel consoleModel){

        lengths.add(0);

        consoleView.textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if(e.getKeyCode() == KeyEvent.VK_ENTER){

                    byte[] buffer = consoleView.textField.getText().getBytes(StandardCharsets.UTF_8);
                    int offset = lengths.get(lastStringIndex);
                    consoleView.textField.setText("");

                    outputStream.write(buffer, 0, buffer.length);

                    lengths.add(outputStream.size());
                    lastStringIndex++;
                    stringsNavigator = lastStringIndex;

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray(), offset, buffer.length);


                    InputStream requestResult = consoleModel.call(inputStream);
                    consoleView.addText(requestResult);

                    try {
                        requestResult.close();
                        inputStream.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });

        consoleView.textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    if(stringsNavigator > 0) {
                        int offset = lengths.get(stringsNavigator - 1);
                        int length = lengths.get(stringsNavigator) - offset;
                        consoleView.textField.setText(new String(outputStream.toByteArray(), offset, length, StandardCharsets.UTF_8));
                        stringsNavigator--;
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    if(stringsNavigator < lastStringIndex-1) {
                        stringsNavigator++;
                        int offset = lengths.get(stringsNavigator);
                        int length = lengths.get(stringsNavigator+1) - offset;
                        consoleView.textField.setText(new String(outputStream.toByteArray(), offset, length, StandardCharsets.UTF_8));
                    }
                }
            }
        });


    }
}
