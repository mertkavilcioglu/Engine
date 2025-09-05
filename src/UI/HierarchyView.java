package UI;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.NodeInfo;
import Sim.Radar;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.HashMap;

public class HierarchyView extends VCSPanel {

    private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel model; // data of tree
    private HashMap<Entity, NodeInfo> leaves = new HashMap<>();

    public HierarchyView(VCSApp app){
        super(app);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(150,app.getWindow().getHeight()));
        this.setBorder(new TitledBorder("Hierarchy"));

        rootNode = new DefaultMutableTreeNode("Hierarchy");
        model = new DefaultTreeModel(rootNode);
        tree = new JTree(model);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                        tree.getLastSelectedPathComponent();

                // if nothing is selected
                if (node == null) return;

                Entity entityFound = searchForEntity(node);
                if (entityFound == null) return;

                app.actionPanel.selectedUnit(entityFound);
                app.mapView.setSelectedEntity(entityFound);
            }
        });
        add(tree, BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode createNode(Entity e){
        DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(e.getName());

        DefaultMutableTreeNode sideNode = new DefaultMutableTreeNode(e.getSideAsName());
        DefaultMutableTreeNode posNode = new DefaultMutableTreeNode("Pos:");
        DefaultMutableTreeNode posXnode = new DefaultMutableTreeNode("X: " + e.getPos().x);
        DefaultMutableTreeNode posYnode = new DefaultMutableTreeNode("Y: " + e.getPos().y);

        leaf.add(sideNode);

        posNode.add(posXnode);
        posNode.add(posYnode);

        leaf.add(posNode);

        DefaultMutableTreeNode velNode = new DefaultMutableTreeNode("Vel:");
        DefaultMutableTreeNode velXnode = new DefaultMutableTreeNode("X: " + e.getSpeed().x);
        DefaultMutableTreeNode velYnode = new DefaultMutableTreeNode("Y: " + e.getSpeed().y);

        velNode.add(velXnode);
        velNode.add(velYnode);

        leaves.put(e, e.getNodeInfo());
        e.getNodeInfo().assignNode("posX", posXnode);
        e.getNodeInfo().assignNode("posY", posYnode);
        e.getNodeInfo().assignNode("velX", velXnode);
        e.getNodeInfo().assignNode("velY", velYnode);
        e.getNodeInfo().assignRoot(leaf);

        leaf.add(velNode);

        for(Component c : e.getComponents()){
            if(c instanceof Radar){
                if(((Radar) c).getRange() != 0){
                    DefaultMutableTreeNode radarNode = new DefaultMutableTreeNode("Radar:");
                    DefaultMutableTreeNode radarRange = new DefaultMutableTreeNode(((Radar) c).getRange());
                    radarNode.add(radarRange);
                    leaf.add(radarNode);
                    e.getNodeInfo().assignNode("radarRoot", radarNode);
                    e.getNodeInfo().assignNode("radarRange", radarRange);
                }
            }
        }

        leaf.setUserObject(e);
        return leaf;
    }

    public void updateComponent(String comp, Entity e){
        switch (comp){
            case "Radar":
                for(Component c : e.getComponents()){
                    if(c instanceof Radar){
                        if(((Radar) c).getRange() != 0){
                            if(e.getNodeInfo().getNode("radarRoot") == null){
                                DefaultMutableTreeNode radarNode = new DefaultMutableTreeNode("Radar:");
                                DefaultMutableTreeNode radarRange = new DefaultMutableTreeNode(((Radar) c).getRange());
                                radarNode.add(radarRange);
                                e.getNodeInfo().getRoot().add(radarNode);
                                e.getNodeInfo().assignNode("radarRoot", radarNode);
                                e.getNodeInfo().assignNode("radarRange", radarRange);
                            }
                            else{
                                e.getNodeInfo().getNode("radarRange").setUserObject(((Radar) c).getRange());
                            }

                        }
                        else{
                            e.getNodeInfo().getRoot().remove(e.getNodeInfo().getNode("radarRoot"));
                            e.getNodeInfo().getNode("radarRange").setUserObject(0);
                            e.removeComponent("Radar");
                            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA");
                        }
                    }
                }
                break;
        }
        model.reload(rootNode);
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
        leaves.put(e, e.getNodeInfo());
        model.reload(rootNode);
    }

    public void entityChanged(){
        model.reload(rootNode);
    }

    public void entityRemoved(Entity e){
        //REMOVE
        if(leaves.get(e).getRoot() != null && rootNode.isNodeChild(leaves.get(e).getRoot())) {
            rootNode.remove(leaves.get(e).getRoot());
            model.reload(rootNode);
        }
    }
    
    private Entity searchForEntity(DefaultMutableTreeNode node){
        Object nodeInfo = node.getUserObject();
        if (nodeInfo.getClass() == Entity.class) {
            return (Entity) nodeInfo;
        }else {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            if (node.isRoot()){
                app.actionPanel.whenRootSelected(nodeInfo);
                app.mapView.setSelectedEntity(null);

                return null;
            }
            return searchForEntity(parent);
        }
    }

    public void update(int deltaTime){
        for(Entity e : app.world.entities){
            leaves.get(e).getNode("posX").setUserObject(e.getPos().x);
            leaves.get(e).getNode("posY").setUserObject(e.getPos().y);
            model.nodeChanged(leaves.get(e).getNode("posX"));
            model.nodeChanged(leaves.get(e).getNode("posY"));

            leaves.get(e).getNode("velX").setUserObject(e.getSpeed().x);
            leaves.get(e).getNode("velY").setUserObject(e.getSpeed().y);
            model.nodeChanged(leaves.get(e).getNode("velX"));
            model.nodeChanged(leaves.get(e).getNode("velY"));
        }

    }

    public void selectNode(Entity key){
        if(key != null){
            TreePath path = new TreePath(leaves.get(key).getRoot().getPath());
            tree.setSelectionPath(path);
        }
    }

    public void clearSelectionInTree(){
        tree.clearSelection();
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
