# 🗄️ Base de Datos - Scripts SQL

Scripts de inicialización y configuración de la base de datos Oracle.

## 📋 Contenido

### `init.sql`
Script principal de inicialización que contiene:
- Creación de secuencias
- Creación de tablas
- Constraints y relaciones
- Datos iniciales (roles, usuarios de prueba, etc.)

## 🚀 Ejecución

### Oracle Cloud
```sql
-- 1. Conectarse a Oracle Cloud
sqlplus ADMIN@laboratoriosdb_high

-- 2. Ejecutar script
@init.sql
```

### Oracle Local (Docker)
```bash
# 1. Copiar script al contenedor
docker cp init.sql oracle-db:/tmp/

# 2. Ejecutar
docker exec -it oracle-db sqlplus system/Oracle123@XE @/tmp/init.sql
```

## 📊 Esquema de Base de Datos

### Tablas Principales

#### USUARIOS
```sql
- ID_USUARIO (PK)
- NOMBRE
- APELLIDO
- EMAIL (UNIQUE)
- PASSWORD (BCrypt)
- ROL (ADMINISTRADOR|PACIENTE|LABORATORISTA|MEDICO)
- FECHA_CREACION
- ACTIVO
```

#### LABORATORIOS
```sql
- ID_LABORATORIO (PK)
- NOMBRE
- DIRECCION
- TELEFONO
- EMAIL (UNIQUE)
- ESPECIALIDAD
- ACTIVO
- FECHA_CREACION
```

#### TIPOS_ANALISIS
```sql
- ID_TIPO_ANALISIS (PK)
- NOMBRE
- DESCRIPCION
- PRECIO
- TIEMPO_ESTIMADO
- REQUISITOS
- ACTIVO
```

#### CITAS
```sql
- ID_CITA (PK)
- ID_PACIENTE (FK → USUARIOS)
- ID_LABORATORIO (FK → LABORATORIOS)
- FECHA_HORA
- ESTADO
- OBSERVACIONES
```

#### RESULTADOS
```sql
- ID_RESULTADO (PK)
- ID_CITA (FK → CITAS)
- ID_TIPO_ANALISIS (FK → TIPOS_ANALISIS)
- FECHA_RESULTADO
- VALORES
- OBSERVACIONES
- ESTADO
```

#### LABORATORIO_ANALISIS (Relación N:M)
```sql
- ID_LABORATORIO (FK)
- ID_TIPO_ANALISIS (FK)
- DISPONIBLE
- FECHA_REGISTRO
```

## 🔐 Usuarios de Prueba

Los siguientes usuarios se crean automáticamente:

| Rol | Email | Password (plano) | Password (hash) |
|-----|-------|------------------|-----------------|
| ADMINISTRADOR | admin@lab.cl | Admin123* | BCrypt hash |
| MEDICO | medico@lab.cl | Medico123* | BCrypt hash |
| LABORATORISTA | lab@lab.cl | Lab123* | BCrypt hash |
| PACIENTE | paciente@lab.cl | Paciente123* | BCrypt hash |

## 🔄 Migraciones

Para actualizar el esquema sin perder datos:

```sql
-- 1. Backup
expdp ADMIN/password@laboratoriosdb_high \
  directory=DATA_PUMP_DIR \
  dumpfile=backup.dmp \
  schemas=ADMIN

-- 2. Aplicar cambios
-- (ejecutar scripts de migración)

-- 3. Verificar
SELECT table_name FROM user_tables;
```

## 📝 Constraints Importantes

### Check Constraints
- `CHK_USUARIO_ROL`: Valida roles válidos
- `CHK_USUARIO_ACTIVO`: Valida estado activo (0 o 1)
- `CHK_CITA_ESTADO`: Valida estados de cita
- `CHK_RESULTADO_ESTADO`: Valida estados de resultado

### Foreign Keys
- Todas las relaciones tienen `ON DELETE CASCADE` o `ON DELETE SET NULL` según corresponda

## 🐛 Troubleshooting

### Error: ORA-00001 (Unique constraint violated)
```sql
-- Verificar duplicados
SELECT email, COUNT(*) 
FROM USUARIOS 
GROUP BY email 
HAVING COUNT(*) > 1;
```

### Error: ORA-02291 (Integrity constraint violated)
```sql
-- Verificar FK
SELECT constraint_name, table_name, r_constraint_name
FROM user_constraints
WHERE constraint_type = 'R';
```

### Resetear secuencias
```sql
-- Resetear todas las secuencias
BEGIN
  FOR s IN (SELECT sequence_name FROM user_sequences) LOOP
    EXECUTE IMMEDIATE 'DROP SEQUENCE ' || s.sequence_name;
  END LOOP;
END;
/

-- Recrear
@init.sql
```

## 📊 Consultas Útiles

### Ver todas las tablas
```sql
SELECT table_name FROM user_tables ORDER BY table_name;
```

### Ver constraints
```sql
SELECT constraint_name, constraint_type, table_name
FROM user_constraints
WHERE table_name = 'USUARIOS';
```

### Ver índices
```sql
SELECT index_name, table_name, uniqueness
FROM user_indexes
WHERE table_name = 'USUARIOS';
```

### Estadísticas de datos
```sql
SELECT 
  'USUARIOS' as tabla, COUNT(*) as registros FROM USUARIOS
UNION ALL
SELECT 'LABORATORIOS', COUNT(*) FROM LABORATORIOS
UNION ALL
SELECT 'CITAS', COUNT(*) FROM CITAS
UNION ALL
SELECT 'RESULTADOS', COUNT(*) FROM RESULTADOS;
```

## 🔧 Mantenimiento

### Reindexar
```sql
ALTER INDEX idx_usuario_email REBUILD;
```

### Analizar estadísticas
```sql
EXEC DBMS_STATS.GATHER_SCHEMA_STATS('ADMIN');
```

### Limpiar datos de prueba
```sql
-- CUIDADO: Esto elimina todos los datos
TRUNCATE TABLE RESULTADOS;
TRUNCATE TABLE CITAS;
TRUNCATE TABLE LABORATORIO_ANALISIS;
TRUNCATE TABLE TIPOS_ANALISIS;
TRUNCATE TABLE LABORATORIOS;
TRUNCATE TABLE USUARIOS;

-- Resetear secuencias
ALTER SEQUENCE SEQ_USUARIO RESTART START WITH 1;
-- ... (repetir para todas las secuencias)
```

## 📞 Soporte

Para problemas con la base de datos, contactar al equipo de desarrollo.

