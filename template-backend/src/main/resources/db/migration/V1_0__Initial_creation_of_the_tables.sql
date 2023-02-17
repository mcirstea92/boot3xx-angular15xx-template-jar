create table if not exists `usr_user`
(
    `id`            bigint       not null primary key,
    `date_of_birth` varchar(255) null,
    `email`         varchar(255) null,
    `name`          varchar(255) null,
    `nickname`      varchar(255) null,
    `password`      varchar(255) null,
    `username`      varchar(255) null,
    `role_id`       bigint       not null
);

create table if not exists `rol_role`
(
    `id`   bigint       not null primary key,
    `name` varchar(255) not null
);

create table if not exists `cfg_configuration`
(
    `id`       bigint       not null primary key,
    `category` varchar(25)  not null,
    `created`  timestamp    not null,
    `name`     varchar(50)  not null,
    `type`     integer      not null,
    `user_id`  bigint       not null,
    `value`    varchar(255) not null,
    `version`  integer      not null
);

create table if not exists `uro_user_roles`
(
    `user_id` bigint not null,
    `role_id` bigint not null
);

insert into `rol_role`
values (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_MODERATOR'),
       (4, 'ROLE_DEMO');

insert into `usr_user` (`id`, `date_of_birth`, `email`, `name`, `nickname`, `password`, `username`, `role_id`)
values (1, '01-01-2022', 'admin@test.ro', 'Admin', 'Admin',
        '$2a$10$xMLkX53QoBKXZUgkhgsAN.lq0kznTgCNM7VxqDd.8C5eojQ9e0soW', 'admin', 1);

insert into `uro_user_roles`(`user_id`, `role_id`)
values (1, 1);


