package UI;

import App.VCSApp;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    private Color appBgColor;
    public AppWindow(VCSApp app){
        super("VCS: Virtual Combat System");

        appBgColor = app.uiColorManager.TOP_BAR_COLOR;
        //setSize(800, 600);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setUndecorated(true);
        getContentPane().setBackground(appBgColor);
        setLayout(new BorderLayout(0,0));
        setBackground(appBgColor);
    }
}
