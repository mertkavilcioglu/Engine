package App;

import Sim.*;
import UI.*;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VCSApp {
    //TODO paneller icin selected
    //TODO PLAY PAUSE BIRLIKTE, TEK GIRME


    //TODO 25.08.2.25
    //imlecin bulunduğu pikseli tutabilelim, belki ileride add remove falan bağlarız
    //TODO imleçle haritadaki entityleri seçebilmek falan da yapılabilse iyi olur
    //TODO sınırdan çıkma kontrolü için, bir sonraki pikseli (sonraki update konumuna göre) hesap etme işini düşün
    //remove order butonları ekle alt panele
    //TODO exit butonu
    //TODO followu bağla
    //TODO haritanın arka planı olsun ve çözünürlüğü düşürülebilir
    //TODO play pause zamanı çıtırdan yaklaşıyor


    // VIRTUAL COMBAT SYSTEM (VCS)
    public World world;
    private JFrame window;
    public MapView mapView;
    public EntityEditorView editorPanel;
    public HierarchyView hierarchyPanel;
    public ActionPanel actionPanel;
    private LogPanel logPanel;
    private PlayPausePanel playPausePanel;
    private ImportPanel importPanel;
    public MapPixelPosPanel mapPixelPosPanel;
    private ArrayList<JLabel> entityNames = new ArrayList<>();
    public PixelColor pixelColor;
    public UIColorManager uiColorManager;

    public Timer simTimer;
    //TODO: mouse hareket ettikçe world render oluyor, bu render oyun durmuşken de mouse hareketinde
    // devam ediyor. mouse hareketine gerek kalmadan, pause olunda da dünya render edilsin ki create
    // entityler gözüksün anında.
    // eğer düzgün çalışmazsa world render ile engine render'ı ayırman gerekebilir belki

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

        //TODO: bu kısımları kendi classlarına taşı:
        importPanel.setBackground(uiColorManager.TOP_BAR_COLOR);

        JPanel mergeSouthPanel = new JPanel(new GridLayout(1,2));
        //mergeSouthPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
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

        JScrollPane hierarchyScroll = new JScrollPane(hierarchyPanel);
        hierarchyScroll.getViewport().setBackground(uiColorManager.DARK_PANEL_COLOR);
        hierarchyScroll.setBorder(null);
        hierarchyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        hierarchyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        window.add(mapView,BorderLayout.CENTER);
        window.add(editorPanel, BorderLayout.EAST);
        window.add(hierarchyScroll, BorderLayout.WEST);
        window.add(mergeSouthPanel, BorderLayout.SOUTH);
        window.add(mergeNorthPanel, BorderLayout.NORTH);

        window.setVisible(true);

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

        window.setFocusable(true);
        window.requestFocus();
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                    if(mapView.getSelectedEntity() != null){
                        removeEntityInstantaneously(mapView.getSelectedEntity());
                        mapView.repaint();
                    }
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int delta = 1000;
                int x = 0;

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
        hierarchyPanel.entityAdded(ent);
        actionPanel.createNewTargetButton(ent);
        // diğer panellere bu entity'yi dağıt
        // log, attack vs.
        mapView.setSelectedEntity(ent);
        mapView.repaint();
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
        mapView.repaint();
    }

    public void removeEntityInstantaneously(Entity e){
        world.entities.remove(e);
        hierarchyPanel.entityRemoved(e);
        actionPanel.deleteEntityFromTarget(e);
        mapView.getHoveredEntities().remove(e);
        mapView.repaint();
    }

    public void log(String message){
        if (logPanel != null) logPanel.messageToLog(message);
    }


    /*public void isEntitySelected(){
        actionPanel.selectedUnit(hierarchyPanel.entitySelected());
    }*/



}
