create table if not exists user
(
    id            bigint       not null primary key,
    date_of_birth varchar(255) null,
    email         varchar(255) null,
    name          varchar(255) null,
    nickname      varchar(255) null,
    password      varchar(255) null,
    username      varchar(255) null,
    role_id       bigint       not null
);

create table if not exists role
(
    id   bigint       not null primary key,
    name varchar(255) not null
);

insert into role
values (1, 'Admin'),
       (2, 'User'),
       (3, 'Demo');

insert into user (id, date_of_birth, email, name, nickname, password, username, role_id)
    value (1, '01-01-2022', 'admin@test.ro', 'Admin', 'Admin',
           '$2a$10$xMLkX53QoBKXZUgkhgsAN.lq0kznTgCNM7VxqDd.8C5eojQ9e0soW', 'admin', 1);