-- Agrega una nueva Acl para la gestion de espacio publico
INSERT INTO ACL VALUES (nextval('seq_acl'),'Servicios de Geomarketing');
-- Agrega los permisos para la aplicacion de espacio publico
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.geomarketing.obtenerdatosgenerales',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO R_USR_PERM VALUES(100,currval('seq_acl_perm'),currval('seq_acl'),1);
INSERT INTO USRGROUPERM VALUES(nextval('seq_acl_perm'),'localgis.geomarketing.obtenerdatosgeomarketing',NULL);
INSERT INTO R_ACL_PERM VALUES(currval('seq_acl_perm'),currval('seq_acl'));
INSERT INTO R_USR_PERM VALUES(100,currval('seq_acl_perm'),currval('seq_acl'),1);