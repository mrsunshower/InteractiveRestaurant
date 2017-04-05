package com.interactiverestaurant.kitchen;

import com.interactiverestaurant.ConsoleHelper;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by KenTerror on 09.01.2017.
 */
public class Waiter implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        ConsoleHelper.writeMessage(arg + " was cooked by " + o.toString());
    }
}
