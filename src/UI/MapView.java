package UI;

import App.VCSApp;
import Sim.Entity;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.io.*;
import java.util.*;


public class MapView extends VCSPanel {
    private World world;
    public Map<String, RGB> allPixelColors;
    Image friendlyAir = new ImageIcon("src/Assets/Symbols/nato_friendly_air.png").getImage();
    Image friendlyLand = new ImageIcon("src/Assets/Symbols/nato_friendly_land.png").getImage();
    Image friendlySea = new ImageIcon("src/Assets/Symbols/nato_friendly_sea.png").getImage();
    Image enemyAir = new ImageIcon("src/Assets/Symbols/nato_enemy_air.png").getImage();
    Image enemyLand = new ImageIcon("src/Assets/Symbols/nato_enemy_land.png").getImage();
    Image enemySea = new ImageIcon("src/Assets/Symbols/nato_enemy_sea.png").getImage();
    int targetWidth = 19;
    Font timesNewRoman = new Font("Times New Roman", Font.PLAIN, 10 );
    boolean isMouseEntered = false;

    private Vec2int pixPos = new Vec2int();
    private Queue<Entity> hoveredEntities = new LinkedList<>();

    private Entity selectedEntity;

    public MapView(VCSApp app) {
        super(app);
        this.world = app.world;
        setBackground(Color.WHITE);

        ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/Assets/map3.png").getImage().getScaledInstance(app.world.map.maxX, app.world.map.maxY, Image.SCALE_DEFAULT));
        Image img = imageIcon.getImage();
        BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        JLabel label = new JLabel();
        label.setIcon(imageIcon);

        add(label);

        world.map.SetBufferedImage(bImage);

        Vec2int pos = new Vec2int();
        RGB color = new RGB();
        allPixelColors = new HashMap<>();

        locateAllPixels(bImage, pos, color);
/*
        Example usage of getting desired pixel color:

        Vec2int desired = new Vec2int();
        desired.x = 948;
        desired.y = 435;
        System.out.println( allPixelColors.get(desired.toString()));

 */     JMenuItem ally = new JMenuItem("Ally");

        JPopupMenu rightClickPopUpMenu = new JPopupMenu();
        JMenuItem createEntity = new JMenuItem("Create Entity");
        createEntity.addActionListener(e -> {
            //TODO: CREATE ENTİTY /////////////////////////////////////////
            createEntity.add(ally);
        });
        rightClickPopUpMenu.add(createEntity);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isMouseEntered = true;
            }
        });

        //when mouse moved around the map
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (isMouseEntered){
                    System.out.println("MapView::mouseMoved - THREAD : " + Thread.currentThread().getName());
                    pixPos = new Vec2int(e.getX(), e.getY());
                    app.mapPixelPosPanel.showPixelPosOfCursor(pixPos);
                    repaint();
                }
            }
        });
        //when mouse click an entity
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isMouseEntered){
                    System.out.println("MapView::mousePressed - THREAD : " + Thread.currentThread().getName());
                    if (!hoveredEntities.isEmpty()) {
                        Entity topEntity = hoveredEntities.poll();
                        app.actionPanel.selectedUnit(topEntity);
                        selectedEntity = topEntity;
                        hoveredEntities.add(topEntity);

                        app.hierarchyPanel.selectNode(topEntity);

                    } else {
                        app.actionPanel.disablePanel();
                        app.hierarchyPanel.clearSelectionInTree();
                        selectedEntity = null;
                    }

                    if(e.getButton() == MouseEvent.BUTTON3){
                        rightClickPopUpMenu.show(e.getComponent(),e.getX(),e.getY());
                    }
                    repaint();
                }
            }
        });
        //when mouse exit the map
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (isMouseEntered){
                    System.out.println("MapView::mouseExited - THREAD : " + Thread.currentThread().getName());
                    app.mapPixelPosPanel.showPixelPosOfCursor(null);
                    isMouseEntered = false;
                }
            }
        });

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
                if(!hoveredEntities.contains(ent))
                    hoveredEntities.add(ent);
            }
            else
                hoveredEntities.remove(ent);
        }

        System.out.println("MapView::paint - THREAD : " + Thread.currentThread().getName());

        for (int i = 0; i < world.entities.size(); i++) {

            Entity e = world.entities.get(i);
            boolean isHovered = hoveredEntities.contains(e);
            Vec2int pos = e.getPos();
            String name = e.getName();

//            int half = targetWidth / 2;
//            g.setColor(Color.GREEN);
//            g.fillRect(pos.x - half, pos.y - half, targetWidth, targetWidth);

            FontMetrics fontMetric = g.getFontMetrics();
            int textLength = fontMetric.stringWidth(name);
            int textX = pos.x - 10 + (20 - textLength) / 2;
            int textY = pos.y - 10;

            if(e.getSide() == 0)
                g.setColor(Color.blue);
            else if (e.getSide() == 1)
                g.setColor(Color.red);

            //g.drawOval(pos.x-10, pos.y-10, 20, 20);
            if(e.getSide() == 0){
                if(e.getType().equals("Plane"))
                    drawNormalizedImageByWidth(g, friendlyAir, pos, targetWidth);
                else if(e.getType().equals("Tank"))
                    drawNormalizedImageByWidth(g, friendlyLand, pos, targetWidth+2);
                else if(e.getType().equals("Ship"))
                    drawNormalizedImageByWidth(g, friendlySea, pos, targetWidth);

            }

            else if(e.getSide() == 1){
                if(e.getType().equals("Plane"))
                    drawNormalizedImageByWidth(g, enemyAir, pos, targetWidth);
                else if(e.getType().equals("Tank"))
                    drawNormalizedImageByWidth(g, enemyLand, pos, targetWidth + 2);
                else if(e.getType().equals("Ship"))
                    drawNormalizedImageByWidth(g, enemySea, pos, targetWidth);
            }
            
            g.setFont(timesNewRoman);
            g.drawString(name, textX, textY);

            int targetWidthForHover = targetWidth + targetWidth*11/16;
            int half = targetWidthForHover / 2;


            if (isHovered && (selectedEntity == null || selectedEntity != e)) {
                //g.drawString("HOVERED", pos.x, pos.y);
                g.drawOval(pos.x - half, pos.y - half, targetWidthForHover, targetWidthForHover);
            }
            else if(selectedEntity == e){
                g.drawRect(pos.x - half, pos.y - half, targetWidthForHover, targetWidthForHover);
            }



            //g.setColor(Color.GREEN);
            //g.drawRect(pos.x, pos.y, 1,1);
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
            File mapPixelColors = new File("src/Assets/MapPixelColors.txt");
            if (mapPixelColors.createNewFile()) {
                System.out.println("File created: " + mapPixelColors.getName());
                FileWriter colorWriter = new FileWriter("src/Assets/MapPixelColors.txt");
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
            } else {
                System.out.println("File already exists.");
                FileInputStream fileStream = new FileInputStream("src/Assets/MapPixelColors.txt");
                DataInputStream inputStream = new DataInputStream(fileStream);
                BufferedReader colorReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
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

    public void setSelectedEntity(Entity e){
        selectedEntity = e;
        repaint();
    }
}
