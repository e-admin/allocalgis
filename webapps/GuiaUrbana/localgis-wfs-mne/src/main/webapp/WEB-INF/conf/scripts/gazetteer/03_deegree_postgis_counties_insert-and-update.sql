-- Daten der Counties aus Importtabellen laden
-- Dabei doppelte einträge entfernen und geometrien zusammenfassen

insert into counties (geographicIdentifier) select name from imp_counties group by name order by name;

-- Geometrien zusammenfassen
update counties set geographicextent = (select multi(geomunion(the_geom)) from imp_counties where counties.geographicidentifier = imp_counties.name);
--  Eintragen der WGS84-Werte für XXBoundYYitude
update counties 
set position = setSRID(PointOnSurface((geographicextent)), 26912),
westboundlongitude = xmin(transform((geographicextent), 4326)),
eastboundlongitude = xmax(transform((geographicextent), 4326)),
southboundlatitude = ymin(transform((geographicextent), 4326)),
northboundlatitude = ymax(transform((geographicextent), 4326));

