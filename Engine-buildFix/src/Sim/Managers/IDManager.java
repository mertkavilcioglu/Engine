package Sim.Managers;

import Sim.Entity;

public class IDManager {

    private static int nextId;

    public IDManager(){
        nextId = 100;
    }

    public String createId(Entity e){
        int uID = nextId;
        nextId++;
        String type;
        String side;
        String id;

        switch (e.getType()){
            case HQ:
                type = "HQ_";
                break;
            case GROUND:
                type = "G_";
                break;
            case AIR:
                type = "A_";
                break;
            case SURFACE:
                type = "S_";
                break;
            default:
                type = "UNKNOWN_";
                break;
        }

        switch (e.getSide()){
            case ALLY:
                side = "A_";
                break;
            case ENEMY:
                side = "E_";
                break;
            default:
                side = "U_";
                break;
        }
        id = String.format("%s%s%d", side,type,uID);

        return id;
    }

    public void updateId(Entity e){
        e.setId(createId(e));
    }
}
