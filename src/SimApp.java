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

    private Entity createEntity(String eName, String ePosX, String ePosY, String eSpeedX, String eSpeedY){
        //TODO add speed, radar range and attack target inputs through UI
        boolean isValid = true;
        if(eName == null || eName.trim().isEmpty()){
            System.out.println("NAME CANNOT BE NULL");
            isValid = false;
        }
        if(!integerValidate(ePosX)){
            System.out.println("POS X IS NOT VALID");
            isValid = false;
        }
        if(!integerValidate(ePosY)){
            System.out.println("POS Y IS NOT VALID");
            isValid = false;
        }
        if(!integerValidate(eSpeedX)){
            System.out.println("SPEED X IS NOT VALID");
            isValid = false;
        }
        if(!integerValidate(eSpeedY)){
            System.out.println("SPEED Y IS NOT VALID");
            isValid = false;
        }
        if(isValid){
            //create entity
            System.out.format("Created entity %s with x:%d and y:%d", eName, stringToInt(ePosX), stringToInt(ePosY));
            Entity u = new Entity();
            u.name = eName;

            u.pos = new Vec2int(stringToInt(ePosX), stringToInt(ePosY));
            //u.speed = Vec2int.getRandom(0,4,0,4);
            u.speed = new Vec2int(stringToInt(eSpeedX), stringToInt(eSpeedY));

            Radar r = new Radar(u);
            u.components.add(r);

            return u;
        }
        return null;
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

        JPanel compPanel = new JPanel();
        compPanel.setLayout(new GridLayout(25,1));
        compPanel.setPreferredSize(new Dimension(150,window.getHeight()));
        //addPanel.setBackground(Color.lightGray);
        window.add(compPanel, BorderLayout.EAST);
        compPanel.setBorder(new TitledBorder("Create Entity"));

        //System.out.println("CURRENT THREAD: 1" + Thread.currentThread().getName());

        TextFieldInputPanel eNamePanel = new TextFieldInputPanel(compPanel, "Name:");
        Vect2IntInputPanel ePositionPanel = new Vect2IntInputPanel(compPanel, "Position:");
        Vect2IntInputPanel eSpeedPanel = new Vect2IntInputPanel(compPanel, "Velocity");

        JPanel hierarchyPanel = new JPanel();
        hierarchyPanel.setLayout(new GridLayout(25,1));
        hierarchyPanel.setPreferredSize(new Dimension(150,window.getHeight()));
        //addPanel.setBackground(Color.lightGray);
        window.add(hierarchyPanel, BorderLayout.WEST);
        hierarchyPanel.setBorder(new TitledBorder("Hierarchy"));

        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e -> {
            System.out.println("button clicked");
            Entity ent = createEntity(eNamePanel.getInputField().getText(), ePositionPanel.getPosXinputField().getText(),
                    ePositionPanel.getPosYinputField().getText(), eSpeedPanel.getPosXinputField().getText(),
                    eSpeedPanel.getPosYinputField().getText());
            if(ent != null){
                world.entities.add(ent);
                addLabel(hierarchyPanel, ent.name);

            }
        });
        createBtn.setFocusable(false);
        compPanel.add(createBtn);



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

    public class TextFieldInputPanel{
        private JLabel inputLabel;
        private JTextField inputField;

        TextFieldInputPanel(JPanel panel, String label){
            inputLabel = new JLabel(label);
            panel.add(inputLabel);
            inputField = new JTextField();
            panel.add(inputField);
        }

        public JTextField getInputField(){
            return inputField;
        }
    }
    public class Vect2IntInputPanel{
        private JLabel panelNameLabel;
        private JPanel gridPanel;
        private JLabel posXlabel;
        private JTextField posXinputField;
        private JLabel posYlabel;
        private JTextField posYinputField;

        Vect2IntInputPanel(JPanel panel, String label){
            panelNameLabel = new JLabel(label);
            panel.add(panelNameLabel);
            gridPanel = new JPanel(new GridLayout(1,4));
            posXlabel = new JLabel("X:");
            posXinputField = new JTextField();
            posYlabel = new JLabel("Y:");
            posYinputField = new JTextField();

            gridPanel.add(posXlabel);
            gridPanel.add(posXinputField);
            gridPanel.add(posYlabel);
            gridPanel.add(posYinputField);

            panel.add(gridPanel);
        }

        public JTextField getPosXinputField(){
            return posXinputField;
        }

        public JTextField getPosYinputField(){
            return posYinputField;
        }
    }

    private void addLabel(JPanel panel, String str){
        JLabel nameLabel = new JLabel(str);
        panel.add(nameLabel);
        panel.revalidate();
    }

}
