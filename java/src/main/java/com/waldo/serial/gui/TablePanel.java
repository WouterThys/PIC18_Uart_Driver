package com.waldo.serial.gui;

import com.waldo.serial.classes.Message.SerialMessage;
import com.waldo.serial.gui.components.IPICMessageTableModel;
import com.waldo.utils.icomponents.IPanel;
import com.waldo.utils.icomponents.ITable;

import javax.swing.*;
import java.awt.*;

import static com.waldo.serial.classes.SerialManager.serMgr;

public class TablePanel extends IPanel implements IMessagePanelListener {

    //private IMessageTableModel messageTableModel;
    private IPICMessageTableModel messageTableModel;
    private ITable<SerialMessage> messageTable;

    public TablePanel() {
        initializeComponents();
        initializeLayouts();

        updateComponents();
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

    }

    //
    // Gui listener
    //
    @Override
    public void initializeComponents() {
        messageTableModel = IPICMessageTableModel.createInstance(serMgr().getMessageType());
        messageTable = new ITable<>(messageTableModel);
        messageTable.setFillsViewportHeight(true);

        setEnabled(false);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(messageTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void updateComponents(Object... objects) {
    }
}