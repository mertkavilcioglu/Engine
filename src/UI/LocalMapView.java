package UI;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.LocalWorld;
import Sim.TDL.TDLTransmitterComp;
import Sim.World;
import Var.RGB;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class LocalMapView extends VCSPanel {
    private LocalWorld localWorld;
    private double minimizingScale = 0.4;

    private ImageIcon turkeyMap = new ImageIcon(new ImageIcon("src/Assets/Turkey_map_painted.png").getImage());
    private final Image friendlyHQ = new ImageIcon("src/Assets/Symbols/nato_hq2j.jpg").getImage();
    private final Image friendlyAir = new ImageIcon("src/Assets/Symbols/nato_friendly_air.png").getImage();
    private final Image friendlyLand = new ImageIcon("src/Assets/Symbols/nato_friendly_land.png").getImage();
    private final Image friendlySea = new ImageIcon("src/Assets/Symbols/nato_friendly_sea.png").getImage();
    private final Image enemyAir = new ImageIcon("src/Assets/Symbols/nato_enemy_air.png").getImage();
    private final Image enemyLand = new ImageIcon("src/Assets/Symbols/nato_enemy_land.png").getImage();
    private final Image enemySea = new ImageIcon("src/Assets/Symbols/nato_enemy_sea.png").getImage();
    private int targetWidth = 19;
    private final Font timesNewRoman = new Font("Times New Roman", Font.PLAIN, 8 );

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private String screenResolution = String.format("%dx%d", gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());

    public LocalMapView(VCSApp app, Entity entity) {
        super(app);
        this.localWorld = entity.getLocalWorld();
        targetWidth = (int) Math.round(targetWidth*minimizingScale*1.8);
        setBackground(UIColorManager.DARK_MAP_BG_BLUE_COLOR);
        setBorder(BorderFactory.createMatteBorder(0,1,0,1,Color.BLACK));
    }

    public void update(){
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //System.out.println("MapView::paint - THREAD : " + Thread.currentThread().getName());

        for (int i = 0; i < localWorld.getEntities().size(); i++) {
            if(!app.world.entitiesToRemove.contains(localWorld.getEntities().get(i))){
                Entity e = localWorld.getEntities().get(i);
                if(!e.isActive())
                    continue;
                Vec2int pos = e.getPos();
                String name = e.getName();
                Vec2int scaledPos = new Vec2int((int) Math.round(pos.x*minimizingScale), (int) Math.round(pos.y * minimizingScale));

                FontMetrics fontMetric = g.getFontMetrics();
                int textLength = fontMetric.stringWidth(name);
                int textX = scaledPos.x - (textLength/2);
                int textY = scaledPos.y - 10;

                if(e.getSide() == Entity.Side.ALLY)
                    g.setColor(Color.blue);
                else if (e.getSide() == Entity.Side.ENEMY)
                    g.setColor(Color.red);

                if(e.getSide() == Entity.Side.ALLY){
                    if (e.getType() == Entity.Type.HQ)
                        drawNormalizedImageByWidth(g, friendlyHQ, scaledPos, targetWidth+2);
                    if(e.getType() == Entity.Type.AIR)
                        drawNormalizedImageByWidth(g, friendlyAir, scaledPos, targetWidth);
                    else if(e.getType() == Entity.Type.GROUND)
                        drawNormalizedImageByWidth(g, friendlyLand, scaledPos, targetWidth+2);
                    else if(e.getType() == Entity.Type.SURFACE)
                        drawNormalizedImageByWidth(g, friendlySea, scaledPos, targetWidth);

                }

                else if(e.getSide() == Entity.Side.ENEMY){
                    if(e.getType() == Entity.Type.AIR)
                        drawNormalizedImageByWidth(g, enemyAir, scaledPos, targetWidth);
                    else if(e.getType() == Entity.Type.GROUND)
                        drawNormalizedImageByWidth(g, enemyLand, scaledPos, targetWidth + 2);
                    else if(e.getType() == Entity.Type.SURFACE)
                        drawNormalizedImageByWidth(g, enemySea, scaledPos, targetWidth);
                }

                g.setFont(timesNewRoman);
                g.drawString(name, textX, textY);

            }
        }
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }

    public void drawNormalizedImageByWidth(Graphics g, Image img, Vec2int pos, int targetWidth){
        int targetHeight = (int) ((double) img.getHeight(null)
                / img.getWidth(null) * targetWidth);
        g.drawImage(img, pos.x - targetWidth/2, pos.y - targetHeight/2,
                targetWidth, targetHeight, this);
    }

    public void initializeLocalMap(Vec2int resolution){
        turkeyMap = new ImageIcon(new ImageIcon("src/Assets/Turkey_map_painted.png").getImage().
                getScaledInstance( (int) Math.round(resolution.x * minimizingScale), (int) Math.round(resolution.y * minimizingScale), Image.SCALE_DEFAULT));
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
        revalidate();
        repaint();
    }


}
