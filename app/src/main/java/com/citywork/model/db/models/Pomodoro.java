package com.citywork.model.db.models;

import android.support.annotation.Nullable;

import com.citywork.RealmListParcelConverter;
import com.citywork.utils.timer.TimerState;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.Objects;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;
import io.realm.com_citywork_model_db_models_PomodoroRealmProxy;
import lombok.Getter;
import lombok.Setter;

@Parcel(implementations = {com_citywork_model_db_models_PomodoroRealmProxy.class}, value = Parcel.Serialization.BEAN, analyze = {Pomodoro.class})
public class Pomodoro extends io.realm.RealmObject {

    public Pomodoro() {
        this.tasks = new RealmList<>();
    }

    public Pomodoro(TimerState timerState) {
        this.tasks = new RealmList<>();
        setTimerState(timerState);
    }

    public Pomodoro(long starttime, long stoptime, TimerState timerState) {
        this.starttime = starttime;
        this.stoptime = stoptime;
        this.tasks = new RealmList<>();
        setTimerState(timerState);
    }

    public Pomodoro(long starttime, long stoptime, @NotNull RealmList<Task> tasks, TimerState timerState) {
        this.starttime = starttime;
        setTimerState(timerState);
        this.stoptime = stoptime;
        this.tasks = tasks;
    }

    @PrimaryKey
    @Getter
    @Setter
    Long id;

    long starttime;

    long stoptime;

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public Long getStoptime() {
        return stoptime;
    }

    public void setStoptime(Long stoptime) {
        this.stoptime = stoptime;
    }

    RealmList<Task> tasks;

    String timerState;

    @Getter
    @Setter
    long reststarttime;
    @Getter
    @Setter
    long stopresttime;

    @Nullable
    @ParcelPropertyConverter(RealmListParcelConverter.class)
    public RealmList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(@Nullable RealmList<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Pomodoro{" +
                "id=" + id +
                ", starttime=" + starttime +
                ", stoptime=" + stoptime +
                ", tasks=" + tasks +
                ", timerState=" + timerState +
                ", reststarttime=" + reststarttime +
                ", stopresttime=" + stopresttime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pomodoro pomodoro = (Pomodoro) o;
        return getId() == pomodoro.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public TimerState getTimerState() {
        return TimerState.valueOf(timerState);
    }

    public void setTimerState(TimerState timerState) {
        this.timerState = timerState.toString();
    }
}
