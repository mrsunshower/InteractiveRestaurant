package com.interactiverestaurant.statistic;

import com.interactiverestaurant.kitchen.Cook;
import com.interactiverestaurant.statistic.event.CookedOrderEventDataRow;
import com.interactiverestaurant.statistic.event.EventDataRow;
import com.interactiverestaurant.statistic.event.EventType;
import com.interactiverestaurant.statistic.event.VideoSelectedEventDataRow;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Collections;


/**
 * Created by KenTerror on 05.03.2017.
 */
public class StatisticManager {
    private static final StatisticManager instance = new StatisticManager();
    private StatisticStorage statisticStorage = new StatisticStorage();
    private Set<Cook> cooks = new HashSet<>();

    public static StatisticManager getInstance() {
        return instance;
    }

    private StatisticManager() {
    }

    public void register(EventDataRow data) {
        statisticStorage.put(data);
    }

    public void register(Cook cook) {
        cooks.add(cook);
    }

    /*private static final int[] TIME_FIELDS =
            {
                    Calendar.HOUR_OF_DAY,
                    Calendar.HOUR,
                    Calendar.AM_PM,
                    Calendar.MINUTE,
                    Calendar.SECOND,
                    Calendar.MILLISECOND
            };

    public TreeMap<Date, Long> getAdvertisementRevenueAgregatedByDay() {
        TreeMap<Date, Long> result = new TreeMap<>();
        for (EventDataRow eventDataRow : statisticStorage.getStorage().get(EventType.SELECTED_VIDEOS)) {
            VideoSelectedEventDataRow vEventDataRow = (VideoSelectedEventDataRow) eventDataRow;
            GregorianCalendar gDate = new GregorianCalendar();
            gDate.setTime(vEventDataRow.getDate());
            for(int i : TIME_FIELDS)
                gDate.clear(i);
            Date date = gDate.getTime();
            Long dayRevenue = result.get(date) ;
            if (dayRevenue == null) dayRevenue = Long.valueOf(0);
            result.put(date, dayRevenue + vEventDataRow.getAmount());
        }
        return result;
    }

    public TreeMap<Date, HashMap<String, Integer>> getCookWorkloadingAgregatedByDay() {
        TreeMap<Date, HashMap<String, Integer>> result = new TreeMap<>();
        for (EventDataRow eventDataRow : statisticStorage.getStorage().get(EventType.COOKED_ORDER)) {
            CookedOrderEventDataRow cookDataRow = (CookedOrderEventDataRow) eventDataRow;
            GregorianCalendar gDate = new GregorianCalendar();
            gDate.setTime(cookDataRow.getDate());
            for(int i : TIME_FIELDS)
                gDate.clear(i);
            Date date = gDate.getTime();
            HashMap<String, Integer> cooksNameWorkDuration = result.get(date);
            if (cooksNameWorkDuration == null) {
                cooksNameWorkDuration = new HashMap<>();
                result.put(date, cooksNameWorkDuration);
            }
            String cookName = cookDataRow.getCookName();
            Integer cookWorkDuration = cooksNameWorkDuration.get(cookName);
            if (cookWorkDuration == null) cookWorkDuration = Integer.valueOf(0);
            cooksNameWorkDuration.put(cookName, cookWorkDuration + cookDataRow.getTime());
        }
        return result;
    }*/

    public Map<Date, Long> getAdvertisementRevenueAgregatedByDay() {
        Map<Date, Long> videoStatistic = new TreeMap<>(Collections.reverseOrder());
        for (EventDataRow eventDataRow : statisticStorage.getStorage().get(EventType.SELECTED_VIDEOS)) {
            Calendar cal = Calendar.getInstance(); // locale-specific
            cal.setTime(eventDataRow.getDate());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long time = cal.getTimeInMillis();
            Date date = new Date(time);

            VideoSelectedEventDataRow videoSelectedEventDataRow = (VideoSelectedEventDataRow) eventDataRow;
            Long cost = videoStatistic.get(date) ;
            if (cost == null) cost = Long.valueOf(0);
            videoStatistic.put(date, cost + videoSelectedEventDataRow.getAmount());
        }
        return videoStatistic;
    }

    public Map<Date, Map<String, Integer>> getCookWorkloadingAgregatedByDay () {
        Map<Date, Map<String, Integer>> cookStatistic = new TreeMap<>(Collections.reverseOrder());

        for (EventDataRow eventDataRow : statisticStorage.getStorage().get(EventType.COOKED_ORDER)) {
            Calendar cal = Calendar.getInstance(); // locale-specific
            cal.setTime(eventDataRow.getDate());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long time = cal.getTimeInMillis();
            Date date = new Date(time);

            Map<String, Integer> cooks = cookStatistic.get(date);
            if (cooks == null) {
                cooks = new TreeMap<>();
                cookStatistic.put(date, cooks);
            }

            CookedOrderEventDataRow cookedOrderEventDataRow = (CookedOrderEventDataRow) eventDataRow;
            String cookName = cookedOrderEventDataRow.getCookName();
            Integer cookingTime = cooks.get(cookName);
            if (cookingTime == null) cookingTime = 0;
            cooks.put(cookName, cookingTime + cookedOrderEventDataRow.getTime());
        }
        return cookStatistic;
    }

    private static class StatisticStorage {
        private Map<EventType, List<EventDataRow>> storage = new HashMap<>();

        private StatisticStorage() {
            for (EventType eventType : EventType.values()) {
                storage.put(eventType, new ArrayList<EventDataRow>());
            }
        }

        private void put(EventDataRow data) {
            storage.get(data.getType()).add(data);
        }

        private Map<EventType, List<EventDataRow>> getStorage() {
            return storage;
        }
    }
}