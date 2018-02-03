package com.waldo.serial.gui;

import com.fazecast.jSerialComm.SerialPort;
import com.waldo.serial.classes.Message.SerialMessage;
import com.waldo.serial.classes.SerialListener;
import com.waldo.serial.classes.SerialManager;
import com.waldo.serial.gui.dialogs.serialsettingsdialog.SerialSettingsDialog;
import com.waldo.utils.GuiUtils;
import com.waldo.utils.ResourceManager;
import com.waldo.utils.icomponents.IDialog;
import com.waldo.utils.icomponents.IFrame;
import com.waldo.utils.icomponents.ILabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.waldo.serial.classes.SerialManager.serMgr;

public class Application extends IFrame implements SerialListener {

    public static final ResourceManager resMgr = new ResourceManager("settings/", "Icons.properties");
    public static final ImageIcon greenBall = resMgr.readImage("Ball.green");
    public static final ImageIcon yellowBall = resMgr.readImage("Ball.yellow");
    public static final ImageIcon redBall = resMgr.readImage("Ball.red");

    private static final String MESSAGE_PNL = "M";
    private static final String TABLE_PNL = "T";


    // Tool bar
    private ILabel statusLbl;
    private ILabel infoLbl;
    private AbstractAction settingsActions;
    private AbstractAction clearAction;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private MessagePanel messagePanel;
    private TablePanel tablePanel;

    private IMessagePanelListener panelListener;

    public Application(String startUpPath) {
        SerialManager.serMgr().init(this);
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

    private void beginWait() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    private void endWait() {
        this.setCursor(Cursor.getDefaultCursor());
    }

    public boolean isUpdating() {
        return this.getCursor().getType() == Cursor.WAIT_CURSOR;
    }

    private void openSettings() {
        SerialSettingsDialog dialog = new SerialSettingsDialog(
                Application.this, "Settings", serMgr().getSerialPort());

        if (dialog.showDialog() == IDialog.OK) {
            beginWait();
            try {
                SerialPort port = dialog.getSerialPort();
                if (port != null) {
                    boolean open = serMgr().open(port);
                    if (open) {
                        if (serMgr().getMessageType().getStartChar().isEmpty()) {
                            messagePanel.updateComponents();
                            panelListener = messagePanel;
                            cardLayout.show(cardPanel, MESSAGE_PNL);
                        } else {
                            tablePanel.initializeComponents();
                            tablePanel.updateComponents();
                            panelListener = tablePanel;
                            cardLayout.show(cardPanel, TABLE_PNL);
                        }
                    }
                    messagePanel.setEnabled(open);
                    tablePanel.setEnabled(open);
                }
                updateStatus(port);
            } finally {
                endWait();
            }
        }
    }

    //
    // Gui interface
    //
    @Override
    public void initializeComponents() {
        // Icon
        try {
            Image image = resMgr.readImage("Main.Icon").getImage();
            setIconImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Stuff
        statusLbl = new ILabel();
        infoLbl = new ILabel();

        messagePanel = new MessagePanel();
        tablePanel = new TablePanel();

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(MESSAGE_PNL, messagePanel);
        cardPanel.add(TABLE_PNL, tablePanel);

        settingsActions = new AbstractAction("Settings") {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSettings();
            }
        };

        clearAction = new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {
                messagePanel.clearMessagePane();
            }
        };
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        add(createToolbarPanel(), BorderLayout.PAGE_START);
        add(cardPanel, BorderLayout.CENTER);
    }

    @Override
    public void updateComponents(Object... objects) {
        updateStatus(null);
        cardLayout.show(cardPanel, MESSAGE_PNL);
        SwingUtilities.invokeLater(this::openSettings);
    }

    //
    // Serial listener
    //
    @Override
    public void onSerialError(String error, Throwable throwable) {
        String message = "";
        if (error != null) {
            message = error;
        }
        if (throwable != null) {
            if (!message.isEmpty()) {
                message += "\r\n Exception: \n";
            }
            message += throwable.toString();
        }

        JOptionPane.showMessageDialog(
                Application.this,
                message,
                "Serial error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void onTransmitted(SerialMessage message) {
        if (message != null) {
            if (panelListener != null) {
                panelListener.addTransmittedMessage(message);
                panelListener.clearInput();
            }
        }
    }

    @Override
    public void onReceived(SerialMessage message) {
        if (message != null) {
            if (panelListener != null) {
                panelListener.addTransmittedMessage(message);
                panelListener.clearInput();
            }
        }
    }
}
