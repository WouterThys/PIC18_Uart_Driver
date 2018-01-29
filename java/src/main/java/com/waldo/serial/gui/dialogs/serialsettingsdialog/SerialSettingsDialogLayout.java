package com.waldo.serial.gui.dialogs.serialsettingsdialog;

import com.fazecast.jSerialComm.SerialPort;
import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.IDialog;

import javax.swing.*;
import java.awt.*;


abstract class SerialSettingsDialogLayout extends IDialog {

    /*
    *                  COMPONENTS
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    PortSettingsPanel portSettingsPanel;
    ManagerSettingsPanel managerSettingsPanel;

    // Gui settings
    // TODO

    // Message settings
    // TODO

    /*
    *                  VARIABLES
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /*
   *                  CONSTRUCTOR
   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    SerialSettingsDialogLayout(Window parent, String title) {
        super(parent, title);
    }

    /*
     *                   METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        portSettingsPanel = new PortSettingsPanel(this);
        managerSettingsPanel = new ManagerSettingsPanel();
    }

    @Override
    public void initializeLayouts() {
        getContentPanel().setLayout(new BoxLayout(getContentPanel(), BoxLayout.X_AXIS));

        portSettingsPanel.setBorder(GuiUtils.createTitleBorder("Port config"));
        managerSettingsPanel.setBorder(GuiUtils.createTitleBorder("Manager config"));
        // ...

        getContentPanel().add(portSettingsPanel);
        getContentPanel().add(managerSettingsPanel);
        // ...

        pack();
    }

    @Override
    public void updateComponents(Object... args) {
        SerialPort serialPort = null;
        if (args.length > 0 && args[0] != null) {
            serialPort = (SerialPort) args[0];
        }

        portSettingsPanel.updateComponents(serialPort);
        managerSettingsPanel.updateComponents();
    }
}