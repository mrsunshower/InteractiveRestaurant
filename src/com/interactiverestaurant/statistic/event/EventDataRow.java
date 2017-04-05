package com.interactiverestaurant.statistic.event;

import java.util.Date;

/**
 * Created by KenTerror on 05.03.2017.
 */
public interface EventDataRow {
    EventType getType();
    Date getDate();
    int getTime();
}
