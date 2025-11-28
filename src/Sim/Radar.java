package Sim;

import Sim.TDL.TDLReceiverComp;
import Sim.TDL.TDLTransmitterComp;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.List;

public class Radar extends Component {
    private int range = 50;

    public Radar(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities, ComponentType.RADAR);
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
                if (parentEntity.getSide().equals(Entity.Side.ALLY) && e.getSide().equals(Entity.Side.ENEMY)){
                    if (!(parentEntity.getLocalWorld().getEntityHashMap().containsKey(e.getId())) && e.isActive()){
                        parentEntity.getLocalWorld().createEntity(e.getId(), e.getName(), e.getSide(), e.getPos(), e.getSpeed(), e.getType());
                    } else if (parentEntity.getLocalWorld().getEntityHashMap().containsKey(e.getId()) && e.isActive()) {
                        parentEntity.getLocalWorld().updateEntity(e.getId(), e.getName(), e.getSide(), e.getPos(), e.getSpeed(), e.getType());
                        ((TDLTransmitterComp) parentEntity.getComponent(ComponentType.TRANSMITTER)).createSurveillanceMsg2(parentEntity.w.app, parentEntity, e);
                    } else if(parentEntity.getLocalWorld().getEntityHashMap().containsKey(e.getId()) && !e.isActive()){
                        hasVisual = false;
                        e.isItDetected(hasVisual);
                        parentEntity.getLocalWorld().removeEntityFromLocal(e.getId());
                    }
                }
            } else if (parentEntity.getLocalWorld().getEntityHashMap().containsKey(e.getId())){
                hasVisual = false;
                e.isItDetected(hasVisual);
                parentEntity.getLocalWorld().removeEntityFromLocal(e.getId());
            }
        }
    }

    @Override
    public void update(int deltaTime) {
        detect(entities);
    }

    @Override
    public Radar copyTo(Entity e) {
        return new Radar(e, entities);
    }

    public void setRange(int r){
        range = r;
    }

    public int getRange(){
        return range;
    }

}

