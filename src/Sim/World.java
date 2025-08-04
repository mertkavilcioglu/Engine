package Sim;

import java.util.ArrayList;

public class World {
    public WorldMap map = new WorldMap();
    public static ArrayList<Entity> entities = new ArrayList<>(); //TODO static kalmadan yapmaya calis

    public void update(int deltaTime) {
        System.out.println(Thread.currentThread().getName());
        //System.out.println("Sim.World::update");
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update(deltaTime);
        }
    }
    public void render() {
        System.out.println("World::render");
    }
}
