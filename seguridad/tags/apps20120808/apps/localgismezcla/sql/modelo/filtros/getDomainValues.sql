
DROP FUNCTION getdomainvalues(text, text, text);
CREATE OR REPLACE FUNCTION getdomainvalues(domainname text, identidad text, locale text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select dictionary.traduccion from Domains inner join DomainNodes on 
domains.id=domainNodes.id_domain inner join dictionary on 
domainNodes.id_description = dictionary.id_vocablo left outer join entidades_municipios on
domainNodes.id_municipio=entidades_municipios.id_entidad where domains.name=$1 and
--domainNodes.pattern=$2 and
domainNodes.type=7 and 
(domainNodes.id_municipio is null or entidades_municipios.id_municipio=$2) and 
dictionary.locale=$3 order by domainnodes.id_municipio),' , ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getdomainvalues(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, text, text) TO consultas;


DROP FUNCTION getdomainvalues(text, integer, text);
CREATE OR REPLACE FUNCTION getdomainvalues(domainname text, identidad integer, locale text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select dictionary.traduccion from Domains inner join DomainNodes on 
domains.id=domainNodes.id_domain inner join dictionary on 
domainNodes.id_description = dictionary.id_vocablo left outer join entidades_municipios on
domainNodes.id_municipio=entidades_municipios.id_entidad where domains.name=$1 and
--domainNodes.pattern=$2 and
domainNodes.type=7 and 
(domainNodes.id_municipio is null or entidades_municipios.id_municipio=$2) and 
dictionary.locale=$3 order by domainnodes.id_municipio),' , ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getdomainvalues(text, integer, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, integer, text) TO public;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, integer, text) TO postgres;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, integer, text) TO consultas;