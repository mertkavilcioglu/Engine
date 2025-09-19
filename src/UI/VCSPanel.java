package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;

public abstract class VCSPanel extends JPanel {
    public final VCSApp app;

    public VCSPanel(VCSApp app){
        this.app = app;
    }
    public abstract void selectedEntityChanged(Entity entity);
    protected void log(String message){
        if (app != null) app.log(message);
    }
    protected void debugLog(String message){
        if (app != null) app.debugLog(message);
    }

}
