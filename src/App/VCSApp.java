package App;

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

    //TODO ilk başta her birim target olsun
    //TODO entitylere taraf ekle, hem sağ panele hem de renge göre order düzenle

    //TODO hierarchyde pozisyonlar güncellenecek
    //TODO hız değiştiği zaman, tree'de hız da güncellenmesi lazım

    //TODO radar componentini create'e bağla


    // VIRTUAL COMBAT SYSTEM (VCS)
    public World world;
    private JFrame window;
    private MapView mapView;
    private EntityEditorView editorPanel;
    private HierarchyView hierarchyPanel;
    private ActionPanel actionPanel;
    private LogPanel logPanel;
    private ButtonsPanel buttonsPanel;
    private ImportPanel importPanel;
    private ArrayList<JLabel> entityNames = new ArrayList<>();

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

        world = new World();
        window = new AppWindow(this);
        mapView = new MapView(this);
        editorPanel = new EntityEditorView(this);
        hierarchyPanel = new HierarchyView(this);
        logPanel = new LogPanel(this);
        actionPanel = new ActionPanel(this);
        buttonsPanel = new ButtonsPanel(this);
        importPanel = new ImportPanel(this);

        JPanel mergeSouthPanel = new JPanel(new GridLayout(1,2));
        mergeSouthPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
        mergeSouthPanel.add(actionPanel);
        mergeSouthPanel.add(logPanel);

        JPanel mergeNorthPanel = new JPanel(new BorderLayout());
        mergeNorthPanel.add(importPanel, BorderLayout.WEST);
        mergeNorthPanel.add(buttonsPanel, BorderLayout.CENTER);

        window.add(mapView,BorderLayout.CENTER);
        window.add(editorPanel, BorderLayout.EAST);
        window.add(hierarchyPanel, BorderLayout.WEST);
        window.add(mergeSouthPanel, BorderLayout.SOUTH);
        window.add(mergeNorthPanel, BorderLayout.NORTH);

        window.setVisible(true);

        Entity mert = world.createEntity("Mert");
        hierarchyPanel.entityAdded(mert);

        Entity emir = world.createEntity("Emir");
        hierarchyPanel.entityAdded(emir);

        Entity seda = world.createEntity("Seda");
        hierarchyPanel.entityAdded(seda);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer(1000, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // update world
                        world.update(1000);
                        hierarchyPanel.update(1000);
                        // render world
                        //w.render();
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

    public void createEntity(String name, Vec2int pos, Vec2int speed){
        Entity ent = world.createEntity(name, pos, speed);
        hierarchyPanel.entityAdded(ent);
        // diğer panellere bu entity'yi dağıt
        // log, attack vs.
    }



}
