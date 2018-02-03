package com.waldo.serial.gui;

import com.waldo.serial.classes.Message.SerialMessage;

public interface IMessagePanelListener {

    void clearInput();

    void clearMessagePane();

    void addReceivedMessage(SerialMessage message);

    void addTransmittedMessage(SerialMessage message);

}
