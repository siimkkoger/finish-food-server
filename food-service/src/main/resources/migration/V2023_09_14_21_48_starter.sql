CREATE TABLE public.test
(
    id         serial    NOT NULL PRIMARY KEY,
    name       boolean   NOT NULL default true,
    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

INSERT INTO public.test (id)
VALUES (DEFAULT),
       (DEFAULT),
       (DEFAULT),
       (DEFAULT),
       (DEFAULT),
       (DEFAULT),
       (DEFAULT),
       (DEFAULT),
       (DEFAULT),
       (DEFAULT);
