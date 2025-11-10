package Sim;

import Vec.Vec2int;

import java.util.ArrayList;
import java.util.List;

public class Radar extends Component {
    private int range = 50;
    private int linkRange = 0;

    public Radar(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities);
        linkRange = parentEntity.getTdlTransmitter().getTransmitterRange();
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
                if (!(parentEntity.getLocalWorld().getEntities().contains(e)) && e.isActive()){
                    if (parentEntity.getSide() == Entity.Side.ALLY) {
                        if (e.getSide() == parentEntity.getSide() && dist <= linkRange) {
                        } else if (e.getSide().equals(Entity.Side.ENEMY)){
                            for (Entity entity : parentEntity.getLocalWorld().getEntities()){
                                parentEntity.getTdlTransmitter().createSurveillanceMsg(parentEntity.w.app, parentEntity, entity.getId(), e);
                            }
                        }
                    }
                }
                else if(parentEntity.getLocalWorld().getEntities().contains(e) && !e.isActive()){
                    hasVisual = false;
                    e.isItDetected(hasVisual);
                }
            } else if (parentEntity.getLocalWorld().getEntities().contains(e)){
                hasVisual = false;
                e.isItDetected(hasVisual);
            }
        }
    }

    @Override
    public void update(int deltaTime) {
        detect(entities);
    }

    public void setRange(int r){
        range = r;
    }

    public int getRange(){
        return range;
    }

}

