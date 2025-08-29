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
import java.util.*;


public class MapView extends VCSPanel {
    private World world;
    Map<String, RGB> allPixelColors;

    Image friendlyAir = new ImageIcon("src/Assets/Symbols/nato_friendly_air.png").getImage();
    Image friendlyLand = new ImageIcon("src/Assets/Symbols/nato_friendly_land.png").getImage();
    Image friendlySea = new ImageIcon("src/Assets/Symbols/nato_friendly_sea.png").getImage();
    Image enemyAir = new ImageIcon("src/Assets/Symbols/nato_enemy_air.png").getImage();
    Image enemyLand = new ImageIcon("src/Assets/Symbols/nato_enemy_land.png").getImage();
    Image enemySea = new ImageIcon("src/Assets/Symbols/nato_enemy_sea.png").getImage();
    int targetWidth = 19;

    private Vec2int pixPos = new Vec2int();
    private Queue<Entity> hoveredEntities = new LinkedList<>();

    public MapView(VCSApp app) {
        super(app);
        this.world = app.world;
        setBackground(Color.WHITE);

        ImageIcon mapImage = new ImageIcon(new ImageIcon("src/Assets/map3.png").getImage().getScaledInstance(app.world.map.maxX, app.world.map.maxY,Image.SCALE_DEFAULT));
        Image img = mapImage.getImage();
        BufferedImage bImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bImage.createGraphics();
        g.drawImage(img,0,0,null);
        g.dispose();
        JLabel label = new JLabel();
        label.setIcon(mapImage);

        add(label);

        world.map.SetBufferedImage(bImage);

        Vec2int pos = new Vec2int();
        RGB color = new RGB();
        allPixelColors = new HashMap<>();

       // locateAllPixels(bImage, pos, color);

/*
        Example usage of getting desired pixel color:

        Vec2int desired = new Vec2int();
        desired.x = 948;
        desired.y = 435;
        System.out.println( allPixelColors.get(desired.toString()));

 */


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                pixPos = new Vec2int(e.getX(), e.getY());
                app.mapPixelPosPanel.showPixel(pixPos);
                for(Entity ent : app.world.entities){
                    if(ent.getPos().distance(pixPos) < targetWidth/2){
                        if(!hoveredEntities.contains(ent))
                            hoveredEntities.add(ent);
                    }
                    else
                        hoveredEntities.remove(ent);
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                app.mapPixelPosPanel.nullPixel();
            }
        });
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!hoveredEntities.isEmpty()){
                    Entity topEntity = hoveredEntities.peek();
                    app.actionPanel.selectedUnit(topEntity);
                    hoveredEntities.remove(topEntity);
                    hoveredEntities.add(topEntity);

                    app.hierarchyPanel.selectNode(topEntity);

                }
                else{
                    app.actionPanel.disablePanel();
                    app.hierarchyPanel.clearSelectionInTree();
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

        for (int i = 0; i < world.entities.size(); i++) {
            Entity e = world.entities.get(i);
            Vec2int pos = e.getPos();
            String name = e.getName();

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


            g.setFont(new Font("Times New Roman", Font.PLAIN, 10 ));
            g.drawString(name, textX, textY);

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
        for(int y=0; y < bImage.getHeight(); y++) {
            for (int x = 0; x < bImage.getWidth(); x++) {
                pos = new Vec2int(x,y);
                int pixel = bImage.getRGB(pos.x, pos.y);
                color = new RGB();

                color.r = (pixel >> 16) & 0xff;
                color.g = (pixel >> 8) & 0xff;
                color.b = (pixel) & 0xff;

                allPixelColors.put(pos.toString(), color);
                System.out.println((allPixelColors.get(pos.toString()).toString()));
                System.out.printf("Pixel at (%d %d) RGB color ( %d %d %d)", pos.x, pos.y, color.r, color.g, color.b);
                System.out.println();
            }
        }
    }

}
