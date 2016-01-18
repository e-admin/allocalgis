-- indizes anlegen
create index idx_counties_geographicidentifier on counties (geographicidentifier);
create index idx_counties_northboundlatitude on counties (northboundlatitude);
create index idx_counties_southboundlatitude on counties (southboundlatitude);
create index idx_counties_eastboundlongitude on counties (eastboundlongitude);
create index idx_counties_westboundlongitude on counties (westboundlongitude);


-- raeumliche indizes anlegen
create index sdx_counties_pos on counties using GIST (position GIST_GEOMETRY_OPS);
create index sdx_counties_ext on counties using GIST (geographicextent GIST_GEOMETRY_OPS);

vacuum analyse counties;
