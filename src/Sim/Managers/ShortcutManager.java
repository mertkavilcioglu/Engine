package Sim.Managers;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.Radar;
import Sim.World;
import UI.MapView;
import Vec.Vec2int;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ShortcutManager {

    private final VCSApp app;
    private final MapView mapView;
    private final World world;
    private boolean ctrlOn = false;

    //TODO: ctrl z yapınca component değişikliği geri alma bozukdu onu düzelt
    public ShortcutManager(VCSApp app){
        this.app = app;
        mapView = app.mapView;
        world = app.world;

        app.getWindow().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                ctrlActivate(e); // CTRL
                entityDelete(e); // DEL & BACKSPACE
                entityCopy(e); // CTRL + C
                entityPaste(e); // CTRL + V
                revertChanges(e); // CTRL + Z
                save(e); // CTRL + S
            }

            @Override
            public void keyReleased(KeyEvent e) {
                ctrlDeactivate(e);
            }
        });
    }

    private void entityDelete(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            if(mapView.getSelectedEntity() != null){
                Entity ent = mapView.getSelectedEntity();

                addChange(ent);

                app.removeEntityInstantaneously(mapView.getSelectedEntity());
                mapView.repaint();
            }
        }
    }

    private void ctrlActivate(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_CONTROL){
            ctrlOn = true;
        }
    }

    private void entityCopy(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_C && ctrlOn){
            if(mapView.getSelectedEntity() != null){
                world.setCopiedEntity(mapView.getSelectedEntity());
            }
        }
    }

    private void entityPaste(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_V && ctrlOn){
            if(world.getCopiedEntity() != null &&
                    world.getCopiedEntity().CanMove(mapView.allPixelColors.get(mapView.getPixPos().toString()),
                            world.getCopiedEntity().getType())){
                Entity ent = world.getCopiedEntity();
                String newName = String.format("%s - Copy", ent.getName());
                Vec2int newPos = mapView.getPixPos();

                Entity clone = app.createEntity(newName, ent.getSide(), newPos, ent.getSpeed(), ent.getType());

                for (Component c : ent.getComponents()){
                    clone.addComponents(c);
                }
                app.editorPanel.updatePanelData(clone);
                app.editorPanel.updateSelectedEntity();
                app.hierarchyPanel.entityChanged();
            }
        }
    }

    private void revertChanges(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_Z && ctrlOn){
            world.revert();
        }
    }

    private void ctrlDeactivate(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_CONTROL){
            ctrlOn = false;
        }
    }

    public void addChange(Entity ent){
        world.changes.push(new Entity(world, ent.getName(), ent.getSide(), ent.getPos(),
                    ent.getSpeed(), ent.getType(), ent.getComponents(), true));
        world.changedEntities.push(ent);
    }

    public void save(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_S && ctrlOn){
            app.loadSavePanel.save();
        }
    }

}
