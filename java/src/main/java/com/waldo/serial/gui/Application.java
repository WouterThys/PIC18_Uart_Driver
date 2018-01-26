package com.waldo.serial.gui;

import com.fazecast.jSerialComm.SerialPort;
import com.waldo.serial.classes.SerialManager;
import com.waldo.serial.gui.dialogs.SerialSettingsDialog;
import com.waldo.utils.GuiUtils;
import com.waldo.utils.ResourceManager;
import com.waldo.utils.icomponents.IDialog;
import com.waldo.utils.icomponents.IFrame;
import com.waldo.utils.icomponents.ILabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.waldo.serial.classes.SerialManager.serMgr;

public class Application extends IFrame {

    public static final ResourceManager resMgr = new ResourceManager("settings/", "Icons.properties");
    public static final ImageIcon greenBall = resMgr.readImage("Ball.green");
    public static final ImageIcon yellowBall = resMgr.readImage("Ball.yellow");
    public static final ImageIcon redBall = resMgr.readImage("Ball.red");


    // Tool bar
    ILabel statusLbl;
    ILabel infoLbl;
    AbstractAction settingsActions;
    AbstractAction clearAction;

    private MessagePanel messagePanel;


    public Application(String startUpPath) {

    }

    private void updateStatus(SerialPort serialPort) {
        if (serialPort != null && serialPort.isOpen()) {
            statusLbl.setIcon(greenBall);
            infoLbl.setText(serialPort.getSystemPortName() + " " + serialPort.getBaudRate());
        } else {
            statusLbl.setIcon(redBall);
            infoLbl.setText(" - no port selected - ");
        }
    }

    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel leftPnl = new JPanel(new BorderLayout());
        JToolBar toolBar = GuiUtils.createNewToolbar(settingsActions, clearAction);

        leftPnl.add(statusLbl, BorderLayout.WEST);
        leftPnl.add(infoLbl, BorderLayout.CENTER);

        panel.add(leftPnl, BorderLayout.WEST);
        panel.add(toolBar, BorderLayout.EAST);

        return  panel;
    }

    @Override
    public void initializeComponents() {
        statusLbl = new ILabel();
        infoLbl = new ILabel();
        messagePanel = new MessagePanel();

        settingsActions = new AbstractAction("Settings") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SerialSettingsDialog dialog = new SerialSettingsDialog(
                        Application.this, "Settings", serMgr().getSerialPort());

                if (dialog.showDialog() == IDialog.OK) {
                    SerialPort port = dialog.getSerialPort();
                    if (port != null) {
                        SerialManager.serMgr().open(port);
                    }
                    updateStatus(port);
                }
            }
        };

        clearAction = new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        add(createToolbarPanel(), BorderLayout.PAGE_START);
        add(messagePanel, BorderLayout.CENTER);
    }

    @Override
    public void updateComponents(Object... objects) {
        updateStatus(null);
    }
}
