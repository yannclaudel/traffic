CREATE DATABASE trafic;
CREATE USER traficuser WITH ENCRYPTED PASSWORD 'traficpwd';
GRANT ALL PRIVILEGES ON DATABASE trafic TO traficuser;

-- connect to trafic
\c trafic
-- Extend the database with TimescaleDB
CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

SELECT timescaledb_pre_restore();

--\! pg_restore -U postgres -Fc -d trafic trafic.bak

--SELECT timescaledb_post_restore();
