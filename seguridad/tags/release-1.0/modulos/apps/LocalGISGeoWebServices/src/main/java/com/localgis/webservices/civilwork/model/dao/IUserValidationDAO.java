package com.localgis.webservices.civilwork.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.localgis.webservices.civilwork.util.LocalGISUser;


public interface IUserValidationDAO
{
	
	public Integer getIdAclNotes(Connection connection) throws SQLException;
	
	public Integer getIdPermFromName(Connection connection,String description) throws SQLException;
	
	public boolean login(Connection connection,Integer userId) throws SQLException;
	
	public boolean administration(Connection connection,Integer userId) throws SQLException;
	
	public boolean notesQuery(Connection connection,Integer userId) throws SQLException;
	
	public boolean notesNew(Connection connection,Integer userId) throws SQLException;
	
	public boolean notesDelete(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsQuery(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsNew(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsModify(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsPostpone(Connection connection,Integer userId) throws SQLException;
	
	public boolean warningsDiscard(Connection connection,Integer userId) throws SQLException;

	public boolean warningsDelete(Connection connection,Integer userId) throws SQLException;
	
	public ArrayList<LocalGISUser> getAdminUserList(Connection connection,Integer idMunicipio) throws SQLException;
	
	public ArrayList<LocalGISUser> getUser(Connection connection,Integer idMunicipio,Integer userId) throws SQLException;
	
}


