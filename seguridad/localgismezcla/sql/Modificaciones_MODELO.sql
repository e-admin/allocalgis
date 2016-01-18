
-- Se añade campo Modod_Acoplado (Boolean) para indicar que el expediente fue
-- generado en modo acoplado y debe sincronizarse con la DGC
ALTER TABLE expediente ADD modo_acoplado BOOLEAN DEFAULT FALSE;