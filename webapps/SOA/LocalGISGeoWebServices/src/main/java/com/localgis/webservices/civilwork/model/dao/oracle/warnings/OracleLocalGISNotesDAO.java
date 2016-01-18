/**
 * OracleLocalGISNotesDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao.oracle.warnings;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISNotesDAO;

public class OracleLocalGISNotesDAO extends LocalGISNotesDAO{
	
	
	@Override
	public void addNoteDocumentThumbnail(Connection connection,
			Document document) throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		pDAO.addWarningDocumentThumbnail(connection, document);
	}

	@Override
	public void addNoteDocument(Connection connection, Integer idWarning,
			Document document) throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		pDAO.addWarningDocument(connection, idWarning, document);
	}
	@Override
	public Document getNoteDocument(Connection connection,
			Integer idDocument, Boolean returnCompleteDocuments)
			throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		return pDAO.getWarningDocument(connection, idDocument,returnCompleteDocuments);
	}

	@Override
	public ArrayList<Document> getNoteDocuments(Connection connection,
			Integer idWarning, Boolean returnCompleteDocuments)
			throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		return pDAO.getWarningDocuments(connection, idWarning,returnCompleteDocuments);
	}
//	@Override
//	public ArrayList<LocalGISNote> getNoteList(Connection connection,
//			String description, Calendar from, Calendar to,
//			Integer startElement, Integer range, Integer idMunicipio,
//			Integer userId, ArrayList<NoteColumn> orderColumns,
//			ArrayList<LayerFeatureBean> features) throws SQLException {
////		throw new NotImplementedException();
//		
//	}
	@Override
	public Integer getNextIdDocumentNoteSequence(Connection connection)
			throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		return pDAO.getNextIdDocumentSequence(connection);
	}

	@Override
	public Integer getNextIdNoteSequence(Connection connection)
			throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		return pDAO.getNextIdWarningSequence(connection);
	}
}
