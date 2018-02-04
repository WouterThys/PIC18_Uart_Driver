package com.waldo.serial.gui;

import com.waldo.serial.classes.Message.SerialMessage;
import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.IPanel;
import com.waldo.utils.icomponents.ITextField;
import com.waldo.utils.icomponents.ITextPane;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.waldo.serial.classes.SerialManager.serMgr;

public class MessagePanel extends IPanel implements IMessagePanelListener {

    private final ITextPane textPn = new ITextPane();
    private final JScrollPane textScrollPane = new JScrollPane(textPn);
    private ITextField inputTf;
    private AbstractAction sendAction;

    private final Style messageStyle = textPn.addStyle("MessageStyle", textPn.getLogicalStyle());
    private final StyledDocument messageDoc = textPn.getStyledDocument();


    public MessagePanel() {
        initializeComponents();
        initializeLayouts();

        updateComponents();
    }

    @Override
    public void clearInput() {
        inputTf.setText("");
    }

    @Override
    public void clearMessagePane() {
        textPn.setText("");
    }

    @Override
    public void addReceivedMessage(SerialMessage message) {
        if (message != null) {
            StyleConstants.setForeground(messageStyle, Color.blue);
            try {
                messageDoc.insertString(messageDoc.getLength(), message.getMessage(), messageStyle);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addTransmittedMessage(SerialMessage message) {
        if (message != null) {
            StyleConstants.setForeground(messageStyle, Color.green);
            try {
                messageDoc.insertString(messageDoc.getLength(), message.getMessage(), messageStyle);
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
        DefaultCaret caret = (DefaultCaret) textPn.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

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
        textScrollPane.setPreferredSize(new Dimension(600, 400));

        add(textScrollPane, BorderLayout.CENTER);
        add(sendPnl, BorderLayout.SOUTH);
    }

    @Override
    public void updateComponents(Object... objects) {

    }
}
