create table tb_expense (
  id bigserial not null primary key,
  description varchar(100) not null,
  amount decimal(15,2) not null,
  status varchar(10) not null,
  date_due date not null,
  date_payment date
);

create table tb_income (
  id bigserial not null primary key,
  description varchar(100) not null,
  amount decimal(15,2) not null,
  status varchar(10) not null,
  date_due date not null,
  date_receipt date
);