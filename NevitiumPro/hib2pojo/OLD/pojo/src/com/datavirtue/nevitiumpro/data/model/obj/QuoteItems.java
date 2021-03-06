package com.datavirtue.nevitiumpro.data.model.obj;

import com.datavirtue.nevitiumpro.data.model.obj.IQuoteItems;
import com.felees.hbnpojogen.persistence.IPojoGenEntity;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.proxy.HibernateProxy;


/** 
 * Object mapping for hibernate-handled table: quote_items.
 * @author autogenerated
 */

@Entity
@Table(name = "quote_items", catalog = "NevitiumPro")
public class QuoteItems implements Cloneable, Serializable, IPojoGenEntity, IQuoteItems {

	/** Serial Version UID. */
	private static final long serialVersionUID = -559029483L;

	/** Use a WeakHashMap so entries will be garbage collected once all entities 
		referring to a saved hash are garbage collected themselves. */
	private static final Map<Serializable, Integer> SAVED_HASHES =
		Collections.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	
	/** hashCode temporary storage. */
	private volatile Integer hashCode;
	

	/** Field mapping. */
	private String code;
	/** Field mapping. */
	private java.math.BigDecimal cost;
	/** Field mapping. */
	private Long date;
	/** Field mapping. */
	private String description;
	/** Field mapping. */
	private Integer id = 0; // init for hibernate bug workaround
	/** Field mapping. */
	private java.math.BigDecimal qtySold;
	/** Field mapping. */
	private Quotes quotesQuotes;
	/** Field mapping. */
	private Boolean tax1;
	/** Field mapping. */
	private Boolean tax2;
	/** Field mapping. */
	private java.math.BigDecimal unitPrice;
	/**
	 * Default constructor, mainly for hibernate use.
	 */
	public QuoteItems() {
		// Default constructor
	} 

	/** Constructor taking a given ID.
	 * @param id to set
	 */
	public QuoteItems(Integer id) {
		this.id = id;
	}
	
	/** Constructor taking a given ID.
	 * @param id Integer object;
	 * @param quotesQuotes Quotes object;
	 */
	public QuoteItems(Integer id, Quotes quotesQuotes) {

		this.id = id;
		this.quotesQuotes = quotesQuotes;
	}
	
 


 
	/** Return the type of this class. Useful for when dealing with proxies.
	* @return Defining class.
	*/
	@Transient
	public Class<?> getClassType() {
		return QuoteItems.class;
	}
 

    /**
     * Return the value associated with the column: code.
	 * @return A String object (this.code)
	 */
	@Basic( optional = true )
	@Column( length = 20  )
	public String getCode() {
		return this.code;
		
	}
	

  
    /**  
     * Set the value related to the column: code.
	 * @param code the code value you wish to set
	 */
	public void setCode(final String code) {
		this.code = code;
	}

    /**
     * Return the value associated with the column: cost.
	 * @return A java.math.BigDecimal object (this.cost)
	 */
	public java.math.BigDecimal getCost() {
		return this.cost;
		
	}
	

  
    /**  
     * Set the value related to the column: cost.
	 * @param cost the cost value you wish to set
	 */
	public void setCost(final java.math.BigDecimal cost) {
		this.cost = cost;
	}

    /**
     * Return the value associated with the column: date.
	 * @return A Long object (this.date)
	 */
	public Long getDate() {
		return this.date;
		
	}
	

  
    /**  
     * Set the value related to the column: date.
	 * @param date the date value you wish to set
	 */
	public void setDate(final Long date) {
		this.date = date;
	}

    /**
     * Return the value associated with the column: description.
	 * @return A String object (this.description)
	 */
	@Basic( optional = true )
	@Column( length = 300  )
	public String getDescription() {
		return this.description;
		
	}
	

  
    /**  
     * Set the value related to the column: description.
	 * @param description the description value you wish to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

    /**
     * Return the value associated with the column: id.
	 * @return A Integer object (this.id)
	 */
    @Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic( optional = false )
	@Column( name = "quote_items_id", nullable = false  )
	public Integer getId() {
		return this.id;
		
	}
	

  
    /**  
     * Set the value related to the column: id.
	 * @param id the id value you wish to set
	 */
	public void setId(final Integer id) {
		// If we've just been persisted and hashCode has been
		// returned then make sure other entities with this
		// ID return the already returned hash code
		if ( (this.id == null || this.id == 0) &&
				(id != null) &&
				(this.hashCode != null) ) {
		SAVED_HASHES.put( id, this.hashCode );
		}
		this.id = id;
	}

    /**
     * Return the value associated with the column: qtySold.
	 * @return A java.math.BigDecimal object (this.qtySold)
	 */
	@Basic( optional = true )
	@Column( name = "qty_sold"  )
	public java.math.BigDecimal getQtySold() {
		return this.qtySold;
		
	}
	

  
    /**  
     * Set the value related to the column: qtySold.
	 * @param qtySold the qtySold value you wish to set
	 */
	public void setQtySold(final java.math.BigDecimal qtySold) {
		this.qtySold = qtySold;
	}

    /**
     * Return the value associated with the column: quotesQuotes.
	 * @return A Quotes object (this.quotesQuotes)
	 */
	@ManyToOne( cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY )
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@JoinColumn(name = "QUOTES_QUOTES_ID", nullable = false )
	public Quotes getQuotesQuotes() {
		return this.quotesQuotes;
		
	}
	

  
    /**  
     * Set the value related to the column: quotesQuotes.
	 * @param quotesQuotes the quotesQuotes value you wish to set
	 */
	public void setQuotesQuotes(final Quotes quotesQuotes) {
		this.quotesQuotes = quotesQuotes;
	}

    /**
     * Return the value associated with the column: tax1.
	 * @return A Boolean object (this.tax1)
	 */
	public Boolean isTax1() {
		return this.tax1;
		
	}
	

  
    /**  
     * Set the value related to the column: tax1.
	 * @param tax1 the tax1 value you wish to set
	 */
	public void setTax1(final Boolean tax1) {
		this.tax1 = tax1;
	}

    /**
     * Return the value associated with the column: tax2.
	 * @return A Boolean object (this.tax2)
	 */
	public Boolean isTax2() {
		return this.tax2;
		
	}
	

  
    /**  
     * Set the value related to the column: tax2.
	 * @param tax2 the tax2 value you wish to set
	 */
	public void setTax2(final Boolean tax2) {
		this.tax2 = tax2;
	}

    /**
     * Return the value associated with the column: unitPrice.
	 * @return A java.math.BigDecimal object (this.unitPrice)
	 */
	@Basic( optional = true )
	@Column( name = "unit_price"  )
	public java.math.BigDecimal getUnitPrice() {
		return this.unitPrice;
		
	}
	

  
    /**  
     * Set the value related to the column: unitPrice.
	 * @param unitPrice the unitPrice value you wish to set
	 */
	public void setUnitPrice(final java.math.BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}


   /**
    * Deep copy.
	* @return cloned object
	* @throws CloneNotSupportedException on error
    */
    @Override
    public QuoteItems clone() throws CloneNotSupportedException {
		
        final QuoteItems copy = (QuoteItems)super.clone();

		copy.setCode(this.getCode());
		copy.setCost(this.getCost());
		copy.setDate(this.getDate());
		copy.setDescription(this.getDescription());
		copy.setId(this.getId());
		copy.setQtySold(this.getQtySold());
		copy.setQuotesQuotes(this.getQuotesQuotes());
		copy.setTax1(this.isTax1());
		copy.setTax2(this.isTax2());
		copy.setUnitPrice(this.getUnitPrice());
		return copy;
	}
	


	/** Provides toString implementation.
	 * @see java.lang.Object#toString()
	 * @return String representation of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("code: " + this.getCode() + ", ");
		sb.append("cost: " + this.getCost() + ", ");
		sb.append("date: " + this.getDate() + ", ");
		sb.append("description: " + this.getDescription() + ", ");
		sb.append("id: " + this.getId() + ", ");
		sb.append("qtySold: " + this.getQtySold() + ", ");
		sb.append("tax1: " + this.isTax1() + ", ");
		sb.append("tax2: " + this.isTax2() + ", ");
		sb.append("unitPrice: " + this.getUnitPrice());
		return sb.toString();		
	}


	/** Equals implementation. 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param aThat Object to compare with
	 * @return true/false
	 */
	@Override
	public boolean equals(final Object aThat) {
		Object proxyThat = aThat;
		
		if ( this == aThat ) {
			 return true;
		}

		
		if (aThat instanceof HibernateProxy) {
 			// narrow down the proxy to the class we are dealing with.
 			try {
				proxyThat = ((HibernateProxy) aThat).getHibernateLazyInitializer().getImplementation(); 
			} catch (org.hibernate.ObjectNotFoundException e) {
				return false;
		   	}
		}
		if (aThat == null)  {
			 return false;
		}
		
		final QuoteItems that; 
		try {
			that = (QuoteItems) proxyThat;
			if ( !(that.getClassType().equals(this.getClassType()))){
				return false;
			}
		} catch (org.hibernate.ObjectNotFoundException e) {
				return false;
		} catch (ClassCastException e) {
				return false;
		}
		
		
		boolean result = true;
		result = result && (((this.getId() == null) && ( that.getId() == null)) || (this.getId() != null  && this.getId().equals(that.getId())));
		result = result && (((getCode() == null) && (that.getCode() == null)) || (getCode() != null && getCode().equals(that.getCode())));
		result = result && (((getCost() == null) && (that.getCost() == null)) || (getCost() != null && getCost().equals(that.getCost())));
		result = result && (((getDate() == null) && (that.getDate() == null)) || (getDate() != null && getDate().equals(that.getDate())));
		result = result && (((getDescription() == null) && (that.getDescription() == null)) || (getDescription() != null && getDescription().equals(that.getDescription())));
		result = result && (((getQtySold() == null) && (that.getQtySold() == null)) || (getQtySold() != null && getQtySold().equals(that.getQtySold())));
		result = result && (((getQuotesQuotes() == null) && (that.getQuotesQuotes() == null)) || (getQuotesQuotes() != null && getQuotesQuotes().getId().equals(that.getQuotesQuotes().getId())));	
		result = result && (((isTax1() == null) && (that.isTax1() == null)) || (isTax1() != null && isTax1().equals(that.isTax1())));
		result = result && (((isTax2() == null) && (that.isTax2() == null)) || (isTax2() != null && isTax2().equals(that.isTax2())));
		result = result && (((getUnitPrice() == null) && (that.getUnitPrice() == null)) || (getUnitPrice() != null && getUnitPrice().equals(that.getUnitPrice())));
		return result;
	}
	
	/** Calculate the hashcode.
	 * @see java.lang.Object#hashCode()
	 * @return a calculated number
	 */
	@Override
	public int hashCode() {
		if ( this.hashCode == null ) {
			synchronized ( this ) {
				if ( this.hashCode == null ) {
					Integer newHashCode = null;

					if ( getId() != null ) {
					newHashCode = SAVED_HASHES.get( getId() );
					}
					
					if ( newHashCode == null ) {
						if ( getId() != null && getId() != 0) {
							newHashCode = getId();
						} else {

						}
					}
					
					this.hashCode = newHashCode;
				}
			}
		}
	return this.hashCode;
	}
	

	
}
