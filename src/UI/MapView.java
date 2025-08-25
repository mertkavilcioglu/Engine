package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.World;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class MapView extends VCSPanel {
    private World world;
    //ImageIcon imageIcon;
    public MapView(VCSApp app) {
        super(app);
        this.world = app.world;
        setBackground(Color.WHITE);

        //GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsDevice defaultScreen = env.getDefaultScreenDevice();

        ImageIcon imageIcon = new ImageIcon(new ImageIcon("C://Users//stj.hbiseri//Downloads//map3.png").getImage().getScaledInstance(app.world.map.maxX, app.world.map.maxY,Image.SCALE_DEFAULT));
        Image img = imageIcon.getImage();
        BufferedImage bImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bImage.createGraphics();
        g.drawImage(img,0,0,null);
        g.dispose();
        JLabel label = new JLabel();
        label.setIcon(imageIcon);

        add(label);
/*
        for(int y=0; y<bImage.getHeight();y++){
            for(int x=0; x<bImage.getWidth();x++){
                int pixel = bImage.getRGB(x,y);

                int red = (pixel>>16) & 0xff;
                int green = (pixel>>8) & 0xff;
                int blue = (pixel) & 0xff;
                System.out.printf("Pixel at (%d %d) RGB color ( %d %d %d)",x,y,red,green,blue);
                System.out.println();
            }

        }

 */
        world.map.SetBufferedImage(bImage);

        /*
        int x = 400;
        int y = 400;
        int clr = bImage.getRGB(x, y);
        int red =   (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue =   clr & 0x000000ff;
        System.out.println("Red Color value = " + red);
        System.out.println("Green Color value = " + green);
        System.out.println("Blue Color value = " + blue);

         */
    }


    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }
    /*
    public boolean canMove(int x, int y, BufferedImage img){
        int pixel = img.getRGB(x,y);
        int red = (pixel>>16) & 0xff;
        int green = (pixel>>8) & 0xff;
        int blue = (pixel) & 0xff;
        if(red == 0 && green == 0 && blue == 0){
            return false;
        }
        return true;
    }*/
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //g.drawImage();

        //g.setColor(Color.DARK_GRAY);
        //g.fillRect(0,0, app.world.map.maxX, app.world.map.maxY);
        //g.setColor(Color.CYAN);
        //g.fillRect(0,0, app.world.map.maxX, app.world.map.midY);
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

            //drawEntity(e);
            //g.setColor(Color.BLACK);

            g.drawOval(pos.x-10, pos.y-10, 20, 20);

            g.setFont(new Font("Times New Roman", Font.PLAIN, 10 ));
            g.drawString(name, textX, textY);
            //g.fillOval(pos.x-10, pos.y-10, 20, 20);

        }
    }


}
