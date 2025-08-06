package Sim;

import java.util.List;

public class Follow{

    Entity findEntity(List<Entity> entities, String trgtname) {
        //Vec.Vec2int targetpos = Arrays.stream(entities).map(if(entities.getName().equals(name)){})
        System.out.println("Follow: : findEntity function");
        Entity entity = null;
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e.name.equals(trgtname)) {
                entity = e;
            }
        }
        return entity;
    }

    public void followTo(List<Entity> entities, String fname, String tname){

        Entity follower = findEntity(entities, fname);
        Entity target = findEntity(entities, tname);

        double distX = target.pos.x - follower.pos.x;
        double distY = target.pos.y - follower.pos.y;
        double dist = follower.pos.distance(target.pos);
        if(dist == 0.0){
            System.out.format("%s reached the target. \n", follower.name);
            return;
        }

        double speed = follower.speed.hypotenuse();
        follower.speed.x = (int) ((distX/dist) * speed);
        follower.speed.y = (int) ((distY/dist) * speed);

        follower.pos.x += follower.speed.x;
        follower.pos.y += follower.speed.y;

        System.out.println("Following in process!!");

    }
}
