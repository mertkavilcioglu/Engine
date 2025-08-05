package App;

import UI.*;
import Sim.Entity;
import Sim.World;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VCSapp {
    //TODO VCSpanel base class |MERT
    //TODO SimApp referans aracı olsun, her panelde app referansı bulunsun |MERT

    //TODO Layout tamamla, tum panelleri ekle
    //TODO tree hiyerarşi

    //TODO paneller icin selected
    //TODO ekrana logu bastırma

    //TODO PLAY PAUSE BIRLIKTE, TEK GIRME

    //OPT.
    //TODO invalid inputsa kırmızı yap fieldı

    // VIRTUAL COMBAT SYSTEM (VCS)
    public World world;
    JFrame window;
    MapView mapView;
    EntityEditorView editorPanel;
    HierarchyView hierarchyPanel;
    SouthPanel southPanel;
    ArrayList<JLabel> entityNames = new ArrayList<>();

    public void run() {
        System.out.println("App::run");
        boolean isWorking = true;
        int time = 0;
        int updateInterval = 1000;


        //world.entities.add(createEntity("Mert"));
        //world.entities.add(createEntity("Emir"));
        //world.entities.add(createEntity("Seda"));


        while (isWorking) {
            //System.out.println("App::run - update begin");
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
            //System.out.println("App::run - update end");
            //System.out.println("");
        }
    }

    public void runWithWindow() throws InterruptedException, InvocationTargetException {

        world = new World();
        window = new AppWindow(this);
        mapView = new MapView(this);
        editorPanel = new EntityEditorView(this);
        hierarchyPanel = new HierarchyView(this);
        southPanel = new SouthPanel(this);

        window.add(mapView,BorderLayout.CENTER);
        window.add(editorPanel, BorderLayout.EAST);
        window.add(hierarchyPanel, BorderLayout.WEST);
        window.add(southPanel, BorderLayout.SOUTH);

        StringEditor eNamePanel = new StringEditor("Name:");
        Vec2intEditor ePositionPanel = new Vec2intEditor("Position:");
        Vec2intEditor eSpeedPanel = new Vec2intEditor("Velocity");

        editorPanel.add(eNamePanel);
        editorPanel.add(ePositionPanel);
        editorPanel.add(eSpeedPanel);

        JButton createBtn = createEntityButton(eNamePanel, ePositionPanel, eSpeedPanel, hierarchyPanel);
        editorPanel.add(createBtn);

        window.setVisible(true);

        Entity mert = world.createEntity("Mert");
        world.entities.add(mert);
        addLeaf(mert.getName(), mert.getPos(), mert.getSpeed());

        Entity emir = world.createEntity("Emir");
        world.entities.add(emir);
        addLeaf(emir.getName(), emir.getPos(), emir.getSpeed());

        Entity seda = world.createEntity("Seda");
        world.entities.add(seda);
        addLeaf(seda.getName(), seda.getPos(), seda.getSpeed());


        //addLabel(hierarchyPanel, "Mert");
        //addLabel(hierarchyPanel, "Emir");
        //addLabel(hierarchyPanel, "Seda");


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer(1000, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println("Update THREAD: 2" + Thread.currentThread().getName());
                        // update world
                        world.update(1000);
                        // render world
                        //w.render();
                        renderToWindow();
                    }
                });
                timer.start();
            }
        });

        //System.out.println("CURRENT THREAD END: 3" + Thread.currentThread().getName());

    }

    private JButton createEntityButton(StringEditor namePanel, Vec2intEditor posPanel,
                                       Vec2intEditor speedPanel, HierarchyView hierarchyPanel){

        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e -> {
            //System.out.println("button clicked");
            try{
                Entity ent = world.createEntity(namePanel.readData(), posPanel.readData(), speedPanel.readData());
                if(ent != null && !ent.isNullName()){
                    world.entities.add(ent);
                    //addLabel(hierarchyPanel, ent.getName());
                    addLeaf(ent.getName(), ent.getPos(), ent.getSpeed());
                }
            }
            catch (NumberFormatException err){ //TODO bi tekrar bak
                posPanel.dataValidate();
                speedPanel.dataValidate();
            }
        });
        createBtn.setFocusable(false);

        return createBtn;
    }

    private void renderToWindow() {
        window.repaint();
    }

    private boolean integerValidate(String s){
        if(s == null)
            return false;
        int str;
        try {
            str = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private int stringToInt(String s){
        return Integer.parseInt(s);
    }


    private void addLabel(JPanel panel, String str){
        JLabel nameLabel = new JLabel(str);
        panel.add(nameLabel);
        panel.revalidate();
    }

    private void addLeaf(String name, Vec2int pos, Vec2int speed){
        hierarchyPanel.addNameLeaf(name, pos, speed);
        hierarchyPanel.revalidate();
    }

    public JFrame getWindow(){
        return window;
    }



}
