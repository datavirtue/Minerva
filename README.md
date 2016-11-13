**********************************************
*           Nevitium Professional            *
*              Project Minerva               *
*                                            *
*          High-Level Documentation          * 
*                 July 2013                  *
*                                            *
*       Data Virtue - Sean K Anderson        * 
*                                            *
**********************************************

This ReadMe details the environment setup and 
configuration related to building and 
maintaining Project Minerva.

Topics:
A. Folder map
1. Nevitium Pro Netbeans project
2. Hibernate Version
3. Hibernate Setup
4. MySQL data modelling
5. BuildHib
6. Automation scripts
7. Backups

A. Folder Map
prj\Minerva\
			|BuildHib <--hibernate.cfg.xml automation tool
			|MySQL Datafiles 
			|MySQL50 <--MySQL Database Engine
			|Nevitium <--old 1.5
			|NevitiumPro\ <--new version with Hibernate
			.			|.hg <--Mercurial tracking data
			.			|MSW Data Model <--MySQL WrkBnch data files
			.			|hib2pojo  <-- BuildHib scripts 
			.			|...[standard Netbeans project folders]
			.			
			|TestPlugin <--Customer ID generator test plugin project
			|ReadMe.txt <--This file
		


1. Nevitium Pro Netbeans project
The Minerva project is based around the Netbeans NevitiumPro project 
folder.  This holds all of the main application code as managed by 
the Netbeans IDE.

2. Hibernate Version
The current version of Hibernate used is 3.2.5 which ships with
Netbeans 7.1/7.2

3. Hibernate Setup
Instructions for generating the entity classes is provided in another
document found in the NevitiumPro project: 
EntityBuildClassOptionDocumentation in com.datavirtue.nevitiumpro.
hibernate folder

There exist scripts which make use of BuildHib.jar to run through
the entity folder in the NevitiumPro project to build the 
hibernate.cfg.xml file.  Instead of maintining all of the entity 
mappings manually I chose to store them in a standard folder and 
automate the configuration. See BuildHib.

4. MySQL data modelling
MySQL Workbench is used to create the application's data model.  MSW
allows you to create tables in a GUI, link up the tables in a GUI
ERM tool, and subsequently update a database on a MySQL server.

Netbeans connects to the MySQL database and generates the entity
classes automatically.  After generating the entities you can run the 
scripts to build the hibernate.cfg.xml mapping file.

5. BuildHib
The BuildHib project consists of a single java file designed to 
build a hibernate.cfg.xml file from all of the .class files in a 
specified folder (entities) with output sent to a specified location.

6. Automation scripts
Under prj\Minerva\NevitiumPro\hib2pojo is a buildhibmappings.bat
that will execute BuildHib.jar and supply the nessesary path locations.

Under C:\1DATA\prj\Minerva\NevitiumPro\hib2pojo\OLD\ is a set of
scripts and resources that I was using before utilizing Netbeans 
automatic entity generation tool.

7. Backups
Under prj\ is a backup_prj.bat file which performs forms a unique
prj.zip filename based on the date/time.  7Zip is used to create a 
single backup file with this name at a hardcoded location.

