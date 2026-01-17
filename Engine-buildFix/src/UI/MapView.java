package UI;

import App.Main;
import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.TDL.TDLTransmitterComp;
import Sim.World;
import Var.RGB;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.io.*;


public class MapView extends VCSPanel {
    private World world;
    private Map<Entity,Vec2int> initialPoints = new HashMap<>();
    private Map<Entity,Vec2int> initialSpeeds = new HashMap<>();
    private Vec2int mapResolution;

    public Map<String, RGB> allPixelColors;
    private ImageIcon turkeyMap = new ImageIcon(new ImageIcon(getClass().getResource("/Assets/Turkey_map_painted.png")).getImage());
    private final Image friendlyHQ = new ImageIcon(getClass().getResource("/Assets/Symbols/nato_hq2j.jpg")).getImage();
    private final Image friendlyAir = new ImageIcon(getClass().getResource("/Assets/Symbols/nato_friendly_air.png")).getImage();
    private final Image friendlyLand = new ImageIcon(getClass().getResource("/Assets/Symbols/nato_friendly_land.png")).getImage();
    private final Image friendlySea = new ImageIcon(getClass().getResource("/Assets/Symbols/nato_friendly_sea.png")).getImage();
    private final Image enemyAir = new ImageIcon(getClass().getResource("/Assets/Symbols/nato_enemy_air.png")).getImage();
    private final Image enemyLand = new ImageIcon(getClass().getResource("/Assets/Symbols/nato_enemy_land.png")).getImage();
    private final Image enemySea = new ImageIcon(getClass().getResource("/Assets/Symbols/nato_enemy_sea.png")).getImage();
    private int targetWidth = 19;
    private final Font timesNewRoman = new Font("Times New Roman", Font.PLAIN, 10 );
    boolean isMouseEntered = false;

    private Vec2int pixPos = new Vec2int();
    private Queue<Entity> hoveredEntities = new LinkedList<>();

    private Entity selectedEntity;
    private Vec2int createPosition = new Vec2int();
    private boolean isDragging = false;

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private String screenResolution = String.format("%dx%d", gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());


    public Vec2int targetPos = null; // just for visual debug

    public void setTargetPos(Vec2int targetPos) {
        this.targetPos = targetPos;
    }

    public enum Mode {
        NORMAL,
        MOVE_POS_CAPTURE
    }
    private Mode mode = Mode.NORMAL;


    public MapView(VCSApp app) {
        super(app);
        this.world = app.world;
        setBackground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        setBorder(BorderFactory.createMatteBorder(0,1,0,1,Color.BLACK));


        allPixelColors = new HashMap<>();

//        turkeyMap = new ImageIcon(new ImageIcon(getClass().getResource("/Assets/map3.png").getImage().getScaledInstance(app.world.map.maxX, app.world.map.maxY, Image.SCALE_DEFAULT));
//        Image mapImg = turkeyMap.getImage();
//        BufferedImage bImage = new BufferedImage(mapImg.getWidth(null), mapImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
//        Graphics2D g = bImage.createGraphics();
//        g.drawImage(mapImg, 0, 0, null);
//        g.dispose();
//        JLabel label = new JLabel();
//        label.setIcon(turkeyMap);
//        add(label);
//        world.map.SetBufferedImage(bImage);
//
//        Vec2int pos = new Vec2int();
//        RGB color = new RGB();
//        locateAllPixels(bImage, pos, color);


/*
        Example usage of getting desired pixel color:

        Vec2int desired = new Vec2int();
        desired.x = 948;
        desired.y = 435;
        System.out.println( allPixelColors.get(desired.toString()));

 */

        JPopupMenu rightClickPopUpMenu = new JPopupMenu();

        JMenu createEntityMenu = new JMenu("Create Entity");

        JMenu ally = new JMenu("Ally");
        JMenuItem aTank = new JMenuItem(Entity.Type.GROUND.getName());
        JMenuItem aPlane = new JMenuItem(Entity.Type.AIR.getName());
        JMenuItem aShip = new JMenuItem(Entity.Type.SURFACE.getName());
        ally.add(aTank);
        ally.add(aPlane);
        ally.add(aShip);
        createEntityMenu.add(ally);

        JMenu enemy = new JMenu("Enemy");
        JMenuItem eTank = new JMenuItem(Entity.Type.GROUND.getName());
        JMenuItem ePlane = new JMenuItem(Entity.Type.AIR.getName());
        JMenuItem eShip = new JMenuItem(Entity.Type.SURFACE.getName());
        enemy.add(eTank);
        enemy.add(ePlane);
        enemy.add(eShip);
        createEntityMenu.add(enemy);
        rightClickPopUpMenu.add(createEntityMenu);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isMouseEntered = true;
                app.window.requestFocus();
            }
        });

        //when mouse moved around the map
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (isMouseEntered){
                    if (isCaptureMode()){
                        setCursor(createPointSelectionCurser(Color.LIGHT_GRAY, Color.LIGHT_GRAY));
                    } else setCursor(Cursor.getDefaultCursor());
                    //System.out.println("MapView::mouseMoved - THREAD : " + Thread.currentThread().getName());
                    pixPos = new Vec2int(e.getX(), e.getY());
                    app.mapPixelPosPanel.showPixelPosOfCursor(pixPos);
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e){
                if (isMouseEntered && !isActionPanelUsingMouseEvent){
                    if(!isDragging && selectedEntity != null){
                        isDragging = true;
                        selectedEntity.getPreviousPositions().push(selectedEntity.getPos());
                        app.world.changedEntities.push(selectedEntity);
                        world.changes.push(new Entity(world, selectedEntity.getName(), selectedEntity.getSide(), selectedEntity.getPos(),
                                    selectedEntity.getSpeed(), selectedEntity.getType(), selectedEntity.getComponents(), true));

                    }
                    pixPos = new Vec2int(e.getX(), e.getY());
                    app.mapPixelPosPanel.showPixelPosOfCursor(pixPos);
                    handleEntityDrag(selectedEntity);
                    if(isDragging) {
                        app.hierarchyPanel.update(1000);
                        app.actionPanel.updateOrderMode(selectedEntity);
                    }
                }

            }

        });
        //when mouse click an entity
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isCaptureMode()){
                    if (isMouseEntered){
                        setCursor(createPointSelectionCurser(Color.LIGHT_GRAY, Color.YELLOW));
                        Vec2int posFromMap = new Vec2int(e.getX(), e.getY());
                        app.actionPanel.setPosFromMap(posFromMap);
//                        app.appListenerController.setCaptureMode(false);
//                        setActionPanelUsingMouseEvent(false);

                    }
                    e.consume();
                    return;
                }

                if (isMouseEntered && !isActionPanelUsingMouseEvent){
                    //System.out.println("MapView::mousePressed - THREAD : " + Thread.currentThread().getName());
                    if (!hoveredEntities.isEmpty()) {
                        Entity topEntity = hoveredEntities.poll();
                        app.actionPanel.setSelectedEntity(topEntity);
                        setSelectedEntity(topEntity);
                        hoveredEntities.add(topEntity);

                        app.hierarchyPanel.selectNode(topEntity);

                    } else {
                        app.actionPanel.disablePanel();
                        app.hierarchyPanel.clearSelectionInTree();
                        setSelectedEntity(null);
                    }

                    if(e.getButton() == MouseEvent.BUTTON3){
                        createPosition = pixPos;
                        rightClickPopUpMenu.show(e.getComponent(),e.getX(),e.getY());
                        if(app.pixelColor.isLocationValidForType(Entity.Type.GROUND.getName(), pixPos)){
                            aTank.setEnabled(true);
                            eTank.setEnabled(true);
                        }
                        else{
                            aTank.setEnabled(false);
                            eTank.setEnabled(false);
                        }

                        if(app.pixelColor.isLocationValidForType(Entity.Type.AIR.getName(), pixPos)){
                            aPlane.setEnabled(true);
                            ePlane.setEnabled(true);
                        }
                        else{
                            aPlane.setEnabled(false);
                            ePlane.setEnabled(false);
                        }

                        if(app.pixelColor.isLocationValidForType(Entity.Type.SURFACE.getName(), pixPos)){
                            aShip.setEnabled(true);
                            eShip.setEnabled(true);
                        }
                        else{
                            aShip.setEnabled(false);
                            eShip.setEnabled(false);
                        }
                    }
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(isDragging){
                    isDragging = false;
//                    app.world.changedEntities.push(selectedEntity);
//                    if(selectedEntity.hasComponent("Radar"))
//                        world.changes2.push(new Entity(world, selectedEntity.getName(), selectedEntity.getSide(), selectedEntity.getPos(),
//                                selectedEntity.getSpeed(), ((Radar)selectedEntity.getComponent("Radar")).getRange(), selectedEntity.getType(), true));
//                    else
//                        world.changes2.push(new Entity(world, selectedEntity.getName(), selectedEntity.getSide(), selectedEntity.getPos(),
//                                selectedEntity.getSpeed(), 0, selectedEntity.getType(), true));


                }
            }
        });
        //when mouse exit the map
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (isMouseEntered){
                    //System.out.println("MapView::mouseExited - THREAD : " + Thread.currentThread().getName());
                    app.mapPixelPosPanel.showPixelPosOfCursor(null);
                    isMouseEntered = false;
                }
            }
        });

        aTank.addActionListener(e -> {
            app.createEntity("ALLY_TANK", Entity.Side.ALLY,createPosition,new Vec2int(0,0), Entity.Type.GROUND);
        });
        eTank.addActionListener(e -> {
            app.createEntity("ENEMY_TANK", Entity.Side.ENEMY,createPosition,new Vec2int(0,0), Entity.Type.GROUND);
        });

        aPlane.addActionListener(e -> {
            app.createEntity("ALLY_PLANE", Entity.Side.ALLY,createPosition,new Vec2int(0,0), Entity.Type.AIR);
        });
        ePlane.addActionListener(e -> {
            app.createEntity("ENEMY_PLANE",Entity.Side.ENEMY,createPosition,new Vec2int(0,0), Entity.Type.AIR);
        });

        aShip.addActionListener(e -> {
            app.createEntity("ALLY_SHIP",Entity.Side.ALLY,createPosition,new Vec2int(0,0), Entity.Type.SURFACE);
        });
        eShip.addActionListener(e -> {
            app.createEntity("ENEMY_SHIP",Entity.Side.ENEMY,createPosition,new Vec2int(0,0), Entity.Type.SURFACE);
        });
    }

    public Entity getSelectedEntity(){
        return selectedEntity;
    }

    public void saveInitialPoints(){
        initialPoints.clear();
        for(Entity ent: world.entities){
            initialPoints.put(ent, new Vec2int(ent.getPos().x, ent.getPos().y));
        }
    }

    public void saveSpeed(){
        for(Entity ent: world.entities){
            initialSpeeds.put(ent, ent.getSpeed());
        }
    }
    public Map<Entity,Vec2int> getInitialPoints(){
        return initialPoints;
    }

    public Map<Entity,Vec2int> getInitialSpeeds(){
        return initialSpeeds;
    }
    
    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for(Entity ent : app.world.entities){
            if(ent.getPos().distance(pixPos) < targetWidth-targetWidth/3){
                if(!hoveredEntities.contains(ent) && ent.isActive())
                    hoveredEntities.add(ent);
            }
            else
                hoveredEntities.remove(ent);
        }

        //System.out.println("MapView::paint - THREAD : " + Thread.currentThread().getName());

        for (int i = 0; i < world.entities.size(); i++) {
            if(!app.world.entitiesToRemove.contains(world.entities.get(i))){
                Entity e = world.entities.get(i);
                if(!e.isActive())
                    continue;
                boolean isHovered = hoveredEntities.contains(e);
                Vec2int pos = e.getPos();
                String name = e.getName();

                FontMetrics fontMetric = g.getFontMetrics();
                int textLength = fontMetric.stringWidth(name);
                int textX = pos.x - (textLength/2);
                int textY = pos.y - 10;

                if(e.getSide() == Entity.Side.ALLY)
                    g.setColor(Color.blue);
                else if (e.getSide() == Entity.Side.ENEMY)
                    g.setColor(Color.red);

                if(e.getSide() == Entity.Side.ALLY){
                    if (e.getType() == Entity.Type.HQ)
                        drawNormalizedImageByWidth(g, friendlyHQ, pos, targetWidth+2);
                    if(e.getType() == Entity.Type.AIR)
                        drawNormalizedImageByWidth(g, friendlyAir, pos, targetWidth);
                    else if(e.getType() == Entity.Type.GROUND)
                        drawNormalizedImageByWidth(g, friendlyLand, pos, targetWidth+2);
                    else if(e.getType() == Entity.Type.SURFACE)
                        drawNormalizedImageByWidth(g, friendlySea, pos, targetWidth);

                }

                else if(e.getSide() == Entity.Side.ENEMY){
                    if(e.getType() == Entity.Type.AIR)
                        drawNormalizedImageByWidth(g, enemyAir, pos, targetWidth);
                    else if(e.getType() == Entity.Type.GROUND)
                        drawNormalizedImageByWidth(g, enemyLand, pos, targetWidth + 2);
                    else if(e.getType() == Entity.Type.SURFACE)
                        drawNormalizedImageByWidth(g, enemySea, pos, targetWidth);
                }

                g.setFont(timesNewRoman);
                g.drawString(name, textX, textY);

                int targetWidthForHover = targetWidth + targetWidth*11/16;
                int half = targetWidthForHover / 2;

                int tdlRangeWidth;

                if((e.hasComponent(Component.ComponentType.TRANSMITTER) && !e.componentsToRemove.containsKey(Component.ComponentType.TRANSMITTER))
                ||(e.hasComponent(Component.ComponentType.TRANSMITTER) && app.editorPanel.getTransmitterEditor() != null))
                    tdlRangeWidth = ((TDLTransmitterComp) e.getComponent(Component.ComponentType.TRANSMITTER)).getTransmitterRange() * 2;
                else
                    tdlRangeWidth = 0;

                if (isHovered && (selectedEntity == null || selectedEntity != e)) {
                    g.drawOval(pos.x - half, pos.y - half, targetWidthForHover, targetWidthForHover);
                }
                else if(selectedEntity == e ){
                    g.drawRect(pos.x - half, pos.y - half, targetWidthForHover, targetWidthForHover);
                    g.setColor(Color.YELLOW);
                    g.drawOval(pos.x - tdlRangeWidth/2, pos.y - tdlRangeWidth/2, tdlRangeWidth, tdlRangeWidth);
                }

//                // ATTACK POINT DEBUG
//                if(targetPos != null){
//                    g.setColor(Color.GREEN);
//                    g.drawRect(targetPos.x, targetPos.y, 4,4);
//                }
            }
        }
    }

    public void drawNormalizedImageByWidth(Graphics g, Image img, Vec2int pos, int targetWidth){
        int targetHeight = (int) ((double) img.getHeight(null)
                / img.getWidth(null) * targetWidth);
        g.drawImage(img, pos.x - targetWidth/2, pos.y - targetHeight/2,
                targetWidth, targetHeight, this);
    }

    public void locateAllPixels(BufferedImage bImage, Vec2int pos, RGB color){
        try {

            Path jarDir;
            try {
                jarDir = Paths.get(
                        Main.class.getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .toURI()
                ).getParent();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            File mapPixelColors = jarDir.resolve("Assets/MapPixelColors.txt").toFile();
            if (mapPixelColors.createNewFile()) {
                System.out.println("File created: " + mapPixelColors.getName());
                fileWriter("Assets/MapPixelColors.txt", bImage ,pos, color);
            } else {
                System.out.println("File already exists.");

                InputStream fileStream = VCSApp.class
                        .getClassLoader()
                        .getResourceAsStream("Assets/MapPixelColors.txt");

                if (fileStream == null) {
                    throw new RuntimeException("Dosya bulunamadı: Assets/MapPixelColors.txt");
                }

                DataInputStream inputStream = new DataInputStream(fileStream);
                BufferedReader colorReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                if (mapPixelColors.equals(null)){
                    System.out.println("File is rewriting.");
                    fileWriter("Assets/MapPixelColors.txt", bImage ,pos, color);
                }
                else {
                    line = colorReader.readLine();
                    if (line.equals(screenResolution)){
                        while ((line = colorReader.readLine()) != null){
                            String[] values = line.split(",");
                            int posX = Integer.parseInt(values[0]);
                            int posY = Integer.parseInt(values[1]);
                            int red = Integer.parseInt(values[2]);
                            int green = Integer.parseInt(values[3]);
                            int blue = Integer.parseInt(values[4]);
                            Vec2int vec = new Vec2int(posX, posY);
                            RGB rgbs = new RGB(red, green, blue);
                            allPixelColors.put(vec.toString(), rgbs);
                        }
                    } else {
                        System.out.println("Resolution not match. Recreating a file.");
                        mapPixelColors.delete();
                        mapPixelColors.createNewFile();
                        fileWriter("Assets/MapPixelColors.txt", bImage ,pos, color);
                    }

                }
                fileStream.close();
                inputStream.close();
                colorReader.close();
                return;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void fileWriter(String fileName, BufferedImage bImage, Vec2int pos, RGB color) throws IOException {
        FileWriter colorWriter = new FileWriter(fileName);
        colorWriter.write(screenResolution);
        colorWriter.write("\n");
        for(int y=0; y < bImage.getHeight(); y++) {
            for (int x = 0; x < bImage.getWidth(); x++) {
                pos = new Vec2int(x,y);
                int pixel = bImage.getRGB(pos.x, pos.y);
                color = new RGB();

                color.r = (pixel >> 16) & 0xff;
                color.g = (pixel >> 8) & 0xff;
                color.b = (pixel) & 0xff;

                allPixelColors.put(pos.toString(), color);
                String textToWrite = String.format("%d,%d,%d,%d,%d\n", pos.x, pos.y, color.r, color.g, color.b);
                colorWriter.write(textToWrite);
            }
        }
        colorWriter.close();
    }

    public void setSelectedEntity(Entity e){
        selectedEntity = e;
        app.editorPanel.selectedEntityChanged(selectedEntity);
        repaint();
    }

    public Queue<Entity> getHoveredEntities(){
        return hoveredEntities;
    }

    private boolean isActionPanelUsingMouseEvent = false;
    public void setActionPanelUsingMouseEvent(boolean isUsing){
        isActionPanelUsingMouseEvent = isUsing;
    }

    public void handleEntityDrag(Entity e){
        if(e == null)
            return;
        if(allPixelColors.get(pixPos.toString()) != null && e.CanMove(allPixelColors.get(pixPos.toString()), e.getType())){
            e.setPos(pixPos);
            if (e.getType() == Entity.Type.HQ) app.editorPanel.updateHQPanelData(e);
            else app.editorPanel.updatePanelData(e);
            repaint();
        }
    }

    public Vec2int getPixPos(){
        return pixPos;
    }

    private Cursor createPointSelectionCurser(Color outerColor, Color inerColor){
        int size = 16;
        BufferedImage curserImage = new BufferedImage(24,24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = curserImage.createGraphics();
        g.setColor(outerColor);
        g.drawRect(0,0,size,size);
        g.setColor(inerColor);
        g.fillRect(6, 6, 5, 5);

        g.dispose();

        Cursor pointCursor = Toolkit.getDefaultToolkit().createCustomCursor
                (curserImage, new Point(size/2,size/2), "custom curser");
        return pointCursor;
    }

    public void initializeTheMap(){
        turkeyMap = new ImageIcon(new ImageIcon(getClass().getResource("/Assets/Turkey_map_painted.png")).getImage().
                getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT));
        setMapResolution(new Vec2int(getWidth(), getHeight()));
        Image mapImg = turkeyMap.getImage();
        BufferedImage bImage = new BufferedImage(mapImg.getWidth(null), mapImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bImage.createGraphics();
        g.drawImage(mapImg, 0, 0, null);
        g.dispose();
        JLabel label = new JLabel();
        label.setIcon(turkeyMap);
//        label.setBorder(BorderFactory.createLineBorder(Color.GREEN));
//        setBorder(BorderFactory.createLineBorder(Color.RED));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(label);
        world.map.SetBufferedImage(bImage);
        world.map.setWorldBounds(getWidth(), getHeight());

        Vec2int pos = new Vec2int();
        RGB color = new RGB();
        revalidate();
        repaint();
        locateAllPixels(bImage, pos, color);
    }

    public boolean isCaptureMode(){
        return mode == Mode.MOVE_POS_CAPTURE;
    }

    public void setCaptureMode(boolean isModeOn){
        if (isModeOn){
            mode = Mode.MOVE_POS_CAPTURE;
        } else mode = Mode.NORMAL;
    }

    public int getMapWidth(){
        return this.getWidth();
    }

    public int getMapHeight(){
        return this.getHeight();
    }

    public Vec2int getMapResolution() {
        return mapResolution;
    }

    public void setMapResolution(Vec2int mapResolution) {
        this.mapResolution = mapResolution;
    }
}
