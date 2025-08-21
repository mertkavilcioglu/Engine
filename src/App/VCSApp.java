package App;

import Sim.Orders.Attack;
import Sim.Orders.Follow;
import Sim.Orders.Move;
import UI.*;
import Sim.Entity;
import Sim.World;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VCSApp {
    //TODO paneller icin selected
    //TODO ekrana logu bastırma
    //TODO PLAY PAUSE BIRLIKTE, TEK GIRME

    //TODO ilk başta her birim target olsun //DONE
    //TODO entitylere taraf ekle, hem sağ panele hem de renge göre order düzenle

    //TODO hierarchyde pozisyonlar güncellenecek //DONE
    //TODO hız değiştiği zaman, tree'de hız da güncellenmesi lazım  //DONE

    //TODO radar componentini create'e bağla


    // VIRTUAL COMBAT SYSTEM (VCS)
    public World world;
    private JFrame window;
    private MapView mapView;
    private EntityEditorView editorPanel;
    private HierarchyView hierarchyPanel;
    public ActionPanel actionPanel;
    private LogPanel logPanel;
    private PlayPausePanel playPausePanel;
    private ImportPanel importPanel;
    private ArrayList<JLabel> entityNames = new ArrayList<>();
    public Follow follow;
    public Attack attack;
    public Move move;

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

    public void attackTest() {
        attack.attackEntity(world.entities.get(0), world.entities.get(1));
    }

    public void runWithWindow() throws InterruptedException, InvocationTargetException {

        world = new World();
        window = new AppWindow(this);
        mapView = new MapView(this);
        editorPanel = new EntityEditorView(this);
        hierarchyPanel = new HierarchyView(this);
        logPanel = new LogPanel(this);
        actionPanel = new ActionPanel(this);
        playPausePanel = new PlayPausePanel(this);
        importPanel = new ImportPanel(this);
        //follow = new Follow(this);
        //attack = new Attack(this);
        //move = new Move(this);

        JPanel mergeSouthPanel = new JPanel(new GridLayout(1,2));
        mergeSouthPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
        mergeSouthPanel.add(actionPanel);
        mergeSouthPanel.add(logPanel);

        JPanel mergeNorthPanel = new JPanel(new BorderLayout());
        mergeNorthPanel.add(importPanel, BorderLayout.WEST);
        mergeNorthPanel.add(playPausePanel, BorderLayout.CENTER);

        JScrollPane hierarchyScroll = new JScrollPane(hierarchyPanel);
        hierarchyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        hierarchyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        window.add(mapView,BorderLayout.CENTER);
        window.add(editorPanel, BorderLayout.EAST);
        window.add(hierarchyScroll, BorderLayout.WEST);
        window.add(mergeSouthPanel, BorderLayout.SOUTH);
        window.add(mergeNorthPanel, BorderLayout.NORTH);

        window.setVisible(true);

        Entity mert = world.createEntity("Mert", 0);
        hierarchyPanel.entityAdded(mert);
        actionPanel.createNewTargetButton(mert);

        Entity emir = world.createEntity("Emir", 0);
        hierarchyPanel.entityAdded(emir);
        actionPanel.createNewTargetButton(emir);

        Entity seda = world.createEntity("Seda", 0);
        hierarchyPanel.entityAdded(seda);
        actionPanel.createNewTargetButton(seda);

        Entity hasan = world.createEntity("Hasan", 0);
        hierarchyPanel.entityAdded(hasan);
        actionPanel.createNewTargetButton(hasan);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer(1000, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // update world
                        world.update(1000);
                        hierarchyPanel.update(1000);
                        actionPanel.update(1000);
                        //isEntitySelected();
                        // render world
                        //w.render();
                        //attack.attackEntity(emir, mert);
                        world.entities.removeAll(world.entitiesToRemove);
                        world.entitiesToRemove.clear();
                        renderToWindow();
                    }
                });
                timer.start();
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
    }

    public void removeEntity(Entity e){
        world.entitiesToRemove.add(e);
        hierarchyPanel.entityRemoved(e);
        actionPanel.deleteTargetButton(e);
        //TODO: tree'den sil (hierarchyPanel.entityRemoved(e);)
        //TODO: gerekirse diğer panellere de bildir bu entitynin silindiğini
    }

    public void log(String message){
        if (logPanel != null) logPanel.messageToLog(message);
    }


    /*public void isEntitySelected(){
        actionPanel.selectedUnit(hierarchyPanel.entitySelected());
    }*/



}
