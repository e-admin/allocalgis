INSERT INTO acl (idacl,name) VALUES(nextval('seq_acl'),'Capas Importadas');

SET @ID_PERM_LEER = 4000;
SET @ID_PERM_ESCRIBIR = 4010;
SET @ID_PERM_ANADIR = 4020;
SET @ID_PERM_MODIFICAR = 4030;

INSERT INTO r_acl_perm(idperm,idacl) VALUES(@ID_PERM_LEER,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(@ID_PERM_ESCRIBIR,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(@ID_PERM_ANADIR,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(@ID_PERM_MODIFICAR,currval('seq_acl'));

SET @ID_USER = 400;

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (@ID_USER,@ID_PERM_LEER,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (@ID_USER,@ID_PERM_ESCRIBIR,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (@ID_USER,@ID_PERM_ANADIR,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (@ID_USER,@ID_PERM_MODIFICAR,currval('seq_acl'),1);

SET @ID_USER = 325;

INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (@ID_USER,@ID_PERM_LEER,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (@ID_USER,@ID_PERM_ESCRIBIR,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (@ID_USER,@ID_PERM_ANADIR,currval('seq_acl'),1);
INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (@ID_USER,@ID_PERM_MODIFICAR,currval('seq_acl'),1);
