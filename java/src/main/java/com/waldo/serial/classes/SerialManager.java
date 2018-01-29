package com.waldo.serial.classes;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerialManager {

    /*
     *                  SINGLETON
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final SerialManager Instance = new SerialManager();
    public static SerialManager serMgr() {
        return Instance;
    }
    private SerialManager() {
    }

    /*
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public enum ParityTypes {
        None  (SerialPort.NO_PARITY),
        Odd   (SerialPort.ODD_PARITY),
        Even  (SerialPort.EVEN_PARITY),
        Mark  (SerialPort.MARK_PARITY),
        Space (SerialPort.SPACE_PARITY);


        private final int intValue;
        ParityTypes(int intValue) {
            this.intValue = intValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public static ParityTypes fromInt(int intValue) {
            switch (intValue) {
                default:
                case SerialPort.NO_PARITY: return None;
                case SerialPort.ODD_PARITY: return Odd;
                case SerialPort.EVEN_PARITY: return Even;
                case SerialPort.MARK_PARITY: return Mark;
                case SerialPort.SPACE_PARITY: return Space;
            }
        }
    }

    public enum FlowControlTypes {
        None
    }

    public enum BaudRateValues {
        _1200  ("1200 bps", 1200),
        _2400  ("2400 bps", 2400),
        _4800  ("4800 bps", 4800),
        _9600  ("9600 bps", 9600),
        _14400 ("14400 bps", 14400),
        _19200 ("19200 bps", 19200),
        _28800 ("28800 bps", 28800),
        _38400 ("38400 bps", 38400),
        _57600 ("57600 bps", 57600),
        _Custom("<Custom>", 0);

        private final int baud;
        private final String string;
        BaudRateValues(String string, int baud) {
            this.string = string;
            this.baud = baud;
        }

        @Override
        public String toString() {
            return string;
        }

        public int getBaud() {
            return baud;
        }

        public static BaudRateValues fromBaud(int baud) {
            switch (baud) {
                case 1200: return _1200;
                case 2400: return _2400;
                case 4800: return _4800;
                case 9600: return _9600;
                case 14400: return _14400;
                case 19200: return _19200;
                case 28800: return _28800;
                case 38400: return _38400;
                case 57600: return _57600;
                default:
                case 0: return _Custom;
            }
        }
    }

    public enum DataBitValues {
        _5 ("5 bits", 5),
        _6 ("6 bits", 6),
        _7 ("7 bits", 7),
        _8 ("8 bits", 8);

        private final int bits;
        private final String string;
        DataBitValues(String string, int bits) {
            this.string = string;
            this.bits = bits;
        }

        @Override
        public String toString() {
            return string;
        }

        public int getBits() {
            return bits;
        }

        public static DataBitValues fromBits(int bits) {
            switch (bits) {
                case 5: return _5;
                case 6: return _6;
                case 7: return _7;
                default:
                case 8: return _8;
            }
        }
    }

    public enum StopBitValues {
        _1 ("1 bit", SerialPort.ONE_STOP_BIT),
        _2 ("2 bits", SerialPort.TWO_STOP_BITS);

        private final int stopBits;
        private final String string;
        StopBitValues(String string, int stopBits) {
            this.string = string;
            this.stopBits = stopBits;
        }

        @Override
        public String toString() {
            return string;
        }

        public int getStopBits() {
            return stopBits;
        }

        public static StopBitValues fromStopBits(int stopBits) {
            switch (stopBits) {
                default:
                case 1: return _1;
                case 2: return _2;
            }
        }
    }

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<SerialListener> serialListenerList = new ArrayList<>();
    private SerialPort serialPort;
    private List<String> txMessageList = new ArrayList<>();
    private List<String> rxMessageList = new ArrayList<>();

    // Settings
    private String messageEnd = "\n";

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void init(SerialListener serialListener) {
        serialListenerList.add(serialListener);
    }

    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public List<SerialPort> getSerialPorts() {
        List<SerialPort> portList = new ArrayList<>();
        Collections.addAll(portList, SerialPort.getCommPorts());
        return portList;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public SerialPort findSerialPortByName(String name) {
        for (SerialPort port : getSerialPorts()) {
            if (port.getSystemPortName().equals(name)) {
                return port;
            }
        }
        return null;
    }

    public String getMessageEnd() {
        if (messageEnd == null) {
            messageEnd = "";
        }
        return messageEnd;
    }

    public void setMessageEnd(String messageEnd) {
        this.messageEnd = messageEnd;
    }

    public void clearRxMessages() {
        rxMessageList.clear();
    }

    public void clearTxMessages() {
        txMessageList.clear();
    }

    public void addSerialListener(SerialListener serialListener) {
        if (!serialListenerList.contains(serialListener)) {
            serialListenerList.add(serialListener);
        }
    }

    public void removeSerialListener(SerialListener serialListener) {
        if (serialListenerList.contains(serialListener)) {
            serialListenerList.remove(serialListener);
        }
    }

    public SerialListener getMainSerialListener() {
        if (serialListenerList.size() > 0) {
            return serialListenerList.get(0);
        }
        return null;
    }

    public void close() {
        if (serialPort != null) {
            try {
                serialPort.removeDataListener();
                serialPort.closePort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean open(SerialPort port) {
        boolean result = false;
        if (port != null) {
            if (serialPort != null) {
                close();
            }
            this.serialPort = port;
            //this.serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            addDataAvailableEvent(this.serialPort);
            if (!this.serialPort.openPort()) {
                onError("Failed to open port: " + port.getDescriptivePortName());
            } else {
                result = true;
            }
        }
        return result;
    }

    public void initSerialPort(SerialPort port, int baud, int bits, int stopBits, int parity) {
        port.setComPortParameters(baud, bits, stopBits, parity);
        open(port);
    }

    public List<String> getTxMessageList() {
        return new ArrayList<>(txMessageList);
    }

    public List<String> getRxMessageList() {
        return new ArrayList<>(rxMessageList);
    }

    public void write(String data) {
        try {
            if (serialPort != null) {
                if (serialPort.isOpen()) {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            serialPort.writeBytes(data.getBytes(), data.length());
                            onTransmitted(data);
                        } catch (Exception e) {
                            onError("Failed to write bytes..", e);
                        }

                    });
                } else {
                    onError("COM port is closed..");
                }
            } else {
                onError("No COM port available..");
            }
        } catch (Exception e) {
            onError(e);
        }
    }

    private void addToMessageList(String message) {
        txMessageList.add(message);
    }

    private void onError(Throwable throwable) {
        onError(throwable.getMessage(), throwable);
    }

    private void onError(String error) {
        onError(error, null);
    }

    private void onError(String error, Throwable throwable) {
        for (SerialListener listener : serialListenerList) {
            listener.onSerialError(error, throwable);
        }
    }

    private void onReceived(String message) {
        for (SerialListener listener : serialListenerList) {
            listener.onReceived(message);
        }
    }

    private void onTransmitted(String message) {
        for (SerialListener listener : serialListenerList) {
            listener.onTransmitted(message);
        }
    }

    private void addDataAvailableEvent(SerialPort serialPort) {
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    SerialPort comPort = event.getSerialPort();
                    byte[] bytes = new byte[comPort.bytesAvailable()];
                    comPort.readBytes(bytes, bytes.length);
                    newDataAvailable(bytes);
                }
            }
        });
    }


    private String inputMessage = "";
    private void newDataAvailable(byte[] newData) {
        String newMessage = new String(newData);
        if (!newMessage.isEmpty()) {
            inputMessage += new String(newData);

            if (getMessageEnd().isEmpty()) {
                rxMessageList.add(inputMessage);
                onReceived(inputMessage);
                inputMessage = "";
            } else {
                if (inputMessage.endsWith(messageEnd)) {
                    rxMessageList.add(inputMessage);
                    onReceived(inputMessage);
                    inputMessage = "";
                }
            }
        }
    }
}
