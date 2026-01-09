package UI;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;

public class MapPixelPosPanel extends VCSPanel{
    Vec2int pixelPos;
    JLabel labelX;
    JLabel labelY;

    public MapPixelPosPanel(VCSApp app) {
        super(app);
        setBackground(app.uiColorManager.TOP_BAR_COLOR);
        //setBorder(BorderFactory.createMatteBorder(0,2,0,0,Color.BLACK));
        this.setLayout(new GridLayout(1,2));
        labelX = new JLabel();
        labelY = new JLabel();
        labelX.setForeground(Color.WHITE);
        labelY.setForeground(Color.WHITE);

        if (pixelPos == null) {
            labelX.setText(" x:    ");
            labelY.setText(" y:    ");
        }
        this.add(labelX);
        this.add(labelY);
        this.setPreferredSize(new Dimension(100,20));
    }

    public void showPixelPosOfCursor(Vec2int p){
        this.pixelPos = p;
        if (pixelPos == null){
            labelX.setText("x: ");
            labelY.setText("y: ");
        }else {
            labelX.setText(" x: " + pixelPos.x);
            labelY.setText(" y: " + pixelPos.y);
        }

    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
