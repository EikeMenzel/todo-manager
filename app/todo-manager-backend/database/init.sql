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

CREATE TABLE "todo"
(
    t_id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    status VARCHAR(12) NOT NULL DEFAULT 'NOT_STARTED',
    priority VARCHAR(6) NOT NULL DEFAULT 'LOW',
    category_id BIGINT,

    FOREIGN KEY (category_id) REFERENCES categories (c_id) ON DELETE CASCADE
);
