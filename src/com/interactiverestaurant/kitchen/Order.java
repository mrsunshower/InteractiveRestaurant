package com.interactiverestaurant.kitchen;

import com.interactiverestaurant.ConsoleHelper;
import com.interactiverestaurant.Tablet;

import java.io.IOException;
import java.util.List;

/**
 * Created by KenTerror on 09.01.2017.
 */
public class Order {
    private Tablet tablet;
    protected List<Dish> dishes;

    public Order(Tablet tablet) throws IOException {
        this.tablet = tablet;
        dishes = ConsoleHelper.getAllDishesForOrder();
    }

    @Override
    public String toString() {
        String line = "";

        if (isEmpty()) {
            return line;
        } else {
            line = dishes.get(0).toString();
            for (int i = 1; i < dishes.size(); i++) {
                line += ", " + dishes.get(i);
            }

            return String.format("Your order: [%s] of %s", line, tablet.toString());
        }
    }

    public int getTotalCookingTime() {
        int time = 0;
        for (int i = 0; i < dishes.size(); i++) {
            time += dishes.get(i).getDuration();
        }
        return time;
    }

    public boolean isEmpty() {
        return dishes.size() == 0;
    }

    public List<Dish> getDishes() {
        return dishes;
    }
}