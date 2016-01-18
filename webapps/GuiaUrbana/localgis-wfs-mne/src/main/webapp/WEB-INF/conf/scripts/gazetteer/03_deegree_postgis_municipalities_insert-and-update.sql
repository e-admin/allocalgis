-- Daten der Municipalities aus Importtabellen laden
-- Dabei doppelte einträge entfernen und geometrien zusammenfassen

--   adm1
insert into municipalities (geographicIdentifier, parentIdentifier) select name, county from imp_municipalities group by name, county order by name;

-- Geometrien zusammenfassen
update municipalities set geographicextent = (select multi(geomunion(the_geom)) from imp_municipalities where municipalities.geographicidentifier = imp_municipalities.name);
--  Eintragen der WGS84-Werte für XXBoundYYitude
update municipalities 
set position = setSRID(PointOnSurface((geographicextent)), 26912),
westboundlongitude = xmin(transform((geographicextent), 4326)),
eastboundlongitude = xmax(transform((geographicextent), 4326)),
southboundlatitude = ymin(transform((geographicextent), 4326)),
northboundlatitude = ymax(transform((geographicextent), 4326));

