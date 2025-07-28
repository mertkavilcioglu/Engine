import java.util.ArrayList;

public class Unit {
    String name;
    Position pos;
    Speed speed;
    ArrayList<Component> components = new ArrayList<>();

    void update(int deltaTime) {
        pos.x += speed.x;
        pos.y += speed.y;
        System.out.format("Unit::update - %s - time: %d position: %d %d speed: %d %d\n", name, deltaTime, pos.x, pos.y, speed.x, speed.y); 
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(deltaTime);
            components.get(i).setPosition(pos);
        }
    }
}
