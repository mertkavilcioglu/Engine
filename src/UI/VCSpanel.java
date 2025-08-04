package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;

public abstract class VCSpanel extends JPanel {
    public final VCSapp app;

    public VCSpanel(VCSapp app){
        this.app = app;
    }
    public abstract void selectedEntityChanged(Entity entity);

}
