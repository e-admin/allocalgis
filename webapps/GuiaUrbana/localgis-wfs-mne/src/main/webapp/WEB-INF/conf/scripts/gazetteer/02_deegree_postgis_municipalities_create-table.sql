select DropGeometryTable ('municipalities');

create table municipalities (
	gid serial PRIMARY KEY,
	geographicIdentifier varchar(255),
	parentIdentifier varchar(255),
	westBoundLongitude double precision,
	eastBoundLongitude double precision,
	southBoundLatitude double precision,
	northBoundLatitude double precision
);
select AddGeometryColumn('municipalities', 'geographicextent', 26912, 'MULTIPOLYGON', 2);
select AddGeometryColumn('municipalities', 'position', 26912, 'POINT', 2);


