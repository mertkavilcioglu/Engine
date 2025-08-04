package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class HierarchyView extends VCSpanel {

    public HierarchyView(VCSapp app){
        super(app);
        this.setLayout(new GridLayout(25,1));
    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }
}
