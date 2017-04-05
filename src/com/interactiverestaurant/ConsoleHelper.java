package com.interactiverestaurant;

import com.interactiverestaurant.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KenTerror on 09.01.2017.
 */
public class ConsoleHelper {
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString()  throws IOException {
        return bufferedReader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
        writeMessage(Dish.allDishesToString());

        List<Dish> allDishesForOrder = new ArrayList<>();
        writeMessage("Choose dish or type exit");

        String line = "";
        while (!(line = readString()).equalsIgnoreCase("exit")) {
            try {
                allDishesForOrder.add(Dish.valueOf(line));
            } catch (Exception e) {
                System.out.println(line + " is not detected");
            }
        }

        return allDishesForOrder;
    }
}
