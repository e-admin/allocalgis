-- Añadimos a diferentes usuarios su rol correspondiente
SET @USER1 = 'EIEL001_PUB';
SET @ROL = 'EIEL_PUB';
SET @IDUSER= select id::integer from iuseruserhdr where name='@USER1';
SET @IDROL= select id::integer from iusergrouphdr where name='@ROL';

insert into iusergroupuser(groupid,userid) values (@IDROL,@IDUSER);

SET @USER1 = 'EIEL001_VAL';
SET @ROL = 'EIEL_VAL';
SET @IDUSER= select id::integer from iuseruserhdr where name='@USER1';
SET @IDROL= select id::integer from iusergrouphdr where name='@ROL';

insert into iusergroupuser(groupid,userid) values (@IDROL,@IDUSER);


SET @USER1 = 'SATEC_PRO';
SET @ROL = 'EIEL_VAL';
SET @IDUSER= select id::integer from iuseruserhdr where name='@USER1';
SET @IDROL= select id::integer from iusergrouphdr where name='@ROL';

insert into iusergroupuser(groupid,userid) values (@IDROL,@IDUSER);

-- Le Añadimos el rol de poder ver las capas de indicadores
SET @USER1 = 'EIEL001_PUB';
SET @ROL = 'EIEL_INDICADORES';
SET @IDUSER= select id::integer from iuseruserhdr where name='@USER1';
SET @IDROL= select id::integer from iusergrouphdr where name='@ROL';

insert into iusergroupuser(groupid,userid) values (@IDROL,@IDUSER);


-- Le Añadimos el rol de poder ver las capas de indicadores
SET @USER1 = 'EIEL001_VAL';
SET @ROL = 'EIEL_INDICADORES';
SET @IDUSER= select id::integer from iuseruserhdr where name='@USER1';
SET @IDROL= select id::integer from iusergrouphdr where name='@ROL';

insert into iusergroupuser(groupid,userid) values (@IDROL,@IDUSER);

-- Le Añadimos el rol de poder ver las capas de indicadores
SET @USER1 = 'SATEC_PRO';
SET @ROL = 'EIEL_INDICADORES';
SET @IDUSER= select id::integer from iuseruserhdr where name='@USER1';
SET @IDROL= select id::integer from iusergrouphdr where name='@ROL';

insert into iusergroupuser(groupid,userid) values (@IDROL,@IDUSER);

