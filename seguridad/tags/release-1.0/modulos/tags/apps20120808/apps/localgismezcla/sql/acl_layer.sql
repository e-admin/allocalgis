
INSERT INTO query_catalog(id, query) VALUES('LMInsertarAcl', 'INSERT INTO acl(idacl, name) VALUES(nextval(''seq_acl''), ?)');
INSERT INTO query_catalog(id, query) VALUES('LMobtenerAcl', 'SELECT idacl, name from acl WHERE name = ?');
INSERT INTO query_catalog(id, query) VALUES('LMInsertarPermisosAcl', 'INSERT INTO r_acl_perm(idperm, idacl) VALUES(871, ?);INSERT INTO r_acl_perm(idperm, idacl) VALUES(872, ?);INSERT INTO r_acl_perm(idperm, idacl) VALUES(873, ?);INSERT INTO r_acl_perm(idperm, idacl) VALUES(874, ?);');
