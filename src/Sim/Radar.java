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
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e == parentEntity) {
                continue;
            }
            if (e.getSide() == parentEntity.getSide()){
                continue;
            }
            Vec2int p = entities.get(i).getPos();
            double dist = parentEntity.getPos().distance(p);
            if(dist <= range) {
                hasVisual = true;
                if (!(parentEntity.getDetectedEntities().contains(e))){
                    parentEntity.setDetectedEntities(e);
                }
            } else if (parentEntity.getDetectedEntities().contains(e)){
                parentEntity.removeFromDetectedEntities(e);
            }
            if (hasVisual) {
                e.setIsDetected(true);
            } else e.setIsDetected(false);
        }
        if(!hasVisual);
            //System.out.format("ComponentRadar::update Unit %s has no detection.\n", parentEntity.name.toUpperCase());
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

