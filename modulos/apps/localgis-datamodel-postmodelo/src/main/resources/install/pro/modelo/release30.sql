UPDATE tables SET geometrytype = 9 WHERE name = 'eiel_c_abast_tcn';
UPDATE tables SET geometrytype = 9 WHERE name = 'eiel_c_abast_rd';
UPDATE tables SET geometrytype = 9 WHERE name = 'eiel_c_saneam_tem';
UPDATE tables SET geometrytype = 9 WHERE name = 'eiel_c_saneam_tcl';
UPDATE tables SET geometrytype = 9 WHERE name = 'eiel_c_saneam_rs';
UPDATE tables SET geometrytype = 9 WHERE name = 'eiel_c_redviaria_tu';
UPDATE tables SET geometrytype = 9 WHERE name = 'eiel_c_tramos_carreteras';


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN

	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'eiel_t_saneam_tem_materialchk' ) THEN	
		ALTER TABLE eiel_t_saneam_tem DROP CONSTRAINT eiel_t_saneam_tem_materialchk;	
	END IF;	
	ALTER TABLE eiel_t_saneam_tem ADD CONSTRAINT eiel_t_saneam_tem_materialchk CHECK (material::text = 'FC'::text OR material::text = 'FU'::text OR material::text = 'HO'::text OR material::text = 'OT'::text OR material::text = 'PC'::text OR material::text = 'PE'::text OR material::text = 'PV'::text OR material::text = 'PL'::text);
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";

