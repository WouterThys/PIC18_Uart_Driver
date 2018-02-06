package com.waldo.serial.gui;

import com.waldo.serial.classes.Message.SerialMessage;
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

public class MessagePanel extends IPanel implements IMessagePanelListener {

    private final ITextPane textPn = new ITextPane();

    private final Style messageStyle = textPn.addStyle("MessageStyle", textPn.getLogicalStyle());
    private final StyledDocument messageDoc = textPn.getStyledDocument();

    // Settings
    private boolean appendWithNewLine = true;
    private Color txColor = new Color(0, 0, 100);
    private Color rxColor = new Color(0,100,0);

    MessagePanel() {
        initializeComponents();
        initializeLayouts();

        updateComponents();
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

        StyleConstants.setBold(messageStyle, true);

        setEnabled(false);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(textPn);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void updateComponents(Object... objects) {

    }
}
