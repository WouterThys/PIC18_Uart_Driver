
/**
 *                              UART DRIVER
 * 
 * Description
 * -----------
 * 
 * 
 * Resources
 * ---------
 * 
 * 
 * Get it started
 * --------------
 * 
 * 1. Define in the main project the crystal frequency (_XTAL_FREQ)
 *      for example: #define _XTAL_FREQ 20000000 to set the frequency to 20MHz
 * 
 * 2. Make sure there is a .h file named PORT_Driver.h mapping the UART ports
 *      to the correct pins of the PIC. The file should contain the following
 *      defines:
 *          * #define UART_TX         PORTCbits.RC7
 *          * #define UART_RX         PORTCbits.RC6
 *          * #define UART_TX_Dir     TRISCbits.TRISC7
 *          * #define UART_RX_Dir     TRISCbits.TRISC6
 * 
 * 
 * 
 */
#ifndef UART_DRIVER_H
#define	UART_DRIVER_H

#ifdef	__cplusplus
extern "C" {
#endif
    
#include <stdbool.h>
#include <stdint.h>
    
extern bool readReady;

typedef struct {
    const char* sender;
    const char* command;
    const char* message;
}READ_Data;
    
/**
* Initializes all the parameters to the default setting, as well as writing the
* tri-state registers. Initializes the UART to the default data rate and settings.
 * @param name The name of the device.
 * @param baud The baud rate.
 * @param interrupts Boolean to set if interrupts should be used.
*/
void D_UART_Init(const char* name, uint16_t baud, bool interrupts);

/**
 * Write data to the TX pin of UART module. 
 * @param command: Command
 * @param data: Date string to write
 */
void D_UART_Write(const char* command, const char* data);

/**
 * Read 8-bit of data from the RX pin of UART module.
 * @return data: returns the data.
 */
READ_Data D_UART_Read();

/**
 * Enable the UART module
 * @param enable Enable or disable UART.
 */
void D_UART_Enable(bool enable);

#ifdef	__cplusplus
}
#endif

#endif	/* UART_DRIVER */