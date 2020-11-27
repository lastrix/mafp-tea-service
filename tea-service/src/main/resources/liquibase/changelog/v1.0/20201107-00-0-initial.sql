CREATE SCHEMA IF NOT EXISTS tea_service;
------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE tea_service.tea_type_seq START 1 INCREMENT BY 1;
CREATE TABLE tea_service.tea_type
(
    tea_type_id INT           NOT NULL DEFAULT nextval('tea_service.tea_type_seq')::regclass,
    name        VARCHAR(128)  NOT NULL,
    description VARCHAR(2048) NOT NULL,
    CONSTRAINT tea_type_pk PRIMARY KEY (tea_type_id)
);

CREATE UNIQUE INDEX tea_type_name_uk ON tea_service.tea_type (lower(name));
CREATE INDEX tea_type_name_idx ON tea_service.tea_type (name);

------------------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE tea_service.tea_cup_seq START 1 INCREMENT BY 1;
CREATE TABLE tea_service.tea_cup
(
    tea_cup_id  BIGINT    NOT NULL DEFAULT nextval('tea_service.tea_cup_seq')::regclass,
    user_id     UUID      NOT NULL,
    tea_type_id INT       NOT NULL,
    amount      INT       NOT NULL, -- ml, how big this cup
    stamp       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT tea_cup_pk PRIMARY KEY (tea_cup_id),
    CONSTRAINT tea_cup_amount_chk CHECK ( amount > 0 ),
    CONSTRAINT tea_cup_tea_type_id_fk FOREIGN KEY (tea_type_id) REFERENCES tea_service.tea_type (tea_type_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX tea_cup_user_id_amount ON tea_service.tea_cup (user_id, amount);
CREATE INDEX tea_cup_stamp ON tea_service.tea_cup (stamp);
CREATE INDEX tea_cup_tea_type_id_idx ON tea_service.tea_cup (tea_type_id);

