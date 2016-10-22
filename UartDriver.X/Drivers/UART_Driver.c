#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <xc.h>
#include <p18f4550.h>

#include "PORT_Driver.h"
#include "UART_Driver.h"

/*******************************************************************************
 *          DEFINES
 ******************************************************************************/
#define BAUD_RATE     19200

#define START_CHAR    0x0A
#define STOP_CHAR     0x05

/*******************************************************************************
 *          MACRO FUNCTIONS
 ******************************************************************************/


/*******************************************************************************
 *          VARIABLES
 ******************************************************************************/
READ_Data readBuffer;
bool readReady;

/*******************************************************************************
 *          BASIC FUNCTIONS
 ******************************************************************************/


/*******************************************************************************
 *          DRIVER FUNCTIONS
 ******************************************************************************/
void D_UART_Init(){
    // Port settings
    UART_TX_Dir = 0;
    UART_RX_Dir = 1;
    
    // Clear the read buffer
    readBuffer.counter = 0;
    readReady = false;
    
    // Disable UART while initializing
    D_UART_Disable();
    
    // TXSTA register settings
    TXSTAbits.TX9 = 0; // Selects 8-bit transmission
    TXSTAbits.SYNC = 0; // Synchronous mode
    TXSTAbits.BRGH = 0; // Low speed
    
    // RCSTA register settings
    RCSTAbits.RX9 = 0; // Selects 8-bit reception

    // BAUDCON register settings
    BAUDCONbits.RXDTP = 1; // RX data is inverted
    BAUDCONbits.TXCKP = 1; // TX data is inverted
    BAUDCONbits.BRG16 = 0; // 8-bit Baud Rate Generator

    SPBRG = ((_XTAL_FREQ/BAUD_RATE)/64)-1; // Baud rate selection
    
    // Interrupts for reading
    D_INT_EnableUart();
}

void D_UART_Write(uint8_t data){
    while(TXSTAbits.TRMT == 0); // Wait while buffer is still full
    TXREG = data;
}

uint8_t D_UART_Read(){
    if(RCSTAbits.FERR == 1) {
        // TODO: create error handler
    }
    if(RCSTAbits.OERR == 1) {
        // TODO: create error handler
    }
    return RCREG;
}

void D_UART_Enable() {
    UART_TX_Dir = 0;
    UART_RX_Dir = 1;
	TXSTAbits.TXEN = 1; // Activate TX
	RCSTAbits.CREN = 1; // Activate RX
	RCSTAbits.SPEN = 1; // Enable UART
}

void D_UART_Disable() {
    UART_TX_Dir = 0;
    UART_RX_Dir = 0;
	TXSTAbits.TXEN = 0; // Deactivate TX
	RCSTAbits.CREN = 0; // Deactivate RX
	RCSTAbits.SPEN = 0; // Enable UART
}

READ_Data D_UART_getReadData() {
    return readBuffer;
}

void putch(char data) {
    D_UART_Write(data); // Write the data
}

void D_UART_FillDataBuffer(uint8_t data){
    switch(readBuffer.counter) {
        case 0:
            if(data == (START_CHAR + DEVICE_NUMBER)) {
                readReady = false;
                readBuffer.counter = 1;
            } else {
                readBuffer.counter = 0;
                return;
            }
            break;
        case 1:
            readBuffer.command = data;
            readBuffer.counter = 2;
            break;
        case 2:
            readBuffer.data = data;
            readBuffer.counter = 3;
            break;
        case 3:
            if(data == STOP_CHAR) {
                readReady = true;
            }
            readBuffer.counter = 0;
            break;
        default: 
            readBuffer.counter = 0;
            break;
    }
}

