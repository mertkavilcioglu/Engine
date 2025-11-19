package Sim.TDL;

import Sim.Component;
import Sim.Entity;

import java.util.ArrayList;

public class TDLTransmitterComp extends Component {
    public TDLTransmitterComp(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities);
        //parent.w.registerTransmitter(this);
    }

    public void send(Message msg) {
        parentEntity.w.send(msg);
    }
    @Override
    public void update(int deltaTime) {

    }
}
