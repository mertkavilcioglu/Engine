package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.GetInput;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ControlPanel extends VCSPanel{
    private Color initialButColor;
    private Color panelBgColor;
    private ImageIcon playIcon = new ImageIcon("src/Assets/Icons/play_icon.png");
    private ImageIcon pauseIcon = new ImageIcon("src/Assets/Icons/pause_icon.png");
    private ImageIcon stopIcon = new ImageIcon("src/Assets/Icons/stop_icon.png");
    private boolean isFirstPlay = true;

    public ControlPanel(VCSApp app) {
        super(app);
        panelBgColor = app.uiColorManager.TOP_BAR_COLOR;
        this.setLayout(new GridBagLayout());
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        buttonpanel.setBackground(app.uiColorManager.TOP_BAR_COLOR);
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
            app.loadSavePanel.changeStateOfSaveButton(false);
            if (!app.loadSavePanel.isAnyFile()){
                if (isFirstPlay){
                    saveInitial();
                    app.logPanel.clearLogArea();
                }
            }
            isFirstPlay = false;
            //app.logPanel.clearLogArea();
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
            app.loadSavePanel.changeStateOfSaveButton(true);
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
            for(Sim.Entity ent:app.world.entities) {
                ent.deleteAllDetectedEntities();
            }
            app.simTimer.stop();
            play.setBackground(initialButColor);
            pause.setBackground(initialButColor);

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
            app.loadSavePanel.changeStateOfSaveButton(true);
        });
    }

    private void saveInitial() {
        File init = new File("src/Assets/InitialValues");
        app.saveSenario(init);
    }

    private void restoreInitials(){
        GetInput input = new GetInput();
        if (!app.loadSavePanel.isAnyFile()){
            File filePath = new File("src/Assets/InitialValues");
            input.readInputForReset(app, String.valueOf(filePath));
        } else if (app.loadSavePanel.getLoadedFilePath() != null) {
            input.readInputForReset(app, String.valueOf(app.loadSavePanel.getLoadedFilePath()));
        } else if (app.loadSavePanel.getSavedFilePath() != null) {
            input.readInputForReset(app, String.valueOf(app.loadSavePanel.getSavedFilePath()));
        }
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
