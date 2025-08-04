import UI.EditorView;
import UI.HierarchyView;
import UI.TextEditor;
import UI.Vec2intEditor;
import Vec.Vec2int;
import Sim.Entity;
import Sim.Radar;
import Sim.World;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class SimApp {
    //TODO UI ile ilgili kisimlari foldera tasi ayri classlarla
    //TODO sim ile ilgili seyler main harici sim folder'a
    //TODO Layout tamamla, tum panelleri ekle
    //TODO panelleri ayri class yap extn. JPanel
    //TODO paneller icin selected
    //TODO projeye isim bul
    //TODO invalid inputsa kırmızı yap fieldı
    //TODO InputVerifier
    World world;
    JFrame window;
    MapView mapView;
    ArrayList<JLabel> entityNames = new ArrayList<>();
    public Entity createEntity(String name) {
        Entity u = new Entity();
        u.setName(name);

        u.setPos(Vec2int.getRandom(world.map.maxX / 8 ,world.map.maxY / 6));
        u.setSpeed(Vec2int.getRandom(0,4,0,4));


            /*
            u.speed = new Vec.Vec2int();
            u.speed.x = 0; //ThreadLocalRandom.current().nextInt(1, 4);
            u.speed.y = 0; //ThreadLocalRandom.current().nextInt(1, 4);
            */
        Radar r = new Radar(u);

        //Scanner scanner = new Scanner(System.in);
        //System.out.format("Assign the range of %s's radar: ", u.name);

        //int input = 20;
            /*
            do
            {
                try {
                    String s = scanner.nextLine();
                    input = Integer.parseInt(s);
                    break;
                }
                catch (Exception e)
                {
                    System.out.println("Couldn't parse input, please try again");
                }
            }
            while (true);
            */

        //r.range = input;

        u.addComponents(r);
        return u;
    }

    private Entity createEntity(String eName, Vec2int pos, Vec2int speed){
        //TODO add speed, radar range and attack target inputs through UI
        //create entity
        if(eName == null){
            //System.out.println("NAME IS NULL");
            return null;
        }

        System.out.format("Created entity %s with x:%d and y:%d", eName, pos.x, pos.y);
        Entity u = new Entity();
        u.setName(eName);

        u.setPos(new Vec2int(pos.x, pos.y));
        //u.speed = Vec.Vec2int.getRandom(0,4,0,4);
        u.setSpeed(new Vec2int(speed.x, speed.y));

        Radar r = new Radar(u);
        u.addComponents(r);

        return u;
    }

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

        window = new JFrame("Military Grade Simulation");
        window.setSize(world.map.maxX, world.map.maxY);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // TODO READ SWING LAYOUT MANAGERS
        // BoderLayout
        // GridLayout
        // GridBagLayout
        window.setLayout(new BorderLayout(10,10));

        // TODO READ SWING COMPNENTS - JPANEL
        // TODO JButton JTextField // Image? Icon?
        // TODO READ SWING RENDERING ON JPANEL
        // TODO GRAPHICS & GRAPHICS2D

        mapView = new MapView(world);
        window.add(mapView,BorderLayout.CENTER);

        EditorView editorPanel = new EditorView();
        editorPanel.setPreferredSize(new Dimension(150,window.getHeight()));
        //addPanel.setBackground(Color.lightGray);
        window.add(editorPanel, BorderLayout.EAST);
        editorPanel.setBorder(new TitledBorder("Create Entity"));

        //System.out.println("CURRENT THREAD: 1" + Thread.currentThread().getName());

        TextEditor eNamePanel = new TextEditor("Name:");
        Vec2intEditor ePositionPanel = new Vec2intEditor("Position:");
        Vec2intEditor eSpeedPanel = new Vec2intEditor("Velocity");
        editorPanel.add(eNamePanel);
        editorPanel.add(ePositionPanel);
        editorPanel.add(eSpeedPanel);

        HierarchyView hierarchyPanel = new HierarchyView();
        hierarchyPanel.setPreferredSize(new Dimension(150,window.getHeight()));
        //addPanel.setBackground(Color.lightGray);
        window.add(hierarchyPanel, BorderLayout.WEST);
        hierarchyPanel.setBorder(new TitledBorder("Hierarchy"));


        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e -> {
            //System.out.println("button clicked");
            try{
                Entity ent = createEntity(eNamePanel.readData(), ePositionPanel.readData(), eSpeedPanel.readData());
                if(ent != null && !ent.isNullName()){
                    world.entities.add(ent);
                    addLabel(hierarchyPanel, ent.getName());
                }
            }
            catch (NumberFormatException err){

            }
        });
        createBtn.setFocusable(false);
        editorPanel.add(createBtn);



        window.setVisible(true);

        world.entities.add(createEntity("Mert"));
        world.entities.add(createEntity("Emir"));
        world.entities.add(createEntity("Seda"));

        addLabel(hierarchyPanel, "Mert");
        addLabel(hierarchyPanel, "Emir");
        addLabel(hierarchyPanel, "Seda");


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

}
