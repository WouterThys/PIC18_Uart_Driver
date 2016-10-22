/**
 * @file UART_Driver.h
 * @author Waldo
 * @date 22/10/2016
 * @brief Documentation for PIC 18 Uart driver. 
 */  

#ifndef UART_DRIVER_H
#define	UART_DRIVER_H

#ifdef	__cplusplus
extern "C" {
#endif
    
#include <stdbool.h>

/**
 * @brief Struct containing data for a read.
 * 
 * This struct is used to get the data from the connected PC.
 */    
typedef struct {
    uint8_t start;
    uint8_t command;
    uint8_t data;
    uint8_t stop;
    uint8_t counter;
} READ_Data;
/**
* Variable flag to indicate a read action is ready
*/
extern bool readReady;
    
    
/**
 * @brief Initialize Uart
 * 
* Initializes all the parameters to the default setting, as well as writing the
* tri-state registers. Initializes the UART to the default data rate and settings.
*/
void D_UART_Init();

/**
 * @brief Uart write data
 * 
 * Write 8-bit of data to the TX pin of UART module. 
 * @param data: Data to write.
 */
void D_UART_Write(uint8_t data);

/**
 * @brief Uart read data
 * 
 * Read 8-bit of data from the RX pin of UART module.
 * @return data: returns the data.
 */
uint8_t D_UART_Read();

/**
 * @brief Enable the uart module
 * 
 * Enable the UART module
 */
void D_UART_Enable();

/**
 * @brief Disable the uart module
 * 
 * Disable the UART module.
 */
void D_UART_Disable();

/**
 * @brief Get the data.
 * 
 * Get the data read from the UART port;
 * @return struct with data
 */
READ_Data D_UART_getReadData();

/**
 * @brief Fill a data buffer
 * 
 * @param data
 */
void D_UART_FillDataBuffer(uint8_t data);

#ifdef	__cplusplus
}
#endif

#endif	/* UART_DRIVER */