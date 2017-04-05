package com.interactiverestaurant;

import com.interactiverestaurant.ad.AdvertisementManager;
import com.interactiverestaurant.ad.NoVideoAvailableException;
import com.interactiverestaurant.kitchen.Order;

import java.io.IOException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by KenTerror on 09.01.2017.
 */
public class Tablet extends Observable {
    public final int number;
    static Logger logger = Logger.getLogger(Tablet.class.getName());

    public Tablet(int number) {
        this.number = number;
    }

    public Order createOrder() {
        Order order = null;
        try {
            order = new Order(this);

            if (!order.isEmpty()) {
                ConsoleHelper.writeMessage(order.toString());
                setChanged();
                notifyObservers(order);
            }
            AdvertisementManager adManager = new AdvertisementManager(order.getTotalCookingTime() * 60);
            adManager.processVideos();
        } catch (NoVideoAvailableException e) {
            logger.log(Level.INFO, "No video is available for the order " + order);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
            return null;
        }
        return order;
    }

    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }
}
