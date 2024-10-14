-- Создаем схему crm, если ее нет
create schema if not exists crm;

create table if not exists seller (
      id bigserial PRIMARY KEY NOT NULL,
      name varchar(20),
      contact_info varchar(20),
      registration_date timestamp
);

create table if not exists transactions (
        id bigserial PRIMARY KEY NOT NULL,
        seller_id bigint,
        foreign key (seller_id) references seller (id),
        amount bigint,
        payment_type varchar(8),
        transaction_date timestamp
);