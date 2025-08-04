package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends VCSpanel{
    public LogPanel(VCSapp app) {
        super(app);
        this.setSize(100,200);
        this.setBorder(BorderFactory.createLineBorder(Color.black,1));
        JLabel label = new JLabel("Log: ");
        this.add(label);

    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
