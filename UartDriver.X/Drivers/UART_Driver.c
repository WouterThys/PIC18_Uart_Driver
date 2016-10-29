#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <xc.h>

#include "PORT_Driver.h"
#include "UART_Driver.h"

/*******************************************************************************
 *          DEFINES
 ******************************************************************************/
#define _XTAL_FREQ 10000000
// &
#define START_CHAR 0x26 
// $
#define STOP_CHAR  0x24 
// :
#define SEP_CHAR   0x3A 
/*******************************************************************************
 *          MACRO FUNCTIONS
 ******************************************************************************/

/*******************************************************************************
 *          VARIABLES
 ******************************************************************************/
uint8_t baud;

const char* startCharacter =       "&";
const char* stopCharacter =        "$";
const char* deviceName;
const char* messageCharacter =     "[M]";

typedef struct {
    uint8_t start;
    const char* command;
    const char* message;
    uint8_t stop;
    uint8_t counter;
} READ_Data;
READ_Data readBuffer;
bool readReady;

/*******************************************************************************
 *          BASIC FUNCTIONS
 ******************************************************************************/
void writeByte(uint8_t data);
uint8_t readByte(void);
void fillDataBuffer(uint8_t data);

void writeByte(uint8_t data) {
    while(TXSTAbits.TRMT == 0); // Wait while buffer is still full
    TXREG = data;
}

uint8_t readByte() {
    if(RCSTAbits.FERR == 1) {
        // TODO: create error handler
    }
    if(RCSTAbits.OERR == 1) {
        // TODO: create error handler
    }
    return RCREG;
}

void fillDataBuffer(uint8_t data){
    switch(readBuffer.counter) {
        case 0:
            if(data == START_CHAR) {
                readReady = false;
                readBuffer.counter = 1;
            } else {
                readBuffer.counter = 0;
                return;
            }
            break;
        case 1:
            if (data == SEP_CHAR) {
                readBuffer.counter = 2;
            } else {
                readBuffer.command = &data;
                readBuffer.command++;
            }
            break;
        case 2:
            if (data == SEP_CHAR) {
                readBuffer.counter = 3;
            } else {
                readBuffer.message = &data;
                readBuffer.message++;
            }
            break;
        case 3:
            if(data == STOP_CHAR) {
                D_UART_Write(readBuffer.command, readBuffer.message);
                readReady = true;
            }
            readBuffer.counter = 0;
            break;
        default: 
            readBuffer.counter = 0;
            break;
    }
}

/*******************************************************************************
 *          DRIVER FUNCTIONS
 ******************************************************************************/
void D_UART_Init(const char* name, uint16_t baud, bool interrupts) {
    // Port settings
    UART_TX_Dir = 0;
    UART_RX_Dir = 1;
    
    // Clear/set variables
    deviceName = name;
    
    // Disable UART while initializing
    D_UART_Enable(false);
    
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
    
    // Invert
    

    SPBRG = ((_XTAL_FREQ/baud)/64)-1; // Baud rate selection
    
    // Interrupts for reading
    if (interrupts) {
        RCONbits.IPEN = 1;   // Enable priority levels on interrupts
        INTCONbits.GIEH = 1; // Enable high interrupt
        INTCONbits.GIEL = 1; // Enable low interrupt
        PIR1bits.RCIF = 0; // Clear flag
        IPR1bits.RCIP = 0; // Low priority
        PIE1bits.RCIE = 1; // Enable UART interrupt
    }
}

void D_UART_Write(const char* command, const char* data) {
    printf(startCharacter);
    
    printf(messageCharacter);
    printf(deviceName);
    // Command
    printf(":");printf(command);
    // Message
    printf(":");printf(data);
    
    printf(stopCharacter);
    __delay_ms(1);
}

uint8_t D_UART_Read(){
    return 0;
}

void D_UART_Enable(bool enable) {
    if(enable) {
        UART_TX_Dir = 0;
        UART_RX_Dir = 1;
        TXSTAbits.TXEN = 1; // Activate TX
        RCSTAbits.CREN = 1; // Activate RX
        RCSTAbits.SPEN = 1; // Enable UART
    } else {
        UART_TX_Dir = 0;
        UART_RX_Dir = 0;
        TXSTAbits.TXEN = 0; // Deactivate TX
        RCSTAbits.CREN = 0; // Deactivate RX
        RCSTAbits.SPEN = 0; // Enable UART
    }
}

void putch(char data) {
    writeByte(data); // Write the data
}

void interrupt low_priority LowISR(void) {
    PORTAbits.RA0 = 1;
    if (PIR1bits.RC1IF) {
        fillDataBuffer(readByte());
        PIR1bits.RC1IF = 0;
    }
    PORTAbits.RA0 = 0;
}

