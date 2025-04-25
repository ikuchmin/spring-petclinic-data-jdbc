package org.springframework.samples.petclinic.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

@Service
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
}
