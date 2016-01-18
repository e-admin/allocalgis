DO
$do$
BEGIN
IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_domainnodes') THEN
	SELECT setval('public.seq_domainnodes', (select max(id)::bigint from domainnodes), true);
END IF;
END
$do$;