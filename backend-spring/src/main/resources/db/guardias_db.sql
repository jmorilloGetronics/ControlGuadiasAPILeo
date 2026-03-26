-- ============================================================
-- Script de inicialización: Control Guardias IES Alixar
-- Ejecutar en MySQL Workbench antes de arrancar el backend
-- ============================================================

CREATE DATABASE IF NOT EXISTS guardias_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE guardias_db;

CREATE TABLE IF NOT EXISTS ausencias (
    id              VARCHAR(36)     NOT NULL,
    fecha           VARCHAR(10)     NOT NULL,
    franja_id       VARCHAR(20)     NOT NULL,
    profesor_ausente VARCHAR(200)   NOT NULL,
    grupo           VARCHAR(50)     NOT NULL,
    aula            VARCHAR(50)     NOT NULL,
    asignatura      VARCHAR(100)    NOT NULL,
    tarea           VARCHAR(500)    NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_fecha (fecha),
    INDEX idx_fecha_franja (fecha, franja_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
