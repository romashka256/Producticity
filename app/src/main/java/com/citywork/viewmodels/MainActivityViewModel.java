package com.citywork.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Build;
import com.citywork.App;
import com.citywork.model.db.DataBaseHelper;
import com.citywork.model.db.models.Building;
import com.citywork.model.db.models.Pomodoro;
import com.citywork.model.interfaces.OnPomodoroLoaded;
import com.citywork.service.TimerService;
import com.citywork.utils.SharedPrefensecUtils;
import com.citywork.utils.TimerManager;
import com.citywork.utils.TimerState;
import com.citywork.viewmodels.interfaces.IMainActivityViewModel;
import lombok.Getter;
import timber.log.Timber;

public class MainActivityViewModel extends ViewModel implements IMainActivityViewModel {

    @Getter
    MutableLiveData<Long> mChangeRemainingTimeEvent = new MutableLiveData<>();
    @Getter
    MutableLiveData<Pomodoro> pomodoroMutableLiveData = new MutableLiveData<>();
    @Getter
    MutableLiveData<Building> buildingMutableLiveData = new MutableLiveData<>();

    private TimerManager timerManager;
    private SharedPrefensecUtils sharedPrefensecUtils;
    private Context context;
    private DataBaseHelper dataBaseHelper;

    public MainActivityViewModel() {
        sharedPrefensecUtils = App.getsAppComponent().getSharedPrefs();
        context = App.getsAppComponent().getApplicationContext();
        dataBaseHelper = App.getsAppComponent().getDataBaseHelper();
    }

    @Override
    public void onCreate() {
        if (sharedPrefensecUtils.getTimerState() == TimerState.ONGOING) {
            dataBaseHelper.getLastBuilding(building -> {
                buildingMutableLiveData.postValue(building);
                Timber.i("Last pomodoro posted");
            });
        }

        timerManager = App.getsAppComponent().getTimerManager();
    }

    @Override
    public void onStop() {
        if (sharedPrefensecUtils.getTimerState() == TimerState.ONGOING) {
            dataBaseHelper.getLastPomodoro(pomodoro -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(TimerService.getIntent(context, pomodoro));
                } else {
                    context.startService(TimerService.getIntent(context, pomodoro));
                }
            });
        }
    }

    @Override
    public long getTimeToGo() {
        return timerManager.getRemainingTime();
    }

    @Override
    public void onServiceConnected(Pomodoro pomodoro) {
        dataBaseHelper.savePomodoro(pomodoro);
    }
}
