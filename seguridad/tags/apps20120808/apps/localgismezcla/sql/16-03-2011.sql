
update columns set name ='fechaalta' where name='FechaAlta';
update columns set name ='fechabaja' where name='FechaBaja';
update columns set name ='fechainstalacion' where name='FechaInstalacion';
update columns set name ='codigodelegacionmeh' where name='codigodelegacionMEH';
update columns set name ='codigoparcela' where name='CodigoParcela';
update columns set name ='codigozonaconcentracion' where name='codigoZonaConcentracion';
update columns set name ='nombreentidadmenor' where name='nombreEntidadMenor';
update columns set name ='nombreparaje' where name='nombreParaje';



update query_catalog set query = 'select distinct id_habitante, nombre, part_apell1, apellido1, part_apell2, apellido2, dni, letradni  from habitantes h, domicilio d, numeros_policia p where h.id_domicilio = d.id_domicilio and d.id_portal = p.id and p.id=? order by dni,letradni, apellido1, part_apell1, apellido2, part_apell2, nombre' where id ='padronHabitantesPoliciaOrdendni';

update query_catalog set query = 'select distinct id_habitante, nombre, part_apell1, apellido1, part_apell2, apellido2, dni, letradni  from habitantes h, domicilio d, numeros_policia p where h.id_domicilio = d.id_domicilio and d.id_portal = p.id and p.id=? order by apellido1, part_apell1, apellido2, part_apell2, nombre, dni,letradni' where id ='padronHabitantesPoliciaOrdenapellidos';


update layers set extended_form ='com.geopista.app.inventario.InventarioExtendedForm' where name ='inventario_parcelas';
update layers set extended_form ='com.geopista.app.inventario.InventarioExtendedForm' where name ='inventario_vias';


insert into query_catalog (id,query,acl,idperm) values ('loadGeometryViasNumPolicia','select vias.id as id_vias, vias.id_municipio,tipoviaine, nombreviaine, nombrecatastro, numeros_policia.id as id_numeros_policia, numeros_policia.rotulo, astext(numeros_policia."GEOMETRY") as geometria, srid(numeros_policia."GEOMETRY") as srid  from vias, numeros_policia where numeros_policia.id_via=vias.id and vias.id_municipio=? order by vias.id',1,9205);



