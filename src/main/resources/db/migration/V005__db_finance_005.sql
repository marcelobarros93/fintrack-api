alter table tb_expense add column user_id varchar(36) not null;
alter table tb_income add column user_id varchar(36) not null;
alter table tb_planning add column user_id varchar(36) not null;