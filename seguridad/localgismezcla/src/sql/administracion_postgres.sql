CREATE TABLE ACL(IDACL  Numeric(10)  NOT NULL,  NAME   VARCHAR(50));

CREATE TABLE APPGEOPISTA(APPID   Numeric(10) NOT NULL, DEF     VARCHAR(255), PARAM1  VARCHAR(64),  PARAM2  VARCHAR(64));


CREATE TABLE DICTIONARY(ID_VOCABLO  Numeric(10)    NOT NULL, LOCALE      VARCHAR(8)  NOT NULL,TRADUCCION  VARCHAR(255));

CREATE TABLE DOMAINCATEGORY(ID Numeric(10) not null,
		            ID_DESCRIPTION Numeric(7) not null);
ALTER TABLE DOMAINCATEGORY ADD PRIMARY KEY (ID);
ALTER TABLE DOMAINCATEGORY ADD FOREIGN KEY (ID_DESCRIPTION) REFERENCES
                           DICTIONARY(ID_VOCABLO);

CREATE TABLE DOMAINS(ID    Numeric(10) NOT NULL, 
                     NAME  VARCHAR(50)        NOT NULL,
                     ID_CATEGORY Numeric (10));
ALTER TABLE DOMAINS ADD FOREIGN KEY (ID_CATEGORY) REFERENCES
		DOMAINCATEGORY (ID);



CREATE TABLE IUSERGROUPHDR
(
  ID            NUMERIC(10)                      NOT NULL,
  NAME          VARCHAR(32)               NOT NULL,
  MGRID         NUMERIC(10)                      NOT NULL,
  TYPE          NUMERIC(10)                      NOT NULL,
  REMARKS       VARCHAR(254),
  CRTRID        NUMERIC(10)                      NOT NULL,
  CRTNDATE      TIMESTAMP                        NOT NULL,
  UPDRID        NUMERIC(10),
  UPDDATE       TIMESTAMP,
  ID_MUNICIPIO  NUMERIC(5)
)
;


CREATE TABLE IUSERUSERHDR
(
  ID              NUMERIC(10)                    NOT NULL,
  NAME            VARCHAR(32)             NOT NULL,
  NOMBRECOMPLETO  VARCHAR(255),
  PASSWORD        VARCHAR(68)             NOT NULL,
  FLAGS           NUMERIC(10)                    NOT NULL,
  STAT            NUMERIC(10)                    NOT NULL,
  NUMBADCNTS      NUMERIC(10)                    NOT NULL,
  REMARKS         VARCHAR(254),
  CRTRID          NUMERIC(14)                    NOT NULL,
  CRTNDATE        TIMESTAMP                          NOT NULL,
  UPDRID          NUMERIC(14),
  UPDDATE         TIMESTAMP,
  MAIL            VARCHAR(64),
  DEPTID          NUMERIC(10),
  BORRADO         NUMERIC(1)                     DEFAULT 0,
  ID_MUNICIPIO    NUMERIC(5)
);


CREATE TABLE QUERY_CATALOG
(
  ID     VARCHAR(64),
  QUERY  VARCHAR(4000)
);


CREATE TABLE USRGROUPERM
(
  IDPERM  NUMERIC(10)                            NOT NULL,
  DEF     VARCHAR(255),
  TYPE    VARCHAR(32)
)
;




CREATE TABLE domainnodes
(
  id numeric(10) NOT NULL,
  id_domain numeric(10) NOT NULL,
  pattern varchar(100),
  id_description numeric(7),
  type numeric(1),
  permissionlevel numeric(1),
  parentdomain numeric(10),
  id_municipio numeric(5),
  CONSTRAINT domainnodes_pkey PRIMARY KEY (id),
  CONSTRAINT "$1" FOREIGN KEY (id_domain) REFERENCES domains (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "$2" FOREIGN KEY (parentdomain) REFERENCES domainnodes (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_domain FOREIGN KEY (id_domain) REFERENCES domains (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH OIDS;



CREATE TABLE IUSERCNT
(
  ID         VARCHAR(20)                  NOT NULL, 
  USERID     NUMERIC(10)                         NOT NULL,
  APPID      NUMERIC(10)                         NOT NULL,
  TIMESTAMP  TIMESTAMP                               NOT NULL,
  TIMECLOSE  TIMESTAMP
  
);


CREATE TABLE IUSERGROUPUSER
(
  GROUPID  NUMERIC(10)                           NOT NULL,
  USERID   NUMERIC(10)                           NOT NULL
);
CREATE TABLE SEQUENCES
(
  ID_SEQUENCE     NUMERIC(8)           DEFAULT '0'                   NOT NULL,
  TABLENAME       VARCHAR(250)            DEFAULT ' '                   NOT NULL,
  FIELD           VARCHAR(250)            DEFAULT ' '                   NOT NULL,
  VALUE           NUMERIC(10)                    DEFAULT '0',
  INCREMENTVALUE  NUMERIC(10)                    DEFAULT '0'                   NOT NULL,
  MINIMUMVALUE    NUMERIC(10)                    DEFAULT '0'                   NOT NULL,
  MAXIMUMVALUE    NUMERIC(10)                    DEFAULT '0'                   NOT NULL,
  CIRCULAR        CHAR(1)                  DEFAULT ' '                   NOT NULL
);

CREATE TABLE R_ACL_PERM
(
  IDPERM  NUMERIC(10)                            NOT NULL,
  IDACL   NUMERIC(10)                            NOT NULL
)
;


CREATE TABLE R_GROUP_PERM
(
  GROUPID  NUMERIC(10)                           NOT NULL,
  IDPERM   NUMERIC(10)                           NOT NULL,
  IDACL    NUMERIC(10)                           NOT NULL
);


CREATE TABLE R_USR_PERM
(
  USERID  NUMERIC(10)                            NOT NULL,
  IDPERM  NUMERIC(10)                            NOT NULL,
  IDACL   NUMERIC(10)                            NOT NULL,
  APLICA  NUMERIC(1)
)
;


ALTER TABLE ACL ADD CONSTRAINT PK_ACL PRIMARY KEY (IDACL);


ALTER TABLE APPGEOPISTA ADD CONSTRAINT PK_APPGEOPISTA PRIMARY KEY (APPID);


ALTER TABLE DICTIONARY ADD CONSTRAINT PK_DICTIONARY PRIMARY KEY (ID_VOCABLO, LOCALE);


ALTER TABLE DOMAINS ADD CONSTRAINT PK_DOMAIN PRIMARY KEY (ID);


ALTER TABLE IUSERGROUPHDR ADD CONSTRAINT IUSERGROUPHPR_PK PRIMARY KEY (ID);


ALTER TABLE IUSERUSERHDR ADD  CONSTRAINT PK_IUSERUSERHDR PRIMARY KEY (ID);


ALTER TABLE QUERY_CATALOG ADD  PRIMARY KEY (ID);


ALTER TABLE USRGROUPERM ADD CONSTRAINT PK_USRGROUPERM PRIMARY KEY (IDPERM);


ALTER TABLE DOMAINNODES ADD CONSTRAINT PK_DOMAINNODE PRIMARY KEY (ID);


ALTER TABLE IUSERCNT ADD CONSTRAINT PK_IUSERCNT PRIMARY KEY (ID);


ALTER TABLE IUSERGROUPUSER ADD CONSTRAINT AK_IUSERGROUPHPR_PK_IUSERGRO UNIQUE (USERID, GROUPID);

ALTER TABLE SEQUENCES ADD 
  PRIMARY KEY (ID_SEQUENCE);


ALTER TABLE SEQUENCES ADD 
  UNIQUE (TABLENAME, FIELD);

ALTER TABLE R_ACL_PERM ADD CONSTRAINT PK_R_ACL_PERM PRIMARY KEY (IDACL, IDPERM);


ALTER TABLE R_GROUP_PERM ADD 
  CONSTRAINT AK_KEY_1_R_GROUP_ UNIQUE (GROUPID, IDPERM, IDACL);


ALTER TABLE R_USR_PERM ADD 
  CONSTRAINT AK_KEY_1_R_USR_PE UNIQUE (USERID, IDPERM, IDACL);



ALTER TABLE DOMAINNODES ADD 
  CONSTRAINT FK_DOMAIN FOREIGN KEY (ID_DOMAIN) 
    REFERENCES DOMAINS (ID);


ALTER TABLE IUSERCNT ADD 
  CONSTRAINT FK_IUSERCNT_REFERENCE_APPGEOPI FOREIGN KEY (APPID) 
    REFERENCES APPGEOPISTA (APPID);

ALTER TABLE IUSERCNT ADD 
  CONSTRAINT FK_IUSERCNT_REFERENCE_IUSERUSE FOREIGN KEY (USERID) 
    REFERENCES IUSERUSERHDR (ID);


ALTER TABLE IUSERGROUPUSER ADD 
  CONSTRAINT FK_IUSERGRO_REFERENCE_IUSERUSE FOREIGN KEY (USERID) 
    REFERENCES IUSERUSERHDR (ID);

ALTER TABLE IUSERGROUPUSER ADD 
  CONSTRAINT FK_IUSERGROUPUSER FOREIGN KEY (GROUPID) 
    REFERENCES IUSERGROUPHDR (ID);


ALTER TABLE R_ACL_PERM ADD 
  CONSTRAINT FK_R_ACL_PE_REFERENCE_ACL FOREIGN KEY (IDACL) 
    REFERENCES ACL (IDACL);

ALTER TABLE R_ACL_PERM ADD 
  CONSTRAINT FK_R_ACL_PE_REFERENCE_USRGROUP FOREIGN KEY (IDPERM) 
    REFERENCES USRGROUPERM (IDPERM);


ALTER TABLE R_GROUP_PERM ADD 
  CONSTRAINT FK_R_GROUP__REFERENCE_IUSERGRO FOREIGN KEY (GROUPID) 
    REFERENCES IUSERGROUPHDR (ID);

ALTER TABLE R_GROUP_PERM ADD 
  CONSTRAINT FK_R_GROUP__REFERENCE_USRGROUP FOREIGN KEY (IDACL, IDPERM) 
    REFERENCES R_ACL_PERM (IDACL,IDPERM);


ALTER TABLE R_USR_PERM ADD 
  CONSTRAINT FK_R_USR_PE_REFERENCE_IUSERUSE FOREIGN KEY (USERID) 
    REFERENCES IUSERUSERHDR (ID);

ALTER TABLE R_USR_PERM ADD CONSTRAINT FK_R_USR_PE_REFERENCE_USRGROUP FOREIGN KEY (IDACL, IDPERM) 
    REFERENCES R_ACL_PERM (IDACL,IDPERM);



CREATE INDEX INDX_NAME_DOMAIN ON DOMAINS
(NAME);


CREATE INDEX IUSERGROUPHDR2 ON IUSERGROUPHDR
(NAME);


CREATE INDEX IUSERGROUPHDR3 ON IUSERGROUPHDR
(MGRID);


CREATE INDEX IUSERGROUPHDR4 ON IUSERGROUPHDR
(TYPE);


CREATE UNIQUE INDEX IUSERGROUPHPR_PK ON IUSERGROUPHDR
(ID);


CREATE UNIQUE INDEX IUSERUSERHDR2 ON IUSERUSERHDR
(NAME);


CREATE UNIQUE INDEX PK_ACL ON ACL
(IDACL);


CREATE UNIQUE INDEX PK_APPGEOPISTA ON APPGEOPISTA
(APPID);


CREATE UNIQUE INDEX PK_DICTIONARY ON DICTIONARY
(ID_VOCABLO, LOCALE);


CREATE UNIQUE INDEX PK_DOMAIN ON DOMAINS
(ID);


CREATE UNIQUE INDEX PK_IUSERUSERHDR ON IUSERUSERHDR
(ID);


CREATE UNIQUE INDEX PK_USRGROUPERM ON USRGROUPERM
(IDPERM);

CREATE UNIQUE INDEX AK_IUSERGROUPHPR_PK_IUSERGRO ON IUSERGROUPUSER
(USERID, GROUPID);


CREATE UNIQUE INDEX INDEx_1_R_GROUP_ ON R_GROUP_PERM
(GROUPID, IDPERM, IDACL)
;


CREATE UNIQUE INDEX INDX_KEY_1_R_USR_PE ON R_USR_PERM
(USERID, IDPERM, IDACL)
;


CREATE INDEX IUSERGROUPUSER1 ON IUSERGROUPUSER
(GROUPID)
;


CREATE UNIQUE INDEX PK_DOMAINNODE ON DOMAINNODES
(ID)
;


CREATE UNIQUE INDEX PK_IUSERCNT ON IUSERCNT
(ID)
;


CREATE UNIQUE INDEX PK_R_ACL_PERM ON R_ACL_PERM
(IDACL, IDPERM)
;



INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'catalog', 'select * from query_catalog'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'insert', 'insert into query_catalog (id,query) values (?,?)'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'allroles', 'select iusergrouphdr.id as id, iusergrouphdr.name as name,  iusergrouphdr.remarks as remarks from iusergrouphdr where id_municipio=?'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'allpermisos', 'select idperm, def, type from UsrGrouPerm'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'allrolespermisos', 'select iusergrouphdr.id as id, iusergrouphdr.name as name, iusergrouphdr.remarks as remarks, r_group_perm.idperm as idperm, r_group_perm.idacl as idacl from iusergrouphdr,r_group_perm where iusergrouphdr.id_municipio=? and iusergrouphdr.id=r_group_perm.groupid order by iusergrouphdr.id'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'allaclspermisos', 'select acl.idacl as idacl, acl.name as name, usrgrouperm.def as def, r_acl_perm.idperm as idperm from acl, r_acl_perm, usrgrouperm where acl.idacl=r_acl_perm.idacl and usrgrouperm.idperm=r_acl_perm.idperm order by idacl'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'allusuarios', 'Select id, name, nombrecompleto, password, remarks,mail, deptid FROM IUSERUSERHDR where borrado!=1 and id_municipio=?'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'allusuariospermisos', 'select r_usr_perm.userid as userid , r_usr_perm.idperm as idperm, r_usr_perm.idacl as idacl, r_usr_perm.aplica as aplica from r_usr_perm order by userid'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'allusuariosroles', 'select iusergroupuser.userid as userid , iusergroupuser.groupid as groupid  from iusergroupuser order by userid'); 
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'alldominionodes', 'select domainnodes.id_domain as id_domain, domainnodes.id as id_node, domainnodes.pattern as pattern,domainnodes.ID_DESCRIPTION as id_descripcion, domainnodes.type as type, domainnodes.PARENTDOMAIN as parentdomain,dictionary.locale as locale, dictionary.traduccion as traduccion ,domainnodes.id_municipio   from  domainnodes LEFT OUTER JOIN dictionary ON (domainnodes.id_description=dictionary.id_vocablo) where domainnodes.id_municipio=? or domainnodes.id_municipio is null order by id_domain, id_node');
INSERT INTO QUERY_CATALOG ( ID, QUERY ) VALUES ( 
'alldominios', 'select domains.id as id_domain, domains.name as name, domains.id_category from domains order by id_category, id_domain'); 
INSERT INTO QUERY_CATALOG(ID,QUERY) VALUES ('getestructuramunicipio','select domains.id as id_domain,domainnodes.id as id_node, domainnodes.pattern as pattern, dictionary.locale as locale, 
dictionary.id_vocablo as id_descripcion, dictionary.traduccion 
as traduccion from domains,domainnodes,dictionary where upper(domains.name)=upper(?) and domainnodes.type=? and domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo
and domainnodes.id_municipio =? order by domainnodes.id');
INSERT INTO QUERY_CATALOG(ID,QUERY) VALUES ('getestructura','select domains.id as id_domain ,domainnodes.id as id_node, domainnodes.pattern as pattern, dictionary.locale as locale, 
dictionary.id_vocablo as id_descripcion, dictionary.traduccion 
as traduccion 
from domains,domainnodes,dictionary where upper(domains.name)=upper(?) and domainnodes.type=? and domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo
and domainnodes.id_municipio is null order by domainnodes.id');

insert into query_catalog (id, query) values ('getcategorias','select domaincategory.id as id ,domaincategory.id_description as iddes,dictionary.locale, dictionary.traduccion
                         from domaincategory,dictionary where  domaincategory.id_description= dictionary.id_vocablo
                         order by domaincategory.id');
commit;

INSERT INTO APPGEOPISTA(APPID, DEF) VALUES (1,'Licencias');
INSERT INTO APPGEOPISTA(APPID, DEF) VALUES (2,'Metadatos');
INSERT INTO APPGEOPISTA(APPID, DEF) VALUES (3,'Administracion');

INSERT INTO SEQUENCES ( ID_SEQUENCE, TABLENAME, FIELD, VALUE, INCREMENTVALUE, MINIMUMVALUE,
MAXIMUMVALUE, CIRCULAR ) VALUES ( 
1, 'DOMAINS', 'ID', 100, 1, 1, 999999999, 'T'); 
INSERT INTO SEQUENCES ( ID_SEQUENCE, TABLENAME, FIELD, VALUE, INCREMENTVALUE, MINIMUMVALUE,
MAXIMUMVALUE, CIRCULAR ) VALUES ( 
2, 'DOMAINNODES', 'ID', 101, 1, 1, 999999999, 'T'); 
INSERT INTO SEQUENCES ( ID_SEQUENCE, TABLENAME, FIELD, VALUE, INCREMENTVALUE, MINIMUMVALUE,
MAXIMUMVALUE, CIRCULAR ) VALUES ( 
3, 'IUSERUSERHDR', 'ID', 101, 1, 1, 999999999, 'T'); 
INSERT INTO SEQUENCES ( ID_SEQUENCE, TABLENAME, FIELD, VALUE, INCREMENTVALUE, MINIMUMVALUE,
MAXIMUMVALUE, CIRCULAR ) VALUES ( 
4, 'IUSERGROUPHDR', 'ID', 101, 1, 1, 999999999, 'T'); 
INSERT INTO SEQUENCES ( ID_SEQUENCE, TABLENAME, FIELD, VALUE, INCREMENTVALUE, MINIMUMVALUE,
MAXIMUMVALUE, CIRCULAR ) VALUES ( 
5, 'DICTIONARY', 'ID_VOCABLO', 101, 1, 1, 999999999, 'T'); 

commit;


INSERT INTO ACL ( IDACL, NAME ) VALUES ( 
1, 'Administracion'); 
INSERT INTO ACL ( IDACL, NAME ) VALUES ( 
2, 'Licencias'); 
commit;


INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
7, 'Geopista.Licencias.View', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
201, 'Geopista.Licencias.Consulta', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
202, 'Geopista.Licencias.Creacion', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
203, 'Geopista.Licencias.Modificacion', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
204, 'Geopista.Licencias.Informes', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
205, 'Geopista.Licencias.Planos', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
206, 'Geopista.Licencias.Historico', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
207, 'Geopista.Licencias.Idiomas', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
10, 'Geopista.Administracion.Login', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
11, 'Geopista.Administracion.Edit', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
12, 'Geopista.Administracion.View', NULL); 
INSERT INTO USRGROUPERM ( IDPERM, DEF, TYPE ) VALUES ( 
200, 'Geopista.Licencias.Login', NULL); 
commit;

INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
10, 1); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
11, 1); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
12, 1); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
204, 2); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
205, 2); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
7, 2); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
201, 2); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
202, 2); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
203, 2); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
200, 2); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
206, 2); 
INSERT INTO R_ACL_PERM ( IDPERM, IDACL ) VALUES ( 
207, 2); 
commit;
 
INSERT INTO IUSERGROUPHDR ( ID, NAME, MGRID, TYPE, REMARKS, CRTRID, CRTNDATE, UPDRID, UPDDATE,
ID_MUNICIPIO ) VALUES ( 
1, 'Licencias', 0, 0, 'Este departamento es muy bonito', 0,  TO_Date( '04/13/1973 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1,  TO_Date( '06/02/2004 08:31:07 PM', 'MM/DD/YYYY HH:MI:SS AM'), 1); 
INSERT INTO IUSERGROUPHDR ( ID, NAME, MGRID, TYPE, REMARKS, CRTRID, CRTNDATE, UPDRID, UPDDATE,
ID_MUNICIPIO ) VALUES ( 
2, 'Administracion', 0, 0, 'rol para administracion', 0,  TO_Date( '04/13/1973 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, 1,  TO_Date( '06/02/2004 08:31:42 PM', 'MM/DD/YYYY HH:MI:SS AM'), 1); 

commit;

INSERT INTO IUSERUSERHDR ( ID, NAME, NOMBRECOMPLETO, PASSWORD, FLAGS, STAT, NUMBADCNTS, REMARKS,
CRTRID, CRTNDATE, UPDRID, UPDDATE, MAIL, DEPTID, BORRADO,
ID_MUNICIPIO ) VALUES ( 
99, 'SYSSUPERUSER', 'Usuario super administrador', '8wDzTBupWGhdmOqFJvbsqA==', 0
, 0, 0, 'Este usuario no se podrá borrar', 1,  TO_Date( '06/21/2004 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
, NULL, NULL, 'Este usuario no se podrá borrar', NULL, 0, 1); 

commit;

INSERT INTO R_GROUP_PERM ( GROUPID, IDPERM, IDACL ) VALUES ( 
2, 10, 1); 
INSERT INTO R_GROUP_PERM ( GROUPID, IDPERM, IDACL ) VALUES ( 
1, 7, 2); 
commit;




INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 201, 2, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 200, 2, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 7, 2, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 207, 2, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 12, 1, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 206, 2, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 11, 1, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 205, 2, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 10, 1, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 204, 2, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 203, 2, 1); 
INSERT INTO R_USR_PERM ( USERID, IDPERM, IDACL, APLICA ) VALUES ( 
99, 202, 2, 1); 
commit;

insert into appgeopista (appid,def) values (1,'Licencias');
insert into appgeopista (appid,def) values (2,'Administracion');
insert into appgeopista (appid,def) values (3,'Metadatos');
insert into appgeopista (appid,def) values (4,'Geopista');
insert into appgeopista (appid,def) values (5,'Planeamiento');
insert into appgeopista (appid,def) values (6,'Catastro');
insert into appgeopista (appid,def) values (7,'Contaminantes');





 


 

 
 







 


 

 
 
 
 
