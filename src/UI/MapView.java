package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.World;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;


public class MapView extends VCSPanel {
    private World world;
    //private MapPixelPosPanel mapPixelPosPanel;
    public MapView(VCSApp app) {
        super(app);
        this.world = app.world;
        setBackground(Color.WHITE);

        ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/Assets/map3.png").getImage().getScaledInstance(app.world.map.maxX, app.world.map.maxY,Image.SCALE_DEFAULT));
        Image img = imageIcon.getImage();
        BufferedImage bImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bImage.createGraphics();
        g.drawImage(img,0,0,null);
        g.dispose();
        JLabel label = new JLabel();
        label.setIcon(imageIcon);

        add(label);

        world.map.SetBufferedImage(bImage);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Vec2int pixPos = new Vec2int(e.getX(), e.getY());
                app.mapPixelPosPanel.showPixel(pixPos);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                app.mapPixelPosPanel.nullPixel();
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

            g.drawOval(pos.x-10, pos.y-10, 20, 20);

            g.setFont(new Font("Times New Roman", Font.PLAIN, 10 ));
            g.drawString(name, textX, textY);

            g.setColor(Color.GREEN);
            g.drawRect(pos.x, pos.y, 1,1);
        }
    }
}
