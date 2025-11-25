package App;

import Sim.*;
import Sim.Component;
import Sim.Managers.IDManager;
import Sim.Managers.ShortcutManager;
import UI.*;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class VCSApp {

    // VIRTUAL COMBAT SYSTEM (VCS)
    public World world;
    public JFrame window;
    public MapView mapView;
    public EntityEditorView editorPanel;
    public HierarchyView hierarchyPanel;
    public ActionPanel actionPanel;
    public LogPanel logPanel;
    public PlayPausePanel playPausePanel;
    public LoadSavePanel loadSavePanel;
    public MapPixelPosPanel mapPixelPosPanel;
    public PixelColor pixelColor;
    public UIColorManager uiColorManager;
    private Entity headQuarter; // TODO BB Bilemedim
    private ShortcutManager shortcutManager;
    public IDManager idManager;

    public Timer simTimer;

    //TODO: undoManager'e taşı revert ile ilgli kısımları
    public void run() {
        System.out.println("App::run");
        boolean isWorking = true;
        int time = 0;
        int updateInterval = 1000; // 1000

        while (isWorking) {
            try {
                Thread.sleep(updateInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // update world
            world.update(updateInterval);

            // render world
            world.render();

            time += updateInterval;
        }
    }


    public void runWithWindow() throws InterruptedException, InvocationTargetException {
        uiColorManager = new UIColorManager();
        world = new World(this);
        window = new AppWindow(this);
        mapView = new MapView(this);
        editorPanel = new EntityEditorView(this);
        hierarchyPanel = new HierarchyView(this);
        logPanel = new LogPanel(this);
        actionPanel = new ActionPanel(this);
        playPausePanel = new PlayPausePanel(this);
        loadSavePanel = new LoadSavePanel(this);
        mapPixelPosPanel = new MapPixelPosPanel(this);
        pixelColor = new PixelColor(this);
        shortcutManager = new ShortcutManager(this);
        idManager = new IDManager();

        JPanel mergeSouthPanel = new JPanel(new GridLayout(1,2));
        mergeSouthPanel.add(actionPanel);
        mergeSouthPanel.add(logPanel);
        mergeSouthPanel.setBackground(uiColorManager.TOP_BAR_COLOR);
        actionPanel.setBorder(BorderFactory.createLineBorder(uiColorManager.DARK_TITLE_COLOR_1,2));
        logPanel.setBorder(BorderFactory.createLineBorder(uiColorManager.DARK_TITLE_COLOR_1,2));

        JPanel mergeNorthPanel = new JPanel(new BorderLayout());
        mergeNorthPanel.add(loadSavePanel, BorderLayout.WEST);
        mergeNorthPanel.add(playPausePanel, BorderLayout.CENTER);
        mergeNorthPanel.add(mapPixelPosPanel, BorderLayout.EAST);
        mergeNorthPanel.setBackground(uiColorManager.TOP_BAR_COLOR);
        EmptyBorder eBorder = new EmptyBorder(4,4,4,4);
        mergeNorthPanel.setBorder(eBorder);
        mergeNorthPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));

        int targetWidthNorth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int targetHeightNorth = Toolkit.getDefaultToolkit().getScreenSize().height / 26;
        mergeNorthPanel.setPreferredSize(new Dimension(targetWidthNorth, targetHeightNorth));

        JScrollPane hierarchyScroll = new JScrollPane(hierarchyPanel.tree);
        hierarchyScroll.getViewport().setBackground(uiColorManager.DARK_PANEL_COLOR);
        hierarchyScroll.setBorder(null);
        hierarchyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        hierarchyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        hierarchyScroll.setVerticalScrollBar(new JScrollBar(Adjustable.VERTICAL,0,0,0,0));
        hierarchyScroll.setHorizontalScrollBar(new JScrollBar(Adjustable.HORIZONTAL,0,0,0,0));

        hierarchyPanel.add(hierarchyScroll);

        window.add(mapView,BorderLayout.CENTER);
        window.add(editorPanel, BorderLayout.EAST);
        window.add(hierarchyPanel, BorderLayout.WEST);
        window.add(mergeSouthPanel, BorderLayout.SOUTH);
        window.add(mergeNorthPanel, BorderLayout.NORTH);

        window.setVisible(true);

        headQuarter = world.createCommander(null, 200);
        hierarchyPanel.entityAdded(headQuarter);

        window.setFocusable(true);
        window.requestFocus();
//        window.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                handleEntityDelete(e); // DEL & BACKSPACE
//                handleCtrlActivate(e); // CTRL
//                handleEntityCopy(e); // CTRL + C
//                handleEntityPaste(e); // CTRL + V
//                handleRevertingChanges(e); // CTRL + Z
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e){
//                handleCtrlDeactivate(e);
//            }
//        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int delta = 1000; // 1000
                int x = 0;

                mapView.initializeTheMap();

                simTimer = new Timer(delta, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println("Timer::update - THREAD : " + Thread.currentThread().getName());
                        // update world
                        world.update(delta);
                        world.processSendList2();
                        pixelColor.update(delta);
                        hierarchyPanel.update(delta);
                        editorPanel.update();

                        //world.entities.removeAll(world.entitiesToRemove);
                        for(Entity ent : world.entitiesToRemove){
                            world.entityHashMap.get(ent.getId()).setActive(false);
                        }
                        world.entitiesToRemove.clear();
                        renderToWindow();
                    }
                });
            }
        });
    }

    private void renderToWindow() {
        window.repaint();
    }


    public JFrame getWindow(){
        return window;
    }

    public Entity createEntity(String name, Entity.Side side, Vec2int pos, Vec2int speed, Entity.Type type){
        Entity ent = world.createEntity(name, side, pos, speed, type);
//        world.createdEntities.push(ent);
//        world.changes.push(World.Change.CREATE);
        //ent.setId(idManager.createId(ent));
        debugLog(String.format("Id of %s : %s", ent.getName(), ent.getId()));
        world.changes.push(new Entity(world, name, side, pos, speed, type, ent.getComponents(), false));

        world.changedEntities.push(ent);
        ent.setActive(true);
        hierarchyPanel.entityAdded(ent);
        actionPanel.createNewTargetButton(ent);
        mapView.setSelectedEntity(ent);
        mapView.repaint();

        return ent;
    }

    public Entity createEntityByReset(String name, Entity.Side side, Vec2int pos, Vec2int speed, int range, Entity.Type type){
        Entity ent;
        if (type == Entity.Type.HQ || type == null){
            ent = world.createCommander(pos, range);
            headQuarter = ent;
        }else {
            ent = world.createEntity(name, side, pos, speed, type);
            actionPanel.createNewTargetButton(ent);
        }
        hierarchyPanel.entityAdded(ent);
        mapView.setSelectedEntity(ent);
        mapView.repaint();
        return ent;
    }

    public void updateSelectedEntity(String newName, Entity.Side newSide, Vec2int newPos, Vec2int newSpeed, int newRange, Entity.Type newType){
        mapView.getSelectedEntity().updateEntity(newName, newSide, newPos, newSpeed, newRange, newType);
        mapView.repaint();
        hierarchyPanel.entityChanged();
    }

    public void removeEntity(Entity e){
        world.entitiesToRemove.add(e);
        hierarchyPanel.entityRemoved(e);
        actionPanel.deleteEntityFromTarget(e);
        mapView.getHoveredEntities().remove(e);
        editorPanel.disablePanelData();
        mapView.setSelectedEntity(null);
        mapView.repaint();
    }

    public void removeEntityInstantaneously(Entity e){
        //world.entities.remove(e);
        e.setActive(false);
        hierarchyPanel.entityRemoved(e);
        actionPanel.deleteEntityFromTarget(e);
        mapView.getHoveredEntities().remove(e);
        editorPanel.disablePanelData();
        mapView.setSelectedEntity(null);
        actionPanel.disablePanel();
        mapView.repaint();
    }

    public void saveSenario(File file){
        File saveFile = file;
        if (saveFile.exists()){
            saveFile.delete();
        }
        try {
            FileWriter myWriter = new FileWriter(saveFile);
            int range = 0;
            for (Sim.Entity ent : world.entities) {
                String posStr;
                posStr = ent.getPos().toString().substring(1, ent.getPos().toString().length() - 1);
                for (Component c : ent.getComponents().values()){
                    if(c instanceof Radar){
                        if(((Radar) c).getRange() != 0){
                            range = ((Radar) c).getRange();
                        } else range = 0;
                    }
                }
                String speedStr = ent.getSpeed().toString().substring(1, ent.getSpeed().toString().length() - 1);
                myWriter.write(ent.getName() + "\n");
                myWriter.write(ent.getSide() == Entity.Side.ENEMY ? "Enemy" : "Ally");
                myWriter.write("\n");
                myWriter.write(ent.getType().getName() + "\n");
                myWriter.write(posStr + "\n");
                myWriter.write(speedStr + "\n");
                myWriter.write(range + "\n");

            }
            myWriter.close();
        }catch (IOException ex) {
            System.out.println("VCSApp:: saveSenario func:: IOException");
            ex.printStackTrace();
        }
    }

    public void log(String message){
        if (logPanel != null) logPanel.messageToLog(message);
    }

    public void debugLog(String message){
        if (logPanel != null) logPanel.debugLogMessage(message);
    }

    public void debugLogError(String message){
        if (logPanel != null) logPanel.debugLogError(message);
    }

    public void createHQ(boolean b){
        if (!b){
            headQuarter = world.createCommander(null, 5000);
            hierarchyPanel.entityAdded(headQuarter);
        }
    }

    public Entity getHQ(){
        return headQuarter;
    }

    public void setHQ(Entity entity){
        headQuarter = entity;
    }

    public ShortcutManager getShortcutManager(){
        return shortcutManager;
    }
}
