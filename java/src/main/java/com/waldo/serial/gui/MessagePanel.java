package com.waldo.serial.gui;

import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.IPanel;
import com.waldo.utils.icomponents.ITextField;
import com.waldo.utils.icomponents.ITextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MessagePanel extends IPanel {

    private ITextPane textPn;
    private ITextField inputTf;
    private AbstractAction sendAction;

    public MessagePanel() {

    }


    @Override
    public void initializeComponents() {
        textPn = new ITextPane();
        inputTf = new ITextField();

        sendAction = new AbstractAction("Send") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        JPanel sendPnl = GuiUtils.createComponentWithActions(inputTf, sendAction);
        JScrollPane scrollPane = new JScrollPane(textPn);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        add(scrollPane, BorderLayout.CENTER);
        add(sendPnl, BorderLayout.SOUTH);
    }

    @Override
    public void updateComponents(Object... objects) {

    }
}
