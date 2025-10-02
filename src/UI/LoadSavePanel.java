package UI;

import App.VCSApp;
import Sim.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadSavePanel extends VCSPanel{
    JButton saveAsButton;
    JButton loadButton;
    JButton saveButton;
    File loadedFilePath = null;
    File fileToSave = null;

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
                    JFileChooser file_upload = new JFileChooser();
                    file_upload.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter restrict = new FileNameExtensionFilter(".txt files", "txt");
                    file_upload.addChoosableFileFilter(restrict);
                    int res = file_upload.showOpenDialog(null);
                    if (res == JFileChooser.APPROVE_OPTION) {
                        loadedFilePath = new File(file_upload.getSelectedFile().getAbsolutePath());
                    }
                    if (!app.world.entities.isEmpty()){
                        for (Entity entity : app.world.entities){
                            app.removeEntity(entity);
                        }
                        app.world.entities.removeAll(app.world.entitiesToRemove);
                    }
                    GetInput input = new GetInput();
                    input.readInput(app.world, String.valueOf(loadedFilePath));
                    for (Entity entity : app.world.entities) {
                        app.hierarchyPanel.entityAdded(entity);
                        app.actionPanel.createNewTargetButton(entity);
                    }
                    app.mapView.repaint();
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
            if (loadedFilePath != null){
                app.saveSenario(loadedFilePath);
            } else if (fileToSave != null){
                app.saveSenario(fileToSave);
            } else saveAs();
        });
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
}
