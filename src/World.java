import java.util.ArrayList;

public class World {
    WorldMap map;
    static ArrayList<Unit> units = new ArrayList<>();

    void update(int deltaTime) {
        System.out.println("World::update");
        for (int i = 0; i < units.size(); i++) {
            units.get(i).update(deltaTime);
        }
    }
    void render() {
        System.out.println("World::render");
    }
}
