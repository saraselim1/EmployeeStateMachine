CREATE TABLE company (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    name  VARCHAR      NOT NULL,
    email VARCHAR      NOT NULL  UNIQUE,
    age   INTEGER      NOT NULL,
    status   VARCHAR      NOT NULL,
    PRIMARY KEY (id));

CREATE TABLE contract (
    id BIGSERIAL PRIMARY KEY,
    duration      INTEGER      NOT NULL,
    start_date    DATETIME     NOT NULL,
    salary        DOUBLE       NOT NULL,
    company_id    INTEGER      NOT NULL,
    employee_id   INTEGER      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) references company(id),
    FOREIGN KEY (employee_id) references employee(id));

CREATE TABLE STATE_MACHINE(
    MACHINE_ID VARCHAR(255) NOT NULL,
    STATE VARCHAR(255),
    STATE_MACHINE_CONTEXT BLOB
);