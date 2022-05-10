alter table tb_expense add column planning_id bigint references tb_planning (id);
alter table tb_income add column planning_id bigint references tb_planning (id);