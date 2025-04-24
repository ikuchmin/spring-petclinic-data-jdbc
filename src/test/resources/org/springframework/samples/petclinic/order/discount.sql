INSERT INTO service (id, name) VALUES (1, 'Grooming');
INSERT INTO service_price_history (id, price, effective_from, service) VALUES (1, 75.00, NOW(), 1);

INSERT INTO service (id, name) VALUES (2, 'Pet Checkup');
INSERT INTO service_price_history (id, price, effective_from, service) VALUES (2, 500.00, NOW(), 2);

INSERT INTO discount (id, code, description, percent) VALUES (1, 'DISCOUNT10', 'Discount of 10% for all', 0.10);

INSERT INTO pet_type (id, name) values (1, 'dog');

INSERT INTO owner (id, first_name, last_name) VALUES (1, 'Ivan', 'Petrov');
INSERT INTO pet (id, name, type_id, birth_date, owner_id) VALUES (1, 'Sharik', 1, '2020-01-01', 1);

INSERT INTO order_ (id, total_cost, created_date, last_modified_date, owner_id, pet_id) VALUES (1, 0, NOW(), NOW(), 1, 1);
INSERT INTO order_item (id, service_id, service_name, price, count, discount_cost, cost, order_) VALUES (1, 1, 'Grooming', 75.00, 1, 0.00, 75.00, 1);
INSERT INTO order_item (id, service_id, service_name, price, count, discount_cost, cost, order_) VALUES (2, 2, 'Pet Checkup', 500.00, 1, 0.00, 500.00, 1);

