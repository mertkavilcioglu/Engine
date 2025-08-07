package UI;

import App.VCSApp;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    public AppWindow(VCSApp app){
        super("VCS: Virtual Combat System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
    }
}
