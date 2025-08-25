package Sim;

import java.awt.image.BufferedImage;

public class WorldMap {
    public int maxX = 950;
    public int maxY = 436;
    public int midY = maxY/2;
    public BufferedImage image;
    public void SetBufferedImage(BufferedImage image){
        this.image = image;
    }

}
