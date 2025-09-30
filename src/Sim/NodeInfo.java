package Sim;

import javax.swing.tree.DefaultMutableTreeNode;

public class NodeInfo {
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode posX;
    private DefaultMutableTreeNode posY;
    private DefaultMutableTreeNode velX;
    private DefaultMutableTreeNode velY;
    private DefaultMutableTreeNode radarRoot;
    private DefaultMutableTreeNode radar;
    private DefaultMutableTreeNode side;

    public void assignRoot(DefaultMutableTreeNode node){
        root = node;
    }

    public void assignNode(String name, DefaultMutableTreeNode node){
        switch (name){
            case "posX":
                posX = node;
                break;
            case "posY":
                posY = node;
                break;
            case "velX":
                velX = node;
                break;
            case "velY":
                velY = node;
                break;
            case "radarRoot":
                radarRoot = node;
                break;
            case "radarRange":
                radar = node;
                break;
            case "side":
                side = node;
                break;
        }
    }

    public DefaultMutableTreeNode getNode(String name){
        switch (name){
            case "posX":
                return posX;
            case "posY":
                return posY;
            case "velX":
                return velX;
            case "velY":
                return velY;
            case "radarRoot":
                return radarRoot;
            case "radarRange":
                return radar;
            case "side":
                return side;

        }
        return null;
    }

    public DefaultMutableTreeNode getRoot(){
        return root;
    }
}
