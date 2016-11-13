/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.InvoiceQuote.LayoutManager.JAXB;

import com.datavirtue.nevitiumpro.InvoiceQuote.LayoutManager.*;
import java.awt.Font;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Administrator
 */

@XmlSeeAlso(TableElement.class)
public class LayoutElement {
    public LayoutElement(){
    
    }
    @XmlElement(name="Table")
    public TableElement getTable() {
        return table;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
            
    public void setTable(TableElement table) {
        this.table = table;
    }

    public float getPointsPerMillimeter() {
        return pointsPerMillimeter;
    }

    public void setPointsPerMillimeter(float pointsPerMillimeter) {
        this.pointsPerMillimeter = pointsPerMillimeter;
    }

    public boolean isPortrait() {
        return portrait;
    }

    public void setPortrait(boolean portrait) {
        this.portrait = portrait;
    }

    public float[] getPaper_size() {
        return paper_size;
    }

    public void setPaper_size(float[] paper_size) {
        this.paper_size = paper_size;
    }

    public float getPos_x() {
        return pos_x;
    }

    public void setPos_x(float pos_x) {
        this.pos_x = pos_x;
    }

    public float getPos_y() {
        return pos_y;
    }

    public void setPos_y(float pos_y) {
        this.pos_y = pos_y;
    }

    public float getSize_x() {
        return size_x;
    }

    public void setSize_x(float size_x) {
        this.size_x = size_x;
    }

    public float getSize_y() {
        return size_y;
    }

    public void setSize_y(float size_y) {
        this.size_y = size_y;
    }

    public String getFont_family() {
        return font_family;
    }

    public void setFont_family(String font_family) {
        this.font_family = font_family;
    }

    public int getFont_style() {
        return font_style;
    }

    public void setFont_style(int font_style) {
        this.font_style = font_style;
    }

    public int getFont_size() {
        return font_size;
    }

    public void setFont_size(int font_size) {
        this.font_size = font_size;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getBorder_thickness() {
        return border_thickness;
    }

    public void setBorder_thickness(float border_thickness) {
        this.border_thickness = border_thickness;
    }

    public String getFg_color() {
        return fg_color;
    }

    public void setFg_color(String fg_color) {
        this.fg_color = fg_color;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public boolean isInclude() {
        return include;
    }

    public void setInclude(boolean include) {
        this.include = include;
    }
    
    
    private TableElement table = new TableElement();
    private String elementName = "";
    private float pointsPerMillimeter = 2.8346457f;
    private boolean portrait = true;
    private float [] paper_size = new float[] {215.9f, 279.4f}; //8.5" X 11" American
    private float pos_x=0;
    private float pos_y=0;
    private float size_x=0;
    private float size_y=0;
    private String font_family="tahoma";
    private int font_style=Font.PLAIN;
    private int font_size=16;
    private String imageFile = "null";
    private String text="";
    private float border_thickness = 2; //zero = no draw
    private String fg_color = "0x000000";
    private String bg_color ="0xFFFFFF";
    private int opacity = 100;
    private String tab = "  ";
    private boolean include = true;

    
}
