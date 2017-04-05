package com.interactiverestaurant;

import com.interactiverestaurant.kitchen.Cook;
import com.interactiverestaurant.kitchen.Waiter;

/**
 * Created by KenTerror on 09.01.2017.
 */
public class Restaurant {
    public static void main(String[] args) {
        // First order
        Tablet tablet = new Tablet(5);
        Cook cook = new Cook("Ivanov");
        tablet.addObserver(cook);
        Waiter waitor = new Waiter();
        cook.addObserver(waitor);
        tablet.createOrder();
        // Second Order
        Tablet tablet2 = new Tablet(5);
        Cook cook2 = new Cook("Petrov");
        tablet2.addObserver(cook2);
        Waiter waitor2 = new Waiter();
        cook2.addObserver(waitor2);
        tablet2.createOrder();
        // Third order
        Tablet tablet3 = new Tablet(5);
        Cook cook3 = new Cook("Ivanov");
        tablet3.addObserver(cook3);
        Waiter waitor3 = new Waiter();
        cook3.addObserver(waitor3);
        tablet3.createOrder();
        // Fourth order
        Tablet tablet4 = new Tablet(5);
        Cook cook4 = new Cook("Ivanov");
        tablet4.addObserver(cook4);
        Waiter waitor4 = new Waiter();
        cook4.addObserver(waitor4);
        tablet4.createOrder();
        // Fifth order
        Tablet tablet5 = new Tablet(5);
        Cook cook5 = new Cook("Petrov");
        tablet5.addObserver(cook5);
        Waiter waitor5 = new Waiter();
        cook5.addObserver(waitor5);
        tablet5.createOrder();
        DirectorTablet directorTablet = new DirectorTablet();
        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();
    }
}