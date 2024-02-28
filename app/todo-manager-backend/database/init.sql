SELECT 'CREATE DATABASE todo-manager'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'todo-manager');

ALTER DATABASE "todo-manager" OWNER TO "db_controller";

CREATE TABLE "users"
(
    u_id     BIGSERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL
);

CREATE TABLE "categories"
(
    c_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    user_id    BIGINT,

    FOREIGN KEY (user_id) REFERENCES users (u_id) ON DELETE CASCADE
);

