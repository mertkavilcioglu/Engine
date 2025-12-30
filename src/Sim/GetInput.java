package Sim;
import App.VCSApp;
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
                world.createEntity(name, side, pos, speed, strToType(typeStr) );
            }else {
                if (!(typeStr.equals(Entity.Type.HQ.getName()))) notCreatedList.add(name);
            }

        }

        if (!notCreatedList.isEmpty()) {
            createPopUp(notCreatedList);
        }


//        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            while (true) {
//
//                ArrayList<String> compList = new ArrayList<>();
//                ArrayList<String> rangeList = new ArrayList<>();
//
//                String name = br.readLine();
//                if (name == null) break;
//                String sideStr = br.readLine();
//                String type = br.readLine();
//                String posStr = br.readLine();
//                String speedStr = br.readLine();
//
//
//                Entity.Side side = Entity.Side.ENEMY;
//                if (sideStr != null && sideStr.toLowerCase().equals("ally")) {
//                    side = Entity.Side.ALLY;
//                }
//                Vec2int pos = strToVec2int(posStr);
//                Vec2int speed = strToVec2int(speedStr);
//                if (type.equals(Entity.Type.HQ.getName())){
//                    Entity hq = world.createCommander(pos, 200);
//
//                }
//                if(world.app.pixelColor.isLocationValidForType(type,pos)){
//                    world.createEntity(name, side, pos, speed, strToType(type) );
//                }else {
//                    if (!(type.equals(Entity.Type.HQ.getName()))) notCreatedList.add(name);
//                }
//
//            }
//            }catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//
//        if (!notCreatedList.isEmpty()) {
//            createPopUp(notCreatedList);
//        }

    }

    public void readInputForReset(VCSApp app, String filePath) throws IOException {
        HashSet<Entity> allEntities = new HashSet<>(app.world.entities);
//        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            while (true) {
//                String name = br.readLine();
//                if (name == null) break;
//                String sideStr = br.readLine();
//                String type = br.readLine();
//                String posStr = br.readLine();
//                String speedStr = br.readLine();
//                String rangeString = br.readLine();
//                int range = 0;
//                try {
//                    if (rangeString == null){
//                        range = 0;
//                    } else range = Integer.parseInt(rangeString);
//                } catch (NumberFormatException e) {
//                    range = 0;
//                }
//
//
//                Entity.Side side = Entity.Side.ENEMY;
//                if (sideStr != null && sideStr.toLowerCase().equals("ally")) {
//                    side = Entity.Side.ALLY;
//                }
//                Vec2int pos = strToVec2int(posStr);
//                Vec2int speed = strToVec2int(speedStr);
//                Entity entity = app.createEntityByReset(name, side, pos, speed, range, strToType(type));
//                if (allEntities.contains(entity)) app.removeEntity(entity);
//
//            }
//        }catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

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

            Entity entity = app.createEntityByReset(name, side, pos, speed, 200, strToType(typeStr));
            if (allEntities.contains(entity)) app.removeEntity(entity);

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

