/*
 * File:   main.c
 * Author: wouter
 *
 * Created on October 22, 2016, 5:17 PM
 */

#include <xc.h>
#include <stdbool.h>
#include <stdint.h>

#include "Drivers/PORT_Driver.h"
#include "Drivers/UART_Driver.h"

#define _XTAL_FREQ 10000000

void main(void) {
    
    D_PORT_Init();
    // Initialize the UART module with a baud rate of 9600, with the use 
    // of interrupts.
    D_UART_Init(9600, true);
    D_UART_Enable(true);
    
    while(1) {
        //D_UART_Write(0x55);
        D_UART_Write(0x12);
        D_UART_Write(0x05);
        __delay_ms(30);__delay_ms(30);
        __delay_ms(30);__delay_ms(30);
        __delay_ms(30);__delay_ms(30);
        __delay_ms(30);__delay_ms(30);
        __delay_ms(30);__delay_ms(30);
        __delay_ms(30);__delay_ms(30);
        __delay_ms(30);__delay_ms(30);
        __delay_ms(30);__delay_ms(30);
        
    }
    return;
}
