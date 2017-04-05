package com.interactiverestaurant.kitchen;

import com.interactiverestaurant.ConsoleHelper;
import com.interactiverestaurant.statistic.StatisticManager;
import com.interactiverestaurant.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by KenTerror on 09.01.2017.
 */
public class Cook extends Observable implements Observer {
    String name;

    public Cook(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void update(Observable o, Object arg) {
        Order order = (Order) arg;
        CookedOrderEventDataRow event = new CookedOrderEventDataRow(o.toString(), this.toString(), order.getTotalCookingTime()*60, order.getDishes());
        StatisticManager.getInstance().register(event);
        ConsoleHelper.writeMessage("Start cooking - " + arg + ", cooking time " + order.getTotalCookingTime() + "min");
        setChanged();
        notifyObservers(arg);
    }
}