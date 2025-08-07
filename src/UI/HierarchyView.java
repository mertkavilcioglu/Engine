package UI;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class HierarchyView extends VCSPanel {

    private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel model; // data of tree

    public HierarchyView(VCSApp app){
        super(app);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(150,app.getWindow().getHeight()));
        this.setBorder(new TitledBorder("Hierarchy"));

        rootNode = new DefaultMutableTreeNode("Hierarchy");
        model = new DefaultTreeModel(rootNode);
        tree = new JTree(model);
        add(tree, BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode createNode(Entity e){
        DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(e.getName());
        DefaultMutableTreeNode posNode = new DefaultMutableTreeNode("Pos:");
        posNode.add(new DefaultMutableTreeNode("X: " + e.getPos().x));
        posNode.add(new DefaultMutableTreeNode("Y: " + e.getPos().y));
        leaf.add(posNode);

        DefaultMutableTreeNode velNode = new DefaultMutableTreeNode("Vel:");
        velNode.add(new DefaultMutableTreeNode("X: " + e.getSpeed().x));
        velNode.add(new DefaultMutableTreeNode("Y: " + e.getSpeed().y));
        leaf.add(velNode);

        return leaf;
    }

    public void addComponentLeaf(DefaultMutableTreeNode root, String name, Vec2int vec){
        DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(name);
        root.add(leaf);
        DefaultMutableTreeNode xLeaf = new DefaultMutableTreeNode(vec.x);
        DefaultMutableTreeNode yLeaf = new DefaultMutableTreeNode(vec.y);
        leaf.add(xLeaf);
        leaf.add(yLeaf);
    }

    public void entityAdded(Entity e){
        DefaultMutableTreeNode node = createNode(e);
        rootNode.add(node);
        model.reload(rootNode);
    }

    public void entityRemoved(Entity e){
        //REMOVE
    }

    public void update(int deltaTime){
        for(Entity e : app.world.entities){
            //
        }

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
