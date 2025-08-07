package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class ImportPanel extends VCSPanel{

    public ImportPanel(VCSApp app) {
        super(app);
        this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        //importpanel.setBounds(0,0,10,50);
        JButton exportb = new JButton("Export");
        exportb.setSize(10,20);
        JButton importb = new JButton("Import");
        importb.setSize(10,20);
        this.add(exportb);
        this.add(importb);
        //importpanel.setBorder(BorderFactory.createEmptyBorder(0,0,50,50));
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
