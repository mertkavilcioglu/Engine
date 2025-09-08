package Sim;

import App.VCSApp;
import UI.MapView;
import Vec.Vec2int;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class World{
    public final VCSApp app;

    public World(VCSApp app) {
        this.app = app;
    }
    public WorldMap map = new WorldMap();

    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<Entity> entitiesToRemove = new ArrayList<>();

    public Entity createEntity2(String name, int side) {
        Entity ent = new Entity(this);
        ent.setName(name);
        ent.setSide(side);
        ent.setPos(new Vec2int(100,100));
        Vec2int speed = ThreadLocalRandom.current().nextInt() % 2 == 0 ? new Vec2int(4,4) : new Vec2int(-4,-4);
        ent.setSpeed(speed);
        Radar r = new Radar(ent,entities);
        ent.addComponents(r);
        ent.type = "Plane";

        ent.maxSpeed = ent.getSpeed().getHypotenuseAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;

        entities.add(ent);
        return ent;
    }

    public Entity createEntity(String name, int side) {
        Entity ent = new Entity(this);
        ent.setName(name);
        ent.setSide(side);
        ent.setPos(Vec2int.getRandom(map.maxX / 8 ,map.maxY / 6));
        ent.setSpeed(Vec2int.getRandom(-4,4,-4,4));
        Radar r = new Radar(ent,entities);
        ent.addComponents(r);
        ent.type = "Plane";

        ent.maxSpeed = ent.getSpeed().getHypotenuseAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;

        entities.add(ent);
        return ent;
    }

    public Entity createEntity(String eName, int eSide, Vec2int pos, Vec2int speed, int range, String type){
        if(eName == null){
            return null;
        }
        System.out.format("Created entity %s with x:%d and y:%d", eName, pos.x, pos.y);
        System.out.format("Created entity type %s", type);
        Entity ent = new Entity(this);
        ent.setName(eName);
        ent.setSide(eSide);

        ent.setPos(new Vec2int(pos.x, pos.y));
        ent.setSpeed(new Vec2int(speed.x, speed.y));

        ent.setType(type);

        ent.maxSpeed = ent.getSpeed().getHypotenuseAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;


        //PixelColor color = new PixelColor(ent, entities);
        //color.CanMove(color.PixelColorFind(ent.getPos().x, ent.getPos().y, map));

        Radar r = new Radar(ent, entities);
        r.setRange(range);
        ent.addComponents(r);
        entities.add(ent);

        return ent;
    }

    public void update(int deltaTime) {
        //System.out.println("World::update - THREAD : " + Thread.currentThread().getName());
        //System.out.println("Sim.World::update");
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update(deltaTime);
        }
    }
    public void render() {
        System.out.println("World::render");
    }
}
