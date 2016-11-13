package com.datavirtue.nevitiumpro.data.model.dao.impl;

import com.felees.hbnpojogen.persistence.impl.GenericHibernateDaoImpl;
import com.datavirtue.nevitiumpro.data.model.obj.ProjectsHasConnections;
import org.springframework.stereotype.Repository;
import com.datavirtue.nevitiumpro.data.model.dao.ProjectsHasConnectionsDao;
import com.datavirtue.nevitiumpro.data.model.obj.ProjectsHasConnectionsPK;
 
/**
 * DAO for table: ProjectsHasConnections.
 * @author autogenerated
 */
@Repository
public class ProjectsHasConnectionsDaoImpl extends GenericHibernateDaoImpl<ProjectsHasConnections, ProjectsHasConnectionsPK> implements ProjectsHasConnectionsDao {
	
	/** Constructor method. */
		public ProjectsHasConnectionsDaoImpl() {
			super(ProjectsHasConnections.class);
		}
	}
