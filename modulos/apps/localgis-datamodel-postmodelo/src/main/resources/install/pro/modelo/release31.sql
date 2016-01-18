CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (select * from iusergroupuser where groupid=(select id from iusergrouphdr where name='EIEL_VAL')) THEN
		--Borramos los permisos que tuviera el role
		delete from r_group_perm where groupid=(select id from iusergrouphdr where name='EIEL_VAL');
		-- Borramos el rol
		delete from iusergrouphdr where name='EIEL_VAL';
		-- Creamos el rol
		insert into iusergrouphdr(id,name,mgrid,type,remarks,crtrid,crtndate,updrid,upddate,id_entidad) 
		values (nextval('seq_iusergrouphdr'),'EIEL_VAL',nextval('seq_r_user_perm'),0,'Rol para Usuario Publicador EIEL',currval('seq_r_user_perm'),'2011-11-17',currval('seq_r_user_perm'),'2011-11-17',0);
	END IF;
	
	--IF NOT EXISTS (select * from iusergroupuser where groupid=(select id from iusergrouphdr where name='EIEL_VAL')) THEN
		delete from r_group_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='LocalGis.EIEL.ExportarSHPMPT');

		delete from r_acl_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='LocalGis.EIEL.ExportarSHPMPT');
		delete from usrgrouperm where def='LocalGis.EIEL.ExportarSHPMPT';

		PERFORM setval('public.seq_acl_perm', (select max(idperm)::bigint from usrgrouperm), true);

		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.ExportarSHPMPT','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));

		insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'EIEL_VAL'), (select idperm from usrgrouperm where def like ('LocalGis.EIEL.ExportarSHPMPT')),(select idacl::integer from acl where name ='EIEL'));
	--END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO1";





update query_catalog set query='select acl.idacl as idacl, acl.name as name, usrgrouperm.def as def, usrgrouperm.name as permname, r_acl_perm.idperm as idperm from acl, r_acl_perm, usrgrouperm where acl.idacl=r_acl_perm.idacl and usrgrouperm.idperm=r_acl_perm.idperm order by idacl,def' where id='allaclspermisos';

update query_catalog set query='select * from eiel_t_carreteras where codprov=? and cod_carrt=?' where id='EIELgetPanelCarreteras';


delete from r_group_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='Geopista.DocumentoSinFeature.Publicar');

delete from r_acl_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='Geopista.DocumentoSinFeature.Publicar');
delete from usrgrouperm where def='Geopista.DocumentoSinFeature.Publicar';

insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'Geopista.DocumentoSinFeature.Publicar','');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='General'));

insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select idperm from usrgrouperm where def like ('Geopista.DocumentoSinFeature.Publicar')),(select idacl::integer from acl where name ='General'));


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'documento_sinfeature') THEN	
		CREATE TABLE documento_sinfeature
		(
		  id_documento_sinfeature character varying(100) NOT NULL,
		  id_entidad numeric(5,0) NOT NULL,
		  usuario character varying(20),
		  nombre character varying(255),
		  fecha_alta timestamp without time zone,
		  fecha_modificacion timestamp without time zone,
		  tipo numeric(4,0),
		  publico numeric(1,0) DEFAULT 1, -- Si el valor es O es privado en caso contrario es publico
		  tamanio numeric(12,0),
		  comentario character varying(1000),
		  thumbnail bytea,
		  is_imgdoctext character(1) DEFAULT 'D'::bpchar, -- Si el valor es D es un documento, si es I es una imagen y si es T es un texto
		  oculto numeric(1,0) DEFAULT 0, -- Si el valor es O es visible en caso contrario es oculto
		  CONSTRAINT tabla_documento_sinfeature_pkey PRIMARY KEY (id_documento_sinfeature),
		  CONSTRAINT check_is_imgdoctext CHECK (is_imgdoctext = 'D'::bpchar OR is_imgdoctext = 'I'::bpchar OR is_imgdoctext = 'T'::bpchar)
		)
		WITH (
		  OIDS=TRUE
		);
		ALTER TABLE documento_sinfeature
		  OWNER TO geopista;
		COMMENT ON COLUMN documento_sinfeature.publico IS 'Si el valor es O es privado en caso contrario es publico';
		COMMENT ON COLUMN documento_sinfeature.is_imgdoctext IS 'Si el valor es D es un documento, si es I es una imagen y si es T es un texto';
		COMMENT ON COLUMN documento_sinfeature.oculto IS 'Si el valor es O es visible en caso contrario es oculto';
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";




delete from r_group_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='LocalGis.EIEL.ActualizarPadron');

delete from r_acl_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='LocalGis.EIEL.ActualizarPadron');
delete from usrgrouperm where def='LocalGis.EIEL.ActualizarPadron';

insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.ActualizarPadron','');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));

insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'EIEL_VAL'), (select idperm from usrgrouperm where def like ('LocalGis.EIEL.ActualizarPadron')),(select idacl::integer from acl where name ='EIEL'));



update query_catalog set query='select acl.idacl as idacl, acl.name as name, usrgrouperm.def as def, usrgrouperm.name as permname, usrgrouperm.name as permname, r_acl_perm.idperm as idperm from acl, r_acl_perm, usrgrouperm where acl.idacl=r_acl_perm.idacl and usrgrouperm.idperm=r_acl_perm.idperm order by idacl,def' where id='allaclspermisos';


update queries SET updatequery = 'UPDATE "eiel_c_tramos_carreteras" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio"=?M,"codprov"=substr(?M::text,1,2),"codmunic"=substr(?M::text,3,5),"cod_carrt"=?6,"pki"=?7,"pkf"=?8,"gestor"=?9,"senaliza"=?10,"firme"=?11,"estado"=?12,"ancho"=?13,"longitud"=length(GeometryFromText(text(?1),?S))/1000,"paso_nivel"=?15,"dimensiona"=?16,"muy_sinuoso"=?17,"pte_excesiva"=?18,"fre_estrech"=?19,"superficie"=?20,"obra_ejec"=?21,"toma_dato"=?22,"fecha_act"=?23,"fecha_desuso"=?24,"observ"=?25 WHERE "id"=?2', insertquery= 'INSERT INTO "eiel_c_tramos_carreteras" ("GEOMETRY","id","id_municipio","codprov","codmunic","cod_carrt","pki","pkf","gestor","senaliza","firme","estado","ancho","longitud","paso_nivel","dimensiona","muy_sinuoso","pte_excesiva","fre_estrech","superficie","obra_ejec","toma_dato","fecha_act","fecha_desuso","observ") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,substr(?M::text,1,2),substr(?M::text,3,5),?6,?7,?8,?9,?10,?11,?12,?13,length(GeometryFromText(text(?1),?S))/1000,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?25)' where id_layer = (select id_layer from layers where name ='carreteras');

