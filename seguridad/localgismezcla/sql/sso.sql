INSERT INTO iuseruserhdr (id,name,password,flags,stat,numbadcnts,crtrid,id_entidad,fecha_proxima_modificacion) 
VALUES ((SELECT max(id)+1 FROM iuseruserhdr),'SSOADMIN','jc/KYmLdqMWTBtEaBuHi2w==',0,0,0,325,0,null);

INSERT INTO r_usr_perm (userid,idperm,idacl,aplica) 
VALUES ((SELECT max(id) FROM iuseruserhdr),10,1,1);