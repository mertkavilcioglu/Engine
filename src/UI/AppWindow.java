package UI;

import App.VCSapp;
import Sim.World;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    public AppWindow(VCSapp app){
        super("VCS: Virtual Combat System");
        setSize(app.world.map.maxX, app.world.map.maxY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
    }
}
