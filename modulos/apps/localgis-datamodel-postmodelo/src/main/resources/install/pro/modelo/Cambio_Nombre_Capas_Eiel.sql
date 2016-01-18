--select * from domainnodes where (pattern='AB' or pattern='AU') and id_domain=(select id from domains where name = 'EIEL')
update domainnodes set pattern='AU' where pattern='AB' and id_domain=(select id from domains where name = 'EIEL');
update domainnodes set pattern='VT' where pattern='VE' and id_domain=(select id from domains where name = 'EIEL');
update domainnodes set pattern='MT' where pattern='MA' and id_domain=(select id from domains where name = 'EIEL');
update domainnodes set pattern='LM' where pattern='LO' and id_domain=(select id from domains where name = 'EIEL');
update domainnodes set pattern='PJ' where pattern='PA' and id_domain=(select id from domains where name = 'EIEL');
update domainnodes set pattern='SU' where pattern='ES' and id_domain=(select id from domains where name = 'EIEL');
update domainnodes set pattern='CSAN' where pattern='CS' and id_domain=(select id from domains where name = 'EIEL');
update domainnodes set pattern='IP' where pattern='PI' and id_domain=(select id from domains where name = 'EIEL');

-- El domainode de vertedero aparecen dos hay que cambiar el primero (el padre) por RVT

