CREATE TABLE hall
(
    id       SERIAL PRIMARY KEY,
    row      INT     NOT NULL,
    cell     INT     NOT NULL,
    isBooked BOOLEAN NOT NULL
);

INSERT INTO hall (row, cell, isBooked)
VALUES (1, 1, false),
       (1, 2, false),
       (1, 3, false),
       (2, 1, false),
       (2, 2, false),
       (2, 3, false),
       (3, 1, false),
       (3, 2, false),
       (3, 3, false);

CREATE TABLE account
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    email    VARCHAR NOT NULL UNIQUE,
    phone    VARCHAR NOT NULL UNIQUE
);

CREATE TABLE ticket
(
    id         SERIAL PRIMARY KEY,
    row        INT NOT NULL,
    cell       INT NOT NULL,
    account_id INT NOT NULL REFERENCES account (id),
    CONSTRAINT place UNIQUE (row, cell)
);
