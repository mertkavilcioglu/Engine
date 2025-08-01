package Sim;

import Vec.Vec2int;

public abstract class Component {
    Entity parentEntity;

    public Component(Entity parent){
        parentEntity = parent;
    }

    public abstract void update(int deltaTime);
    void setPosition(Vec2int pos){
        parentEntity.pos = pos;
    }
}
