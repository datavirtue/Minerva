package com.datavirtue.nevitiumpro.data.model.obj;

import com.datavirtue.nevitiumpro.data.model.obj.IInventoryImages;
import com.felees.hbnpojogen.persistence.IPojoGenEntity;
import java.io.Serializable;
import java.util.Arrays;
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
 * Object mapping for hibernate-handled table: inventory_images.
 * @author autogenerated
 */

@Entity
@Table(name = "inventory_images", catalog = "NevitiumPro")
public class InventoryImages implements Cloneable, Serializable, IPojoGenEntity, IInventoryImages {

	/** Serial Version UID. */
	private static final long serialVersionUID = -559029499L;

	/** Use a WeakHashMap so entries will be garbage collected once all entities 
		referring to a saved hash are garbage collected themselves. */
	private static final Map<Serializable, Integer> SAVED_HASHES =
		Collections.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	
	/** hashCode temporary storage. */
	private volatile Integer hashCode;
	

	/** Field mapping. */
	private Byte[] bitmap;
	/** Field mapping. */
	private Long date;
	/** Field mapping. */
	private Integer id = 0; // init for hibernate bug workaround
	/** Field mapping. */
	private String imageFormat;
	/** Field mapping. */
	private String imageUrl;
	/** Field mapping. */
	private Inventory inventoryInventory;
	/** Field mapping. */
	private String title;
	/**
	 * Default constructor, mainly for hibernate use.
	 */
	public InventoryImages() {
		// Default constructor
	} 

	/** Constructor taking a given ID.
	 * @param id to set
	 */
	public InventoryImages(Integer id) {
		this.id = id;
	}
	
	/** Constructor taking a given ID.
	 * @param id Integer object;
	 * @param inventoryInventory Inventory object;
	 */
	public InventoryImages(Integer id, Inventory inventoryInventory) {

		this.id = id;
		this.inventoryInventory = inventoryInventory;
	}
	
 


 
	/** Return the type of this class. Useful for when dealing with proxies.
	* @return Defining class.
	*/
	@Transient
	public Class<?> getClassType() {
		return InventoryImages.class;
	}
 

    /**
     * Return the value associated with the column: bitmap.
	 * @return A Byte[] object (this.bitmap)
	 */
	public Byte[] getBitmap() {
		return this.bitmap;
		
	}
	

  
    /**  
     * Set the value related to the column: bitmap.
	 * @param bitmap the bitmap value you wish to set
	 */
	public void setBitmap(final Byte[] bitmap) {
		this.bitmap = bitmap;
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
     * Return the value associated with the column: id.
	 * @return A Integer object (this.id)
	 */
    @Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic( optional = false )
	@Column( name = "inventory_images_id", nullable = false  )
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
     * Return the value associated with the column: imageFormat.
	 * @return A String object (this.imageFormat)
	 */
	@Basic( optional = true )
	@Column( name = "image_format", length = 3  )
	public String getImageFormat() {
		return this.imageFormat;
		
	}
	

  
    /**  
     * Set the value related to the column: imageFormat.
	 * @param imageFormat the imageFormat value you wish to set
	 */
	public void setImageFormat(final String imageFormat) {
		this.imageFormat = imageFormat;
	}

    /**
     * Return the value associated with the column: imageUrl.
	 * @return A String object (this.imageUrl)
	 */
	@Basic( optional = true )
	@Column( name = "image_url", length = 80  )
	public String getImageUrl() {
		return this.imageUrl;
		
	}
	

  
    /**  
     * Set the value related to the column: imageUrl.
	 * @param imageUrl the imageUrl value you wish to set
	 */
	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

    /**
     * Return the value associated with the column: inventoryInventory.
	 * @return A Inventory object (this.inventoryInventory)
	 */
	@ManyToOne( cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY )
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@JoinColumn(name = "INVENTORY_INVENTORY_ID", nullable = false )
	public Inventory getInventoryInventory() {
		return this.inventoryInventory;
		
	}
	

  
    /**  
     * Set the value related to the column: inventoryInventory.
	 * @param inventoryInventory the inventoryInventory value you wish to set
	 */
	public void setInventoryInventory(final Inventory inventoryInventory) {
		this.inventoryInventory = inventoryInventory;
	}

    /**
     * Return the value associated with the column: title.
	 * @return A String object (this.title)
	 */
	@Basic( optional = true )
	@Column( length = 128  )
	public String getTitle() {
		return this.title;
		
	}
	

  
    /**  
     * Set the value related to the column: title.
	 * @param title the title value you wish to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}


   /**
    * Deep copy.
	* @return cloned object
	* @throws CloneNotSupportedException on error
    */
    @Override
    public InventoryImages clone() throws CloneNotSupportedException {
		
        final InventoryImages copy = (InventoryImages)super.clone();

		copy.setBitmap(this.getBitmap());
		copy.setDate(this.getDate());
		copy.setId(this.getId());
		copy.setImageFormat(this.getImageFormat());
		copy.setImageUrl(this.getImageUrl());
		copy.setInventoryInventory(this.getInventoryInventory());
		copy.setTitle(this.getTitle());
		return copy;
	}
	


	/** Provides toString implementation.
	 * @see java.lang.Object#toString()
	 * @return String representation of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("bitmap: " + (this.bitmap == null ? null : Arrays.toString(this.getBitmap())) + ", ");
		sb.append("date: " + this.getDate() + ", ");
		sb.append("id: " + this.getId() + ", ");
		sb.append("imageFormat: " + this.getImageFormat() + ", ");
		sb.append("imageUrl: " + this.getImageUrl() + ", ");
		sb.append("title: " + this.getTitle());
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
		
		final InventoryImages that; 
		try {
			that = (InventoryImages) proxyThat;
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
		result = result && (((getBitmap() == null) && (that.getBitmap() == null)) || (getBitmap() != null && getBitmap().equals(that.getBitmap())));
		result = result && (((getDate() == null) && (that.getDate() == null)) || (getDate() != null && getDate().equals(that.getDate())));
		result = result && (((getImageFormat() == null) && (that.getImageFormat() == null)) || (getImageFormat() != null && getImageFormat().equals(that.getImageFormat())));
		result = result && (((getImageUrl() == null) && (that.getImageUrl() == null)) || (getImageUrl() != null && getImageUrl().equals(that.getImageUrl())));
		result = result && (((getInventoryInventory() == null) && (that.getInventoryInventory() == null)) || (getInventoryInventory() != null && getInventoryInventory().getId().equals(that.getInventoryInventory().getId())));	
		result = result && (((getTitle() == null) && (that.getTitle() == null)) || (getTitle() != null && getTitle().equals(that.getTitle())));
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
