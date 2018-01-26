package com.waldo.serial.classes;

public interface SerialListener {
    void onSerialError(String error, Throwable throwable);
    void onTransmitted(String message);
    void onReceived(String message);
}
