import java.util.List;

public class Follow extends Component{

    public Follow(Entity parent){
        super(parent);
    }

    boolean isPresent = 

    public void followTo(Entity target){

        Vec2int p = target.pos;
        double distX = target.pos.x - parentEntity.pos.x;
        double distY = target.pos.y - parentEntity.pos.y;
        double dist = parentEntity.pos.distance(p);
        if(dist == 0.0){
            System.out.format("%s reached the target %s. \n", parentEntity.name, target.name);
            return;
        }

        double speed = parentEntity.speed.hypotenuse();
        parentEntity.speed.x = (int) ((distX/dist) * speed);
        parentEntity.speed.y = (int) ((distY/dist) * speed);

        parentEntity.pos.x += parentEntity.speed.x;
        parentEntity.pos.y += parentEntity.speed.y;

    }


    @Override
    public void update(int deltaTime) {
        followTo(Entity );
    }
}
