-- ========================================
-- SCRIPT DE VERIFICACIÓN DE BASE DE DATOS
-- Sistema de Gestión de Laboratorios Clínicos
-- ========================================

SET PAGESIZE 50;
SET LINESIZE 120;

PROMPT ========================================
PROMPT VERIFICACIÓN DE TABLAS
PROMPT ========================================

-- Verificar todas las tablas
SELECT table_name, num_rows, last_analyzed
FROM user_tables
ORDER BY table_name;

PROMPT;
PROMPT ========================================
PROMPT VERIFICACIÓN DE SECUENCIAS
PROMPT ========================================

-- Verificar secuencias
SELECT sequence_name, last_number
FROM user_sequences
ORDER BY sequence_name;

PROMPT;
PROMPT ========================================
PROMPT CONTEO DE REGISTROS POR TABLA
PROMPT ========================================

-- Contar registros
SELECT 'ROLES' AS TABLA, COUNT(*) AS CANTIDAD FROM ROLES
UNION ALL
SELECT 'USUARIOS', COUNT(*) FROM USUARIOS
UNION ALL
SELECT 'LABORATORIOS', COUNT(*) FROM LABORATORIOS
UNION ALL
SELECT 'TIPOS_ANALISIS', COUNT(*) FROM TIPOS_ANALISIS
UNION ALL
SELECT 'LABORATORIO_ANALISIS', COUNT(*) FROM LABORATORIO_ANALISIS
UNION ALL
SELECT 'CITAS', COUNT(*) FROM CITAS
UNION ALL
SELECT 'RESULTADOS_ANALISIS', COUNT(*) FROM RESULTADOS_ANALISIS
ORDER BY TABLA;

PROMPT;
PROMPT ========================================
PROMPT VERIFICACIÓN DE LA TABLA RESULTADOS_ANALISIS
PROMPT ========================================

-- Verificar estructura de RESULTADOS_ANALISIS
DESC RESULTADOS_ANALISIS;

PROMPT;
PROMPT ========================================
PROMPT CONSTRAINTS DE RESULTADOS_ANALISIS
PROMPT ========================================

SELECT constraint_name, constraint_type, search_condition
FROM user_constraints
WHERE table_name = 'RESULTADOS_ANALISIS'
ORDER BY constraint_type;

PROMPT;
PROMPT ========================================
PROMPT ÍNDICES DE RESULTADOS_ANALISIS
PROMPT ========================================

SELECT index_name, uniqueness, column_name
FROM user_ind_columns
WHERE table_name = 'RESULTADOS_ANALISIS'
ORDER BY index_name, column_position;

PROMPT;
PROMPT ========================================
PROMPT MUESTRA DE DATOS DE RESULTADOS_ANALISIS
PROMPT ========================================

SELECT 
    ID_RESULTADO,
    ID_CITA,
    ID_LABORATORISTA,
    ESTADO,
    FECHA_RESULTADO,
    FECHA_CREACION
FROM RESULTADOS_ANALISIS
ORDER BY FECHA_CREACION DESC;

PROMPT;
PROMPT ========================================
PROMPT VERIFICACIÓN COMPLETADA
PROMPT ========================================


