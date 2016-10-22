
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
    
/**
* Initializes all the parameters to the default setting, as well as writing the
* tri-state registers. Initializes the UART to the default data rate and settings.
 * @param baud The baud rate.
 * @param interrupts Boolean to set if interrupts should be used.
*/
void D_UART_Init(uint8_t baud, bool interrupts);

/**
 * Write 8-bit of data to the TX pin of UART module. 
 * @param data: Data to write.
 */
void D_UART_Write(uint8_t data);

/**
 * Read 8-bit of data from the RX pin of UART module.
 * @return data: returns the data.
 */
uint8_t D_UART_Read();

/**
 * Enable the UART module
 * @param enable Enable or disable UART.
 */
void D_UART_Enable(bool enable);

#ifdef	__cplusplus
}
#endif

#endif	/* UART_DRIVER */