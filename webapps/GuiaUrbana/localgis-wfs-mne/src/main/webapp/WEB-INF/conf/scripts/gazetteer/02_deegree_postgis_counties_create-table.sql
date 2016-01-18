select DropGeometryTable ('counties');

create table counties (
	gid serial PRIMARY KEY,
	geographicIdentifier varchar(255),
	westBoundLongitude double precision,
	eastBoundLongitude double precision,
	southBoundLatitude double precision,
	northBoundLatitude double precision
);
select AddGeometryColumn('counties', 'geographicextent', 26912, 'MULTIPOLYGON', 2);
select AddGeometryColumn('counties', 'position', 26912, 'POINT', 2);


