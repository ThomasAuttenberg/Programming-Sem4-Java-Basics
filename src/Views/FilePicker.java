package Views;

import javax.swing.*;
import java.io.File;

public class FilePicker extends JFileChooser {

    FilePicker(String directoryPath){
        super(new File(directoryPath));
        JPanel panel = new JPanel();

    }
}
