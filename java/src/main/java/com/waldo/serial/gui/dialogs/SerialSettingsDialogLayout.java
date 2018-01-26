package com.waldo.serial.gui.dialogs;

import com.fazecast.jSerialComm.SerialPort;
import com.waldo.serial.classes.SerialManager.*;
import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.IComboBox;
import com.waldo.utils.icomponents.IDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

import static com.waldo.serial.classes.SerialManager.*;


abstract class SerialSettingsDialogLayout extends IDialog {

    /*
    *                  COMPONENTS
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    // Port config
    private final DefaultComboBoxModel<String> comPortCbModel = new DefaultComboBoxModel<>();
    IComboBox<String> comPortCb = new IComboBox<>(comPortCbModel);

    private final DefaultComboBoxModel<BaudRateValues> baudCbModel = new DefaultComboBoxModel<>(BaudRateValues.values());
    IComboBox<BaudRateValues> baudCb = new IComboBox<>(baudCbModel);

    private final DefaultComboBoxModel<DataBitValues> dataBitsCbModel = new DefaultComboBoxModel<>(DataBitValues.values());
    IComboBox<DataBitValues> dataBitsCb = new IComboBox<>(dataBitsCbModel);

    private final DefaultComboBoxModel<StopBitValues> stopBitsCbModel = new DefaultComboBoxModel<>(StopBitValues.values());
    IComboBox<StopBitValues> stopBitsCb = new IComboBox<>(stopBitsCbModel);

    private final DefaultComboBoxModel<ParityTypes> parityCbModel = new DefaultComboBoxModel<>(ParityTypes.values());
    IComboBox<ParityTypes> parityCb = new IComboBox<>(parityCbModel);

    private final DefaultComboBoxModel<FlowControlTypes> flowControlCbModel = new DefaultComboBoxModel<>(FlowControlTypes.values());
    IComboBox<FlowControlTypes> flowControlTypesCb = new IComboBox<>(flowControlCbModel);

    // Gui settings
    // TODO

    // Message settings
    // TODO

    /*
    *                  VARIABLES
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    SerialPort selectedPort;

    /*
   *                  CONSTRUCTOR
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    SerialSettingsDialogLayout(Window parent, String title) {
        super(parent, title);

    }

    /*
     *                   METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    void updateComPortCb() {
        comPortCbModel.removeAllElements();

        for (SerialPort port : serMgr().getSerialPorts()) {
            comPortCbModel.addElement(port.getSystemPortName());
        }

        if (selectedPort != null) {
            comPortCb.selectItem(selectedPort.getDescriptivePortName());
        }

    }

    void updateBaudCb() {
        if (selectedPort != null) {
            baudCb.setSelectedItem(BaudRateValues.fromBaud(selectedPort.getBaudRate()));
        }
    }

    BaudRateValues getSelectedBaudRate() {
        return (BaudRateValues) baudCb.getSelectedItem();
    }

    void updateDataBitsCb() {
        if (selectedPort != null) {
            dataBitsCb.setSelectedItem(DataBitValues.fromBits(selectedPort.getNumDataBits()));
        }
    }

    DataBitValues getSelectedDataBitValue() {
        return (DataBitValues) dataBitsCb.getSelectedItem();
    }

    void updateStopBitsCb() {
        if (selectedPort != null) {
            stopBitsCb.setSelectedItem(StopBitValues.fromStopBits(selectedPort.getNumStopBits()));
        }
    }

    StopBitValues getSelectedStopBitsValue() {
        return (StopBitValues) stopBitsCb.getSelectedItem();
    }

    void updateParityCb() {
        if (selectedPort != null) {
            parityCb.setSelectedItem(ParityTypes.fromInt(selectedPort.getParity()));
        }
    }

    ParityTypes getSelectedParityType() {
        return (ParityTypes) parityCb.getSelectedItem();
    }

    void updateFlowControlCb() {
        if (selectedPort != null) {

        }
    }


    private JPanel createSerialPortPanel() {
        JPanel panel = new JPanel();

        GuiUtils.GridBagHelper gbc = new GuiUtils.GridBagHelper(panel);
        gbc.addLine("Port: ", comPortCb);
        gbc.addLine("Baud: ", baudCb);
        gbc.addLine("Data bits: ", dataBitsCb);
        gbc.addLine("Stop bits: ", stopBitsCb);
        gbc.addLine("Parity: ", parityCb);
        gbc.addLine("Flow control: ", flowControlTypesCb);

        return panel;
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        comPortCb.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String port = (String) e.getItem();
                updateComponents(serMgr().findSerialPortByName(port));
            }
        });
    }

    @Override
    public void initializeLayouts() {
        getContentPanel().setLayout(new BoxLayout(getContentPanel(), BoxLayout.X_AXIS));

        JPanel portSettingsPnl = createSerialPortPanel();
        // ...


        portSettingsPnl.setBorder(GuiUtils.createTitleBorder("Port config"));
        // ...

        getContentPanel().add(portSettingsPnl);
        // ...

        pack();
    }

    @Override
    public void updateComponents(Object... args) {
        if (args.length > 0 && args[0] != null) {
            selectedPort = (SerialPort) args[0];
        }
        if (!isUpdating()) {
            beginWait();
            try {
                updateComPortCb();
                updateBaudCb();
                updateDataBitsCb();
                updateStopBitsCb();
                updateParityCb();
                updateFlowControlCb();
            } finally {
                endWait();
            }
        }
    }
}