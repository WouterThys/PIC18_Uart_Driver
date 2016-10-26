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

/*******************************************************************************
 *          BASIC FUNCTIONS
 ******************************************************************************/
void writeByte(uint8_t data);
uint8_t readByte(void);

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
        
    }
}

void D_UART_Write(uint8_t command, const char* data) {
    printf(startCharacter);
    
    printf(messageCharacter);
    printf(deviceName);
    printf(":");printf(data);
    
    printf(stopCharacter);
    __delay_ms(10);
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

