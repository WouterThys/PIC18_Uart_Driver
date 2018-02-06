package com.waldo.serial.gui.components;

import com.waldo.serial.classes.Message.SerialMessage;
import com.waldo.serial.classes.SerialManager;
import com.waldo.utils.GuiUtils;
import com.waldo.utils.icomponents.IAbstractTableModel;
import com.waldo.utils.icomponents.ILabel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import static com.waldo.serial.gui.Application.resMgr;

public class IPICMessageTableModel extends IAbstractTableModel<SerialMessage> {

    public static final ImageIcon greenBall = resMgr.readImage("Ball.green");
    public static final ImageIcon redBall = resMgr.readImage("Ball.red");

    public static final ImageIcon txIcon = resMgr.readImage("Message.Tx = mail/16/mail_out.png");
    public static final ImageIcon rxIcon = resMgr.readImage("Message.Rx = mail/16/mail_into.png");

    private static final String[] COLUMN_NAMES = {"", "Command", "Message", "Ack"};
    private static final Class[] COLUMN_CLASSES = {ILabel.class, String.class, String.class, JButton.class};

    public static IPICMessageTableModel createInstance(SerialManager.MessageTypes type) {
        String[] columnNames;
        Class[] classes;
        if (type.isAcknowledge()) {
            columnNames = Arrays.copyOfRange(COLUMN_NAMES, 0, 2);
            classes = Arrays.copyOfRange(COLUMN_CLASSES, 0, 2);
        } else {
            columnNames = COLUMN_NAMES;
            classes = COLUMN_CLASSES;
        }
        return new IPICMessageTableModel(columnNames, classes);
    }

    private IPICMessageTableModel(String[] columnNames, Class[] classes) {
        super(columnNames, classes);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SerialMessage message = getItemAt(rowIndex);
        if (message != null) {
            switch (columnIndex) {
                case -1:
                    return message;
                case 0: // Rx or Tx
                    return message.isConverted();
                case 1: // Command
                    return message.getCommand();
                case 2: // Message
                    return message.getMessage();
                case 3: // Ack
                    AbstractAction action1 = new AbstractAction("Action 1", greenBall) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("Action 1");
                        }
                    };
                    return new JButton(action1);
            }
        }
        return null;
    }

    @Override
    public boolean hasTableCellRenderer() {
        return false;
    }

    @Override
    public DefaultTableCellRenderer getTableCellRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof SerialMessage) {
//                    if (row == 0) {
//                        TableColumn tableColumn = table.getColumnModel().getColumn(column);
//                        tableColumn.setMaxWidth(32);
//                        tableColumn.setMinWidth(32);
//                    }
//
//                    SerialMessage message = (SerialMessage) value;
//                    if (message.isRx()) {
//                        return new ITableIcon(component.getBackground(), row, isSelected, rxIcon);
//                    } else {
//                        return new ITableIcon(component.getBackground(), row, isSelected, txIcon);
//                    }
                    AbstractAction action1 = new AbstractAction("Action 1", greenBall) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("Action 1");
                        }
                    };

                    AbstractAction action2 = new AbstractAction("Action 2", redBall) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("Action 2");
                        }
                    };

                    JToolBar toolBar = GuiUtils.createNewToolbar(action1, action2);
                    return toolBar;
                }
                return component;
            }
        };
    }
}
