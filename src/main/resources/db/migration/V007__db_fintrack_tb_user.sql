create table tb_user(
    id varchar(36) not null primary key,
    name varchar(100) not null,
    email varchar(100) not null,
    password varchar(255) not null
);