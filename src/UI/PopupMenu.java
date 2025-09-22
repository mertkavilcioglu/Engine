package UI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PopupMenu {
    private ImageIcon saveIcon = new ImageIcon("src/Assets/Icons/save_icon.png");
    Image img = saveIcon.getImage();
    Image newimg = img.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
    ImageIcon newIcon = new ImageIcon(newimg);
    public PopupMenu(ArrayList<String> entName){
        String nameStr =  entName.toString().substring(1,entName.toString().length()-1); 
        JOptionPane.showMessageDialog(null,nameStr + " not created because of their locations.");

    }
    public PopupMenu(){

        String[] options = { "Save","Don't save" };
        JFrame frame = new JFrame();
        frame.getContentPane();
        int choice = JOptionPane.showOptionDialog(frame,
                "Do you want to save?", "Select an option",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,newIcon,
                options, options[0]);
        if (choice == 0) {
            //SAVE
        }
        if (choice == 1) {
            //DONT SAVE
        }

    }


}
