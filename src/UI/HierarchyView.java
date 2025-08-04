package UI;

import App.VCSapp;
import Sim.Entity;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class HierarchyView extends VCSpanel {

    public JTree tree;
    DefaultMutableTreeNode rootNode;
    public HierarchyView(VCSapp app){
        super(app);
        this.setLayout(new GridLayout(25,1));
        this.setPreferredSize(new Dimension(150,app.getWindow().getHeight()));
        this.setBorder(new TitledBorder("Hierarchy"));

        rootNode = new DefaultMutableTreeNode("Hierarchy");
        tree = new JTree(rootNode);
        add(tree);
    }

    public void addNameLeaf (String name ){
        DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(name);
        rootNode.add(leaf);
    }

    public void addComponentLeaf(DefaultMutableTreeNode root, String name, Vec2int vec){
        DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(name);
        root.add(leaf);
        DefaultMutableTreeNode xLeaf = new DefaultMutableTreeNode(vec.x);
        DefaultMutableTreeNode yLeaf = new DefaultMutableTreeNode(vec.y);
        leaf.add(xLeaf);
        leaf.add(yLeaf);
    }


    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }
}

//DefaultMutableTreeNode Mert = new DefaultMutableTreeNode("Mert");
//DefaultMutableTreeNode Emir = new DefaultMutableTreeNode("Emir");
//DefaultMutableTreeNode Seda = new DefaultMutableTreeNode("Seda");
//
//DefaultMutableTreeNode Position1 = new DefaultMutableTreeNode("Position");
//DefaultMutableTreeNode Velocity1 = new DefaultMutableTreeNode("Velocity");
//
//DefaultMutableTreeNode Position2 = new DefaultMutableTreeNode("Position");
//DefaultMutableTreeNode Velocity2 = new DefaultMutableTreeNode("Velocity");
//
//DefaultMutableTreeNode Position3 = new DefaultMutableTreeNode("Position");
//DefaultMutableTreeNode Velocity3 = new DefaultMutableTreeNode("Velocity");
//
//        Mert.add(Position1);
//        Mert.add(Velocity1);
//
//        Emir.add(Position2);
//        Emir.add(Velocity2);
//
//        Seda.add(Position3);
//        Seda.add(Velocity3);
//
//
//        rootNode.add(Mert);
//        rootNode.add(Emir);
//        rootNode.add(Seda);
//
//
//JTree tree = new JTree(rootNode);
