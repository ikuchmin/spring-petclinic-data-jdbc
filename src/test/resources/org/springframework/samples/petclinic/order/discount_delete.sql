delete from order_discount where order_ in (1);
delete from order_item where order_ in (1);
delete from order_ where id in (1);

delete from pet where id in (1);
delete from owner where id in (1);

delete from pet_type where id in (1);

delete from discount where id in (1);

delete from service_price_history where service in (1, 2);
delete from service where id in (1, 2);
