-- =============================================
-- Esquema PostgreSQL / Supabase
-- Gestion de Usuarios - Arquitectura Hexagonal
-- Ejecutar en el SQL Editor de Supabase (o psql).
-- role/status usan VARCHAR + CHECK (el adaptador enlaza con setString).
-- =============================================
CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN', 'MEMBER', 'REVIEWER')),
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING'
                CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED')),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- Nota: no se inserta admin inicial; el hash del schema.sql de MySQL es un placeholder.
-- Cree usuarios desde Swagger (POST /api/...).
