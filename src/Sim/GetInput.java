package Sim;
import App.VCSApp;
import Vec.Vec2int;
import UI.PopupMenu;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GetInput {

    public void readInput(World world, String filePath) {
        ArrayList<String> notCreatedList = new ArrayList<String>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (true) {
                String name = br.readLine();
                if (name == null) break;
                String sideStr = br.readLine();
                String type = br.readLine();
                String posStr = br.readLine();
                String speedStr = br.readLine();
                br.readLine();

                int side = 1;
                if (sideStr != null && sideStr.toLowerCase().equals("ally")) {
                    side = 0;
                }
                Vec2int pos = strToVec2int(posStr);
                Vec2int speed = strToVec2int(speedStr);
                int range = 50;
                if(world.app.pixelColor.isLocationValidForType(type,pos)){
                    world.createEntity(name, side, pos, speed, range, strToType(type) );
                }else {
                    notCreatedList.add(name);
                }

            }
            }catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        if (!notCreatedList.isEmpty()) {
            new PopupMenu(notCreatedList);
        }

    }

    public void readInputForReset(VCSApp app, String filePath){
        HashSet<Entity> allEntities = new HashSet<>(app.world.entities);
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (true) {
                String name = br.readLine();
                if (name == null) break;
                String sideStr = br.readLine();
                String type = br.readLine();
                String posStr = br.readLine();
                String speedStr = br.readLine();
                String rangeString = br.readLine();
                int range = 0;
                try{
                    if (rangeString == null){
                        range = 0;
                    } else range = Integer.parseInt(rangeString);
                }catch (Exception e){
                    range = 0;
                }

                int side = 1;
                if (sideStr != null && sideStr.toLowerCase().equals("ally")) {
                    side = 0;
                }
                Vec2int pos = strToVec2int(posStr);
                Vec2int speed = strToVec2int(speedStr);
                Entity entity = app.createEntityByReset(name, side, pos, speed, range, strToType(type));
                if (allEntities.contains(entity)) app.removeEntity(entity);

            }
        }catch (IOException ex) {
            throw new RuntimeException(ex);
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


}

