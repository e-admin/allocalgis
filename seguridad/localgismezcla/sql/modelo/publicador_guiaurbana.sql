-- La tabla attributes repite atributos para cada capa, si se publica un mapa
-- para todos los municipios los atributos se repinte.
ALTER TABLE localgisguiaurbana.attribute ADD COLUMN mapid integer;