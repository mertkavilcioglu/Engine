package UI;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.HashMap;

public class HierarchyView extends VCSPanel {

    private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel model; // data of tree
    private HashMap<String, DefaultMutableTreeNode> leaves = new HashMap<>();

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
        DefaultMutableTreeNode posXnode = new DefaultMutableTreeNode("X: " + e.getPos().x);
        DefaultMutableTreeNode posYnode = new DefaultMutableTreeNode("Y: " + e.getPos().y);

        posNode.add(posXnode);
        posNode.add(posYnode);

        leaves.put(e.getName() + "/pos/x", posXnode);
        leaves.put(e.getName() + "/pos/y", posYnode);

        leaf.add(posNode);

        DefaultMutableTreeNode velNode = new DefaultMutableTreeNode("Vel:");
        DefaultMutableTreeNode velXnode = new DefaultMutableTreeNode("X: " + e.getSpeed().x);
        DefaultMutableTreeNode velYnode = new DefaultMutableTreeNode("Y: " + e.getSpeed().y);

        velNode.add(velXnode);
        velNode.add(velYnode);

        leaves.put(e.getName() + "/vel/x", velXnode);
        leaves.put(e.getName() + "/vel/y", velYnode);

        leaf.add(velNode);
        //TODO: aynı isimde entityler olursa burada sıkıntı çıkar, yeni isim türeterek entity oluştur bu durumda
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
        leaves.put(e.getName(), node);
        model.reload(rootNode);

        //TODO: aynı isimde entityler olursa burada sıkıntı çıkar, yeni isim türeterek entity oluştur bu durumda
        //TODO: ayrıca scroll ekle panele
    }

    public void entityRemoved(Entity e){
        //REMOVE
    }

    public void update(int deltaTime){
        for(Entity e : app.world.entities){
            leaves.get((e.getName() + "/pos/x")).setUserObject(e.getPos().x);
            leaves.get((e.getName() + "/pos/y")).setUserObject(e.getPos().y);
            model.nodeChanged(leaves.get((e.getName() + "/pos/x")));
            model.nodeChanged(leaves.get((e.getName() + "/pos/y")));
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
