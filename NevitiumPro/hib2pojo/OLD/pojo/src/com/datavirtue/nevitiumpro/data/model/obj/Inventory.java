package com.datavirtue.nevitiumpro.data.model.obj;

import com.datavirtue.nevitiumpro.data.model.obj.IInventory;
import com.datavirtue.nevitiumpro.data.model.obj.InventoryHasConnections;
import com.datavirtue.nevitiumpro.data.model.obj.InventoryImages;
import com.felees.hbnpojogen.persistence.IPojoGenEntity;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.proxy.HibernateProxy;


/** 
 * Object mapping for hibernate-handled table: inventory.
 * @author autogenerated
 */

@Entity
@Table(name = "inventory", catalog = "NevitiumPro")
public class Inventory implements Cloneable, Serializable, IPojoGenEntity, IInventory {

	/** Serial Version UID. */
	private static final long serialVersionUID = -559029503L;

	/** Use a WeakHashMap so entries will be garbage collected once all entities 
		referring to a saved hash are garbage collected themselves. */
	private static final Map<Serializable, Integer> SAVED_HASHES =
		Collections.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	
	/** hashCode temporary storage. */
	private volatile Integer hashCode;
	

	/** Field mapping. */
	private Boolean available;
	/** Field mapping. */
	private String category;
	/** Field mapping. */
	private String code;
	/** Field mapping. */
	private java.math.BigDecimal cost;
	/** Field mapping. */
	private Double cutoff;
	/** Field mapping. */
	private String description;
	/** Field mapping. */
	private Integer id = 0; // init for hibernate bug workaround
	/** Field mapping. */
	private Set<InventoryHasConnections> inventoryHasConnectionss = new HashSet<InventoryHasConnections>();

	/** Field mapping. */
	private Set<InventoryImages> inventoryImageses = new HashSet<InventoryImages>();

	/** Field mapping. */
	private Long lastReceived;
	/** Field mapping. */
	private Long lastSale;
	/** Field mapping. */
	private Double onhand;
	/** Field mapping. */
	private Boolean partialQtyAllowed;
	/** Field mapping. */
	private java.math.BigDecimal price;
	/** Field mapping. */
	private String size;
	/** Field mapping. */
	private Boolean tax1;
	/** Field mapping. */
	private Boolean tax2;
	/** Field mapping. */
	private String upc;
	/** Field mapping. */
	private String weight;
	/**
	 * Default constructor, mainly for hibernate use.
	 */
	public Inventory() {
		// Default constructor
	} 

	/** Constructor taking a given ID.
	 * @param id to set
	 */
	public Inventory(Integer id) {
		this.id = id;
	}
	
 


 
	/** Return the type of this class. Useful for when dealing with proxies.
	* @return Defining class.
	*/
	@Transient
	public Class<?> getClassType() {
		return Inventory.class;
	}
 

    /**
     * Return the value associated with the column: available.
	 * @return A Boolean object (this.available)
	 */
	public Boolean isAvailable() {
		return this.available;
		
	}
	

  
    /**  
     * Set the value related to the column: available.
	 * @param available the available value you wish to set
	 */
	public void setAvailable(final Boolean available) {
		this.available = available;
	}

    /**
     * Return the value associated with the column: category.
	 * @return A String object (this.category)
	 */
	@Basic( optional = true )
	@Column( length = 30  )
	public String getCategory() {
		return this.category;
		
	}
	

  
    /**  
     * Set the value related to the column: category.
	 * @param category the category value you wish to set
	 */
	public void setCategory(final String category) {
		this.category = category;
	}

    /**
     * Return the value associated with the column: code.
	 * @return A String object (this.code)
	 */
	@Basic( optional = true )
	@Column( length = 16  )
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
     * Return the value associated with the column: cutoff.
	 * @return A Double object (this.cutoff)
	 */
	public Double getCutoff() {
		return this.cutoff;
		
	}
	

  
    /**  
     * Set the value related to the column: cutoff.
	 * @param cutoff the cutoff value you wish to set
	 */
	public void setCutoff(final Double cutoff) {
		this.cutoff = cutoff;
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
	@Column( name = "inventory_id", nullable = false  )
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
     * Return the value associated with the column: inventoryHasConnections.
	 * @return A Set&lt;InventoryHasConnections&gt; object (this.inventoryHasConnections)
	 */
 	@OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "id.inventoryInventory"  )
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( name = "inventory_id", nullable = false  )
	public Set<InventoryHasConnections> getInventoryHasConnectionss() {
		return this.inventoryHasConnectionss;
		
	}
	
	/**
	 * Adds a bi-directional link of type InventoryHasConnections to the inventoryHasConnectionss set.
	 * @param inventoryHasConnections item to add
	 */
	public void addInventoryHasConnections(InventoryHasConnections inventoryHasConnections) {
		inventoryHasConnections.getId().setInventoryInventory(this);
		this.inventoryHasConnectionss.add(inventoryHasConnections);
	}

  
    /**  
     * Set the value related to the column: inventoryHasConnections.
	 * @param inventoryHasConnections the inventoryHasConnections value you wish to set
	 */
	public void setInventoryHasConnectionss(final Set<InventoryHasConnections> inventoryHasConnections) {
		this.inventoryHasConnectionss = inventoryHasConnections;
	}

    /**
     * Return the value associated with the column: inventoryImages.
	 * @return A Set&lt;InventoryImages&gt; object (this.inventoryImages)
	 */
 	@OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "inventoryInventory"  )
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( name = "inventory_id", nullable = false  )
	public Set<InventoryImages> getInventoryImageses() {
		return this.inventoryImageses;
		
	}
	
	/**
	 * Adds a bi-directional link of type InventoryImages to the inventoryImageses set.
	 * @param inventoryImages item to add
	 */
	public void addInventoryImages(InventoryImages inventoryImages) {
		inventoryImages.setInventoryInventory(this);
		this.inventoryImageses.add(inventoryImages);
	}

  
    /**  
     * Set the value related to the column: inventoryImages.
	 * @param inventoryImages the inventoryImages value you wish to set
	 */
	public void setInventoryImageses(final Set<InventoryImages> inventoryImages) {
		this.inventoryImageses = inventoryImages;
	}

    /**
     * Return the value associated with the column: lastReceived.
	 * @return A Long object (this.lastReceived)
	 */
	@Basic( optional = true )
	@Column( name = "last_received"  )
	public Long getLastReceived() {
		return this.lastReceived;
		
	}
	

  
    /**  
     * Set the value related to the column: lastReceived.
	 * @param lastReceived the lastReceived value you wish to set
	 */
	public void setLastReceived(final Long lastReceived) {
		this.lastReceived = lastReceived;
	}

    /**
     * Return the value associated with the column: lastSale.
	 * @return A Long object (this.lastSale)
	 */
	@Basic( optional = true )
	@Column( name = "last_sale"  )
	public Long getLastSale() {
		return this.lastSale;
		
	}
	

  
    /**  
     * Set the value related to the column: lastSale.
	 * @param lastSale the lastSale value you wish to set
	 */
	public void setLastSale(final Long lastSale) {
		this.lastSale = lastSale;
	}

    /**
     * Return the value associated with the column: onhand.
	 * @return A Double object (this.onhand)
	 */
	public Double getOnhand() {
		return this.onhand;
		
	}
	

  
    /**  
     * Set the value related to the column: onhand.
	 * @param onhand the onhand value you wish to set
	 */
	public void setOnhand(final Double onhand) {
		this.onhand = onhand;
	}

    /**
     * Return the value associated with the column: partialQtyAllowed.
	 * @return A Boolean object (this.partialQtyAllowed)
	 */
	@Basic( optional = true )
	@Column( name = "partial_qty_allowed"  )
	public Boolean isPartialQtyAllowed() {
		return this.partialQtyAllowed;
		
	}
	

  
    /**  
     * Set the value related to the column: partialQtyAllowed.
	 * @param partialQtyAllowed the partialQtyAllowed value you wish to set
	 */
	public void setPartialQtyAllowed(final Boolean partialQtyAllowed) {
		this.partialQtyAllowed = partialQtyAllowed;
	}

    /**
     * Return the value associated with the column: price.
	 * @return A java.math.BigDecimal object (this.price)
	 */
	public java.math.BigDecimal getPrice() {
		return this.price;
		
	}
	

  
    /**  
     * Set the value related to the column: price.
	 * @param price the price value you wish to set
	 */
	public void setPrice(final java.math.BigDecimal price) {
		this.price = price;
	}

    /**
     * Return the value associated with the column: size.
	 * @return A String object (this.size)
	 */
	@Basic( optional = true )
	@Column( length = 15  )
	public String getSize() {
		return this.size;
		
	}
	

  
    /**  
     * Set the value related to the column: size.
	 * @param size the size value you wish to set
	 */
	public void setSize(final String size) {
		this.size = size;
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
     * Return the value associated with the column: upc.
	 * @return A String object (this.upc)
	 */
	@Basic( optional = true )
	@Column( length = 14  )
	public String getUpc() {
		return this.upc;
		
	}
	

  
    /**  
     * Set the value related to the column: upc.
	 * @param upc the upc value you wish to set
	 */
	public void setUpc(final String upc) {
		this.upc = upc;
	}

    /**
     * Return the value associated with the column: weight.
	 * @return A String object (this.weight)
	 */
	@Basic( optional = true )
	@Column( length = 15  )
	public String getWeight() {
		return this.weight;
		
	}
	

  
    /**  
     * Set the value related to the column: weight.
	 * @param weight the weight value you wish to set
	 */
	public void setWeight(final String weight) {
		this.weight = weight;
	}


   /**
    * Deep copy.
	* @return cloned object
	* @throws CloneNotSupportedException on error
    */
    @Override
    public Inventory clone() throws CloneNotSupportedException {
		
        final Inventory copy = (Inventory)super.clone();

		copy.setAvailable(this.isAvailable());
		copy.setCategory(this.getCategory());
		copy.setCode(this.getCode());
		copy.setCost(this.getCost());
		copy.setCutoff(this.getCutoff());
		copy.setDescription(this.getDescription());
		copy.setId(this.getId());
		if (this.getInventoryHasConnectionss() != null) {
			copy.getInventoryHasConnectionss().addAll(this.getInventoryHasConnectionss());
		}
		if (this.getInventoryImageses() != null) {
			copy.getInventoryImageses().addAll(this.getInventoryImageses());
		}
		copy.setLastReceived(this.getLastReceived());
		copy.setLastSale(this.getLastSale());
		copy.setOnhand(this.getOnhand());
		copy.setPartialQtyAllowed(this.isPartialQtyAllowed());
		copy.setPrice(this.getPrice());
		copy.setSize(this.getSize());
		copy.setTax1(this.isTax1());
		copy.setTax2(this.isTax2());
		copy.setUpc(this.getUpc());
		copy.setWeight(this.getWeight());
		return copy;
	}
	


	/** Provides toString implementation.
	 * @see java.lang.Object#toString()
	 * @return String representation of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("available: " + this.isAvailable() + ", ");
		sb.append("category: " + this.getCategory() + ", ");
		sb.append("code: " + this.getCode() + ", ");
		sb.append("cost: " + this.getCost() + ", ");
		sb.append("cutoff: " + this.getCutoff() + ", ");
		sb.append("description: " + this.getDescription() + ", ");
		sb.append("id: " + this.getId() + ", ");
		sb.append("lastReceived: " + this.getLastReceived() + ", ");
		sb.append("lastSale: " + this.getLastSale() + ", ");
		sb.append("onhand: " + this.getOnhand() + ", ");
		sb.append("partialQtyAllowed: " + this.isPartialQtyAllowed() + ", ");
		sb.append("price: " + this.getPrice() + ", ");
		sb.append("size: " + this.getSize() + ", ");
		sb.append("tax1: " + this.isTax1() + ", ");
		sb.append("tax2: " + this.isTax2() + ", ");
		sb.append("upc: " + this.getUpc() + ", ");
		sb.append("weight: " + this.getWeight());
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
		
		final Inventory that; 
		try {
			that = (Inventory) proxyThat;
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
		result = result && (((isAvailable() == null) && (that.isAvailable() == null)) || (isAvailable() != null && isAvailable().equals(that.isAvailable())));
		result = result && (((getCategory() == null) && (that.getCategory() == null)) || (getCategory() != null && getCategory().equals(that.getCategory())));
		result = result && (((getCode() == null) && (that.getCode() == null)) || (getCode() != null && getCode().equals(that.getCode())));
		result = result && (((getCost() == null) && (that.getCost() == null)) || (getCost() != null && getCost().equals(that.getCost())));
		result = result && (((getCutoff() == null) && (that.getCutoff() == null)) || (getCutoff() != null && getCutoff().equals(that.getCutoff())));
		result = result && (((getDescription() == null) && (that.getDescription() == null)) || (getDescription() != null && getDescription().equals(that.getDescription())));
		result = result && (((getLastReceived() == null) && (that.getLastReceived() == null)) || (getLastReceived() != null && getLastReceived().equals(that.getLastReceived())));
		result = result && (((getLastSale() == null) && (that.getLastSale() == null)) || (getLastSale() != null && getLastSale().equals(that.getLastSale())));
		result = result && (((getOnhand() == null) && (that.getOnhand() == null)) || (getOnhand() != null && getOnhand().equals(that.getOnhand())));
		result = result && (((isPartialQtyAllowed() == null) && (that.isPartialQtyAllowed() == null)) || (isPartialQtyAllowed() != null && isPartialQtyAllowed().equals(that.isPartialQtyAllowed())));
		result = result && (((getPrice() == null) && (that.getPrice() == null)) || (getPrice() != null && getPrice().equals(that.getPrice())));
		result = result && (((getSize() == null) && (that.getSize() == null)) || (getSize() != null && getSize().equals(that.getSize())));
		result = result && (((isTax1() == null) && (that.isTax1() == null)) || (isTax1() != null && isTax1().equals(that.isTax1())));
		result = result && (((isTax2() == null) && (that.isTax2() == null)) || (isTax2() != null && isTax2().equals(that.isTax2())));
		result = result && (((getUpc() == null) && (that.getUpc() == null)) || (getUpc() != null && getUpc().equals(that.getUpc())));
		result = result && (((getWeight() == null) && (that.getWeight() == null)) || (getWeight() != null && getWeight().equals(that.getWeight())));
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