package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BoxView;
import java.awt.*;

public class EntityEditorView extends VCSpanel {
    public EntityEditorView(VCSapp app){
        super(app);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(150,app.getWindow().getHeight()));
        this.setBorder(new TitledBorder("Create Entity"));

    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }
}
//TODO window yap
