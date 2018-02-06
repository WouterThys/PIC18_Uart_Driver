package com.waldo.serial.gui;

import com.waldo.serial.classes.Message.SerialMessage;
import com.waldo.utils.icomponents.IPanel;
import com.waldo.utils.icomponents.ITextPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

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

    public boolean isAppendWithNewLine() {
        return appendWithNewLine;
    }

    public void setAppendWithNewLine(boolean appendWithNewLine) {
        this.appendWithNewLine = appendWithNewLine;
    }

    public Color getTxColor() {
        return txColor;
    }

    public void setTxColor(Color txColor) {
        this.txColor = txColor;
    }

    public Color getRxColor() {
        return rxColor;
    }

    public void setRxColor(Color rxColor) {
        this.rxColor = rxColor;
    }

    @Override
    public void clearMessagePane() {
        textPn.setText("");
    }

    @Override
    public void addReceivedMessage(SerialMessage message) {
        if (message != null) {
            StyleConstants.setForeground(messageStyle, rxColor);
            try {
                String m = message.getInput();
                if (appendWithNewLine && !m.endsWith("\n")) m += "\n";
                messageDoc.insertString(messageDoc.getLength(), m, messageStyle);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addTransmittedMessage(SerialMessage message) {
        if (message != null) {
            StyleConstants.setForeground(messageStyle, txColor);
            try {
                String m = message.getInput();
                if (appendWithNewLine && !m.endsWith("\n")) m += "\n";
                messageDoc.insertString(messageDoc.getLength(), m, messageStyle);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        textPn.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    //
    // Gui listener
    //
    @Override
    public void initializeComponents() {
        textPn.setEditable(false);

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
