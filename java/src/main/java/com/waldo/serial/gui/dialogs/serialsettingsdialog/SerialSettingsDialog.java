package com.waldo.serial.gui.dialogs.serialsettingsdialog;


import com.fazecast.jSerialComm.SerialPort;

import java.awt.*;

import static com.waldo.serial.classes.SerialManager.serMgr;

public class SerialSettingsDialog extends SerialSettingsDialogLayout {

    public SerialSettingsDialog(Window parent, String title, SerialPort serialPort) {
        super(parent, title);

        initializeComponents();
        initializeLayouts();
        updateComponents(serialPort);

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

    public SerialPort getSerialPort() {
        return portSettingsPanel.getSelectedPort();
    }

    @Override
    protected void onOK() {
        if (portSettingsPanel.getSelectedPort() != null) {
            copyPortValues(portSettingsPanel.getSelectedPort());
            copyManagerValues();
        }
        super.onOK();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
    }
}
