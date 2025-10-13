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
//            if (e.getSide() == parentEntity.getSide()){
//                continue;
//            }
            Vec2int p = entities.get(i).getPos();
            double dist = parentEntity.getPos().distance(p);
            if(dist <= range) {
                hasVisual = true;
                e.setIsDetected(hasVisual);
                if (!(parentEntity.getKnownEntities().contains(e)) && e.isActive()){
                    parentEntity.addKnownEntity(e);
                }
                else if(parentEntity.getKnownEntities().contains(e) && !e.isActive()){
                    parentEntity.removeKnownEntity(e);
                    hasVisual = false;
                    e.setIsDetected(hasVisual);
                }
            } else if (parentEntity.getKnownEntities().contains(e)){
                parentEntity.removeKnownEntity(e);
                hasVisual = false;
                e.setIsDetected(hasVisual);
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

