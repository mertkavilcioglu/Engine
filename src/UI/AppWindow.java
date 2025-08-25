package UI;

import App.VCSApp;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    public AppWindow(VCSApp app){
        super("VCS: Virtual Combat System");

        //setSize(800, 600);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLayout(new BorderLayout(10,10));
    }
}
