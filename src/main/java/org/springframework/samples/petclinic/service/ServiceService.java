package org.springframework.samples.petclinic.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;


    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServiceDto retrieveService(Long id) {
        LocalDateTime now = LocalDateTime.now();

        return retrieveServiceOnDate(id, now);
    }

    public ServiceDto retrieveServiceOnDate(Long id, LocalDateTime date) {
        var service = serviceRepository.findById(id).orElseThrow();

        BigDecimal currentPrice = service.getServicePriceHistories().stream()
            .filter(history -> history.getEffectiveFrom().isBefore(date))
            .max(Comparator.comparing(ServicePriceHistory::getEffectiveFrom))
            .map(ServicePriceHistory::getPrice)
            .orElseThrow(() -> new IllegalStateException("No valid price found for the current date"));

        return new ServiceDto(service.getId(), service.getName(), currentPrice);
    }

    public List<Service> inactivateService(Long specialityId) {
        serviceRepository.updateServiceStatusBySpeciality(specialityId, ServiceStatus.INACTIVE);

        return serviceRepository.findByRequiredSpecialities_SpecialtyId(specialityId);
    }
}
