package Sim;

import java.util.ArrayList;

public abstract class Component {
    protected Entity parentEntity;
    protected ArrayList<Entity> entities;
    protected ComponentType type;

    public enum ComponentType{
        RADAR("Radar"),
        RECEIVER("Receiver"),
        TRANSMITTER("Transmitter");

        public String name;
        ComponentType(String name){
            this.name = name;
        }
    }

    public Component(Entity parent, ArrayList<Entity> entities, ComponentType type){
        parentEntity = parent;
        this.entities = entities;
        this.type = type;
    }

    public abstract void update(int deltaTime);

    public abstract Component copyTo(Entity e);

    public ComponentType getType(){
        return type;
    }

}
