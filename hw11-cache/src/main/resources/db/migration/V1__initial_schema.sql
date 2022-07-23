create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table address
(
    id bigserial not null primary key,
    street varchar(50)
);

ALTER TABLE client ADD COLUMN address_id bigint;

ALTER TABLE client
    ADD CONSTRAINT client_address_id_fk
        FOREIGN KEY (address_id) REFERENCES address;

create table phone
(
    id bigserial not null primary key,
    number varchar(50),
    client_id bigint
);

ALTER TABLE phone
    ADD CONSTRAINT phone_client_id_fk
        FOREIGN KEY (client_id) REFERENCES client;
