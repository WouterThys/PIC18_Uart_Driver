package com.waldo.serial.gui.dialogs;


import com.fazecast.jSerialComm.SerialPort;

import java.awt.*;

public class SerialSettingsDialog extends SerialSettingsDialogLayout {


    public SerialSettingsDialog(Window parent, String title, SerialPort serialPort) {
        super(parent, title);

        initializeComponents();
        initializeLayouts();
        updateComponents(serialPort);

    }

    private void copyValues(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.setBaudRate(getSelectedBaudRate().getBaud());
            serialPort.setNumDataBits(getSelectedDataBitValue().getBits());
            serialPort.setNumStopBits(getSelectedStopBitsValue().getStopBits());
            serialPort.setParity(getSelectedParityType().getIntValue());
            //serialPort.setFlowControl(getSelectedD);
        }
    }

    public SerialPort getSerialPort() {
        return selectedPort;
    }

    @Override
    protected void onOK() {
        if (selectedPort != null) {
            copyValues(selectedPort);
        }
        super.onOK();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
    }
}
