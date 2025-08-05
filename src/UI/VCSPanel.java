package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;

public abstract class VCSPanel extends JPanel {
    public final VCSapp app;

    public VCSPanel(VCSapp app){
        this.app = app;
    }
    public abstract void selectedEntityChanged(Entity entity);

}
