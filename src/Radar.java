import java.util.List;
import java.util.Vector;

import static java.lang.Math.sqrt;

public class Radar extends Component{
    int range = 50;

    public Radar(Entity parent) {
        super(parent);
    }


    void detect(List<Entity> entities) {
        // calc distance for every unit
        // check range

        boolean hasVisual = false;
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e == parentEntity) {
                continue;
            }
            Vec2int p = entities.get(i).pos;
            double dist = parentEntity.pos.distance(p);
            if(dist <= range) {
                System.out.format("ComponentRadar::update Unit %s has detected unit %s - distance: %.2f.\n",
                        parentEntity.name.toUpperCase(), entities.get(i).name.toUpperCase(),
                        dist);
                hasVisual = true;
            }
        }
        if(!hasVisual)
            System.out.format("ComponentRadar::update Unit %s has no detection.\n", parentEntity.name.toUpperCase());
    }

    @Override
    public void update(int deltaTime) {
        detect(World.entities);
        //System.out.println("ComponentRadar::update");
    }
}

