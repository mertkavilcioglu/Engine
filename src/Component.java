public abstract class Component {
    Unit parentUnit;

    public Component(Unit parent){
        parentUnit = parent;
    }

    public abstract void update(int deltaTime);
    void setPosition(Position pos){
        parentUnit.pos = pos;
    }
}
