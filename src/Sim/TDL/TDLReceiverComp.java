package Sim.TDL;

import Sim.Component;
import Sim.Entity;

import java.util.ArrayList;

public class TDLReceiverComp extends Component {
    public TDLReceiverComp(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities);
        //parent.w.registerReceiver(this);
    }

    @Override
    public void update(int deltaTime) {

    }
}
