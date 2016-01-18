-- Actualizaciones de query_catalog para la tabla de los nucleos de emisarios (puntos de vertido)
-- eiel_saneam_tr_tem_pv
update query_catalog set query='select em.clave_tem,em.codmunic_tem, em.codprov_tem, em.tramo_em, em.pmf, em.pmi, nucleos.codprov_pobl, nucleos.codmunic_pobl, nucleos.codentidad_pobl, nucleos.codpoblamiento_pobl, nucleos.observ, nucleos.fecha_revision, nucleos.estado_revision from eiel_tr_saneam_pv_pobl nucleos, eiel_t_saneam_pv pv, eiel_tr_saneam_tem_pv em where nucleos.orden_pv = pv.orden_pv and em.clave_pv = pv.orden_pv and em.clave_tem = ? and em.codprov_tem = ? and em.codmunic_tem = ? and em.tramo_em = ?' where id='EIELgetListaNucleosEmisario'

update query_catalog set query='select *  from eiel_tr_saneam_tem_pv em inner join eiel_t_saneam_pv pv on pv.orden_pv=em.clave_pv and pv.codprov=? and pv.codmunic=? inner join eiel_tr_saneam_pv_pobl nucl on pv.orden_pv = nucl.orden_pv where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? '   where id='EIELgetNucleosEmisario'


--Cambiar tabla planeamiento urbano: agregar campo bloqueado
ALTER TABLE eiel_t_planeam_urban ADD COLUMN bloqueado varchar(32);

-- Cambio de dominio EIEL_SI_NO
UPDATE domainnodes set pattern='NO' where id=1050
UPDATE domainnodes set pattern='SI' where id=1051


-- Consulta para obetener los ordenes de un punto de vertido
insert into query_catalog values('EIELobtenerOrdenPuntosVertido','select orden_pv from eiel_t_saneam_pv where clave=? and codprov=? and codmunic=?',1,9205);