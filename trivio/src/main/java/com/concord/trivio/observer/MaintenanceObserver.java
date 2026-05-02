package com.concord.trivio.observer;

import com.concord.trivio.entity.Maintenance;

public interface MaintenanceObserver {
    void updateStatus(Maintenance maintenance);
}