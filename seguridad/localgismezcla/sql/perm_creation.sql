insert into usrgrouperm (idperm, def) values (1, 'LocalGIS.Movilidad.Login');
insert into acl (idacl, name) values (nextval('seq_acl'), 'Movilidad');
insert into r_acl_perm (idperm, idacl) values (1, currval('seq_acl'));
