/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.exceptions;

/**
 *
 * @author Administrator
 */
public class NullEntityException extends Exception {
    public NullEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public NullEntityException(String message) {
        super(message);
    }
    
}
