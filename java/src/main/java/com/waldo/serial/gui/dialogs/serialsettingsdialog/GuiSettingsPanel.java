package com.waldo.serial.gui.dialogs.serialsettingsdialog;

import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.ICheckBox;
import com.waldo.utils.icomponents.IPanel;

import javax.swing.*;
import java.awt.*;

public class GuiSettingsPanel extends IPanel {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private ICheckBox appendWithNewLineCb;
    private JButton txColorBtn;
    private JButton rxColorBtn;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Color txColor;
    private Color rxColor;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    GuiSettingsPanel() {
        initializeComponents();
        initializeLayouts();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public boolean isAppendWithNewLine() {
        return appendWithNewLineCb.isSelected();
    }

    public Color getRxColor() {
        return rxColor;
    }

    public Color getTxColor() {
        return txColor;
    }


    private void setTxColor(Color txColor) {
        if (txColor != null) {
            this.txColor = txColor;
            txColorBtn.setBackground(txColor);
        }
    }

    private void setRxColor(Color rxColor) {
        if (rxColor != null) {
            this.rxColor = rxColor;
            rxColorBtn.setBackground(rxColor);
        }
    }

    /*
             *                  LISTENERS
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        appendWithNewLineCb = new ICheckBox();
        txColorBtn = new JButton();
        rxColorBtn = new JButton();

        txColorBtn.addActionListener(e -> {
            Color tx = JColorChooser.showDialog(GuiSettingsPanel.this, "TX color", txColor);
            setTxColor(tx);
        });

        rxColorBtn.addActionListener(e -> {
            Color rx = JColorChooser.showDialog(GuiSettingsPanel.this, "RX color", rxColor);
            setRxColor(rx);
        });
    }

    @Override
    public void initializeLayouts() {

        JPanel panel = new JPanel();
        GuiUtils.GridBagHelper gbc = new GuiUtils.GridBagHelper(panel, 140);
        gbc.addLine("Append new line: ", appendWithNewLineCb);
        gbc.addLine("TX message color: ", txColorBtn);
        gbc.addLine("RX message color: ", rxColorBtn);

        add(panel);
    }

    @Override
    public void updateComponents(Object... args) {
        if (args.length > 0) {
            appendWithNewLineCb.setSelected((Boolean) args[0]);
            setTxColor((Color) args[1]);
            setRxColor((Color) args[2]);
        }
    }
}
