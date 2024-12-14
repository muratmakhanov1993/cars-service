create extension if not exists "uuid-ossp";

create table cars
(
    id    uuid primary key default uuid_generate_v4(),
    make  varchar                                                            not null,
    model varchar                                                            not null,
    price numeric check ( price > 0 )                                        not null,
    year  int check ( year between 1986 and EXTRACT(YEAR FROM CURRENT_DATE)) not null,
    vin   varchar(17) unique                                                 not null
);

create table users
(
    username varchar primary key,
    password varchar not null,
    role     varchar not null
);