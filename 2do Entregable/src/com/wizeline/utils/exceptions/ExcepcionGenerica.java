/**
 * 
 */
package com.wizeline.utils.exceptions;

/**
 * @author lsalgadot
 *
 */
@SuppressWarnings("serial")
public class ExcepcionGenerica extends RuntimeException {
	public ExcepcionGenerica(String mensajeError) {
		super(mensajeError);
	}
	
}
