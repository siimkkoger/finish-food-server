-- Drop all tables and sequences in the public schema
DO '
DECLARE
    t text;
BEGIN
    FOR t IN (SELECT table_name FROM information_schema.tables WHERE table_schema = ''public'' AND table_type = ''BASE TABLE'') LOOP
            EXECUTE ''DROP TABLE IF EXISTS public.'' || t || '' CASCADE'';
        END LOOP;
END;
' LANGUAGE plpgsql;

-- Drop all functions in the public schema
DO '
DECLARE
    r text;
BEGIN
    FOR r IN (SELECT routine_name FROM information_schema.routines WHERE routine_schema = ''public'' AND routine_type = ''FUNCTION'') LOOP
            EXECUTE ''DROP FUNCTION IF EXISTS public.'' || r || '' CASCADE'';
        END LOOP;
END;
' LANGUAGE plpgsql;

-- Drop the public schema
DROP SCHEMA IF EXISTS public CASCADE;
