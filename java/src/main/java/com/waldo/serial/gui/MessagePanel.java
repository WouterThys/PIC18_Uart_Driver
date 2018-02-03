package com.waldo.serial.gui;

import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.IPanel;
import com.waldo.utils.icomponents.ITextField;
import com.waldo.utils.icomponents.ITextPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.waldo.serial.classes.SerialManager.serMgr;

public class MessagePanel extends IPanel {

    JScrollPane scrollPane;
    private final ITextPane textPn = new ITextPane();;
    private ITextField inputTf;
    private AbstractAction sendAction;

    private final Style messageStyle = textPn.addStyle("MessageStyle", textPn.getLogicalStyle());
    private final StyledDocument messageDoc = textPn.getStyledDocument();


    public MessagePanel() {
        initializeComponents();
        initializeLayouts();

        updateComponents();
    }

    public void clearInput() {
        inputTf.setText("");
    }

    public void clearMessagePane() {
        textPn.setText("");
    }

    public void addReceivedMessage(String message) {
        if (message != null) {
            String m = message + "\n";
            StyleConstants.setForeground(messageStyle, Color.blue);
            try {
                messageDoc.insertString(messageDoc.getLength(), m, messageStyle);
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTransmittedMessage(String message) {
        if (message != null) {
            String m = message + "\n";
            StyleConstants.setForeground(messageStyle, Color.green);
            try {
                messageDoc.insertString(messageDoc.getLength(), m, messageStyle);
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        textPn.setEnabled(enabled);
        inputTf.setEnabled(enabled);
        sendAction.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    //
    // Gui listener
    //
    @Override
    public void initializeComponents() {
        textPn.setEditable(false);

        sendAction = new AbstractAction("Send") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = inputTf.getText();
                if (data != null && !data.isEmpty()) {
                    serMgr().write(data);
                }
                inputTf.requestFocus();
            }
        };

        inputTf = new ITextField("Send stuff");
        inputTf.addActionListener(sendAction);

        setEnabled(false);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        JPanel sendPnl = GuiUtils.createComponentWithActions(inputTf, sendAction);
        scrollPane = new JScrollPane(textPn);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        add(scrollPane, BorderLayout.CENTER);
        add(sendPnl, BorderLayout.SOUTH);
    }

    @Override
    public void updateComponents(Object... objects) {

    }
}
