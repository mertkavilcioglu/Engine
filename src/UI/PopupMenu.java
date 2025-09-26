package UI;

import App.VCSApp;
import Sim.Component;
import Sim.Radar;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PopupMenu {

    public PopupMenu(ArrayList<String> entName){
        String nameStr =  entName.toString().substring(1,entName.toString().length()-1);
        JOptionPane.showMessageDialog(null,nameStr + " not created because of their locations.");

    }
}
