package com.waldo.serial.gui.dialogs.serialsettingsdialog;

import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.ICheckBox;
import com.waldo.utils.icomponents.ITextField;

import javax.swing.*;

import static com.waldo.serial.classes.SerialManager.serMgr;

class ManagerSettingsPanel extends JPanel implements GuiUtils.GuiInterface {
    
    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private ICheckBox useMessageEndCb;
    private ITextField messageEndTf;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    ManagerSettingsPanel() {
        initializeComponents();
        initializeLayouts();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void setMessageEndTf(String messageEnd) {
        if (messageEnd != null && !messageEnd.isEmpty()) {
            if (messageEnd.equals("\n")) {
                messageEndTf.setText("<new line>");
            } else {
                messageEndTf.setText(messageEnd);
            }
        } else {
            messageEndTf.setText("");
        }
    }

    public String getMessageEnd() {
        if (useMessageEndCb.isSelected()) {
            return messageEndTf.getText();
        }
        return "";
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        useMessageEndCb = new ICheckBox();
        useMessageEndCb.addActionListener(e -> messageEndTf.setEnabled(useMessageEndCb.isSelected()));
        messageEndTf = new ITextField(false, 7);
    }

    @Override
    public void initializeLayouts() {
        //setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        GuiUtils.GridBagHelper gbc = new GuiUtils.GridBagHelper(panel, 140);
        gbc.addLine("Use message end: ", useMessageEndCb);
        gbc.addLine("Message end: ", messageEndTf);

        add(panel);
    }

    @Override
    public void updateComponents(Object... args) {
        String currentEnd = serMgr().getMessageEnd();
        if (!currentEnd.isEmpty()) {
            setMessageEndTf(currentEnd);
            messageEndTf.setEnabled(true);
            useMessageEndCb.setSelected(true);
        } else {
            messageEndTf.setText("");
            messageEndTf.setEnabled(false);
            useMessageEndCb.setSelected(false);
        }
    }
}
