import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.sqrt;

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

            Radar r = new Radar(u);
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
                components.get(i).setPosition(pos);
            }

        }
    }
    public static abstract class Component {
        Unit parentUnit;

        public Component(Unit parent){
            parentUnit = parent;
        }

        public abstract void update(int deltaTime);
        void setPosition(Position pos){
            parentUnit.pos = pos;
        }
    }
    public static class Radar extends Component {
        int range = 10;

        public Radar(Unit parent) {
            super(parent);
        }


        void detect(Position selfPosition, List<Unit> units) {
            // calc distance for every unit
            // check range

            boolean hasVisual = false;
            for (int i = 0; i < units.size(); i++) {
                if(sqrt((units.get(i).pos.x - selfPosition.x)*(units.get(i).pos.x - selfPosition.x) + (units.get(i).pos.y - selfPosition.y)) <= range && (units.get(i).pos.x != selfPosition.x || units.get(i).pos.y != selfPosition.y)){
                    System.out.println("ComponentRadar::update Unit " + parentUnit.name.toUpperCase() + " has detected unit " + units.get(i).name.toUpperCase());
                    hasVisual = true;

                    // Position check for distance calculation accuracy
                    // System.out.println("Position of unit " + parentUnit.name.toUpperCase() + " : " + parentUnit.pos.x + " " + parentUnit.pos.y);
                    // System.out.println("Position of unit " + units.get(i).name.toUpperCase() + " : " + units.get(i).pos.x + " " + units.get(i).pos.y);

                }
                //System.out.println("ComponentRadar::update Unit " + parentUnit.name.toUpperCase() + " has no detection ");
            }
            if(!hasVisual)
                System.out.println("ComponentRadar::update Unit " + parentUnit.name.toUpperCase() + " has no detection ");
            System.out.println("");
        }

        @Override
        public void update(int deltaTime) {
            detect(parentUnit.pos, World.units);
            //System.out.println("ComponentRadar::update");
        }
    }
}
