import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
            u.speed.x = ThreadLocalRandom.current().nextInt(1, 4);
            u.speed.y = ThreadLocalRandom.current().nextInt(1, 4);

            Radar r = new Radar(u);

            Scanner scanner = new Scanner(System.in);
            System.out.format("Assign the range of %s's radar: ", u.name);
            r.range = scanner.nextInt();

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
}

