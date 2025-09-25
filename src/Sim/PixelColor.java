package Sim;

import App.VCSApp;
import Var.RGB;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.Objects;

public class PixelColor {
    public final VCSApp app;

    public PixelColor(VCSApp app) {
        this.app = app;
    }

    //Entity ent = new Entity(this);
    public RGB PixelColorFind(int x, int y) {
        RGB color = new RGB();

        if (x >= app.world.map.image.getWidth()
        || y >= app.world.map.image.getHeight()) {
            return color;
        }
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
            e.setColor(PixelColorFind(e.getPos().x, e.getPos().y));
            //System.out.println(e.getColor());
        }
    }

    
    public boolean isLocationValidForType(String type, Vec2int pos) {
        RGB currentColor = PixelColorFind(pos.x, pos.y);
        if(Objects.equals(type, "Tank") && !(currentColor.r==25 && currentColor.g == 25 && currentColor.b == 40)){
            return true;
        }
        if(Objects.equals(type, "Ship") && (currentColor.r==25 && currentColor.g == 25 && currentColor.b == 40)){
            return true;
        }
        if(Objects.equals(type, "Plane")){
            return true;
        }
        return false;
    }
}

