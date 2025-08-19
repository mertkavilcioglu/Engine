package UI;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EntityEditorView extends VCSPanel {
    String[] components = {"Radar"};
    RadarEditor radarPanel = null;
    JButton addComponentButton;
    JPanel addSidePanel;
    JPanel addTypePanel;
    JComboBox addSideBox;
    JComboBox addTypeBox;
    String type ="";
    public EntityEditorView(VCSApp app){
        super(app);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(150,app.getWindow().getHeight()));
        this.setBorder(new TitledBorder("Create Entity"));

        StringEditor eNamePanel = new StringEditor("Name:");
        Vec2intEditor ePositionPanel = new Vec2intEditor("Position:");
        Vec2intEditor eSpeedPanel = new Vec2intEditor("Velocity");

        String sides[] = {"Ally", "Enemy"};
        addSideBox = new JComboBox<>(sides);
        addSideBox.setEditable(false);
        addSideBox.setSelectedIndex(0);
        addSideBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addSidePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addSidePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addSidePanel.add(new JLabel("Side: "));
        addSidePanel.add(addSideBox);

        String types[] = {"Tank", "Plane", "Ship"};
        addTypeBox = new JComboBox<>(types);
        addTypeBox.setEditable(false);
        addTypeBox.setSelectedIndex(0);
        addTypeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addTypePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addTypePanel.add(new JLabel("Type: "));
        addTypePanel.add(addTypeBox);

        add(eNamePanel);
        add(addSidePanel);
        add(addTypePanel);
        add(ePositionPanel);
        add(eSpeedPanel);
        JButton createButton = new JButton("Create");
/*
        JButton tank = new JButton("Tank");
        tank.addActionListener(e -> { type = "tank";});
        add(tank);

        JButton plane = new JButton("Plane");
        plane.addActionListener(e -> { type = "plane";});
        add(plane);

        JButton ship = new JButton("Ship");
        ship.addActionListener(e -> { type = "ship";});
        add(ship);

 */
        createButton.addActionListener(e -> {
            try{
                String name = eNamePanel.readData();
                Vec2int pos = ePositionPanel.readData();
                Vec2int speed = eSpeedPanel.readData();
                int range = 0;
                if(radarPanel != null)
                    range = radarPanel.readData();
                int side = addSideBox.getSelectedIndex();
                type = (String) addTypeBox.getSelectedItem();
                app.createEntity(name, side, pos, speed, range, type);
            }
            catch (Exception ex){
               //TODO eNamePanel.dataV düzgün bir validate yapmaya çalış
                ePositionPanel.dataValidate();
                eSpeedPanel.dataValidate();
                if(radarPanel != null)
                    radarPanel.dataValidate();
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

    public void setRadarPanel(RadarEditor rdr){
        radarPanel = rdr;
    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }

}
//TODO window yap
