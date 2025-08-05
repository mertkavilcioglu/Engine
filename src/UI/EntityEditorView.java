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

        StringEditor eNamePanel = new StringEditor("Name:");
        Vec2intEditor ePositionPanel = new Vec2intEditor("Position:");
        Vec2intEditor eSpeedPanel = new Vec2intEditor("Velocity");
        add(eNamePanel);
        add(ePositionPanel);
        add(eSpeedPanel);
        add(app.createEntityButton(eNamePanel, ePositionPanel,  eSpeedPanel, null));
        add(Box.createVerticalGlue());
        add(addComponentButton(eNamePanel, ePositionPanel,  eSpeedPanel, null));

    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }

    public JButton addComponentButton(StringEditor namePanel, Vec2intEditor posPanel,
                                      Vec2intEditor speedPanel, HierarchyView hierarchyPanel){

        JButton createBtn = new JButton("Add C");
        createBtn.addActionListener(e -> {
            //System.out.println("button clicked");
            /*try{
                Entity ent = world.createEntity(namePanel.readData(), posPanel.readData(), speedPanel.readData());
                if(ent != null && !ent.isNullName()){
                    world.entities.add(ent);
                    //addLabel(hierarchyPanel, ent.getName());
                    addLeaf(ent.getName());
                }
            }
            catch (NumberFormatException err){ //TODO bi tekrar bak
                posPanel.dataValidate();
                speedPanel.dataValidate();
            }*/
        });
        createBtn.setFocusable(false);
        return createBtn;
    }
}
//TODO window yap
