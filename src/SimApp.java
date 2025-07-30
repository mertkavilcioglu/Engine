import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

public class SimApp {
    World world;
    JFrame window;
    MapView mapView;

    public Entity createUnit(String name) {
        Entity u = new Entity();
        u.name = name;

        u.pos = Vec2int.getRandom(world.map.maxX / 8 ,world.map.maxY / 6);
        u.speed = Vec2int.getRandom(0,4,0,4);

            /*
            u.speed = new Vec2int();
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

        u.components.add(r);

        return u;
    }

    private Entity createEntity(String eName, String ePosX, String ePosY){
        boolean isValid = true;
        if(eName == null || eName.trim().isEmpty()){
            System.out.println("NAME CANNOT BE NULL");
            isValid = false;
        }
        if(!IntegerValidate(ePosX)){
            System.out.println("POS X IS NOT VALID");
            isValid = false;
        }
        if(!IntegerValidate(ePosY)){
            System.out.println("POS Y IS NOT VALID");
            isValid = false;
        }
        if(isValid){
            //create entity
            System.out.format("Created entity %s with x:%d and y:%d", eName, stringToInt(ePosX), stringToInt(ePosY));
            Entity u = new Entity();
            u.name = eName;

            u.pos = new Vec
            u.speed = Vec2int.getRandom(0,4,0,4);

            Radar r = new Radar(u);
            u.components.add(r);

            return u;
        }
    }

    public void run() {
        System.out.println("App::run");
        boolean isWorking = true;
        int time = 0;
        int updateInterval = 1000;


        world.entities.add(createUnit("Mert"));
        world.entities.add(createUnit("Emir"));
        world.entities.add(createUnit("Seda"));


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

        JPanel compPanel = new JPanel();
        compPanel.setLayout(new GridLayout(25,1));
        compPanel.setPreferredSize(new Dimension(150,window.getHeight()));
        //addPanel.setBackground(Color.lightGray);
        window.add(compPanel, BorderLayout.EAST);
        compPanel.setBorder(new TitledBorder("Create Entity"));

        //System.out.println("CURRENT THREAD: 1" + Thread.currentThread().getName());

        JLabel eName = new JLabel("Name:");
        compPanel.add(eName);
        JTextField eNameField = new JTextField();
        compPanel.add(eNameField);
        JLabel poslabel = new JLabel("Position:");
        compPanel.add(poslabel);

        JPanel posPnl = new JPanel(new GridLayout(1,4));
        JLabel posXlabel = new JLabel("X:");
        JTextField posXfield = new JTextField();
        JLabel posYlabel = new JLabel("Y:");
        JTextField posYfield = new JTextField();

        posPnl.add(posXlabel);
        posPnl.add(posXfield);
        posPnl.add(posYlabel);
        posPnl.add(posYfield);

        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e -> {
            System.out.println("button clicked");
            world.entities.add(createEntity(eNameField.getText(), posXfield.getText(), posYfield.getText()));
        });
        createBtn.setFocusable(false);


        compPanel.add(posPnl);
        compPanel.add(createBtn);

        window.setVisible(true);

        world.entities.add(createUnit("Mert"));
        world.entities.add(createUnit("Emir"));
        world.entities.add(createUnit("Seda"));

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



    private boolean IntegerValidate(String s){
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
}
