package UI;

import App.Main;
import App.VCSApp;
import Sim.Entity;
import Sim.GetInput;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AppWindow extends JFrame {
    VCSApp app;
    private Color appBgColor;
    private ImageIcon saveIcon = new ImageIcon(getClass().getResource("/Assets/Icons/save_icon.png"));
    Image img = saveIcon.getImage();
    Image newimg = img.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
    ImageIcon newIcon = new ImageIcon(newimg);

    public AppWindow(VCSApp app) {
        super("VCS: Virtual Combat System");
        this.app = app;

        appBgColor = app.uiColorManager.TOP_BAR_COLOR;
        //setSize(800, 600);
        setResizable(true);
        //setMinimumSize(new Dimension(960,600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        getContentPane().setBackground(appBgColor);
        setLayout(new BorderLayout(0, 0));
        setBackground(appBgColor);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                Path jarDir;
                try {
                    jarDir = Paths.get(
                            Main.class.getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .toURI()
                    ).getParent();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                File forCheckIsSaved = jarDir.resolve("Assets/CheckFile").toFile();
                app.saveSenario(forCheckIsSaved);
                if (app.loadSavePanel.isAnyFile()){
                    if (app.loadSavePanel.getLoadedFilePath() != null){
                        if (!compareIfEqual(app.loadSavePanel.getLoadedFilePath(), forCheckIsSaved)){
                            savePopUp(app.loadSavePanel.getLoadedFilePath());
                        } else setDefaultCloseOperation(EXIT_ON_CLOSE);
                    } else if (app.loadSavePanel.getSavedFilePath() != null) {
                        if (!compareIfEqual(app.loadSavePanel.getSavedFilePath(), forCheckIsSaved)){
                            savePopUp(app.loadSavePanel.getSavedFilePath());
                        } else setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                } else {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(forCheckIsSaved));
                        if (br.readLine() == null) {
                            forCheckIsSaved.delete();
                            setDefaultCloseOperation(EXIT_ON_CLOSE);
                        } else if (app.world.entities.size() == 1){
                            Entity entity = app.world.entities.getFirst();
                            Vec2int intPos = new Vec2int(500, 250);
                            if ((entity.getType() == Entity.Type.HQ) && (entity.getPos().x == 500 && entity.getPos().y == 250)){
                                forCheckIsSaved.delete();
                                setDefaultCloseOperation(EXIT_ON_CLOSE);
                            } else savePopUp(null);
                        } else savePopUp(null);
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                }


            }
        });
    }

    private boolean compareIfEqual(File createdFile, File checkFile){
        try (BufferedReader firstFile = Files.newBufferedReader(createdFile.toPath());
             BufferedReader secFile = Files.newBufferedReader(checkFile.toPath())) {
            String firstFileLine = "";
            String secFileLine = "";
            while ((firstFileLine = firstFile.readLine()) != null) {
                secFileLine = secFile.readLine();
                if (secFileLine == null || !firstFileLine.equals(secFileLine)) {
                    checkFile.delete();
                    return false;
                }
            }
            if (secFile.readLine() != null) {
                checkFile.delete();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkFile.delete();
        return true;
    }

    private void savePopUp(File file){
        String[] options = { "Save","Don't save", "Cancel" };
        JFrame popUpFrame = new JFrame();
        popUpFrame.setAlwaysOnTop(true);
        int choice = JOptionPane.showOptionDialog(popUpFrame,
                "Do you want to save?", "Select an option",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,newIcon,
                options, options[0]);
        if (choice == 0) {
            if (file == null){
                app.loadSavePanel.saveAs();
            } else {
                app.saveSenario(file);
            }
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        else if (choice == 1) {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }
}
