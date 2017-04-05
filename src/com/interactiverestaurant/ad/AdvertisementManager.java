package com.interactiverestaurant.ad;

import com.interactiverestaurant.ConsoleHelper;
import com.interactiverestaurant.statistic.StatisticManager;
import com.interactiverestaurant.statistic.event.VideoSelectedEventDataRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() throws NoVideoAvailableException {
        // ищем список видео для показа согласно критериям
        List<Advertisement> bestAds = new VideoHelper().findAllYouNeed();
        // сортируем полученный список
        Collections.sort(bestAds, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement video1, Advertisement video2) {
                long dif = video2.getAmountPerOneDisplaying() - video1.getAmountPerOneDisplaying();
                if (dif == 0) dif = video2.getDuration() - video1.getDuration();
                return (int) dif;
            }
        });

        long totalCost = 0;
        int totalTime = 0;
        for (Advertisement bestAd : bestAds) {
            totalCost += bestAd.getAmountPerOneDisplaying();
            totalTime += bestAd.getDuration();
        }
        VideoSelectedEventDataRow event = new VideoSelectedEventDataRow(bestAds, totalCost, totalTime);
        StatisticManager.getInstance().register(event);

        // выводим список
        for (Advertisement ad : bestAds) {
            ConsoleHelper.writeMessage(ad.getName() + " is displaying... " +
                    ad.getAmountPerOneDisplaying() + ", " +
                    1000 * ad.getAmountPerOneDisplaying() / ad.getDuration());
            ad.revalidate();
        }
    }

    private class VideoHelper {
        private int bestPrice = 0;
        private int maxTime = 0;
        private int numberOfClips = 0;
        private List<Advertisement> bestAds = new ArrayList<>();
        private List<Advertisement> candidates = new ArrayList<>();

        public List<Advertisement> findAllYouNeed() {
            // отбор кандидатов
            for (Advertisement ad : storage.list()) {
                if (ad.getDuration() <= timeSeconds && ad.getHits() > 0)
                    candidates.add(ad);
            }
            if (candidates.isEmpty()) {
                throw new NoVideoAvailableException();
            } else {
                findBestAds(new BinaryPattern(candidates.size()));
            }
            return bestAds;
        }

        // рекурсивная функция формирования списка для показа
        public void findBestAds(BinaryPattern pattern) {
            while (true) {
                checkAds(pattern.getPattern());
                if (!pattern.full()) pattern.increment();
                else break;
                findBestAds(pattern);
            }
        }

        // проверка очередного набора видеоклипов
        private void checkAds(int[] pattern) {
            int price = 0;
            int time = 0;
            List<Advertisement> list = new ArrayList<>();
            for (int i = 0; i < candidates.size(); i++) {
                price += candidates.get(i).getAmountPerOneDisplaying() * pattern[i];
                time += candidates.get(i).getDuration() * pattern[i];
                if (pattern[i] == 1) list.add(candidates.get(i));
            }
            if (time > timeSeconds) return;
            if (!(price > bestPrice)) {
                if (!(price == bestPrice && time > maxTime)) {
                    if (!(price == bestPrice && time == maxTime && list.size() < numberOfClips)) {
                        return;
                    }
                }
            }
            bestAds = list;
            bestPrice = price;
            maxTime = time;
            numberOfClips = list.size();
        }

        // формирование двоичных масок для сбора списка видеоклипов
        private class BinaryPattern {
            private int length;
            private int count;

            public BinaryPattern(int size) {
                this.length = size;
                this.count = 0;
            }

            public int[] getPattern() {
                String regString = Integer.toBinaryString(count);
                int dif = length - regString.length();
                int[] pattern = new int[length];
                for (int j = dif; j < pattern.length; j++) {
                    if (regString.charAt(j - dif) == '1') pattern[j] = 1;
                    else pattern[j] = 0;
                }
                return pattern;
            }

            public void increment() {
                count++;
            }

            public boolean full() {
                return count == (int) Math.pow(2, length) - 1;
            }
        }
    }
}
/*
public class AdvertisementManager {
    private static Map<List<Advertisement>, SetFuture> powerSet = new HashMap<>();
    private static int maxSetCost = 0;
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() {
        List<Advertisement> candidates = new ArrayList<>();
        //Include only compatible by duration video and video that have more than 0 hits
        for (Advertisement ad : storage.list()) {
            if (ad.getDuration() <= timeSeconds && ad.getHits() > 0)
                candidates.add(ad);
        }

        //If no one compatible
        if (candidates.isEmpty()) {
            throw new NoVideoAvailableException();
        }

        //build a powerset in a map(list, duration and cost);
        buildPowerSet(candidates);

        //remove all sets that exceeded time of cooking and calculate time and cost of each set, and find maxSetCost in addition
        fitSetsInTime();

        //remove all sets that cost less then maxSetCost
        getTheMostExpensiveSet();

        //if sets more than a one, get optimal one with bigger duration time.
        if(powerSet.size() > 1) {
            getOptimalVideoSetByDuration();
        }

        //If time equals then get optimal one with less clips in a set
        List<Advertisement> optimalVideoList = getOptimalVideoSetBySetSize();


        Collections.sort(optimalVideoList, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                int result = (int)(o2.getAmountPerOneDisplaying() - o1.getAmountPerOneDisplaying());
                if (result == 0) {
                    result = o2.getDuration() - o1.getDuration();
                }

                return result;
            }
        });

        //Show videos & update ads' data
        show(optimalVideoList);
    }

    private void buildPowerSet(List<Advertisement> list) {
        powerSet.put(list, new SetFuture());
        for (int i = 0; i < list.size(); i++) {
            List<Advertisement> temp = new ArrayList<>(list);
            temp.remove(i);
            if (temp.size() != 0){
                buildPowerSet(temp);
            }
        }
    }

    private void fitSetsInTime() {
        Iterator<Map.Entry<List<Advertisement>, SetFuture>> iterator = powerSet.entrySet().iterator();
        while(iterator.hasNext()) {
            int setSeconds = 0;
            int setCost = 0;
            Map.Entry<List<Advertisement>, SetFuture> pair = iterator.next();

            for (Advertisement advertisement : pair.getKey()) {
                setSeconds += advertisement.getDuration();
                setCost += advertisement.getAmountPerOneDisplaying();
            }

            if (setSeconds > timeSeconds) {
                iterator.remove();
            } else {
                if(setCost > maxSetCost) {
                    maxSetCost = setCost;
                }

                pair.getValue().setSetCost(setCost);
                pair.getValue().setSetDuration(setSeconds);
            }
        }
    }

    private void getTheMostExpensiveSet() {
        Iterator<Map.Entry<List<Advertisement>, SetFuture>> iterator = powerSet.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<List<Advertisement>, SetFuture> pair = iterator.next();
            if (pair.getValue().getSetCost() != maxSetCost) {
                iterator.remove();
            }
        }
    }

    private void getOptimalVideoSetByDuration() {
        int maxSetDuration = 0;
        for (SetFuture setFuture : powerSet.values()) {
            if(setFuture.getSetDuration() > maxSetDuration) {
                maxSetDuration = setFuture.getSetDuration();
            }
        }

        Iterator<Map.Entry<List<Advertisement>, SetFuture>> iterator = powerSet.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<List<Advertisement>, SetFuture> pair = iterator.next();
            if (pair.getValue().getSetDuration() != maxSetDuration) {
                iterator.remove();
            }
        }
    }

    private List<Advertisement> getOptimalVideoSetBySetSize() {
        int lessSize = Integer.MAX_VALUE;
        for (List<Advertisement> list : powerSet.keySet()) {
            if(list.size() < lessSize) {
                lessSize = list.size();
            }
        }

        Iterator<Map.Entry<List<Advertisement>, SetFuture>> iterator = powerSet.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<List<Advertisement>, SetFuture> pair = iterator.next();
            if (pair.getKey().size() == lessSize) {
                return pair.getKey();
            }
        }

        return new ArrayList<>();
    }

    private void show(List<Advertisement> optimalVideoList) {
        for (Advertisement ad : optimalVideoList){
            ConsoleHelper.writeMessage( ad.getName() + " is displaying... " +
                    ad.getAmountPerOneDisplaying() + ", " +
                    1000 * ad.getAmountPerOneDisplaying()/ad.getDuration() );
            ad.revalidate();
        }
    }

    private class SetFuture {
        private int setCost;
        private int setDuration;

        public SetFuture() {
            setCost = 0;
            setDuration = 0;
        }

        public int getSetCost() {
            return setCost;
        }

        public void setSetCost(int setCost) {
            this.setCost = setCost;
        }

        public int getSetDuration() {
            return setDuration;
        }

        public void setSetDuration(int setDuration) {
            this.setDuration = setDuration;
        }
    }
}*/
