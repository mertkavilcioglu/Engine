package Sim;

import App.VCSApp;
import Var.RGB;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PixelColor {
    public final VCSApp app;

    public PixelColor(VCSApp app) {
        this.app = app;
    }

    //Entity ent = new Entity(this);
    public RGB PixelColorFind(int x, int y) {
        RGB color = new RGB();

        int pixel = app.world.map.image.getRGB(x, y);
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        color.r = red;
        color.g = green;
        color.b = blue;
        return color;
    }

    public void update(int deltaTime){
        for(Entity e : app.world.entities){
            e.setColor(PixelColorFind(e.getPos().x-10, e.getPos().y-10));
            System.out.println(e.getColor());
        }
    }

    public boolean CanMove(ArrayList<Integer> rgb) {
        if(rgb.get(0) == 93 && rgb.get(1) == 94 && rgb.get(2) == 97){
            return true;

        }
        return false;
    }
}

