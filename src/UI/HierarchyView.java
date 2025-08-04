package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class HierarchyView extends VCSpanel {

    public HierarchyView(VCSapp app){
        super(app);
        this.setLayout(new GridLayout(25,1));
        this.setPreferredSize(new Dimension(150,app.getWindow().getHeight()));
        this.setBorder(new TitledBorder("Hierarchy"));
    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }
}
