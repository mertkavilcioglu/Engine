package UI;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.Radar;
import Sim.TDL.TDLReceiverComp;
import Sim.TDL.TDLTransmitterComp;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class EntityEditorView extends VCSPanel {
    private Component.ComponentType components;
    private RadarEditor radarEditor = null;
    private TransmitterEditor transmitterEditor = null;
    private ReceiverEditor receiverEditor = null;
    private JButton addComponentButton;
    private JPanel addSidePanel;
    private JPanel addTypePanel;
    private JComboBox addSideBox;
    private JComboBox addTypeBox;
    private boolean addSideBoxFocused = false;
    private boolean addTypeBoxFocused = false;
    private String type ="";

    private StringEditor eNamePanel;
    private Vec2intEditor ePositionPanel;
    private Vec2intEditor eSpeedPanel;

    //private JButton updateButton = new JButton("Update Entity");

    public Color panelColor;
    public Color panelComponentColor;
    public Color borderColor;

    public boolean isFocusGaied = false;

    public EntityEditorView(VCSApp app){
        super(app);
        panelColor = app.uiColorManager.DARK_PANEL_COLOR;
        panelComponentColor = app.uiColorManager.DARK_MAP_BG_COLOR;
        borderColor = app.uiColorManager.DARK_TITLE_COLOR_1;
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        int targetWidth = Toolkit.getDefaultToolkit().getScreenSize().width / 8;
        int targetHeight = Toolkit.getDefaultToolkit().getScreenSize().height * 5 / 8;
        this.setPreferredSize(new Dimension(targetWidth, targetHeight));

        //BoxLayout lay = new BoxLayout(this,BoxLayout.Y_AXIS);
        //this.setBorder(new TitledBorder("Update Entity"));
        EmptyBorder eBorder = new EmptyBorder(0,7,0,7);
        setBorder(eBorder);

         eNamePanel = new StringEditor("Name:", app);
         ePositionPanel = new Vec2intEditor("Position:", app);

         eSpeedPanel = new Vec2intEditor("Velocity", app);

        // Custom Panel Colors
        TitledBorder titledBorder = new TitledBorder("Update Entity");
        titledBorder.setTitleColor(app.uiColorManager.DARK_TITLE_COLOR_1);
        //titledBorder.setBorder(BorderFactory.createLineBorder(app.uiColorManager.DARK_TITLE_COLOR_1, 2));
        //this.setBorder(titledBorder);
        this.setBackground(panelColor);

        eNamePanel.setBackground(panelColor);
        ePositionPanel.setBackground(panelColor);
        eSpeedPanel.setBackground(panelColor);


        String sides[] = {"Ally", "Enemy"};
        addSideBox = new JComboBox<>(sides);
        addSideBox.setEditable(false);
        addSideBox.setSelectedIndex(0);
        addSideBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addSidePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addSidePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel sideLbl = new JLabel("Side: ");
        sideLbl.setForeground(Color.WHITE);
        addSidePanel.add(sideLbl);
        addSidePanel.add(addSideBox);

        String types[] = {Entity.Type.GROUND.getName(), Entity.Type.AIR.getName(), Entity.Type.SURFACE.getName()};
        addTypeBox = new JComboBox<>(types);
        addTypeBox.setEditable(false);
        addTypeBox.setSelectedIndex(0);
        addTypeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addTypePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel typeLbl = new JLabel("Type: ");
        typeLbl.setForeground(Color.WHITE);
        addTypePanel.add(typeLbl);
        addTypePanel.add(addTypeBox);

        addSidePanel.setBackground(panelColor);
        addTypePanel.setBackground(panelColor);

        add(eNamePanel);
        add(addSidePanel);
        add(addTypePanel);
        add(ePositionPanel);
        add(eSpeedPanel);

        addSideBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == addSideBox && addSideBox.hasFocus()){
                    updateSelectedEntity();
                }

            }
        });

        addTypeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == addTypeBox && addTypeBox.hasFocus()){
                    if(app.pixelColor.isLocationValidForType(addTypeBox.getSelectedItem().toString(),
                            app.mapView.getSelectedEntity().getPos())){
                        updateSelectedEntity();
                    }
                    else{
                        switch (app.mapView.getSelectedEntity().getType()){
                            case Entity.Type.GROUND:
                                addTypeBox.setSelectedIndex(0);
                                break;
                            case Entity.Type.AIR:
                                addTypeBox.setSelectedIndex(1);
                                break;
                            case Entity.Type.SURFACE:
                                addTypeBox.setSelectedIndex(2);
                                break;
                        }
                    }
                }

            }
        });


        //add(app.createEntityButton(eNamePanel, ePositionPanel,  eSpeedPanel, null), BorderLayout.CENTER);
        add(new JLabel(" "));
        addComponentButton = new JButton("Add Component");
        addComponentButton.setFocusable(false);
        //addComponentButton.setBounds(150,300,150,30);
        addComponentButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        addComponentButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        JPopupMenu popupMenu = new JPopupMenu();
        for (Component.ComponentType comp : Component.ComponentType.values()){
            JMenuItem item = new JMenuItem(comp.name);
            if(comp.name.equals(Component.ComponentType.RECEIVER.name) ||comp.name.equals(Component.ComponentType.TRANSMITTER.name))
                ;//item.setEnabled(false);
            item.addActionListener(e -> {
                switch (comp){
                    case RADAR:
                        if(radarEditor == null){
                            radarEditor = new RadarEditor(comp.name, this, Component.ComponentType.RADAR, "5000");
                            remove(addComponentButton);
                            add(radarEditor);
//                            if(!app.mapView.getSelectedEntity().hasComponent("Radar")){
//                                app.mapView.getSelectedEntity().addComponents(new Radar(app.mapView.getSelectedEntity(),
//                                        app.world.entities));
//                            }
                            updateSelectedEntity();
                            add(addComponentButton);
                            revalidate();
                            radarEditor.txt.requestFocus();
                        }
                        break;
                    case RECEIVER:
                        if(receiverEditor == null){
                            receiverEditor = new ReceiverEditor(comp.name, this, Component.ComponentType.RECEIVER);
                            remove(addComponentButton);
                            add(receiverEditor);
                            updateSelectedEntity();
                            add(addComponentButton);
                            revalidate();
                        }
                        break;

                    case TRANSMITTER:
                        if(transmitterEditor == null){
                            transmitterEditor = new TransmitterEditor(comp.name, this, Component.ComponentType.TRANSMITTER, "300");
                            remove(addComponentButton);
                            add(transmitterEditor);
                            updateSelectedEntity();
                            add(addComponentButton);
                            revalidate();
                            transmitterEditor.txt.requestFocus();
                        }
                        break;
                }
            });
            popupMenu.add(item);
        }
        addComponentButton.addActionListener(e -> {
            int x=0;
            int y = addComponentButton.getPreferredSize().height;
            popupMenu.show(addComponentButton,x,y);
        });
        add(addComponentButton, BorderLayout.CENTER);

        addTypeBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                addTypeBoxFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                addTypeBoxFocused = false;
            }
        });

        addSideBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                addSideBoxFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                addSideBoxFocused = false;
            }
        });

        eNamePanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocusGaied = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocusGaied = false;
            }
        });

        if(app.mapView.getSelectedEntity() == null){
            disablePanelData();
        }else if (app.mapView.getSelectedEntity().getType() == Entity.Type.HQ)
            disablePanelData();


    }

    public void update(){
        //updatePanelData(app.mapView.getSelectedEntity());
    }

    public void removeComponent(JPanel panel, Component.ComponentType type) {
        remove(panel);
        remove(addComponentButton);
        add(addComponentButton);
        switch (type){
            case RADAR:
                if(radarEditor != null){
                    radarEditor = null;

                }
                break;
            case TRANSMITTER:
                if(transmitterEditor != null)
                    transmitterEditor = null;
                break;
            case RECEIVER:
                if(receiverEditor != null)
                    receiverEditor = null;
                break;
        }
        revalidate();
        repaint();

    }

    public void setRadarEditor(RadarEditor rdr){
        radarEditor = rdr;
    }

    public void setReceiverEditor(ReceiverEditor receiverEditor) {
        this.receiverEditor = receiverEditor;
    }

    public void setTransmitterEditor(TransmitterEditor transmitterEditor) {
        this.transmitterEditor = transmitterEditor;
    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        if(entity == null){
            disablePanelData();
        } else if (entity.getType() == Entity.Type.HQ || entity.getType() == null) {
            entity.setType(Entity.Type.HQ);
            updateHQPanelData(entity);
        } else {
            //updateButton.setEnabled(true);
            addComponentButton.setEnabled(true);
            updatePanelData(entity);
        }
    }

    public void updateHQPanelData(Entity e){
        eNamePanel.setData("HEADQUARTER");
        if(!ePositionPanel.getIsFocusedX())
            ePositionPanel.setDataX(e.getPos());
        if(!ePositionPanel.getIsFocusedY())
            ePositionPanel.setDataY(e.getPos());
        eSpeedPanel.setData(new Vec2int(0,0));
        addSideBox.setSelectedIndex(0);

        HashMap<Component.ComponentType, Component> components = e.getComponents();
        if(components.containsKey(Component.ComponentType.RADAR) && ((Radar) components.get(Component.ComponentType.RADAR)).getRange() != 0 ){
            if(radarEditor == null){
                radarEditor = new RadarEditor("Radar:", this, Component.ComponentType.RADAR, "5000");
                remove(addComponentButton);
                add(radarEditor);
                add(addComponentButton);
                radarEditor.setData(((Radar) components.get(Component.ComponentType.RADAR)).getRange());
            }
            else if(!radarEditor.getIsFocused()){
                radarEditor.setData(((Radar) components.get(Component.ComponentType.RADAR)).getRange());
            }
        }
        if(components.containsKey(Component.ComponentType.TRANSMITTER)){
            if(transmitterEditor == null){
                transmitterEditor = new TransmitterEditor("Transmitter:", this, Component.ComponentType.TRANSMITTER, "300");
                remove(addComponentButton);
                add(transmitterEditor);
                add(addComponentButton);
                transmitterEditor.setData(((TDLTransmitterComp) components.get(Component.ComponentType.TRANSMITTER)).getTransmitterRange());
            }
            else if(!transmitterEditor.getIsFocused()){
                transmitterEditor.setData(((TDLTransmitterComp) components.get(Component.ComponentType.TRANSMITTER)).getTransmitterRange());
            }
        }
        if(components.containsKey(Component.ComponentType.RECEIVER)){
            if(receiverEditor == null){
                receiverEditor = new ReceiverEditor("Receiver:", this, Component.ComponentType.RECEIVER);
                remove(addComponentButton);
                add(receiverEditor);
                add(addComponentButton);
            }
        }

        if(!e.getComponents().containsKey(Component.ComponentType.RADAR) ||
                e.componentsToRemove.containsKey(Component.ComponentType.RADAR)){
            if(radarEditor != null && !radarEditor.getIsFocused()){
                remove(radarEditor);
                radarEditor = null;
            }
        }

        if(!e.getComponents().containsKey(Component.ComponentType.TRANSMITTER) ||
                (e.componentsToRemove.containsKey(Component.ComponentType.TRANSMITTER))){
            if(transmitterEditor != null && !transmitterEditor.getIsFocused()){
                remove(transmitterEditor);
                transmitterEditor = null;
            }
        }

        if(!e.getComponents().containsKey(Component.ComponentType.RECEIVER) ||
                e.componentsToRemove.containsKey(Component.ComponentType.RECEIVER)){
            if(receiverEditor != null){
                remove(receiverEditor);
                receiverEditor = null;
            }
        }

        revalidate();
        //updateButton.setEnabled(true);
        addComponentButton.setEnabled(true);
        eNamePanel.getInputField().setEnabled(false);
        ePositionPanel.setInputEnabled(true);
        eSpeedPanel.setInputEnabled(false);
        addSideBox.setEnabled(false);
        addTypeBox.setVisible(true);
        addTypeBox.setEnabled(false);
    }

    public void updatePanelData(Entity e){
        if(e == null)
            return;

        if (e.getType() == Entity.Type.HQ)
            updateHQPanelData(e);
        else {
            addTypeBox.setVisible(true);

            if(!eNamePanel.getIsFocused())
                eNamePanel.setData(e.getName());

            if(!ePositionPanel.getIsFocusedX())
                ePositionPanel.setDataX(e.getPos());
            if(!ePositionPanel.getIsFocusedY())
                ePositionPanel.setDataY(e.getPos());

            if(!eSpeedPanel.getIsFocusedX())
                eSpeedPanel.setDataX(e.getSpeed());
            if(!eSpeedPanel.getIsFocusedY())
                eSpeedPanel.setDataY(e.getSpeed());

            if(!addSideBoxFocused)
                addSideBox.setSelectedIndex(e.getSide().getIndex());

            if(!addTypeBoxFocused){
                switch (e.getType()){
                    case Entity.Type.GROUND:
                        addTypeBox.setSelectedIndex(0);
                        break;
                    case Entity.Type.AIR:
                        addTypeBox.setSelectedIndex(1);
                        break;
                    case Entity.Type.SURFACE:
                        addTypeBox.setSelectedIndex(2);
                        break;
                }
            }

//            for(Component c : e.getComponents().values()){
//                if(c instanceof Radar && ((Radar) c).getRange() != 0 ){
//                    if(radarEditor == null){
//                        radarEditor = new RadarEditor("Radar:", this, Component.ComponentType.RADAR, "5000");
//                        remove(addComponentButton);
//                        add(radarEditor);
//                        add(addComponentButton);
//                        radarEditor.setData(((Radar) c).getRange());
//                    }
//                    else if(!radarEditor.getIsFocused()){
//                        radarEditor.setData(((Radar) c).getRange());
//                    }
//                }
//                else if(c instanceof TDLReceiverComp){
//                    if(receiverEditor == null){
//                        receiverEditor = new ReceiverEditor("Receiver:", this, Component.ComponentType.RECEIVER);
//                        remove(addComponentButton);
//                        add(receiverEditor);
//                        add(addComponentButton);
//                    }
//                }
//                else if(c instanceof TDLTransmitterComp){
//                    if(transmitterEditor == null){
//                        transmitterEditor = new TransmitterEditor("Transmitter:", this, Component.ComponentType.TRANSMITTER, "300");
//                        remove(addComponentButton);
//                        add(transmitterEditor);
//                        add(addComponentButton);
//                        transmitterEditor.setData(((TDLTransmitterComp) c).getTransmitterRange());
//                    }
//                    else if(!transmitterEditor.getIsFocused()){
//                        transmitterEditor.setData(((TDLTransmitterComp) c).getTransmitterRange());
//                    }
//                }
//            }

            HashMap<Component.ComponentType, Component> components = e.getComponents();
            if(components.containsKey(Component.ComponentType.RADAR) && ((Radar) components.get(Component.ComponentType.RADAR)).getRange() != 0 ){
                if(radarEditor == null){
                    radarEditor = new RadarEditor("Radar:", this, Component.ComponentType.RADAR, "5000");
                    remove(addComponentButton);
                    add(radarEditor);
                    add(addComponentButton);
                    radarEditor.setData(((Radar) components.get(Component.ComponentType.RADAR)).getRange());
                }
                else if(!radarEditor.getIsFocused()){
                    radarEditor.setData(((Radar) components.get(Component.ComponentType.RADAR)).getRange());
                }
            }
            if(components.containsKey(Component.ComponentType.TRANSMITTER)){
                if(transmitterEditor == null){
                    transmitterEditor = new TransmitterEditor("Transmitter:", this, Component.ComponentType.TRANSMITTER, "300");
                    remove(addComponentButton);
                    add(transmitterEditor);
                    add(addComponentButton);
                    transmitterEditor.setData(((TDLTransmitterComp) components.get(Component.ComponentType.TRANSMITTER)).getTransmitterRange());
                }
                else if(!transmitterEditor.getIsFocused()){
                    transmitterEditor.setData(((TDLTransmitterComp) components.get(Component.ComponentType.TRANSMITTER)).getTransmitterRange());
                }
            }
            if(components.containsKey(Component.ComponentType.RECEIVER)){
                if(receiverEditor == null){
                    receiverEditor = new ReceiverEditor("Receiver:", this, Component.ComponentType.RECEIVER);
                    remove(addComponentButton);
                    add(receiverEditor);
                    add(addComponentButton);
                }
            }

            if(!e.getComponents().containsKey(Component.ComponentType.RADAR) ||
                    e.componentsToRemove.containsKey(Component.ComponentType.RADAR)){
                if(radarEditor != null && !radarEditor.getIsFocused()){
                    remove(radarEditor);
                    radarEditor = null;
                }
            }

            if(!e.getComponents().containsKey(Component.ComponentType.TRANSMITTER) ||
                    (e.componentsToRemove.containsKey(Component.ComponentType.TRANSMITTER))){
                if(transmitterEditor != null && !transmitterEditor.getIsFocused()){
                    remove(transmitterEditor);
                    transmitterEditor = null;
                }
            }

            if(!e.getComponents().containsKey(Component.ComponentType.RECEIVER) ||
                    e.componentsToRemove.containsKey(Component.ComponentType.RECEIVER)){
                if(receiverEditor != null){
                    remove(receiverEditor);
                    receiverEditor = null;
                }
            }

            revalidate();

            //updateButton.setEnabled(true);
            addComponentButton.setEnabled(true);
            eNamePanel.getInputField().setEnabled(true);
            ePositionPanel.setInputEnabled(true);
            eSpeedPanel.setInputEnabled(true);
            addSideBox.setEnabled(true);
            addTypeBox.setEnabled(true);
        }
    }

    public void disablePanelData(){
        addTypeBox.setVisible(true);
        eNamePanel.setData("");
        ePositionPanel.setData(new Vec2int());
        eSpeedPanel.setData(new Vec2int());

        if(radarEditor != null){
            remove(radarEditor);
            radarEditor = null;
        }

        if(transmitterEditor != null){
            remove(transmitterEditor);
            transmitterEditor = null;
        }

        if(receiverEditor != null){
            remove(receiverEditor);
            receiverEditor = null;
        }
        revalidate();

        //updateButton.setEnabled(false);
        addComponentButton.setEnabled(false);
        eNamePanel.getInputField().setEnabled(false);
        ePositionPanel.setInputEnabled(false);
        eSpeedPanel.setInputEnabled(false);
        addSideBox.setEnabled(false);
        addTypeBox.setEnabled(false);
    }

    private Entity.Type strToType(String str){
        if(str.equals(Entity.Type.AIR.getName()))
            return Entity.Type.AIR;
        else if(str.equals(Entity.Type.GROUND.getName()))
            return Entity.Type.GROUND;
        else if(str.equals(Entity.Type.SURFACE.getName()))
            return Entity.Type.SURFACE;

        return null;
    }

    public Entity.Side intToSide(int i){
        if(i == 0)
            return Entity.Side.ALLY;
        return Entity.Side.ENEMY;
    }

    public void updateSelectedEntity(){
        try{
            Entity selectedEntity = app.mapView.getSelectedEntity();
            app.getShortcutManager().addChange(selectedEntity);

            Vec2int oldPos = app.mapView.getSelectedEntity().getPos();
            String name = eNamePanel.readData();
            Vec2int pos = ePositionPanel.readData();
            Vec2int speed = eSpeedPanel.readData();

            int radarRange = 0;
            if(radarEditor != null){
                radarRange = radarEditor.readData();
                selectedEntity.componentsToRemove.remove(Component.ComponentType.RADAR);

                if(!selectedEntity.hasComponent(Component.ComponentType.RADAR)){
                    selectedEntity.addComponent(new Radar(selectedEntity,
                            app.world.entities));
                    //debugLog("added radar");
                }
            }

            int transmitterRange = 0;
            if(transmitterEditor != null){
                transmitterRange = transmitterEditor.readData();
                selectedEntity.componentsToRemove.remove(Component.ComponentType.TRANSMITTER);

                if(!selectedEntity.hasComponent(Component.ComponentType.TRANSMITTER)){
                    TDLTransmitterComp transmitter = new TDLTransmitterComp(selectedEntity,
                            app.world.entities);
                    transmitter.setRange(transmitterRange);
                    selectedEntity.addComponent(transmitter);
                }
                else{
                    ((TDLTransmitterComp) selectedEntity.getComponent(Component.ComponentType.TRANSMITTER)).
                            setRange(transmitterRange);
                }

            }

            if(receiverEditor != null){
                selectedEntity.componentsToRemove.remove(Component.ComponentType.RECEIVER);
                if(!selectedEntity.hasComponent(Component.ComponentType.RECEIVER)){
                    selectedEntity.addComponent(new TDLReceiverComp(selectedEntity,
                            app.world.entities));
                    //debugLog("added radar");
                }
            }

            int sideInt = addSideBox.getSelectedIndex();
            type = (String) addTypeBox.getSelectedItem();
            if(app.pixelColor.isLocationValidForType(type, pos) && name != null){
                app.updateSelectedEntity(name, intToSide(sideInt), pos, speed, radarRange, strToType(type), selectedEntity.getId());
            }
            else if(!app.pixelColor.isLocationValidForType(type, pos)){
                ePositionPanel.error();
                app.world.changedEntities.pop();
                app.world.changes.pop();
            }

            for(Component c : selectedEntity.getComponents().values())
                app.hierarchyPanel.updateComponent(c.getType(), app.mapView.getSelectedEntity());

            if(!app.mapView.getSelectedEntity().getPreviousPositions().isEmpty() &&
                    app.mapView.getSelectedEntity().getPreviousPositions().getLast().x != pos.x &&
                    app.mapView.getSelectedEntity().getPreviousPositions().getLast().y != pos.y){
                app.mapView.getSelectedEntity().getPreviousPositions().push(oldPos);

            }
            app.hierarchyPanel.update(1000);}
        catch (Exception ex){
            app.world.changedEntities.pop();
            app.world.changes.pop();
            System.out.println("Deleted last change from stack due to invalid entity update attempt");
            System.out.println("CATCHED SMT");
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
            ePositionPanel.dataValidate();
            eSpeedPanel.dataValidate();
            if(radarEditor != null)
                radarEditor.dataValidate();
        }
    }

    public TransmitterEditor getTransmitterEditor() {
        return transmitterEditor;
    }

    public RadarEditor getRadarEditor() {
        return radarEditor;
    }

    public ReceiverEditor getReceiverEditor() {
        return receiverEditor;
    }
}
