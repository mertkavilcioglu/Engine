package Sim.Managers;

public class IDManager {

    private static int nextId;

    public IDManager(){
        nextId = 100;
    }

    public int createId(){
        int id = nextId;
        nextId++;
        return id;
    }
}
