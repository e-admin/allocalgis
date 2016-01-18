INSERT INTO query_catalog (id, query) VALUES ('GIGetMapasPrivadosPublicados', 'SELECT mapidgeopista, traduccion FROM localgisguiaurbana.map, maps, dictionary WHERE locale=? AND mappublic=0 AND mapidentidad=? AND mapidgeopista=id_map AND id_name=id_vocablo GROUP BY mapidgeopista, traduccion');

INSERT INTO query_catalog (id, query) VALUES ('GIGetCapasMapa', 'select distinct(l.layeridgeopista), l.layername, traduccion from localgisguiaurbana.map m, localgisguiaurbana.layer l, localgisguiaurbana.maplayer ml, layers, dictionary where l.layerid=ml.layerid and layers.id_layer=l.layeridgeopista and layers.id_name=id_vocablo and locale=? and ml.mapid=m.mapid and m.mapidgeopista=? GROUP BY l.layeridgeopista, l.layername, traduccion');

INSERT INTO query_catalog (id, query) VALUES ('GIGetColumnasCapa', 'select traduccion, c.id, c.name, c.id_table, t.name from attributes a, columns c, tables t, dictionary d where c.id=a.id_column and c.id_table=t.id_table and a.id_layer=? and c.name not like ''GEOMETRY'' and a.id_alias=d.id_vocablo and locale=?');

-- GRANTS
GRANT USAGE ON SCHEMA localgisguiaurbana TO consultas;
GRANT SELECT ON TABLE localgisguiaurbana.maplayer TO consultas;
GRANT SELECT ON TABLE localgisguiaurbana.layer TO consultas;
GRANT SELECT ON TABLE localgisguiaurbana.map TO consultas;
GRANT SELECT ON TABLE localgisguiaurbana.legend TO consultas;

GRANT SELECT ON TABLE layers TO consultas;
GRANT SELECT ON TABLE dictionary TO consultas;
GRANT SELECT ON TABLE vias TO consultas;

GRANT SELECT ON TABLE maps TO consultas;
GRANT SELECT ON TABLE entidad_supramunicipal TO consultas;
GRANT SELECT ON TABLE entidades_municipios TO consultas;
GRANT SELECT ON TABLE tables TO consultas;
GRANT SELECT ON TABLE spatial_ref_sys TO consultas;

GRANT SELECT ON TABLE expediente_licencia TO consultas;
GRANT SELECT ON TABLE solicitud_licencia TO consultas;
GRANT SELECT ON TABLE persona_juridico_fisica TO consultas;
GRANT SELECT ON TABLE tipo_licencia TO consultas;
GRANT SELECT ON TABLE domainnodes TO consultas;
GRANT SELECT ON TABLE datos_ocupacion TO consultas;
GRANT SELECT ON TABLE actividad_contaminante TO consultas;
GRANT SELECT ON TABLE vertedero TO consultas;
GRANT SELECT ON TABLE zonas_arboladas TO consultas;

GRANT SELECT ON TABLE columns TO consultas;
GRANT SELECT ON TABLE domains TO consultas;
GRANT SELECT ON TABLE tramosabastecimiento TO consultas;
GRANT SELECT ON TABLE captaciones TO consultas;
GRANT SELECT ON TABLE conducciones TO consultas;
GRANT SELECT ON TABLE depositos TO consultas;
GRANT SELECT ON TABLE piezas TO consultas;
GRANT SELECT ON TABLE potabilizadoras TO consultas;
GRANT SELECT ON TABLE colectores TO consultas;
GRANT SELECT ON TABLE depuradoras TO consultas;
GRANT SELECT ON TABLE elementossaneamiento TO consultas;
GRANT SELECT ON TABLE emisarios TO consultas;
GRANT SELECT ON TABLE saneamientoautonomo TO consultas;
GRANT SELECT ON TABLE tramossaneamiento TO consultas;

-- 18-09-2009 Modificacion para la consulta de obtener los mapas privados publicados
UPDATE query_catalog set query='SELECT mapidgeopista, traduccion FROM localgisguiaurbana.map, maps, dictionary WHERE locale=? AND mappublic=0 AND mapidentidad=? AND mapidgeopista=id_map AND id_name=id_vocablo GROUP BY mapidgeopista, traduccion' WHERE id='GIGetMapasPrivadosPublicados';

-- 12-01-2010 Permiso para plantilla de Informe de Licencia de Obra
GRANT SELECT ON TABLE estado TO consultas;


-- 04-03-2010 -----
update query_catalog set query = 'select l.layeridgeopista, l.layername, traduccion from localgisguiaurbana.map m, localgisguiaurbana.layer l, localgisguiaurbana.maplayer ml, layers, dictionary where l.layerid=ml.layerid and layers.id_layer=l.layeridgeopista and layers.id_name=id_vocablo and locale=? and ml.mapid=m.mapid and m.mapidgeopista=? GROUP BY l.layeridgeopista, l.layername, traduccion' where id='GIGetCapasMapa';