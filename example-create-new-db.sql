-- 1)
-- cd C:\Program Files\PostgreSQL\<your PostgreSQL version>\bin
-- psql -U postgres

-- 2)
CREATE DATABASE devdb;

-- 3)
CREATE USER devuser WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE devdb TO devuser;
ALTER ROLE devuser CREATEDB;
ALTER USER devuser WITH SUPERUSER;