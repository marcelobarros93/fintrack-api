create table tb_planning (
    id bigserial not null primary key,
    description varchar(100) not null,
    amount decimal(15,2) not null,
    due_day int not null,
    type varchar(10) not null,
    start_at date not null,
    end_at date not null,
    active boolean not null,
    version integer not null,
    created_at timestamp not null default ((now() at time zone 'utc')),
    updated_at timestamp not null
);

alter table tb_expense add column version integer not null default 0;
alter table tb_expense add column created_at timestamp not null default (now() at time zone 'utc');
alter table tb_expense add column updated_at timestamp not null default (now() at time zone 'utc');

alter table tb_income add column version integer not null default 0;
alter table tb_income add column created_at timestamp not null default (now() at time zone 'utc');
alter table tb_income add column updated_at timestamp not null default (now() at time zone 'utc');