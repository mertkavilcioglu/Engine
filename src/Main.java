import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static class App {
        public static void main(String[] args) {
            App app = new App();
            app.run();
        }

        public Unit createUnit(String name) {
            Unit u = new Unit();
            u.name = name;
            u.pos = new Position();
            u.pos.x = ThreadLocalRandom.current().nextInt(10);
            u.pos.y = ThreadLocalRandom.current().nextInt(10);
            u.speed = new Speed();
            u.speed.x = ThreadLocalRandom.current().nextInt(3);
            u.speed.y = ThreadLocalRandom.current().nextInt(3);

            Radar r = new Radar();
            u.components.add(r);
            return u;
        }
        public void run() {
            System.out.println("App::run");
            boolean isWorking = true;
            int time = 0;
            int updateInterval = 1000;
            World w = new World();
            w.units.add(createUnit("Mert"));
            w.units.add(createUnit("Emir"));
            w.units.add(createUnit("Seda"));


            while (isWorking) {
                System.out.println("App::run - update begin");
                try {
                    Thread.sleep(updateInterval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // update world
                w.update(updateInterval);

                // render world
                w.render();

                time += updateInterval;
                System.out.println("App::run - update end");
                System.out.println("");
            }
        }
    }
    public static class World  {
        WorldMap map;
        ArrayList<Unit> units = new ArrayList<>();

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
    public static class WorldMap {
        int maxX = 1000;
        int maxY = 1000;
    }
    public static class Position {
        int x,y;
    }
    public static class Speed {
        int x,y;
    }
    public static class Unit {
        String name;
        Position pos;
        Speed speed;
        ArrayList<Component> components = new ArrayList<>();

        void update(int deltaTime) {

            pos.x += speed.x;
            pos.y += speed.y;
            System.out.println("Unit::update -" + name + " - " + "time:" + deltaTime + " position: " + pos.x + " " + pos.y + " speed: " + speed.x + " " + speed.y);
            for (int i = 0; i < components.size(); i++) {
                components.get(i).update(deltaTime);
            }

        }
    }
    public static abstract class Component {
        public abstract void update(int deltaTime);
    }
    public static class Radar extends Component {
        int range = 500;

        void detect(Position selfPosition, List<Unit> units) {
            // calc distance for every unit
            // check range
        }

        @Override
        public void update(int deltaTime) {
            //System.out.println("ComponentRadar::update");
        }
    }
}
