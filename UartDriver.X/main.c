/*
 * File:   main.c
 * Author: wouter
 *
 * Created on October 22, 2016, 5:17 PM
 */

#include <xc.h>
#include "Drivers/PORT_Driver.h"
#include "Drivers/UART_Driver.h"

#define _XTAL_FREQ 20000000

void main(void) {
    
    D_PORT_Init();
    // Initialize the UART module with a baud rate of 9600, with the use 
    // of interrupts.
    D_UART_Init(9600, true);
    
    while(1) {
        
        
    }
    return;
}
