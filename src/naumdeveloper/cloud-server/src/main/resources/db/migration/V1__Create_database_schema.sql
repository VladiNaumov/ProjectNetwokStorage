CREATE SEQUENCE users_user_id_seq;
CREATE SEQUENCE user_auc_auc_id_seq;

CREATE TABLE IF NOT EXISTS public."users"
(
    user_id integer NOT NULL DEFAULT nextval('"users_user_id_seq"'::regclass),
    login character varying(80) COLLATE pg_catalog."default" NOT NULL,
    email character varying(250) COLLATE pg_catalog."default",
    CONSTRAINT pk_user_id PRIMARY KEY (user_id),
    CONSTRAINT login_unique UNIQUE (login)
)

TABLESPACE pg_default;

ALTER TABLE public."users" OWNER to postgres;

CREATE TABLE IF NOT EXISTS public."user_auc"
(
    auc_id integer NOT NULL DEFAULT nextval('"user_auc_auc_id_seq"'::regclass),
    user_id integer NOT NULL,
    password character varying(33) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_auc_id PRIMARY KEY (auc_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public."user_auc"
    OWNER to postgres;

create function isRegister(login character varying(80), password character varying(33))
returns boolean as $$
declare passed boolean;
begin
	select (a.password = $2) into passed
	from users u
	left join user_auc a on a.user_id=u.user_id
	where u.login = $1;
	return passed;
end;
$$ language plpgsql;

create function registerNewUser(login character varying(80), password character varying(33), email character varying(250))
returns boolean as $$
declare
	passed boolean;
	f_user_id integer;
begin
	insert into users (login, email) values ($1, $3);
	select u.user_id into f_user_id
	from users u where u.login = $1;
	insert into user_auc (user_id, password) values (f_user_id, $2);
	select isRegister($1, $2) into passed;
	return passed;
end;
$$ language plpgsql;

INSERT INTO "users" (login, email) VALUES('test', 'test@test.com');
INSERT INTO "user_auc" (user_id, password) VALUES (1, 'b59c67bf196a4758191e42f76670ceba');