alter table bienes_inventario add revision_actual numeric(10) default -1;
alter table bienes_inventario add revision_expirada numeric(10) default 9999999999;

alter table semoviente drop constraint semoviente_fkey;
alter table vehiculo drop constraint vehiculo_fkey;
alter table vias_inventario drop constraint vias_inventario_fkey;
alter table credito_derecho drop constraint credito_derecho_fkey;
alter table valor_mobiliario drop constraint valor_mobiliario_fkey;
alter table derechos_reales drop constraint derechosreales_fk;
alter table muebles drop constraint muebles_fkey;
alter table inmuebles drop constraint inmuebles_fkey;

alter table usos_funcionales_inventario drop constraint usos_funcionales_inventario_fkey;
alter table refcatastrales_inventario drop constraint refcatastrales_inventario_fkey;
alter table mejoras_inventario drop constraint mejoras_inventario_fkey;
alter table observaciones_inventario drop constraint observaciones_inventario_fkey;
alter table usos_funcionales_inventario drop constraint usos_funcionales_inventario_pkey;
alter table refcatastrales_inventario drop constraint refcatastrales_inventario_pkey;
alter table mejoras_inventario drop constraint mejoras_inventario_pkey;
alter table observaciones_inventario drop constraint observaciones_inmueble_urbano_pkey;
alter table bienes_inventario drop constraint bienes_inventario_pkey;

alter table usos_funcionales_inventario add revision_actual numeric(10) default -1;
alter table usos_funcionales_inventario add revision_expirada numeric(10) default 9999999999;
alter table inmuebles_rusticos add revision_actual numeric(10) default -1;
alter table inmuebles_rusticos add revision_expirada numeric(10) default 9999999999;
alter table inmuebles_rusticos drop constraint inmuebles_rusticos_pkey;
alter table inmuebles_rusticos add constraint inmuebles_rusticos_pkey primary key (id, revision_actual);
alter table inmuebles_urbanos add revision_actual numeric(10) default -1;
alter table inmuebles_urbanos add revision_expirada numeric(10) default 9999999999;
alter table inmuebles_urbanos drop constraint inmuebles_urbanos_pkey;
alter table inmuebles_urbanos add constraint inmuebles_urbanos_pkey primary key (id, revision_actual);
alter table refcatastrales_inventario add revision_actual numeric(10) default -1;
alter table refcatastrales_inventario add revision_expirada numeric(10) default 9999999999;
alter table mejoras_inventario add revision_actual numeric(10) default -1;
alter table mejoras_inventario add revision_expirada numeric(10) default 9999999999;
alter table observaciones_inventario add revision_actual numeric(10) default -1;
alter table observaciones_inventario add revision_expirada numeric(10) default 9999999999;

insert into bienes_inventario select b.id,b.numinventario,b.fecha_alta,b.fecha_baja,b.fecha_ultima_modificacion,b.tipo,b.descripcion,b.id_municipio,b.nombre,b.uso,
b.borrado,b.id_cuenta_contable,b.id_cuenta_amortizacion,b.id_seguro,organizacion,b.bloqueado, i.revision_actual,i.revision_expirada from bienes_inventario b,
inmuebles i where i.id=b.id and b.revision_actual=-1 order by i.revision_actual;
insert into bienes_inventario select b.id,b.numinventario,b.fecha_alta,b.fecha_baja,b.fecha_ultima_modificacion,b.tipo,b.descripcion,b.id_municipio,b.nombre,b.uso,
b.borrado,b.id_cuenta_contable,b.id_cuenta_amortizacion,b.id_seguro,organizacion,b.bloqueado, i.revision_actual,i.revision_expirada from bienes_inventario b,
semoviente i where i.id=b.id and b.revision_actual=-1 order by i.revision_actual;
insert into bienes_inventario select b.id,b.numinventario,b.fecha_alta,b.fecha_baja,b.fecha_ultima_modificacion,b.tipo,b.descripcion,b.id_municipio,b.nombre,b.uso,
b.borrado,b.id_cuenta_contable,b.id_cuenta_amortizacion,b.id_seguro,organizacion,b.bloqueado, i.revision_actual,i.revision_expirada from bienes_inventario b,
vehiculo i where i.id=b.id and b.revision_actual=-1 order by i.revision_actual;
insert into bienes_inventario select b.id,b.numinventario,b.fecha_alta,b.fecha_baja,b.fecha_ultima_modificacion,b.tipo,b.descripcion,b.id_municipio,b.nombre,b.uso,
b.borrado,b.id_cuenta_contable,b.id_cuenta_amortizacion,b.id_seguro,organizacion,b.bloqueado, i.revision_actual,i.revision_expirada from bienes_inventario b,
vias_inventario i where i.id=b.id and b.revision_actual=-1 order by i.revision_actual;
insert into bienes_inventario select b.id,b.numinventario,b.fecha_alta,b.fecha_baja,b.fecha_ultima_modificacion,b.tipo,b.descripcion,b.id_municipio,b.nombre,b.uso,
b.borrado,b.id_cuenta_contable,b.id_cuenta_amortizacion,b.id_seguro,organizacion,b.bloqueado, i.revision_actual,i.revision_expirada from bienes_inventario b,
credito_derecho i where i.id=b.id and b.revision_actual=-1 order by i.revision_actual;
insert into bienes_inventario select b.id,b.numinventario,b.fecha_alta,b.fecha_baja,b.fecha_ultima_modificacion,b.tipo,b.descripcion,b.id_municipio,b.nombre,b.uso,
b.borrado,b.id_cuenta_contable,b.id_cuenta_amortizacion,b.id_seguro,organizacion,b.bloqueado, i.revision_actual,i.revision_expirada from bienes_inventario b,
valor_mobiliario i where i.id=b.id and b.revision_actual=-1 order by i.revision_actual;
insert into bienes_inventario select b.id,b.numinventario,b.fecha_alta,b.fecha_baja,b.fecha_ultima_modificacion,b.tipo,b.descripcion,b.id_municipio,b.nombre,b.uso,
b.borrado,b.id_cuenta_contable,b.id_cuenta_amortizacion,b.id_seguro,organizacion,b.bloqueado, i.revision_actual,i.revision_expirada from bienes_inventario b,
derechos_reales i where i.id=b.id and b.revision_actual=-1 order by i.revision_actual;
insert into bienes_inventario select b.id,b.numinventario,b.fecha_alta,b.fecha_baja,b.fecha_ultima_modificacion,b.tipo,b.descripcion,b.id_municipio,b.nombre,b.uso,
b.borrado,b.id_cuenta_contable,b.id_cuenta_amortizacion,b.id_seguro,organizacion,b.bloqueado, i.revision_actual,i.revision_expirada from bienes_inventario b,
muebles i where i.id=b.id and b.revision_actual=-1 order by i.revision_actual;
delete from bienes_inventario where revision_actual=-1;
insert into usos_funcionales_inventario select nextval('seq_usos_funcionales_inventario'),u.id_bien,u.uso,u.superficie,u.fecha,b.revision_actual,b.revision_expirada from bienes_inventario b, usos_funcionales_inventario u 
where u.id_bien=b.id and u.revision_actual=-1 order by b.revision_actual;
delete from usos_funcionales_inventario where revision_actual=-1;
insert into refcatastrales_inventario select nextval('seq_refcatastrales_inventario'), u.ref_catastral, u.id_bien, u.descripcion,b.revision_actual,b.revision_expirada from bienes_inventario b, refcatastrales_inventario u 
where u.id_bien=b.id and u.revision_actual=-1 order by b.revision_actual;
delete from refcatastrales_inventario where revision_actual=-1;
insert into mejoras_inventario select nextval('seq_mejoras_inventario'), u.id_bien, u.descripcion, u. fecha, u.fecha_ejecucion, u.fecha_ultima_modificacion, 
u.importe,b.revision_actual,b.revision_expirada from bienes_inventario b, mejoras_inventario u 
where u.id_bien=b.id and u.revision_actual=-1 order by b.revision_actual;
delete from mejoras_inventario where revision_actual=-1;
insert into observaciones_inventario select nextval('seq_observaciones'), u.descripcion, u.fecha, u.fecha_ultima_modificacion, u.id_bien,b.revision_actual,b.revision_expirada 
from bienes_inventario b, observaciones_inventario u 
where u.id_bien=b.id and u.revision_actual=-1 order by b.revision_actual;
delete from observaciones_inventario where revision_actual=-1;

alter table bienes_inventario add constraint bienes_inventario_pkey primary key (id, revision_actual);
alter table semoviente add constraint semoviente_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table vehiculo add constraint vehiculo_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table vias_inventario add constraint vias_inventario_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table credito_derecho add constraint credito_derecho_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table valor_mobiliario add constraint valor_mobiliario_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table derechos_reales add constraint derechos_reales_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table muebles add constraint muebles_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table inmuebles add constraint inmuebles_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table usos_funcionales_inventario add constraint usos_funcionales_inventario_fkey FOREIGN KEY (id_bien,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table refcatastrales_inventario add constraint refcatastrales_inventario_fkey FOREIGN KEY (id_bien,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table mejoras_inventario add constraint mejoras_inventario_fkey FOREIGN KEY (id_bien,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
alter table observaciones_inventario add constraint observaciones_inventario_fkey FOREIGN KEY (id_bien,revision_actual) REFERENCES bienes_inventario (id,revision_actual);

alter table usos_funcionales_inventario add constraint usos_funcionales_inventario_pkey primary key(id,revision_actual);
alter table refcatastrales_inventario add constraint refcatastrales_inventario_pkey primary key(id,revision_actual);
alter table mejoras_inventario add constraint mejoras_inventario_pkey primary key(id,revision_actual);
alter table observaciones_inventario add constraint observaciones_inmueble_urbano_pkey primary key(id,revision_actual);

alter sequence seq_amortizacion RESTART WITH 3;
