create table tb_category(
    id bigserial primary key not null,
    name varchar(20) not null,
    user_id varchar(36) references tb_user (id) not null,
    active boolean not null default true,
    type varchar(10) not null,
    version integer not null,
    created_at timestamp not null default ((now() at time zone 'utc')),
    updated_at timestamp not null,
    unique (name, user_id)
);

alter table tb_planning add column category_id bigint references tb_category (id);
alter table tb_income add column category_id bigint references tb_category (id);
alter table tb_expense add column category_id bigint references tb_category (id);