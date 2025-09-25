package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.GetInput;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class PlayPausePanel extends VCSPanel{
    private Color initialButColor;
    private Color panelBgColor;
    private ImageIcon playIcon = new ImageIcon("src/Assets/Icons/play_icon.png");
    private ImageIcon pauseIcon = new ImageIcon("src/Assets/Icons/pause_icon.png");
    private ImageIcon stopIcon = new ImageIcon("src/Assets/Icons/stop_icon.png");
    private boolean isFirstPlay = true;

    public PlayPausePanel(VCSApp app) {
        super(app);
        panelBgColor = app.uiColorManager.TOP_BAR_COLOR;
        this.setLayout(new GridBagLayout());
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        buttonpanel.setBackground(app.uiColorManager.TOP_BAR_COLOR);
        //playpanel.setSize(20,90);
        //playpanel.setBounds(20,100,20,90);
        JButton play = new JButton();
        ImageIcon rescaledPlay = new ImageIcon(
                playIcon.getImage().getScaledInstance(12,12,Image.SCALE_SMOOTH));
        play.setIcon(rescaledPlay);
        play.setFocusable(false);
        play.setSize(10,20);
        play.setBackground(app.uiColorManager.BUTTON_COLOR);
        JButton pause = new JButton();
        ImageIcon rescaledPause = new ImageIcon(
                pauseIcon.getImage().getScaledInstance(12,12,Image.SCALE_SMOOTH));
        pause.setIcon(rescaledPause);
        pause.setSize(10,20);
        pause.setFocusable(false);
        pause.setBackground(app.uiColorManager.BUTTON_COLOR);
        JButton reset = new JButton();
        ImageIcon rescaledStop = new ImageIcon(
                stopIcon.getImage().getScaledInstance(12,12,Image.SCALE_SMOOTH));
        reset.setIcon(rescaledStop);
        reset.setSize(10,20);
        reset.setFocusable(false);
        reset.setBackground(app.uiColorManager.BUTTON_COLOR);
        buttonpanel.add(play);
        buttonpanel.add(pause);
        buttonpanel.add(reset);
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        this.add(buttonpanel);
        initialButColor = play.getBackground();
        reset.setEnabled(false);
        pause.setEnabled(false);
        setBackground(panelBgColor);
        //setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        play.addActionListener(e -> {
            app.importPanel.changeStateOfSaveButton(false);
            if (isFirstPlay){
                saveInitial();
            }
            isFirstPlay = false;
            app.logPanel.clearLogArea();
            if(app.mapView.getInitialPoints().isEmpty()){
                app.mapView.saveInitialPoints();
            }
            if(app.mapView.getInitialSpeeds().isEmpty()){
                app.mapView.saveSpeed();
            }
            app.simTimer.start();
            play.setBackground(Color.GREEN);
            pause.setBackground(initialButColor);
            reset.setBackground(initialButColor);
            app.actionPanel.setIfPaused(false);
            play.setEnabled(false);
            pause.setEnabled(true);
            reset.setEnabled(true);
        });

        pause.addActionListener(e -> {
            app.importPanel.changeStateOfSaveButton(false);
            app.simTimer.stop();
            pause.setBackground(Color.YELLOW);
            play.setBackground(initialButColor);
            reset.setBackground(initialButColor);
            app.actionPanel.setIfPaused(true);
            pause.setEnabled(false);
            play.setEnabled(true);
            reset.setEnabled(true);
        });

        reset.addActionListener(e ->{
//            Map<Entity, Vec2int> initialPositions = app.mapView.getInitialPoints();
//            for(Sim.Entity ent:app.world.entities) {
//                ent.deleteAllDetectedEntities();
//                Vec.Vec2int pos = initialPositions.get(ent);
//                if (pos != null) {
//                    ent.setPos(new Vec.Vec2int(pos.x,pos.y));
//
//                }
//            }
//            Map<Entity, Vec2int> initialSpeeds = app.mapView.getInitialSpeeds();
//            for(Sim.Entity ent:app.world.entities) {
//                Vec.Vec2int speed = initialSpeeds.get(ent);
//                if (speed != null) {
//                    ent.setSpeed(speed);
//
//                }
//            }
            for(Sim.Entity ent:app.world.entities) {
                ent.deleteAllDetectedEntities();
            }
            app.simTimer.stop();
            play.setBackground(initialButColor);
            pause.setBackground(initialButColor);

//            reset.setBackground(Color.RED);
//            new Thread(()->{
//            try {
//                Thread.sleep(125);
//                reset.setBackground(initialButColor);
//            } catch (InterruptedException ee) {
//                throw new RuntimeException(ee);
//            }
//        }).start();
            for (Entity entity : app.world.entities){
                app.removeEntity(entity);
            }
            app.world.entities.removeAll(app.world.entitiesToRemove);
            app.world.entitiesToRemove.clear();
            app.world.clearAllStack();
            restoreInitials();
            app.mapView.repaint();
            reset.setEnabled(false);
            pause.setEnabled(false);
            play.setEnabled(true);
            isFirstPlay = true;
            app.importPanel.changeStateOfSaveButton(true);
        });
    }

    private void saveInitial() {
        File init = new File("src/Assets/InitialValues");
        init.delete();
        try {
            FileWriter myWriter = new FileWriter("src/Assets/InitialValues");
            Map<Entity, Vec2int> initialPositions = app.mapView.getInitialPoints();
            for (Sim.Entity ent : app.world.entities) {
                Vec.Vec2int pos = initialPositions.get(ent);
                String posStr;
                if (pos != null) {
                    posStr = pos.toString().substring(1, pos.toString().length() - 1);

                } else {
                    posStr = ent.getPos().toString().substring(1, ent.getPos().toString().length() - 1);
                }
                String speedStr = ent.getSpeed().toString().substring(1, ent.getSpeed().toString().length() - 1);
                myWriter.write(ent.getName() + "\n");
                myWriter.write(ent.getSide() == (1) ? "Enemy" : "Ally");
                myWriter.write("\n");
                myWriter.write(ent.getType() + "\n");
                myWriter.write(posStr + "\n");
                myWriter.write(speedStr + "\n");
                myWriter.write("null" + "\n");//radar information of entity

            }
            myWriter.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void restoreInitials(){
        File filePath = new File("src/Assets/InitialValues");
        GetInput input = new GetInput();
        input.readInputForReset(app, String.valueOf(filePath));
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
