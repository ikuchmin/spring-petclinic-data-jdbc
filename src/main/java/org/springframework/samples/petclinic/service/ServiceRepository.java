package org.springframework.samples.petclinic.service;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ServiceRepository extends ListCrudRepository<Service, Long> {

    @Modifying
    @Query("""
        UPDATE service s SET status = :status WHERE s.id IN
                 (SELECT sr.service FROM service_speciality_ref sr WHERE sr.specialty_id = :specialityId)""")
    int updateServiceStatusBySpeciality(Long specialityId, ServiceStatus status);

    @Query("""
        SELECT s.* FROM service s
        JOIN service_speciality_ref sr ON s.id = sr.service
        WHERE sr.specialty_id = :specialityId""")
    List<Service> findByRequiredSpecialities_SpecialtyId(Long specialityId);
}

