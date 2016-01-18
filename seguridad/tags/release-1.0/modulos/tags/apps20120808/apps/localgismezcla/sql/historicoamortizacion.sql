-- Table: historico_amortizacion

-- DROP TABLE historico_amortizacion;

CREATE TABLE historico_amortizacion
(
  id_bien integer NOT NULL,
  anio numeric(4,0) NOT NULL,
  total_amortizado_porcentaje numeric(10,2),
  id_cuenta_contable numeric(8,0),
  id_cuenta_amortizacion numeric(8,0),
  total_amortizado_anio numeric(10,2),
  CONSTRAINT historico_amortizacion_pkey PRIMARY KEY (id_bien, anio),
  CONSTRAINT fk_amortizacion_pk FOREIGN KEY (id_cuenta_amortizacion)
      REFERENCES amortizacion (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_contable_pk FOREIGN KEY (id_cuenta_contable)
      REFERENCES contable (id_clasificacion) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historico_amortizacion OWNER TO geopista;
