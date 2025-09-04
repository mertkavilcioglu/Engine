package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.GetInput;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImportPanel extends VCSPanel{

    public ImportPanel(VCSApp app) {
        super(app);
        this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        //importpanel.setBounds(0,0,10,50);
        JButton exportb = new JButton("Export");
        exportb.setSize(10,20);
        JButton importb = new JButton("Import");
        importb.setSize(10,20);
        this.add(exportb);
        this.add(importb);
        //importpanel.setBorder(BorderFactory.createEmptyBorder(0,0,50,50));
        importb.addActionListener(e -> {
            if(e.getSource() == importb) {
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

        });
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
