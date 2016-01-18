
------- ### ACL EIEL_Indicadores ###
--select * from acl where name='Capas EIEL Indicadores'

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Capas EIEL Indicadores') THEN	
		insert into acl values (NEXTVAL('seq_acl'), 'Capas EIEL Indicadores');

		-- Se asocia el ACL con los permisos de leer, escribir, etc
		insert into r_acl_perm values (871, CURRVAL('seq_acl'));
		insert into r_acl_perm values (872, CURRVAL('seq_acl'));
		insert into r_acl_perm values (873, CURRVAL('seq_acl'));
		insert into r_acl_perm values (874, CURRVAL('seq_acl'));

		-- Se le dan permisos al superuser
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 871, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 872, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 873, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 874, CURRVAL('seq_acl'), 1); 

		-- Se le dan permisos a satec_pre
		--insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (325, 871, CURRVAL('seq_acl'), 1); 
		--insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (325, 872, CURRVAL('seq_acl'), 1); 
		--insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (325, 873, CURRVAL('seq_acl'), 1); 
		--insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (325, 874, CURRVAL('seq_acl'), 1); 
	END IF;


END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";
