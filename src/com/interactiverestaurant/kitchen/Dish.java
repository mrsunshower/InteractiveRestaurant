package com.interactiverestaurant.kitchen;

public enum Dish {
    Fish(25),
    Steak(30),
    Soup(15),
    Juice(5),
    Water(3);

    private int duration;

    Dish(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public static String allDishesToString() {
        String line = "";
        line += Dish.values()[0];
        for (int i = 1; i < Dish.values().length; i++) {
            line += ", " + Dish.values()[i];
        }
        return line;
    }
}
