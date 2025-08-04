import java.util.List;

import static java.lang.Math.*;

public class Follow extends Component{

    public Follow(Entity parent) {
        super(parent);
    }

    /*Entity findEntity(List<Entity> entities, String trgtname) {
        //Vec2int targetpos = Arrays.stream(entities).map(if(entities.getName().equals(name)){})
        System.out.println("Follow: : findEntity function");
        Entity entity = new Entity();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e.name.equals(trgtname)) {
                entity = e;
            }
        }
        return entity;
    }

     */

    public void followTo(List<Entity> entities){

        Entity target = entities.get(2);
        if(parentEntity == target){
            target = entities.getFirst();
        }

        double distX = target.pos.x - parentEntity.pos.x;
        double distY = target.pos.y - parentEntity.pos.y;
        double dist = parentEntity.pos.distance(target.pos);
        if(dist == 0.0){
            System.out.format("%s reached the target. \n", parentEntity.name);
            return;
        }

        double speedMax = sqrt((distX*distX) + (distY*distY));
        double angle = asin((distX/dist));

        double speed = parentEntity.speed.hypotenuse();

        while (true){
            speedMax = speedMax/speed;
            parentEntity.speed.x = (int) (sin(angle) * speedMax);
            parentEntity.speed.y = (int) (cos(angle) * speedMax);
            if(parentEntity.speed.x < 4 && parentEntity.speed.y < 4){
                break;
            }
        }

        //parentEntity.speed.x = (int) (sin(angle) * speedMax);
        //parentEntity.speed.y = (int) (cos(angle) * speedMax);


        //follower.speed.x = (int) ((distX/dist) * speed);
        //follower.speed.y = (int) ((distY/dist) * speed);

        //parentEntity.pos.x += parentEntity.speed.x;
        //parentEntity.pos.y += parentEntity.speed.y;

        System.out.println("Following in process!!");

    }

    @Override
    public void update(int deltaTime) {
        followTo(World.entities);

    }
}
