package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class EntityEditorView extends VCSpanel {
    public EntityEditorView(VCSapp app){
        super(app);
        this.setLayout(new GridLayout(15,1));
    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }
}
//TODO window yap
