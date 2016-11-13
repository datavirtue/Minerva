/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.InvoiceQuote.LayoutManager.JAXB;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Administrator
 */
@XmlSeeAlso(TableColumnElement.class)
public class TableElement {
public TableElement(){
    
}
    public float getRow_height() {
        return row_height;
    }

    public void setRow_height(float row_height) {
        this.row_height = row_height;
    }

    public int getRow_count() {
        return row_count;
    }

    public void setRow_count(int row_count) {
        this.row_count = row_count;
    }
    @XmlElement(name="Column")
    public ArrayList<TableColumnElement> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<TableColumnElement> columns) {
        this.columns = columns;
    }
        
    private float row_height = 14.17f;
    private int row_count = 1;
    private ArrayList<TableColumnElement> columns = new ArrayList();

    
    
}
