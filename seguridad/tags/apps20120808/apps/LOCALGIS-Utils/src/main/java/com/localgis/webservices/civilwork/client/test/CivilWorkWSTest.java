package com.localgis.webservices.civilwork.client.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.axis2.AxisFault;

import com.localgis.webservices.civilwork.client.CivilWorkWSStub;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddIntervention;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.AddNote;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.CancelIntervention;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.ChangeInterventionDate;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteIntervention;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.DeleteNote;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.FinderInterventionConditions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.FinderNoteConditions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredInterventionList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredNextDateInterventionList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteFilteredNoteList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteInterventionList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNexDateInterventionList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetCompleteNoteList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetIntervention;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetInterventionListByConditions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNote;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNoteListByContidions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteInterventionList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteNexDateInterventionList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumCompleteNoteList;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumInterventions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetNumNotes;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.GetStatistics;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.LayerFeatureBean;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISIntervention2;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.LocalGISNote;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyIntervention;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.ModifyNote;

    public class CivilWorkWSTest extends junit.framework.TestCase{

    	public String userName = "syssuperuser";
    	public String password = "sysgeopass";
    	private com.localgis.webservices.civilwork.client.CivilWorkWSStub getStub() throws AxisFault{
    		//ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem("WebContent/WEB-INF", null);
          	//CivilWorkWSStub stub = new CivilWorkWSStub(ctx);
    		CivilWorkWSStub stub = new CivilWorkWSStub();
          	//stub._getServiceClient().engageModule("addressing");
            //stub._getServiceClient().engageModule("rampart");
            //stub._getServiceClient().getOptions().setUserName("syssuperuser");
            //stub._getServiceClient().getOptions().setPassword("sysgeopass");
            return stub;
    	}

        private LayerFeatureBean getLayerFeatureBean(Integer idLayer,Integer idFeature){
        	LayerFeatureBean feature = new LayerFeatureBean();
            feature.setIdLayer(idLayer);
            feature.setIdFeature(idFeature);
        	return feature;
        }

        public  void testaddNote() throws java.lang.Exception{
        	CivilWorkWSStub stub = getStub();
            AddNote actualNote=(AddNote)getTestObject(AddNote.class);
            LocalGISNote note = new LocalGISNote();
            note.setDescription("Descripción de nota 1");           
            note.setFeatureRelation(new LayerFeatureBean[]{getLayerFeatureBean(1,1),getLayerFeatureBean(1,2)});
            note.setIdMunicipio(16078);
            note.setStartWarning(new GregorianCalendar());
            note.setUserCreator(100);
            actualNote.setNote(note);
            actualNote.setUserName(userName);
            actualNote.setPassword(password);
            assertNotNull(stub.addNote(actualNote));
        }
        public  void testaddIntervention() throws java.lang.Exception{

            CivilWorkWSStub stub = getStub();
            AddIntervention actualIntervention = (AddIntervention)getTestObject(AddIntervention.class);
            actualIntervention.setIdMunicipio(16078);
            LocalGISIntervention2 intervention = new LocalGISIntervention2();
            intervention.setIdMunicipio(16078);
            intervention.setActuationType("AE");
            intervention.setAssignedUser(100);
            intervention.setUserCreator(100);
            intervention.setCauses("Causas para actuacion 1");
            intervention.setDescription("Descripción para actuación 1");
            intervention.setStartWarning(new GregorianCalendar());
            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            intervention.setNextWarning(cal);
            intervention.setFeatureRelation(new LayerFeatureBean[]{getLayerFeatureBean(1,3),getLayerFeatureBean(1,4)});
            actualIntervention.setIntervention2(intervention);
            actualIntervention.setUserName(userName);
            actualIntervention.setPassword(password);
            assertNotNull(stub.addIntervention(actualIntervention));
        }
        public  void testgetCompleteNexDateInterventionList() throws java.lang.Exception{

            CivilWorkWSStub stub = getStub();
            GetCompleteNexDateInterventionList interventionList = (GetCompleteNexDateInterventionList)getTestObject(GetCompleteNexDateInterventionList.class);
            interventionList.setIdMunicipio(16078);
            interventionList.setToNext(new GregorianCalendar());
            interventionList.setUserName(userName);
            interventionList.setPassword(password);
            assertNotNull(stub.getCompleteNexDateInterventionList(interventionList));
        }

        
        public  void testcancelIntervention() throws java.lang.Exception{
        
        	CivilWorkWSStub stub = getStub();
        	CancelIntervention noteToCancel = (CancelIntervention)getTestObject(CancelIntervention.class);
        	noteToCancel.setIdWarning(2);
        	noteToCancel.setUserName(userName);
        	noteToCancel.setPassword(password);
        	assertNotNull(stub.cancelIntervention(noteToCancel));

        }

        public  void testgetCompleteInterventionList() throws java.lang.Exception{
        	
        	CivilWorkWSStub stub = getStub();
        	GetCompleteInterventionList interventionList = (GetCompleteInterventionList)getTestObject(GetCompleteInterventionList.class);
        	interventionList.setIdMunicipio(16078);
        	interventionList.setUserName(userName);
        	interventionList.setPassword(password);
            assertNotNull(stub.getCompleteInterventionList(interventionList));

        }

        public  void testgetNoteListByContidions() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetNoteListByContidions noteList = (GetNoteListByContidions)getTestObject(GetNoteListByContidions.class);
        	FinderNoteConditions conditions = new FinderNoteConditions();
        	conditions.setIdMunicipio(16078);
        	conditions.setOrderByColumns("description:asc");
        	noteList.setConditions(conditions);
        	noteList.setUserName(userName);
        	noteList.setPassword(password);
            assertNotNull(stub.getNoteListByContidions(noteList));

        }
        
        public  void testgetCompleteFilteredInterventionList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetCompleteFilteredInterventionList interventionList = (GetCompleteFilteredInterventionList)getTestObject(GetCompleteFilteredInterventionList.class);
        	interventionList.setIdMunicipio(16078);
        	interventionList.setRange(2);
        	interventionList.setStartElement(3);
        	interventionList.setUserName(userName);
        	interventionList.setPassword(password);
            assertNotNull(stub.getCompleteFilteredInterventionList(interventionList));

        }
        
        public  void testgetNumCompleteNexDateInterventionList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetNumCompleteNexDateInterventionList interventionList = (GetNumCompleteNexDateInterventionList)getTestObject(GetNumCompleteNexDateInterventionList.class);
        	interventionList.setIdMunicipio(16078);
        	Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, 1);
        	interventionList.setToNext(cal);
        	interventionList.setUserName(userName);
        	interventionList.setPassword(password);
            assertNotNull(stub.getNumCompleteNexDateInterventionList(interventionList));

        }
        
        public  void testgetInterventionListByConditions() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetInterventionListByConditions interventionList = (GetInterventionListByConditions)getTestObject(GetInterventionListByConditions.class);
        	FinderInterventionConditions param = new FinderInterventionConditions();
        	param.setIdMunicipio(16078);
        	param.setOrderColumns("priority:asc;next_warning:asc");
        	interventionList.setConditions(param);
        	interventionList.setUserName(userName);
        	interventionList.setPassword(password);
        	assertNotNull(stub.getInterventionListByConditions(interventionList));

        }
        
        public  void testchangeInterventionDate() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	ChangeInterventionDate changeIntervention = (ChangeInterventionDate)getTestObject(ChangeInterventionDate.class);
        	changeIntervention.setIdWarning(3);
        	Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, 3);
        	changeIntervention.setNewDate(cal);
        	changeIntervention.setUserName(userName);
        	changeIntervention.setPassword(password);
            assertNotNull(stub.changeInterventionDate(changeIntervention));
                  
        }
        
        public  void testgetNote() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetNote note = (GetNote)getTestObject(GetNote.class);
        	note.setIdMunicipio(16078);
        	note.setIdNote(1);
        	note.setUserName(userName);
        	note.setPassword(password);
            assertNotNull(stub.getNote(note));

        }
        
        public  void testgetNoteList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetNoteList getNoteList120=(GetNoteList)getTestObject(GetNoteList.class);
        	getNoteList120.setIdMunicipio(16078);
        	getNoteList120.setUserName(userName);
        	getNoteList120.setPassword(password);
        	assertNotNull(stub.getNoteList(getNoteList120));

        }

        public  void testgetNumNotes() throws java.lang.Exception{
        	
        	CivilWorkWSStub stub = getStub();
        	GetNumNotes numNotes = (GetNumNotes)getTestObject(GetNumNotes.class);
        	numNotes.setIdMunicipio(16078);
        	numNotes.setUserName(userName);
        	numNotes.setPassword(password);
            assertNotNull(stub.getNumNotes(numNotes));

        }

        public  void testmodifyNote() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	ModifyNote modifyNote124 = (ModifyNote)getTestObject(ModifyNote.class);
        	modifyNote124.setIdMunicipio(16078);
        	LocalGISNote note = new LocalGISNote();
        	note.setId(1);
        	note.setIdMunicipio(16078);
        	note.setDescription("Descripcion cambiada!");
        	Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, -10);
        	note.setStartWarning(cal);
        	note.setUserCreator(100);
        	modifyNote124.setNote(note);
        	modifyNote124.setUserName(userName);
        	modifyNote124.setPassword(password);
        	assertNotNull(stub.modifyNote(modifyNote124));
  
        }
        
        
        /**
         * Auto generated test method
         */
        public  void testgetNumCompleteInterventionList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetNumCompleteInterventionList getNumCompleteInterventionList126=(GetNumCompleteInterventionList)getTestObject(GetNumCompleteInterventionList.class);
        	getNumCompleteInterventionList126.setIdMunicipio(16078);
        	getNumCompleteInterventionList126.setUserName(userName);
        	getNumCompleteInterventionList126.setPassword(password);
            assertNotNull(stub.getNumCompleteInterventionList(getNumCompleteInterventionList126));

        }
        
        public  void testgetNumInterventions() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetNumInterventions getNumInterventions128 = (GetNumInterventions)getTestObject(GetNumInterventions.class);
        	getNumInterventions128.setIdMunicipio(16078);
        	getNumInterventions128.setUserName(userName);
        	getNumInterventions128.setPassword(password);
        	assertNotNull(stub.getNumInterventions(getNumInterventions128));

        }
        
        public  void testdeleteNote() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	DeleteNote deleteNote130 = (DeleteNote)getTestObject(DeleteNote.class);
        	deleteNote130.setIdMunicipio(16078);
        	deleteNote130.setIdNote(1);
        	deleteNote130.setUserName(userName);
        	deleteNote130.setPassword(password);
            assertNotNull(stub.deleteNote(deleteNote130));

        }

        public  void testmodifyIntervention() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	ModifyIntervention modifyIntervention132 = (ModifyIntervention)getTestObject(ModifyIntervention.class);
        	modifyIntervention132.setUserName(userName);
        	modifyIntervention132.setPassword(password);
            assertNotNull(stub.modifyIntervention(modifyIntervention132));

        }
        
        public  void testgetCompleteNoteList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetCompleteNoteList getCompleteNoteList134 = (GetCompleteNoteList)getTestObject(GetCompleteNoteList.class);
        	getCompleteNoteList134.setUserName(userName);
        	getCompleteNoteList134.setPassword(password);
        	assertNotNull(stub.getCompleteNoteList(getCompleteNoteList134));

        }
        
        public  void testgetCompleteFilteredNoteList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetCompleteFilteredNoteList getCompleteFilteredNoteList136 = (GetCompleteFilteredNoteList)getTestObject(GetCompleteFilteredNoteList.class);
        	getCompleteFilteredNoteList136.setUserName(userName);
        	getCompleteFilteredNoteList136.setPassword(password);
        	assertNotNull(stub.getCompleteFilteredNoteList(getCompleteFilteredNoteList136));

        }
 
        public  void testgetCompleteFilteredNextDateInterventionList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetCompleteFilteredNextDateInterventionList getCompleteFilteredNextDateInterventionList138 = (GetCompleteFilteredNextDateInterventionList)getTestObject(GetCompleteFilteredNextDateInterventionList.class);
        	getCompleteFilteredNextDateInterventionList138.setUserName(userName);
        	getCompleteFilteredNextDateInterventionList138.setPassword(password);
        	assertNotNull(stub.getCompleteFilteredNextDateInterventionList(getCompleteFilteredNextDateInterventionList138));

        }
        
        public  void testgetInterventionList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetInterventionList getInterventionList140 = (GetInterventionList)getTestObject(GetInterventionList.class);
        	getInterventionList140.setUserName(userName);
        	getInterventionList140.setPassword(password);
        	assertNotNull(stub.getInterventionList(getInterventionList140));

        }
        
        public  void testgetStatistics() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetStatistics getStatistics144 = (GetStatistics)getTestObject(GetStatistics.class);
        	getStatistics144.setUserName(userName);
        	getStatistics144.setPassword(password);
        	assertNotNull(stub.getStatistics(getStatistics144));

        }
        
        public  void testgetNumCompleteNoteList() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetNumCompleteNoteList getNumCompleteNoteList150 = (GetNumCompleteNoteList)getTestObject(GetNumCompleteNoteList.class);
        	getNumCompleteNoteList150.setIdMunicipio(16078);
        	getNumCompleteNoteList150.setUserName(userName);
        	getNumCompleteNoteList150.setPassword(password);
        	assertNotNull(stub.getNumCompleteNoteList(getNumCompleteNoteList150));
            
        }
        
        public  void testdeleteIntervention() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	DeleteIntervention deleteIntervention152 = (DeleteIntervention)getTestObject(DeleteIntervention.class);
        	deleteIntervention152.setIdIntervention(3);
        	deleteIntervention152.setUserName(userName);
        	deleteIntervention152.setPassword(password);
        	assertNotNull(stub.deleteIntervention(deleteIntervention152));

        }
        
        public  void testgetIntervention() throws java.lang.Exception{

        	CivilWorkWSStub stub = getStub();
        	GetIntervention getIntervention154 = (GetIntervention)getTestObject(GetIntervention.class);
        	getIntervention154.setIdIntervention(3);
        	getIntervention154.setIdMunicipio(16078);
        	getIntervention154.setUserName(userName);
        	getIntervention154.setPassword(password);
        	assertNotNull(stub.getIntervention(getIntervention154));

        }

        @SuppressWarnings("unchecked")
		public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
        }

        
        

    }
    