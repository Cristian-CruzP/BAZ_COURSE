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
	private String mensajeError;
	private String ok = "Peticion Correcta";
	private String err = "Reintentar";
	
	public ExcepcionGenerica(String mensajeError) {
		super(mensajeError);
	}
	

	class ClaseInterna{
		@SuppressWarnings("unused")
		void valida() {
			String resultado;
			resultado = mensajeError == null ? ok: err;
		}
	}
	
	
}
