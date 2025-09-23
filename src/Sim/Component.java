package Sim;

import Vec.Vec2int;

import java.util.ArrayList;

public abstract class Component {
    protected Entity parentEntity;
    protected ArrayList<Entity> entities;

    public Component(Entity parent, ArrayList<Entity> entities){
        parentEntity = parent;
        this.entities = entities;
    }

    public abstract void update(int deltaTime);
    void setPosition(Vec2int pos){
        parentEntity.setPos(pos);
    }
}
