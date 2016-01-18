
UPDATE lcg_nodos_capas_campos set tag_traduccion = 'localgiseiel.panels.label.estado_revision' WHERE campo_bd = 'estado_revision' and tag_traduccion = 'localgiseiel.panels.label.estado';
UPDATE lcg_nodos_capas_campos set dominio = 'eiel_Estado de conservación' where campo_bd = 'estado' and clave_capa = 'CN';
UPDATE lcg_nodos_capas_campos set dominio = 'eiel_Estado de conservación', metodo = 'Estado' where campo_bd = 'estado' and clave_capa = 'RD';
UPDATE lcg_nodos_capas_campos set dominio = 'eiel_Estado de conservación', metodo = 'Estado' where campo_bd = 'estado' and clave_capa = 'RS';