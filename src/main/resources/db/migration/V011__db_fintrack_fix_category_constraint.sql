alter table tb_category drop constraint tb_category_name_user_id_key;

alter table tb_category add constraint tb_category_name_user_id_type_key unique (name, user_id, type);