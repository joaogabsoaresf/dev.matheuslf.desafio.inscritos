CREATE TABLE projects
(
    id          UUID                        NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    is_active   BOOLEAN                     NOT NULL,
    name        VARCHAR(100)                NOT NULL,
    description VARCHAR(255),
    start_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_projects PRIMARY KEY (id)
);