import java.util.ArrayList;

public class World {
    public WorldMap map = new WorldMap();
    public static ArrayList<Entity> entities = new ArrayList<>();

    void update(int deltaTime) {
        System.out.println(Thread.currentThread().getName());
        //System.out.println("World::update");
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update(deltaTime);
        }
    }
    void render() {
        System.out.println("World::render");
    }
}
