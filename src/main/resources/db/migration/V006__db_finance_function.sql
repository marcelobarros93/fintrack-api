create function date_trunc_month(mydate date)
returns date
language plpgsql
as
$$

begin
   return date_trunc('month', mydate);
end;
$$;