package com.datavirtue.nevitiumpro.data.model.obj;
import com.datavirtue.nevitiumpro.data.model.obj.Projects;
import com.datavirtue.nevitiumpro.data.model.obj.Quotes;
import javax.persistence.Basic;


/** 
 * Object interface mapping for hibernate-handled table: projects_has_quotes.
 * @author autogenerated
 */

public interface IProjectsHasQuotesPK {



    /**
     * Return the value associated with the column: projectsProjects.
	 * @return A Projects object (this.projectsProjects)
	 */
	Projects getProjectsProjects();
	

  
    /**  
     * Set the value related to the column: projectsProjects.
	 * @param projectsProjects the projectsProjects value you wish to set
	 */
	void setProjectsProjects(final Projects projectsProjects);

    /**
     * Return the value associated with the column: quotesQuotes.
	 * @return A Quotes object (this.quotesQuotes)
	 */
	Quotes getQuotesQuotes();
	

  
    /**  
     * Set the value related to the column: quotesQuotes.
	 * @param quotesQuotes the quotesQuotes value you wish to set
	 */
	void setQuotesQuotes(final Quotes quotesQuotes);

	// end of interface
}