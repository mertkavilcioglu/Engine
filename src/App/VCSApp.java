package App;

import Sim.*;
import UI.*;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VCSApp {

    // VIRTUAL COMBAT SYSTEM (VCS)
    public World world;
    private JFrame window;
    public MapView mapView;
    public AppListenerController appListenerController;
    public EntityEditorView editorPanel;
    public HierarchyView hierarchyPanel;
    public ActionPanel actionPanel;
    public LogPanel logPanel;
    public PlayPausePanel playPausePanel;
    public ImportPanel importPanel;
    public MapPixelPosPanel mapPixelPosPanel;
    public PixelColor pixelColor;
    public UIColorManager uiColorManager;
    public LocalFile localFile;
    private boolean ctrlOn = false;

    public Timer simTimer;

    public void run() {
        System.out.println("App::run");
        boolean isWorking = true;
        int time = 0;
        int updateInterval = 1000;

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
        appListenerController = new AppListenerController();
        world = new World(this);
        window = new AppWindow(this);
        mapView = new MapView(this);
        editorPanel = new EntityEditorView(this);
        hierarchyPanel = new HierarchyView(this);
        logPanel = new LogPanel(this);
        actionPanel = new ActionPanel(this);
        playPausePanel = new PlayPausePanel(this);
        importPanel = new ImportPanel(this);
        mapPixelPosPanel = new MapPixelPosPanel(this);
        pixelColor = new PixelColor(this);
        localFile = new LocalFile();

        JPanel mergeSouthPanel = new JPanel(new GridLayout(1,2));
        //mergeSouthPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
        mergeSouthPanel.setBackground(uiColorManager.TOP_BAR_COLOR);
        mergeSouthPanel.add(actionPanel);
        mergeSouthPanel.add(logPanel);
        actionPanel.setBorder(BorderFactory.createLineBorder(uiColorManager.DARK_TITLE_COLOR_1,2));
        logPanel.setBorder(BorderFactory.createLineBorder(uiColorManager.DARK_TITLE_COLOR_1,2));

        JPanel mergeNorthPanel = new JPanel(new BorderLayout());
        mergeNorthPanel.add(importPanel, BorderLayout.WEST);
        mergeNorthPanel.add(playPausePanel, BorderLayout.CENTER);
        mergeNorthPanel.add(mapPixelPosPanel, BorderLayout.EAST);
        mergeNorthPanel.setBackground(uiColorManager.TOP_BAR_COLOR);
        EmptyBorder eBorder = new EmptyBorder(4,4,4,4);
        mergeNorthPanel.setBorder(eBorder);
        mergeNorthPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));

        int targetWidthNorth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int targetHeightNorth = Toolkit.getDefaultToolkit().getScreenSize().height / 26;
        mergeNorthPanel.setPreferredSize(new Dimension(targetWidthNorth, targetHeightNorth));

//        int targetWidthSouth = Toolkit.getDefaultToolkit().getScreenSize().width;
//        int targetHeightSouth = Toolkit.getDefaultToolkit().getScreenSize().height / 4;
//        mergeSouthPanel.setPreferredSize(new Dimension(targetWidthSouth, targetHeightSouth));

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

        File savedFile = localFile.createLocalFile("SavedSenario");
        if (savedFile.exists()){
            GetInput input = new GetInput();
            input.readInputForReset(this, String.valueOf(savedFile));
        } else {
            Entity mert = world.createEntity2("Mert", 1);
            hierarchyPanel.entityAdded(mert);
            actionPanel.createNewTargetButton(mert);

            Entity emir = world.createEntity2("Emir", 0);
            hierarchyPanel.entityAdded(emir);
            actionPanel.createNewTargetButton(emir);

            Entity seda = world.createEntity2("Seda", 0);
            hierarchyPanel.entityAdded(seda);
            actionPanel.createNewTargetButton(seda);

            Entity hasan = world.createEntity2("Hasan", 0);
            hierarchyPanel.entityAdded(hasan);
            actionPanel.createNewTargetButton(hasan);
        }

        window.setFocusable(true);
        window.requestFocus();
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                    if(mapView.getSelectedEntity() != null){
                        Entity ent = mapView.getSelectedEntity();
                        world.latestDeletedEntities.push(ent);
                        world.latestChanges.push(World.Change.DELETE);
                        removeEntityInstantaneously(mapView.getSelectedEntity());
                        mapView.repaint();
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                    ctrlOn = true;
                }

                if(e.getKeyCode() == KeyEvent.VK_C && ctrlOn){
                    if(mapView.getSelectedEntity() != null){
                        world.setCopiedEntity(mapView.getSelectedEntity());
                    }
                }
                
                if(e.getKeyCode() == KeyEvent.VK_V && ctrlOn){
                    if(world.getCopiedEntity() != null &&
                            world.getCopiedEntity().CanMove(mapView.allPixelColors.get(mapView.getPixPos().toString()),
                                    world.getCopiedEntity().getType())){
                        Entity ent = world.getCopiedEntity();
                        String newName = String.format("%s - Copy", ent.getName());
                        Vec2int newPos = mapView.getPixPos();

                        if(ent.hasComponent("Radar"))
                            createEntity(newName, ent.getSide(), newPos, ent.getSpeed(), ((Radar)ent.getComponent("Radar")).getRange(), ent.getType());
                        else
                            createEntity(newName, ent.getSide(), newPos, ent.getSpeed(), 0, ent.getType());
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_Z && ctrlOn){
                    world.revertLastChange();
                }
            }

            @Override
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                    ctrlOn = false;
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int delta = 1000;
                int x = 0;

                //TODO: MAP BURADA BOYUT KAZANIYOR İLK
                mapView.initializeTheMap();


                simTimer = new Timer(delta, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println("Timer::update - THREAD : " + Thread.currentThread().getName());
                        // update world
                        world.update(delta);
                        pixelColor.update(delta);
                        hierarchyPanel.update(delta);
                        editorPanel.update();

                        world.entities.removeAll(world.entitiesToRemove);
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

    public void createEntity(String name, int side, Vec2int pos, Vec2int speed, int range, String type){
        Entity ent = world.createEntity(name, side, pos, speed, range, type);
        world.latestCreatedEntities.push(ent);
        world.latestChanges.push(World.Change.CREATE);
        hierarchyPanel.entityAdded(ent);
        actionPanel.createNewTargetButton(ent);
        mapView.setSelectedEntity(ent);
        mapView.repaint();
    }

    public Entity createEntityByRevert(String name, int side, Vec2int pos, Vec2int speed, int range, String type){
        Entity ent = world.createEntity(name, side, pos, speed, range, type);
        hierarchyPanel.entityAdded(ent);
        actionPanel.createNewTargetButton(ent);
        mapView.setSelectedEntity(ent);
        mapView.repaint();
        return ent;
    }

    public Entity createEntityByReset(String name, int side, Vec2int pos, Vec2int speed, int range, String type){
        Entity ent = world.createEntity(name, side, pos, speed, range, type);
        hierarchyPanel.entityAdded(ent);
        actionPanel.createNewTargetButton(ent);
        mapView.setSelectedEntity(ent);
        mapView.repaint();
        return ent;
    }

    public void updateSelectedEntity(String newName, int newSide, Vec2int newPos, Vec2int newSpeed, int newRange, String newType){
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
        world.entities.remove(e);
        hierarchyPanel.entityRemoved(e);
        actionPanel.deleteEntityFromTarget(e);
        mapView.getHoveredEntities().remove(e);
        editorPanel.disablePanelData();
        mapView.setSelectedEntity(null);
        actionPanel.disablePanel();
        mapView.repaint();
    }

    public void log(String message){
        if (logPanel != null) logPanel.messageToLog(message);
    }

    public void debugLog(String message){
        if (logPanel != null) logPanel.debugLogMessage(message);
    }

}
