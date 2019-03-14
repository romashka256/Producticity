package com.citywork.utils;

import com.citywork.Constants;
import com.citywork.model.db.models.Building;
import com.citywork.model.db.models.City;
import com.citywork.model.db.models.Pomodoro;
import com.citywork.utils.timer.TimerState;

import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

public class PomodoroManger {

    @Getter
    @Setter
    private Pomodoro pomodoro;
    @Getter
    private Building building;
    @Setter
    @Getter
    private int peopleCount;
    @Getter
    @Setter
    private City city;

    private Long timerValue;

    public void setBuilding(Building building) {
        this.building = building;
        this.pomodoro = building.getPomodoro();
        this.peopleCount = building.getPeople_count();

        if (city != null && city.getBuildings() != null && city.getBuildings().contains(building)) {
            this.building = city.getBuildings().get(city.getBuildings().indexOf(building));
        }
    }

    public void createEmptyInstance() {
        pomodoro = new Pomodoro(TimerState.NOT_ONGOING);
        if (timerValue != null) {
            building = new Building(pomodoro);
        } else {
            building = new Building(pomodoro);
        }
    }

    public void setTimeToPomodoro(long timerValue) {
        long startTime = System.currentTimeMillis();
        long stopTime = startTime + timerValue * 1000;

        pomodoro.setStarttime(startTime);
        pomodoro.setStoptime(stopTime);
        building.setPeople_count(calculatePeopleCount(startTime, stopTime));
        if (city == null)
            city = new City();
        city.getBuildings().add(building);
    }

    public int calculatePeopleCount(long starttime, long stopTime) {
        return Calculator.calculatePeopleCount(Calculator.getTime(starttime, stopTime));
    }

    public int calculatePeopleCount(long time) {
        return Calculator.calculatePeopleCount(time);
    }

    public TimerState setComleted() {
        if (pomodoro.getTimerState() == TimerState.ONGOING) {
            pomodoro.setTimerState(TimerState.WORK_COMPLETED);
            return TimerState.WORK_COMPLETED;
        } else if (pomodoro.getTimerState() == TimerState.REST_ONGOING) {
            pomodoro.setTimerState(TimerState.COMPLETED);
            return TimerState.COMPLETED;
        }

        return TimerState.WORK_COMPLETED;
    }

    public TimerState prepareBeforeStart() {
        if (pomodoro.getTimerState() != TimerState.REST && pomodoro.getTimerState() != TimerState.REST_ONGOING) {
            pomodoro.setTimerState(TimerState.ONGOING);
            return TimerState.ONGOING;
        } else {
            pomodoro.setTimerState(TimerState.REST_ONGOING);
            return TimerState.REST_ONGOING;
        }
    }
}
