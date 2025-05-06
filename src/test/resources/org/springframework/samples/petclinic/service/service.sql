-- Insert test data into the service table
INSERT INTO service (id, name, status) VALUES (1, 'Test Service', 'ACTIVE');

-- Insert test data into the service_price_history table
INSERT INTO service_price_history (id, service, price, effective_from) VALUES
(1, 1, 50.00, NOW() - INTERVAL '10 DAYS'),
(2, 1, 75.00, NOW() - INTERVAL '5 DAYS'),
(3, 1, 100.00, NOW() + INTERVAL '1 DAY');
