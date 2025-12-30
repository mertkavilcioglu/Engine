package Sim;
import App.VCSApp;
import Sim.TDL.TDLReceiverComp;
import Sim.TDL.TDLTransmitterComp;
import UI.LoadSavePanel;
import Vec.Vec2int;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GetInput {

    public void readInput(World world, String filePath) throws IOException {
        ArrayList<String> notCreatedList = new ArrayList<String>();


        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String entry = new String();

        while (entry != null) {
            entry = reader.readLine();
            if (entry == null)
                continue;
            entry = entry.trim();
            if (entry.isEmpty())
                continue;
            String name = entry;
            String sideStr = reader.readLine();
            String typeStr = reader.readLine();
            String posStr = reader.readLine();
            String speedStr = reader.readLine();
            String comp1Str = reader.readLine();
            String range1Str = reader.readLine();
            String comp2Str = reader.readLine();
            String range2Str = reader.readLine();
            String comp3Str = reader.readLine();
            String range3Str = reader.readLine();

            ArrayList<String> compList = new ArrayList<>();
            ArrayList<String> rangeList = new ArrayList<>();

            compList.add(comp1Str);
            compList.add(comp2Str);
            compList.add(comp3Str);
            rangeList.add(range1Str);
            rangeList.add(range2Str);
            rangeList.add(range3Str);

            Entity.Side side = Entity.Side.ENEMY;
            if (sideStr != null && sideStr.toLowerCase().equals("ally")) {
                side = Entity.Side.ALLY;
            }
            Vec2int pos = strToVec2int(posStr);
            Vec2int speed = strToVec2int(speedStr);
            if (typeStr.equals(Entity.Type.HQ.getName())){
                Entity hq = world.createCommander(pos, 300); //range is not used

            }
            if(world.app.pixelColor.isLocationValidForType(typeStr,pos)){
                Entity ent = world.createEntity(name, side, pos, speed, strToType(typeStr) );

                for(int i=0 ; i<3 ; i++){
                    if(compList.get(i).equalsIgnoreCase(LoadSavePanel.SaveComponentType.COMP_RADAR.toString())){
                        ent.addComponent(new Radar(ent,ent.w.entities));
                        ((Radar) ent.getComponent(Component.ComponentType.RADAR)).setRange(Integer.parseInt(rangeList.get(i)));
                        //System.out.println("added radar by load");
                    }
                    else if(compList.get(i).equalsIgnoreCase(LoadSavePanel.SaveComponentType.COMP_TRANSMITTER.toString())){
                        ent.addComponent(new TDLTransmitterComp(ent,ent.w.entities));
                        ((TDLTransmitterComp) ent.getComponent(Component.ComponentType.TRANSMITTER)).setRange(Integer.parseInt(rangeList.get(i)));
                        //System.out.println("added transmitter by load");
                    }
                    else if(compList.get(i).equalsIgnoreCase(LoadSavePanel.SaveComponentType.COMP_RECEIVER.toString())){
                        ent.addComponent(new TDLReceiverComp(ent,ent.w.entities));
                        //System.out.println("added receiver by load");
                    }
                }

            }else {
                if (!(typeStr.equals(Entity.Type.HQ.getName()))) notCreatedList.add(name);
            }

        }

        if (!notCreatedList.isEmpty()) {
            createPopUp(notCreatedList);
        }


    }

    public void readInputForReset(VCSApp app, String filePath) throws IOException {
        HashSet<Entity> allEntities = new HashSet<>(app.world.entities);


        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String entry = new String();

        while (entry != null) {
            entry = reader.readLine();
            if (entry == null)
                continue;
            entry = entry.trim();
            if (entry.isEmpty())
                continue;
            String name = entry;
            String sideStr = reader.readLine();
            String typeStr = reader.readLine();
            String posStr = reader.readLine();
            String speedStr = reader.readLine();
            String comp1Str = reader.readLine();
            String range1Str = reader.readLine();
            String comp2Str = reader.readLine();
            String range2Str = reader.readLine();
            String comp3Str = reader.readLine();
            String range3Str = reader.readLine();

            ArrayList<String> compList = new ArrayList<>();
            ArrayList<String> rangeList = new ArrayList<>();

            compList.add(comp1Str);
            compList.add(comp2Str);
            compList.add(comp3Str);
            rangeList.add(range1Str);
            rangeList.add(range2Str);
            rangeList.add(range3Str);

            Entity.Side side = Entity.Side.ENEMY;
            if (sideStr != null && sideStr.toLowerCase().equals("ally")) {
                side = Entity.Side.ALLY;
            }
            Vec2int pos = strToVec2int(posStr);
            Vec2int speed = strToVec2int(speedStr);

            Entity ent = app.createEntityByReset(name, side, pos, speed, 200, strToType(typeStr));
            for(int i=0 ; i<3 ; i++){
                if(compList.get(i).equalsIgnoreCase(LoadSavePanel.SaveComponentType.COMP_RADAR.toString())){
                    ent.addComponent(new Radar(ent,ent.w.entities));
                    ((Radar) ent.getComponent(Component.ComponentType.RADAR)).setRange(Integer.parseInt(rangeList.get(i)));
                    //System.out.println("added radar by reset");
                }
                else if(compList.get(i).equalsIgnoreCase(LoadSavePanel.SaveComponentType.COMP_TRANSMITTER.toString())){
                    ent.addComponent(new TDLTransmitterComp(ent,ent.w.entities));
                    ((TDLTransmitterComp) ent.getComponent(Component.ComponentType.TRANSMITTER)).setRange(Integer.parseInt(rangeList.get(i)));
                    //System.out.println("added transmitter by reset");
                }
                else if(compList.get(i).equalsIgnoreCase(LoadSavePanel.SaveComponentType.COMP_RECEIVER.toString())){
                    ent.addComponent(new TDLReceiverComp(ent,ent.w.entities));
                    //System.out.println("added receiver by reset");
                }
            }

            if (allEntities.contains(ent)) app.removeEntity(ent);

        }
    }

    public Vec2int strToVec2int(String str){
        if(str == null){
            return new Vec2int(0,0);
        }
        String lower = str.toLowerCase();
        if(lower.equals("null")){
            return new Vec2int(0,0);
        }


        try {
            String[] parts = str.split(",");
            if(parts.length == 2){
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                return new Vec2int(x,y);
            }
        }catch (Exception ignored){}
        return new Vec2int(0,0);

    }

    private Entity.Type strToType(String str){
        if(str.equals(Entity.Type.AIR.getName()))
            return Entity.Type.AIR;
        else if(str.equals(Entity.Type.GROUND.getName()))
            return Entity.Type.GROUND;
        else if(str.equals(Entity.Type.SURFACE.getName()))
            return Entity.Type.SURFACE;

        return null;
    }

    public void createPopUp(ArrayList<String> entName){
        String nameStr =  entName.toString().substring(1,entName.toString().length()-1);
        JOptionPane.showMessageDialog(null,nameStr + " not created.");
    }


}

