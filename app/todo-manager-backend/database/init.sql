SELECT 'CREATE DATABASE todo-manager'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'todo-manager');

ALTER DATABASE "todo-manager" OWNER TO "db_controller";

CREATE TABLE "users"
(
    u_id              BIGSERIAL PRIMARY KEY,
    username          VARCHAR(30) NOT NULL,
    password          VARCHAR(60) NOT NULL
);