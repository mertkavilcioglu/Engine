package UI;

import javax.swing.*;
import java.util.ArrayList;

public class PopupMenu {
    public PopupMenu(ArrayList<String> entName){
        String nameStr =  entName.toString().substring(1,entName.toString().length()-1); 
        JOptionPane.showMessageDialog(null,nameStr + " not created because of their locations.");

    }
}
