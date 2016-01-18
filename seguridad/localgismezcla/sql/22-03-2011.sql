ALTER TABLE vias ADD COLUMN tipo varchar(20);

CREATE INDEX "indice_tipo"
  ON bienes_inventario
  USING btree
  (tipo);
  
  
CREATE INDEX "indice_numinventario"
  ON bienes_inventario
  USING btree
  (numinventario);
  
  
 CREATE INDEX "indice_id_bienes_inventario"
  ON bienes_inventario
  USING btree
  (id);
  
   CREATE INDEX "indice_id_vias_inventario"
  ON vias_inventario
  USING btree
  (id);
  
  CREATE INDEX "indice_revision_expirada"
  ON bienes_inventario
  USING btree
  (revision_expirada);
  
      CREATE INDEX "indice_borrado"
  ON bienes_inventario
  USING btree
  (borrado);

  
 CREATE INDEX "indice_inventario_idmunicipio"
  ON bienes_inventario
  USING btree
  (id_municipio);
  
  CREATE INDEX "indice_observaciones_id_bien"
  ON observaciones_inventario
  USING btree
  (id_bien);
  
  CREATE INDEX "indice_inventario_feature_id_feature"
  ON inventario_feature
  USING btree
  (id_feature);  
  