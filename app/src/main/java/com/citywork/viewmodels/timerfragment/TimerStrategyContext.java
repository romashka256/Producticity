package com.citywork.viewmodels.timerfragment;

import com.citywork.viewmodels.interfaces.ITimerFragmentViewModel;

import lombok.Setter;

public class TimerStrategyContext {

    @Setter
    private TimerStrategy timerStrategy;

    public void onTick(long time, ITimerFragmentViewModel timerFragmentViewModel) {
        timerStrategy.onTick(time, timerFragmentViewModel);
    }

    public void onComplete(ITimerFragmentViewModel timerFragmentViewModel) {
        timerStrategy.onComplete(timerFragmentViewModel);
    }

    public void onCancel(ITimerFragmentViewModel timerFragmentViewModel) {
        timerStrategy.onCancel(timerFragmentViewModel);
    }
}