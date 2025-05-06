-- Insert test data into the service table
INSERT INTO service (id, name, status) VALUES (1, 'Test Service', 'ACTIVE');
INSERT INTO service (id, name, status) VALUES (2, 'Test Service With Speciality', 'ACTIVE');

INSERT INTO specialty (id, name) VALUES (1, 'surgery');
INSERT INTO specialty (id, name) VALUES (2, 'radiology');

INSERT INTO service_speciality_ref (id, specialty_id, service) VALUES (1, 2, 1);

INSERT INTO service_speciality_ref (id, specialty_id, service) VALUES (2, 1, 2);
INSERT INTO service_speciality_ref (id, specialty_id, service) VALUES (3, 2, 2);

