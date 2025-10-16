package Sim;

import Vec.Vec2int;

import java.util.ArrayList;
import java.util.List;

public class Radar extends Component {
    private int range = 50;

    public Radar(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities);
    }


    void detect(List<Entity> entities) {
        boolean hasVisual = false;
        Entity e;
        for (int i = 0; i < entities.size(); i++) {
            e = entities.get(i);
            if (e == parentEntity) {
                continue;
            }
            Vec2int p = entities.get(i).getPos();
            double dist = parentEntity.getPos().distance(p);
            if(dist <= range) {
                hasVisual = true;
                e.isItDetected(hasVisual);
                //for add link to give order but not add to knownentities of hq
                if (parentEntity.getType() == Entity.Type.HQ || parentEntity.w.app.headQuarter.getKnownEntities().contains(parentEntity)) {
                    if (e.getType() != Entity.Type.HQ){
                        if (e.getSide() == Entity.Side.ALLY) e.setIsInLink(hasVisual);
                    }
                }
                if (!(parentEntity.getKnownEntities().contains(e)) && e.isActive()){
                    parentEntity.addKnownEntity(e);
                    updateKnownEntities(e);
                }
                else if(parentEntity.getKnownEntities().contains(e) && !e.isActive()){
                    parentEntity.removeKnownEntity(e);
                    hasVisual = false;
                    e.isItDetected(hasVisual);
                }
            } else if (parentEntity.getKnownEntities().contains(e)){
                boolean isAnySee = false;
                for (Entity entity : parentEntity.getKnownEntities()){
                    if (entity != e && entity.getKnownEntities().contains(e)){
                        isAnySee = true;
                    }
                }
                if (!isAnySee){
                    parentEntity.removeKnownEntity(e);
                    hasVisual = false;
                    e.isItDetected(hasVisual);
                    if (parentEntity.getType() == Entity.Type.HQ) e.setIsInLink(hasVisual);
                } else parentEntity.addKnownEntity(e);
            }
        }
        if (!parentEntity.getKnownEntities().isEmpty())
            parentEntity.getTdlTransmitter().createInfoMessage(parentEntity.w.app, parentEntity, parentEntity.getKnownEntities());
    }


    //TODO neden çalışmadığına bak
    private void updateKnownEntities(Entity e){
        for (Entity entity : parentEntity.getKnownEntities()){
            if (entity.getSide() == parentEntity.getSide()){
                if (e != entity){
                    if (!(e.getKnownEntities().contains(entity))){
                        e.addKnownEntity(entity);
                    }
                }
            }
        }
    }

    @Override
    public void update(int deltaTime) {
        detect(entities);
        //System.out.println("ComponentRadar::update");
    }

    public void setRange(int r){
        range = r;
    }

    public int getRange(){
        return range;
    }

}

