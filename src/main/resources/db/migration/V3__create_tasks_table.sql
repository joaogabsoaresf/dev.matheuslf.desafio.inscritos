CREATE TABLE tasks
(
    id          UUID                        NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    is_active   BOOLEAN                     NOT NULL,
    title       VARCHAR(150)                NOT NULL,
    description VARCHAR(255),
    status      VARCHAR(255)                NOT NULL,
    priority    VARCHAR(255),
    due_date    date,
    project_id  UUID                        NOT NULL,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_PROJECT FOREIGN KEY (project_id) REFERENCES projects (id);