package Sim.Managers;

import App.VCSApp;
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

    public ShortcutManager(VCSApp app){
        this.app = app;
        mapView = app.mapView;
        world = app.world;
        app.getWindow().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleEntityDelete(e); // DEL & BACKSPACE
                handleCtrlActivate(e); // CTRL
                handleEntityCopy(e); // CTRL + C
                handleEntityPaste(e); // CTRL + V
                handleRevertingChanges(e); // CTRL + Z
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleCtrlDeactivate(e);
            }
        });
    }

    private void handleEntityDelete(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            if(mapView.getSelectedEntity() != null){
                Entity ent = mapView.getSelectedEntity();
//                world.deletedEntities.push(ent);
//                world.changes.push(World.Change.DELETE);

                if(ent.hasComponent("Radar")){
                    world.changes2.push(new Entity(world, ent.getName(), ent.getSide(), ent.getPos(),
                            ent.getSpeed(), ((Radar)ent.getComponent("Radar")).getRange(), ent.getType(), true));
                }

                else{
                    world.changes2.push(new Entity(world, ent.getName(), ent.getSide(), ent.getPos(),
                            ent.getSpeed(), 0, ent.getType(), true));
                }

                world.changedEntities.push(ent);
                app.removeEntityInstantaneously(mapView.getSelectedEntity());
                mapView.repaint();
            }
        }
    }

    private void handleCtrlActivate(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_CONTROL){
            ctrlOn = true;
        }
    }

    private void handleEntityCopy(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_C && ctrlOn){
            if(mapView.getSelectedEntity() != null){
                world.setCopiedEntity(mapView.getSelectedEntity());
            }
        }
    }

    private void handleEntityPaste(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_V && ctrlOn){
            if(world.getCopiedEntity() != null &&
                    world.getCopiedEntity().CanMove(mapView.allPixelColors.get(mapView.getPixPos().toString()),
                            world.getCopiedEntity().getType())){
                Entity ent = world.getCopiedEntity();
                String newName = String.format("%s - Copy", ent.getName());
                Vec2int newPos = mapView.getPixPos();

                if(ent.hasComponent("Radar"))
                    app.createEntity(newName, ent.getSide(), newPos, ent.getSpeed(), ((Radar)ent.getComponent("Radar")).getRange(), ent.getType());
                else
                    app.createEntity(newName, ent.getSide(), newPos, ent.getSpeed(), 0, ent.getType());
            }
        }
    }

    private void handleRevertingChanges(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_Z && ctrlOn){
            world.revert2();
        }
    }

    private void handleCtrlDeactivate(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_CONTROL){
            ctrlOn = false;
        }
    }

}
