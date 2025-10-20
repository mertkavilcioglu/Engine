package Sim;

import Vec.Vec2int;

import java.util.ArrayList;
import java.util.List;

//TODO şuan kullanılmıyor tüm sisteme bağlamak lazım
public class Range extends Component{
    private int linkRange = 200;

    public Range(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities);
    }

    public void addLink(List<Entity> entities){
        for (Entity e : entities){
            if (e == parentEntity) {
                continue;
            }
            if (e.getSide() == Entity.Side.ALLY){
                Vec2int p = e.getPos();
                double dist = parentEntity.getPos().distance(p);
                if (dist <= linkRange){
                    if (!(parentEntity.getLinkedEntities().contains(e)) && e.isActive()){
                        parentEntity.addLinkedEntity(e);
                    }
                    else if(parentEntity.getLinkedEntities().contains(e) && !e.isActive()){
                        parentEntity.removeLinkedEntity(e);
                    }
                } else if (parentEntity.getLinkedEntities().contains(e)){
                    parentEntity.removeLinkedEntity(e);
                }
            }
        }
    }

    public void setLinkRange(int r){
        linkRange = r;
    }

    public int getLinkRange(){
        return linkRange;
    }

    @Override
    public void update(int deltaTime) {
        addLink(entities);
    }
}
