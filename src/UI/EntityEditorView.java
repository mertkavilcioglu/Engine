package UI;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.Radar;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLOutput;

public class EntityEditorView extends VCSPanel {
    String[] components = {"Radar"};
    RadarEditor radarPanel = null;
    JButton addComponentButton;
    JPanel addSidePanel;
    JPanel addTypePanel;
    JComboBox addSideBox;
    JComboBox addTypeBox;
    String type ="";

    StringEditor eNamePanel = new StringEditor("Name:");
    Vec2intEditor ePositionPanel = new Vec2intEditor("Position:");
    Vec2intEditor eSpeedPanel = new Vec2intEditor("Velocity");

    JButton updateButton = new JButton("Update Entity");

    public EntityEditorView(VCSApp app){
        super(app);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(150,app.getWindow().getHeight()));
        this.setBorder(new TitledBorder("Update Entity"));

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

        createButton.addActionListener(e -> {
            //app.attackTest();
            try{
                String name = eNamePanel.readData();
                Vec2int pos = ePositionPanel.readData();
                Vec2int speed = eSpeedPanel.readData();
                int range = 0;
                if(radarPanel != null)
                    range = radarPanel.readData();
                int side = addSideBox.getSelectedIndex();
                type = (String) addTypeBox.getSelectedItem();
                if(app.pixelColor.isLocationValidForType(type, pos)){
                    app.createEntity(name, side, pos, speed, range, type);
                    log("New unit named " + name + " created.");
                }
                else if(app.pixelColor.isLocationValidForType(type, pos) == false){
                    ePositionPanel.error();
                }

            }
            catch (Exception ex){
                ePositionPanel.dataValidate();
                eSpeedPanel.dataValidate();
                if(radarPanel != null)
                    radarPanel.dataValidate();
            }
        });
        //add(createButton);

        updateButton.addActionListener(e -> {
            try{
                String name = eNamePanel.readData();
                Vec2int pos = ePositionPanel.readData();
                Vec2int speed = eSpeedPanel.readData();
                int range = 0;
                if(radarPanel != null)
                    range = radarPanel.readData();

                int side = addSideBox.getSelectedIndex();
                type = (String) addTypeBox.getSelectedItem();
                if(app.pixelColor.isLocationValidForType(type, pos)){
                    app.updateSelectedEntity(name, side, pos, speed, range, type);
                }
                else if(!app.pixelColor.isLocationValidForType(type, pos)){
                    ePositionPanel.error();
                }
                app.hierarchyPanel.updateComponent("Radar", app.mapView.getSelectedEntity());

            }
            catch (Exception ex){
                System.out.println("CATCHED SMT");
                System.out.println(ex.getMessage());
                ePositionPanel.dataValidate();
                eSpeedPanel.dataValidate();
                if(radarPanel != null)
                    radarPanel.dataValidate();
            }
        });
        add(updateButton);


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
        if(entity == null){
            clearPanelData();
        }
        else{
            updateButton.setEnabled(true);
            addComponentButton.setEnabled(true);
            updatePanelData(entity);
        }
    }

    public void updatePanelData(Entity e){
        eNamePanel.setData(e.getName());
        ePositionPanel.setData(e.getPos());
        eSpeedPanel.setData(e.getSpeed());
        addSideBox.setSelectedIndex(e.getSide());
        switch (e.getType()){
            case "Tank":
                addTypeBox.setSelectedIndex(0);
                break;
            case "Plane":
                addTypeBox.setSelectedIndex(1);
                break;
            case "Ship":
                addTypeBox.setSelectedIndex(2);
                break;
        }
        for(Component c : e.getComponents()){
            if(c instanceof Radar && ((Radar) c).getRange() != 0){
                if(radarPanel == null){
                    radarPanel = new RadarEditor("Radar:", this);
                    remove(addComponentButton);
                    add(radarPanel);
                    add(addComponentButton);
                    radarPanel.setData(((Radar) c).getRange());
                    revalidate();//TODO: radar bilgisi sağda güncel değil bide treede yok
                    //TODO: hiyerarşide radar güncellenmiyor, silip geri ekledikten sonra
                }
                else{
                    radarPanel.setData(((Radar) c).getRange());
                    revalidate();
                }
            }
            else{
                if(radarPanel != null){
                    remove(radarPanel);
                    radarPanel = null;
                }
                revalidate();
            }
        }

        updateButton.setEnabled(true);
        addComponentButton.setEnabled(true);
        eNamePanel.getInputField().setEnabled(true);
        ePositionPanel.setInputEnabled(true);
        eSpeedPanel.setInputEnabled(true);
        addSideBox.setEnabled(true);
        addTypeBox.setEnabled(true);
    }

    public void clearPanelData(){
        eNamePanel.setData("");
        ePositionPanel.setData(new Vec2int());
        eSpeedPanel.setData(new Vec2int());

        if(radarPanel != null){
            remove(radarPanel);
            radarPanel = null;
        }
        revalidate();

        updateButton.setEnabled(false);
        addComponentButton.setEnabled(false);
        eNamePanel.getInputField().setEnabled(false);
        ePositionPanel.setInputEnabled(false);
        eSpeedPanel.setInputEnabled(false);
        addSideBox.setEnabled(false);
        addTypeBox.setEnabled(false);
    }

}
//TODO window yap
