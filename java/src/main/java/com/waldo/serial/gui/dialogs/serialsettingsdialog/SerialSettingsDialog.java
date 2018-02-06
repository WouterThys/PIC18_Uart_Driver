package com.waldo.serial.gui.dialogs.serialsettingsdialog;


import com.fazecast.jSerialComm.SerialPort;
import com.waldo.serial.gui.MessagePanel;

import java.awt.*;

import static com.waldo.serial.classes.SerialManager.serMgr;

public class SerialSettingsDialog extends SerialSettingsDialogLayout {

    private final MessagePanel messagePanel;

    public SerialSettingsDialog(Window parent, String title, SerialPort serialPort, MessagePanel messagePanel) {
        super(parent, title);

        this.messagePanel = messagePanel;

        initializeComponents();
        initializeLayouts();
        updateComponents(
                serialPort,
                messagePanel.isAppendWithNewLine(),
                messagePanel.getTxColor(),
                messagePanel.getRxColor());

    }

    private void copyPortValues(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.setBaudRate(portSettingsPanel.getSelectedBaudRate().getBaud());
            serialPort.setNumDataBits(portSettingsPanel.getSelectedDataBitValue().getBits());
            serialPort.setNumStopBits(portSettingsPanel.getSelectedStopBitsValue().getStopBits());
            serialPort.setParity(portSettingsPanel.getSelectedParityType().getIntValue());
            //serialPort.setFlowControl(getSelectedD);
        }
    }

    private void copyManagerValues() {
        serMgr().setMessageType(managerSettingsPanel.getSelectedMessageType());
    }

    private void copyGuiValues() {
        messagePanel.setAppendWithNewLine(guiSettingsPanel.isAppendWithNewLine());
        messagePanel.setTxColor(guiSettingsPanel.getTxColor());
        messagePanel.setRxColor(guiSettingsPanel.getRxColor());
    }

    public SerialPort getSerialPort() {
        return portSettingsPanel.getSelectedPort();
    }

    @Override
    protected void onOK() {
        if (portSettingsPanel.getSelectedPort() != null) {
            copyPortValues(portSettingsPanel.getSelectedPort());
            copyManagerValues();
            copyGuiValues();
        }
        super.onOK();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
    }
}
