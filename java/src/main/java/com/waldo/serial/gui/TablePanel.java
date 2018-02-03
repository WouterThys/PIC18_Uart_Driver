package com.waldo.serial.gui;

import com.waldo.serial.classes.Message.SerialMessage;
import com.waldo.serial.gui.components.IMessageTableModel;
import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.IPanel;
import com.waldo.utils.icomponents.ITable;
import com.waldo.utils.icomponents.ITextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.waldo.serial.classes.SerialManager.serMgr;

public class TablePanel extends IPanel implements IMessagePanelListener {

    private IMessageTableModel messageTableModel;
    private ITable<SerialMessage> messageTable;

    private ITextField inputTf;
    private AbstractAction sendAction;

    public TablePanel() {
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
        messageTableModel.clearItemList();
    }

    @Override
    public void addReceivedMessage(SerialMessage message) {
        if (message != null) {
            messageTableModel.addItem(message);
        }
    }

    @Override
    public void addTransmittedMessage(SerialMessage message) {
        if (message != null) {
            messageTableModel.addItem(message);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        messageTable.setEnabled(enabled);
        inputTf.setEnabled(enabled);
        sendAction.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    //
    // Gui listener
    //
    @Override
    public void initializeComponents() {
        messageTableModel = IMessageTableModel.createInstance(serMgr().getMessageType());
        messageTable = new ITable<>(messageTableModel);

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
        JScrollPane scrollPane = new JScrollPane(messageTable);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        add(scrollPane, BorderLayout.CENTER);
        add(sendPnl, BorderLayout.SOUTH);
    }

    @Override
    public void updateComponents(Object... objects) {

    }
}