package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.TDL.Message;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class LogPanel extends VCSPanel {
    private JTextPane logArea;
    private final DefaultListModel<Message> messageModelAll;
    private final JList<Message> messageList;
    private Border defaultBorder;
    private HashMap<Message.MessageClass, JButton> filterButtons = new HashMap<>();
    private HashMap<Message.MessageClass, JScrollPane> filteredLists = new HashMap<>();
    private JPanel filteredLogPanel = new JPanel();
    private JScrollPane currentList = null;
    //TODO: selected filter paneli ekle globale, select filter fonksiyonu ekle
    // parametre olarak msg class alsın, filtreler hashmapi ekle ve gelen parametreye göre
    // gösterilen filtred paneli güncelle

    public LogPanel(VCSApp app) {
        super(app);
        this.setLayout(new GridLayout(1,2));
        this.setPreferredSize(new Dimension(getWidth(),getHeight()));
        this.setBorder(BorderFactory.createLineBorder(Color.black,1));
        this.setBackground(UIColorManager.DARK_PANEL_COLOR);
        logArea = new JTextPane();
        logArea.setEditable(false);
        logArea.setCaretColor(UIColorManager.DARK_PANEL_COLOR);
        logArea.setForeground(Color.WHITE);




        TitledBorder logTitledBorder = new TitledBorder("Log: ");
        logTitledBorder.setTitleColor(Color.WHITE);
        logTitledBorder.setBorder(BorderFactory.createLineBorder(UIColorManager.DARK_MAP_BG_BLUE_COLOR,2));

        TitledBorder debugTitledBorder = new TitledBorder("Debug: ");
        debugTitledBorder.setTitleColor(Color.WHITE);
        debugTitledBorder.setBorder(BorderFactory.createLineBorder(UIColorManager.DARK_MAP_BG_BLUE_COLOR,2));

        logArea.setBorder(debugTitledBorder);
        logArea.setBackground(UIColorManager.DARK_PANEL_COLOR);
        int width = this.getWidth();

        {
            messageModelAll = new DefaultListModel<>();
            messageList = new JList<>(messageModelAll);
            messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            messageList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Message m) {
                        ArrayList<String> mes = m.getMsg2();
                        String s1 = mes.get(0);
                        String s2 = mes.get(1);
                        Color c = m.getColor();
                        String hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                        String html = String.format("<html><span style='color:white;'> %s </span> <span style='color:%s;'> %s </span></html>", s1, hex, s2);
                        lbl.setText(html);


                    }
                    return lbl;
                }
            });
            messageList.setBackground(UIColorManager.DARK_PANEL_COLOR);
            messageList.setForeground(Color.WHITE);
            messageList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int index = messageList.locationToIndex(e.getPoint());
                        if (index != -1) {
                            Rectangle msgBound = messageList.getCellBounds(index, index);
                            if (msgBound != null && msgBound.contains(e.getPoint())) {
                                Message message = messageList.getModel().getElementAt(index);
                                showMsgDetails(message);
                            }
                        }
                    }
                }
            });

            JScrollPane scrollListAll = new JScrollPane(messageList);
            scrollListAll.setBackground(UIColorManager.DARK_PANEL_COLOR);
            scrollListAll.getViewport().getView().setBackground(UIColorManager.DARK_PANEL_COLOR);
            scrollListAll.setBorder(null);
            scrollListAll.setPreferredSize(new Dimension(width / 2, getHeight()));
            filteredLists.put(Message.MessageClass.ALL, scrollListAll);
            currentList = scrollListAll;
        }

        JScrollPane debugPanel = new JScrollPane(logArea);
        debugPanel.setBackground(UIColorManager.DARK_PANEL_COLOR);
        debugPanel.getViewport().getView().setBackground(UIColorManager.DARK_PANEL_COLOR);
        debugPanel.setBorder(null);
        //this.add(label);

        debugPanel.setPreferredSize(new Dimension(width/2, getHeight()));


        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
       // messagePanel.add(scrollList);

        JPanel filterButtonsPanel = new JPanel();
        filterButtonsPanel.setBackground(UIColorManager.DARK_PANEL_COLOR);
        filterButtonsPanel.setLayout(new GridLayout(Message.MessageClass.values().length, 1));

        for(Message.MessageClass mc : Message.MessageClass.values()){
            JButton button = new JButton(mc.getCode());
            button.setBackground(UIColorManager.BUTTON_COLOR);
            defaultBorder = button.getBorder();
            filterButtons.put(mc, button);
            button.setFocusable(false);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedButtonVisual(button);
                }
            });
            filterButtonsPanel.add(button);
        }
        //messagePanel.add(filterPanel);
        this.add(messagePanel);
        this.add(debugPanel);

        filteredLogPanel.setLayout(new BoxLayout(filteredLogPanel, BoxLayout.X_AXIS));
        filteredLogPanel.setBackground(UIColorManager.DARK_PANEL_COLOR);


        selectFilter(Message.MessageClass.ALL);
        //filteredLogPanel.add(currentList);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, filteredLogPanel, filterButtonsPanel);
        splitPane.setResizeWeight(1);
        splitPane.setDividerSize(0);
        splitPane.setBorder(logTitledBorder);
        splitPane.setBackground(UIColorManager.DARK_PANEL_COLOR);
        messagePanel.add(splitPane, BorderLayout.CENTER);
    }

    private void selectFilter(Message.MessageClass messageClass) {
        if(currentList != null){
            filteredLogPanel.remove(currentList);
            currentList = filteredLists.get(messageClass);
            filteredLogPanel.add(currentList);
        }
    }

    private void selectedButtonVisual(JButton button){
        for(JButton b : filterButtons.values()){
            b.setBorder(defaultBorder);
            b.setBackground(UIColorManager.BUTTON_COLOR);
            b.setForeground(Color.BLACK);
        }

        button.setBorder(null);
        button.setBackground(UIColorManager.DARK_PANEL_COLOR);
        button.setForeground(UIColorManager.BUTTON_COLOR);
    }

    public void toLog(Message message){
        if(message.getSrcID().equals("HQ") || message.getSrcID().charAt(0) == 'A')
            if (message.getTargetID() != null && (message.getTargetID().equals("HQ") || message.getTargetID().charAt(0) == 'A')){
                messageModelAll.insertElementAt(message, 0);
            }
        //messageList.ensureIndexIsVisible(messageModel.size() - 1);
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
            messageModelAll.removeAllElements();
            messageList.repaint();
        } catch (BadLocationException e){
            e.printStackTrace();
        }
    }

    public void showMsgDetails(Message message){
        if(message.type.equals(Message.MessageType.KNOWN_INFO)){
            JTextArea textArea = new JTextArea(message.getMsgDetail());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
            JOptionPane.showMessageDialog(null, scrollPane, "Units Reported by " + message.getSrcID(),
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else{

            JOptionPane.showMessageDialog(
                    null,
                    message.getMsgDetail(),
                    message.getClass().getSimpleName(),
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        //for unselect item after popup closed
        messageList.clearSelection();
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
