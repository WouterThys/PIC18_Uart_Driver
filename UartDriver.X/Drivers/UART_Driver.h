#ifndef UART_DRIVER_H
#define	UART_DRIVER_H

#ifdef	__cplusplus
extern "C" {
#endif
    
#include <stdbool.h>
#include <stdint.h>
    
/**
 * Boolean indicating data can be read.
 */    
extern bool readReady;

/**
 * Data struct for reading data.
 */
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
 * Read data from the RX pin of UART module.
 * @return data: returns the data struct.
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