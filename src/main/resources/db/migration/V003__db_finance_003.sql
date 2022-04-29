create table planning(
    id bigserial not null primary key,
    description varchar(100) not null,
    amount decimal(15,2) not null,
    due_day int not null,
    type varchar(10) not null,
    start_at date not null,
    end_at date not null,
    created_at timestamp not null
)