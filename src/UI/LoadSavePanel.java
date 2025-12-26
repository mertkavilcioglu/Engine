package UI;

import App.VCSApp;
import Sim.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadSavePanel extends VCSPanel{
    JButton saveAsButton;
    JButton loadButton;
    JButton saveButton;
    File loadedFilePath = null;
    File fileToSave = null;
    boolean isHaveHQ = false;

    public LoadSavePanel(VCSApp app) {
        super(app);
        this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));

        saveAsButton = new JButton("Save As");
        saveAsButton.setSize(10,20);
        saveAsButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        loadButton = new JButton("Load");
        loadButton.setSize(10,20);
        loadButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        saveButton = new JButton("Save");
        saveButton.setSize(10,20);
        saveButton.setBackground(app.uiColorManager.BUTTON_COLOR);

        this.add(loadButton);
        this.add(saveAsButton);
        this.add(saveButton);
        this.setBackground(app.uiColorManager.TOP_BAR_COLOR);
        setBackground(app.uiColorManager.TOP_BAR_COLOR);

        //choose an existing txt file to load as a scenario
        loadButton.addActionListener(e -> {
            try {
                if(e.getSource() == loadButton) {
                    load();
                }
            } catch (HeadlessException ex) {
                ex.printStackTrace();
            }

        });

        //save with choosing where to save
        saveAsButton.addActionListener(e -> {
            saveAs();
        });

        //save if there is an existing file for that scenario or save as
        saveButton.addActionListener(e -> {
            save();
        });
    }

    public void load(){
        JFileChooser fileUpload = new JFileChooser();
        fileUpload.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter nameRestrict = new FileNameExtensionFilter(".txt files", "txt");
        fileUpload.addChoosableFileFilter(nameRestrict);
        int res = fileUpload.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            loadedFilePath = new File(fileUpload.getSelectedFile().getAbsolutePath());
        }
        if (!app.world.entities.isEmpty()){
            ArrayList<Entity> entitiesRemoved = new ArrayList<>();
            for (Entity entity : app.world.entities){
                app.removeEntityInstantaneously(entity);
                entitiesRemoved.add(entity);
            }
            app.world.entities.removeAll(entitiesRemoved);
            entitiesRemoved.clear();
        }
        GetInput input = new GetInput();
        app.controlPanel.reset();
        app.logPanel.clearLogArea();
        input.readInput(app.world, String.valueOf(loadedFilePath));
        for (Entity entity : app.world.entities) {
            if(!entity.isActive())
                continue;
            if(entity.getType() == Entity.Type.HQ){
                isHaveHQ = true;
                app.setHQ(entity);
            }
            //app.hierarchyPanel.entityAdded(entity);
            app.actionPanel.createNewTargetButton(entity);
        }
        app.createHQ(!isHaveHQ);
        app.mapView.repaint();
    }

    public void saveAs(){
        JFrame fileFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Specify a file to save");
        fileChooser.setSelectedFile(new File("untitled.txt"));
        int userSelection = fileChooser.showSaveDialog(fileFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")){
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
            }
            app.saveSenario(fileToSave);
        }
    }

    public boolean isAnyFile(){
        if (loadedFilePath != null || fileToSave != null){
            return true;
        } else return false;
    }

    public File getLoadedFilePath(){
        return loadedFilePath;
    }

    public File getSavedFilePath(){
        return fileToSave;
    }

    public void changeStateOfSaveButton(boolean isInitial){
        saveButton.setEnabled(isInitial);
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }

    public void save(){
        if (loadedFilePath != null){
            app.saveSenario(loadedFilePath);
        } else if (fileToSave != null){
            app.saveSenario(fileToSave);
        } else saveAs();
    }
}
