import java.util.ArrayList;

public class Entity {
    String name;
    Vec2int pos;
    Vec2int speed;
    ArrayList<Component> components = new ArrayList<>();

    void update(int deltaTime) {
        //pos.x += speed.x;
        //pos.y += -speed.y;
        System.out.format("Entity::update - %s - time: %d\n", this, deltaTime);
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(deltaTime);
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s - Pos: %s, Speed: %s", name, pos.toString(), speed.toString());
    }
}
