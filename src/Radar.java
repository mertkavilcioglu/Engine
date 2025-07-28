import java.util.List;

import static java.lang.Math.sqrt;

public class Radar extends Component{
    int range = 10;

    public Radar(Unit parent) {
        super(parent);
    }


    void detect(Position selfPosition, List<Unit> units) {
        // calc distance for every unit
        // check range

        boolean hasVisual = false;
        for (int i = 0; i < units.size(); i++) {
            if(sqrt((units.get(i).pos.x - selfPosition.x)*(units.get(i).pos.x - selfPosition.x) + (units.get(i).pos.y - selfPosition.y)*(units.get(i).pos.y - selfPosition.y)) <= range && (units.get(i).pos.x != selfPosition.x || units.get(i).pos.y != selfPosition.y)){
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

