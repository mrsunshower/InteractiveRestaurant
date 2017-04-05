package com.interactiverestaurant;

import com.interactiverestaurant.statistic.StatisticManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by KenTerror on 05.03.2017.
 */
public class DirectorTablet {
    public void printAdvertisementProfit(){
        Map<Date, Long> adCostByDay = StatisticManager.getInstance().getAdvertisementRevenueAgregatedByDay();
        long total = 0;
        for (Map.Entry<Date, Long> pair : adCostByDay.entrySet() ) {
            ConsoleHelper.writeMessage(String.format(Locale.ENGLISH, "%s - %.2f",
                    new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(pair.getKey()),
                    0.01d * pair.getValue()));
            //ConsoleHelper.writeMessage(String.format(Locale.US, "%1$td-%1$tb-%1$tY - %2$.2f", pair.getKey(), pair.getValue()/100.0));
            total += pair.getValue();
        }
        ConsoleHelper.writeMessage(String.format(Locale.US, "Total - %.2f", total/100.0));
    }

    public void printCookWorkloading(){
        Map<Date, Map<String, Integer>> cookWorkload = StatisticManager.getInstance().getCookWorkloadingAgregatedByDay();
        for (Map.Entry<Date, Map<String, Integer>> pair : cookWorkload.entrySet()) {
            ConsoleHelper.writeMessage(String.format("%s", new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(pair.getKey())));
            for (Map.Entry<String, Integer> cooks : pair.getValue().entrySet()) {
                ConsoleHelper.writeMessage(String.format("%s - %d min", cooks.getKey(), (int)Math.ceil(cooks.getValue() / 60.0)));
                //ConsoleHelper.writeMessage(String.format(Locale.US, "%s - %d min", cooks.getKey(), (int) Math.ceil(cooks.getValue()/60.0)));
            }
            ConsoleHelper.writeMessage("");
        }
    }

    public void printActiveVideoSet(){}
    public void printArchivedVideoSet(){}
}
