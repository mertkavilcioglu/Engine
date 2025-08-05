package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class SouthPanel extends VCSPanel {


    public SouthPanel(VCSapp app) {
        super(app);
        this.setLayout(new GridLayout(1,2));
        ActionPanel actionPanel = new ActionPanel(app);
        LogPanel logPanel = new LogPanel(app);
        this.setBorder(BorderFactory.createLineBorder(Color.black,2));
        this.add(actionPanel);
        this.add(logPanel);
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
