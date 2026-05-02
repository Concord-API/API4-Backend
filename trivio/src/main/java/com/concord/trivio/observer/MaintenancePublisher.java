package com.concord.trivio.observer;

import com.concord.trivio.entity.Maintenance;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MaintenancePublisher {

    private final List<MaintenanceObserver> observers;

    public MaintenancePublisher(List<MaintenanceObserver> observers) {
        this.observers = new ArrayList<>(observers);
    }

    public void subscribe(MaintenanceObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(MaintenanceObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Maintenance maintenance) {
        for (MaintenanceObserver observer : observers) {
            observer.updateStatus(maintenance);
        }
    }
}