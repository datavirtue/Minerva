/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.InvoiceQuote.LayoutManager.JAXB;
import com.datavirtue.nevitiumpro.InvoiceQuote.LayoutManager.LayoutElement;
import com.datavirtue.nevitiumpro.InvoiceQuote.LayoutManager.TableColumnElement;
import com.datavirtue.nevitiumpro.InvoiceQuote.LayoutManager.TableElement;
import de.schlichtherle.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList; 
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*; 


/**
 *
 * @author Administrator
 */
@XmlRootElement(name="DocumentLayout")
@XmlSeeAlso({LayoutElement.class,TableElement.class, TableColumnElement.class})
public class JAXBLayout {    
    
    
    private ArrayList<LayoutElement> element = new ArrayList<LayoutElement>();
    @XmlTransient
    private String filename;
    
    @XmlElement(name="DocumentElement")
    public ArrayList<LayoutElement> getElement() {
        return element;
    }

    public void setElement(ArrayList<LayoutElement> element) {
        this.element = element;
    }
  
    public void saveToStream(OutputStream stream) throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(JAXBLayout.class);
        Marshaller marsh = context.createMarshaller();
        marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //marsh.marshal(this, System.out);
        marsh.marshal(this, stream);
    }
    
    public static JAXBLayout loadJAXBLayoutFromStream(InputStream stream) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXBLayout.class);
 	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	JAXBLayout layout = (JAXBLayout) jaxbUnmarshaller.unmarshal(stream);
	return layout;
    }
    
    public void saveToFile(String filename) throws JAXBException, Exception{
        if (filename == null || filename.length() < 3) {
            Exception e = new Exception("No filename has been specified for this layout.");
            throw e;
        }
        
        JAXBContext context = JAXBContext.newInstance(JAXBLayout.class);
        Marshaller marsh = context.createMarshaller();
        File file = new File(filename);
        marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //marsh.marshal(this, System.out);
        marsh.marshal(this, file);
    }
    
    public static JAXBLayout loadJAXBLayoutFromFile(String filename) throws JAXBException, IOException, Exception{
        java.io.File file = new java.io.File(filename);
	JAXBContext jaxbContext = JAXBContext.newInstance(JAXBLayout.class);
 	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	JAXBLayout layout = (JAXBLayout) jaxbUnmarshaller.unmarshal(file);
	return layout;
    }
    
    
}
