/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Validation;

/**
 *
 * @author Administrator
 */
public class Verifier {
    
    /*
     * InputVerifier constants
     */
    public static final boolean REQUIRED = true;
    public static final boolean NOT_REQUIRED = false;
    
    public static final boolean YIELD_FOCUS = true; 
    public static final boolean HOLD_FOCUS = false;
    
    /*
     * Standard regex patterns
     */
    public static final String ANYTHING = "^.*$";
    public static final String PHONE10NOEXT = "^/(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]??)\\s*)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)([2-9]1[02-9]??|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})/$";
    public static final String EMAIL = "^[_a-zA-Z0-9-]+(\\\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\\\.[a-zA-Z0-9-]+)*\\\\.(([0-9]{1,3})|([a-zA-Z]{2,3})|(aero|arpa|coop|info|museum|name))$";
    public static final String URL = "^((http:\\/\\/www\\.)|(www\\.)|(http:\\/\\/))[a-zA-Z0-9._-]+\\.[a-zA-Z.]{2,5}$";
    public static final String ALPHA = "^[a-zA-Z]+$";
    public static final String ALPHANUMERIC = "^(?![\\d]+$)[a-z0-9]+$";
    
}
