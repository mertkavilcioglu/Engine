package UI;

import App.VCSapp;
import Sim.Entity;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EntityEditorView extends VCSPanel {
    String[] components = {"Radar"};
    RadarEditor radarPanel = null;
    JButton addComponentButton;

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
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            try{
                String name = eNamePanel.readData();
                Vec2int pos = ePositionPanel.readData();
                Vec2int speed = eSpeedPanel.readData();

                app.createEntity(name, pos, speed);
            }
            catch (Exception ex){
               //TODO eNamePanel.dataV düzgün bir validate yapmaya çalış
                ePositionPanel.dataValidate();
                eSpeedPanel.dataValidate();
            }





        });
        add(createButton);
        //add(app.createEntityButton(eNamePanel, ePositionPanel,  eSpeedPanel, null), BorderLayout.CENTER);
        add(new JLabel(" "));
        addComponentButton = new JButton("Add Component");
        addComponentButton.setBounds(150,300,150,30);

        JPopupMenu popupMenu = new JPopupMenu();
        for (String comp : components){
            JMenuItem item = new JMenuItem(comp);
            item.addActionListener(e -> {
                switch (comp){
                    case "Radar":
                        if(radarPanel == null){
                            radarPanel = new RadarEditor("Radar:", this);
                            remove(addComponentButton);
                            add(radarPanel);
                            add(addComponentButton);
                            revalidate();
                            break;
                        }
                }
            });
            popupMenu.add(item);
        }
        addComponentButton.addActionListener(e -> {
            int x=0;
            int y = popupMenu.getPreferredSize().height;
            popupMenu.show(addComponentButton,x,y);
        });
        add(addComponentButton, BorderLayout.CENTER);
    }

    public void removeComponent(JPanel panel, String compName){
        remove(panel);
        remove(addComponentButton);
        add(addComponentButton);
        switch (compName){
            case "Radar":
                if(radarPanel != null){
                    radarPanel = null;
                    break;
                }
        }
        revalidate();

    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }

}
//TODO window yap
