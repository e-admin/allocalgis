-- indizes anlegen
create index idx_municipalities_geographicidentifier on municipalities (geographicidentifier);
create index idx_municipalities_northboundlatitude on municipalities (northboundlatitude);
create index idx_municipalities_southboundlatitude on municipalities (southboundlatitude);
create index idx_municipalities_eastboundlongitude on municipalities (eastboundlongitude);
create index idx_municipalities_westboundlongitude on municipalities (westboundlongitude);


-- raeumliche indizes anlegen
create index sdx_municipalities_pos on municipalities using GIST (position GIST_GEOMETRY_OPS);
create index sdx_municipalities_ext on municipalities using GIST (geographicextent GIST_GEOMETRY_OPS);

vacuum analyse municipalities;
