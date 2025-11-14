package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.TDL.Message;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LogPanel extends VCSPanel {
    private JTextPane logArea;
    private DefaultListModel<Message> messageModel;
    private JList<Message> messageList;

    public LogPanel(VCSApp app) {
        super(app);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(getWidth(),getHeight()));
        this.setBorder(BorderFactory.createLineBorder(Color.black,1));
        this.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        logArea = new JTextPane();
        logArea.setEditable(false);
        logArea.setCaretColor(app.uiColorManager.DARK_PANEL_COLOR);
        logArea.setForeground(Color.WHITE);

        messageModel = new DefaultListModel<>();
        messageList = new JList<>(messageModel);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        messageList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Message){
                    Message m = (Message) value;
                    lbl.setText(m.getMsg());
                } return lbl;
            }
        });


        TitledBorder logTitledBorder = new TitledBorder("Log: ");
        logTitledBorder.setTitleColor(Color.WHITE);
        logTitledBorder.setBorder(BorderFactory.createLineBorder(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR,2));

        TitledBorder debugTitledBorder = new TitledBorder("Debug: ");
        debugTitledBorder.setTitleColor(Color.WHITE);
        debugTitledBorder.setBorder(BorderFactory.createLineBorder(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR,2));

        messageList.setBorder(logTitledBorder);
        messageList.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        messageList.setForeground(Color.WHITE);
        messageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    int index = messageList.locationToIndex(e.getPoint());
                    if (index != -1){
                        Rectangle msgBound = messageList.getCellBounds(index, index);
                        if (msgBound != null && msgBound.contains(e.getPoint())){
                            Message message = messageList.getModel().getElementAt(index);
                            showMsgDetails(message);
                        }
                    }
                }
            }
        });

        logArea.setBorder(debugTitledBorder);
        logArea.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        JScrollPane scrollList = new JScrollPane(messageList);
        scrollList.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        scrollList.getViewport().getView().setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        scrollList.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        scrollPane.getViewport().getView().setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        scrollPane.setBorder(null);
        //this.add(label);
        int width = this.getWidth();
        scrollPane.setPreferredSize(new Dimension(width/2, getHeight()));
        scrollList.setPreferredSize(new Dimension(width/2, getHeight()));
        this.add(scrollList);
        this.add(scrollPane);
    }

//    public void addMsgToLog(Message message){ TODO: BURAYI DÜZELT
//        if (message.getSrc().getSide() == Entity.Side.ALLY) {
//            if (message.type.equals(Message.MessageType.ENTITY_INFO)){
//                boolean isAlly = false;
//                for (Entity entity : message.getReceiverList()){
//                    if (entity.getSide() == Entity.Side.ALLY) isAlly = true;
//                }
//                if (isAlly) messageModel.addElement(message);
//            } else if (message.getTargetReceiver().getSide() == Entity.Side.ALLY)
//                messageModel.addElement(message);
//        }
//        messageList.ensureIndexIsVisible(messageModel.size() - 1);
//    }

    public void toLog(Message message){
        if(message.getSrcID().equals("HQ") || message.getSrcID().charAt(0) == 'A')
            if (!message.getTargetID().equals(null) && (message.getTargetID().equals("HQ") || message.getTargetID().charAt(0) == 'A'))
                messageModel.addElement(message);
        //if (message.getApp().world.getEntityHashMap().get(message.getSrcID()).getSide().equals(Entity.Side.ALLY))


        messageList.ensureIndexIsVisible(messageModel.size() - 1);
    }

    public void messageToLog(String message){
        String text = message + "\n";
        coloredText(logArea, text, Color.WHITE);
    }

    public void debugLogMessage(String msg){
        String text = msg + "\n";
        coloredText(logArea, text, Color.YELLOW);
    }

    public void debugLogError(String msg){
        String text = msg + "\n";
        coloredText(logArea, text, Color.RED);
    }

    private void coloredText(JTextPane textPane, String msg, Color color){
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("ColorStyle", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), msg, style);
        }catch (BadLocationException e){
            e.printStackTrace();
        }
    }

     public void clearLogArea(){
        try {
            logArea.getStyledDocument().remove(0, logArea.getStyledDocument().getLength());
            messageModel.removeAllElements();
            messageList.repaint();
        } catch (BadLocationException e){
            e.printStackTrace();
        }
    }

    //TODO log a mesaj kodlarını list olarak bastır seçilen mesajın pop up ı mesaj içeriğini barındırsın
    //TODO mesajlaşma standardlarını belirle
    public void linkMsg(Message msg){
        if (msg.type == Message.MessageType.ATTACK_ORDER){

        } else if (msg.type == Message.MessageType.MOVE_ORDER) {

        } else if (msg.type == Message.MessageType.FOLLOW_ORDER) {

        } else if (msg.type == Message.MessageType.ENTITY_INFO) {

        } else debugLogError("Wrong Message Type!");
    }

    public void showMsgDetails(Message message){
        JOptionPane.showMessageDialog(
                null,
                message.getMsgDetail(),
                message.getClass().getSimpleName(),
                JOptionPane.INFORMATION_MESSAGE
        );

        //for unselect item after popup closed
        messageList.clearSelection();
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
