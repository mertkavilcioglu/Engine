package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class NorthPanel extends VCSpanel{
    JPanel playpanel;
    JPanel importpanel;

    public NorthPanel(VCSapp app) {
        super(app);
        this.setLayout(new BorderLayout());

        playpanel = new JPanel(new GridBagLayout());
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        //playpanel.setSize(20,90);
        //playpanel.setBounds(20,100,20,90);
        JButton play = new JButton("p");
        play.setSize(10,20);
        JButton pause = new JButton("s");
        pause.setSize(10,20);
        JButton reset = new JButton("r");
        reset.setSize(10,20);
        buttonpanel.add(play);
        buttonpanel.add(pause);
        buttonpanel.add(reset);
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        playpanel.add(buttonpanel);

        importpanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        //importpanel.setBounds(0,0,10,50);
        JButton exportb = new JButton("Export");
        exportb.setSize(10,20);
        JButton importb = new JButton("Import");
        importb.setSize(10,20);
        importpanel.add(exportb);
        importpanel.add(importb);
        //importpanel.setBorder(BorderFactory.createEmptyBorder(0,0,50,50));

        this.add(importpanel, BorderLayout.WEST);
        this.add(playpanel, BorderLayout.CENTER);

    }



    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
