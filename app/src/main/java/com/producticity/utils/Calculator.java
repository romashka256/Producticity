package com.producticity.utils;

import com.producticity.Constants;
import com.producticity.model.db.models.Building;
import com.producticity.model.db.models.City;
import com.producticity.utils.timer.TimerState;

import java.util.List;

public class Calculator {


    public static String getMinutesAndSecondsFromSeconds(long allseconds) {
        long minutes = allseconds / 60;
        long seconds = allseconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static long getRemainingTime(long stopTimerTime) {
        long current = System.currentTimeMillis();
        return (stopTimerTime - current) / 1000;
    }

    public static int calculatePeopleCount(long time) {
        return (int) ((time / 30) * Constants.DEFAULT_PEOPLE_PER_30SEC);
    }

    public static int calculatePeopleCountByPercent(int all, float percent) {
        return (int) (all * (percent / 100));
    }

    public static int getTime(long startTime, long stopTime) {
        return (int) (stopTime - startTime) / 1000;
    }

    public static int calculatePercentOfTime(long time, long fulltime) {
        return (int) (100 - ((100 * time) / fulltime));
    }

    public static int calculateBuidling(long time) {
        return (int) (time / (15 * 61));
    }

    public static int calculatePeopleCount(List<City> cityList) {
        int count = 0;
        Integer timerState;
        for (City city : cityList) {
            if (city.getBuildings() != null)
                for (Building building : city.getBuildings()) {
                    timerState = building.getPomodoro().getTimerState();
                    if (timerState >= TimerState.WORK_COMPLETED)
                        count += building.getPeople_count();
                }
        }
        return count;
    }
}
