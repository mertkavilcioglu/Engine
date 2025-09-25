package UI;

import App.VCSApp;
import Sim.*;
import Sim.Component;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImportPanel extends VCSPanel{
    JButton exportButton;
    JButton importButton;
    JButton saveButton;

    public ImportPanel(VCSApp app) {
        super(app);
        this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        //importpanel.setBounds(0,0,10,50);
        exportButton = new JButton("Export");
        exportButton.setSize(10,20);
        exportButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        importButton = new JButton("Import");
        importButton.setSize(10,20);
        importButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        saveButton = new JButton("Save");
        saveButton.setSize(10,20);
        saveButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        this.add(exportButton);
        this.add(importButton);
        this.add(saveButton);
        this.setBackground(app.uiColorManager.TOP_BAR_COLOR);
        setBackground(app.uiColorManager.TOP_BAR_COLOR);
        //importpanel.setBorder(BorderFactory.createEmptyBorder(0,0,50,50));

        importButton.addActionListener(e -> {
            try {
                if(e.getSource() == importButton) {
                    int prevSize = app.world.entities.size();

                    JFileChooser file_upload = new JFileChooser();
                    int res = file_upload.showOpenDialog(null);
                    File file_path = null;
                    if (res == JFileChooser.APPROVE_OPTION) {
                        file_path = new File(file_upload.getSelectedFile().getAbsolutePath());

                    }
                    GetInput input = new GetInput();
                    input.readInput(app.world, String.valueOf(file_path));
                    for (int i = prevSize; i < app.world.entities.size(); i++) {
                        Sim.Entity ent = app.world.entities.get(i);
                        app.hierarchyPanel.entityAdded(ent);
                        app.actionPanel.createNewTargetButton(ent);
                    }
                    app.mapView.repaint();
                }
            }catch (RuntimeException r){
                //System.out.println("User did not select import file");
            }

        });
        AtomicBoolean flag2 = new AtomicBoolean(true);

        exportButton.addActionListener(e -> {
            int id = 0;
            try {
                boolean flag = true;
                while (flag){

                    File myObj = new File( id + "-Simulation_Plan.txt");

                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                        flag = false;
                    } else {
                        id++;
                    }
                }

            } catch (IOException i) {
                System.out.println("An error occurred.");
                i.printStackTrace();
            }

            try {
                FileWriter myWriter = new FileWriter(id + "-Simulation_Plan.txt");
                //int size = app.world.entities.size();
                Map<Entity, Vec2int> initialPositions = app.mapView.getInitialPoints();
                for(Sim.Entity ent:app.world.entities){
                    Vec.Vec2int pos = initialPositions.get(ent);
                    String posStr;
                    if(pos!=null){
                        posStr = pos.toString().substring(1,pos.toString().length()-1);

                    }else{
                        posStr = ent.getPos().toString().substring(1,ent.getPos().toString().length()-1);
                    }
                    String speedStr = ent.getSpeed().toString().substring(1, ent.getSpeed().toString().length()-1);
                    if(flag2.get()){
                        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                        int width = gd.getDisplayMode().getWidth();
                        int height = gd.getDisplayMode().getHeight();
                        myWriter.write(width +"x" + height + "\n");
                        flag2.set(false);
                    }
                    myWriter.write(ent.getName() + "\n");
                    myWriter.write(ent.getSide() == (1) ? "Enemy":"Ally");
                    myWriter.write("\n");
                    myWriter.write(ent.getType() + "\n");
                    myWriter.write(posStr + "\n");
                    myWriter.write(speedStr + "\n");
                    myWriter.write("null" + "\n");//radar information of entity

                }
                myWriter.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        saveButton.addActionListener(e -> {
            File saveFile = app.localFile.createLocalFile("SavedSenario");
            saveFile.delete();
                try {
                    FileWriter myWriter = new FileWriter(saveFile);
                    int range = 0;
                    for (Sim.Entity ent : app.world.entities) {
                        String posStr;
                        posStr = ent.getPos().toString().substring(1, ent.getPos().toString().length() - 1);
                        for (Component c : ent.getComponents()){
                            if(c instanceof Radar){
                                if(((Radar) c).getRange() != 0){
                                    range = ((Radar) c).getRange();
                                } else range = 0;
                            }
                        }
                        String speedStr = ent.getSpeed().toString().substring(1, ent.getSpeed().toString().length() - 1);
                        myWriter.write(ent.getName() + "\n");
                        myWriter.write(ent.getSide() == (1) ? "Enemy" : "Ally");
                        myWriter.write("\n");
                        myWriter.write(ent.getType() + "\n");
                        myWriter.write(posStr + "\n");
                        myWriter.write(speedStr + "\n");
                        myWriter.write(range + "\n");

                    }
                    myWriter.close();
                }catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
        });
    }

    public void changeStateOfSaveButton(boolean isInitial){
        saveButton.setEnabled(isInitial);
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
