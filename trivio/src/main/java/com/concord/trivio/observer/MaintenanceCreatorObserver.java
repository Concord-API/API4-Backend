package com.concord.trivio.observer;

import com.concord.trivio.entity.Maintenance;
import com.concord.trivio.entity.MaintenanceStatus;
import com.concord.trivio.entity.MaintenanceType;
import com.concord.trivio.repository.MaintenanceRepository;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceCreatorObserver implements MaintenanceObserver {

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceCreatorObserver(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    public void updateStatus(Maintenance maintenance) {
        if (maintenance.getStatus() != MaintenanceStatus.COMPLETED) {
            return;
        }

        long recorrencia = maintenance.getContract().getRecurrenceMaintenance();

        Maintenance proxima = new Maintenance();
        proxima.setContract(maintenance.getContract());
        proxima.setDate(maintenance.getDate().plusDays(recorrencia));
        proxima.setType(MaintenanceType.PREVENTIVA);
        proxima.setStatus(MaintenanceStatus.SCHEDULED);
        proxima.setPreventive(true);
        proxima.setActive(true);
        proxima.setLatitude(maintenance.getLatitude());
        proxima.setLongitude(maintenance.getLongitude());

        maintenanceRepository.save(proxima);
    }
}
