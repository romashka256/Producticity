package com.producticity.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.producticity.R;
import com.producticity.viewmodels.SettingsViewModel;
import com.zcw.togglebutton.ToggleButton;

import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.producticity.ui.BreakChooseDialog.breakTypeTag;
import static com.producticity.ui.BreakChooseDialog.selectedTag;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.settings_toolbar_back)
    ImageView backButtomIV;
    @BindView(R.id.settings_long_winnotif_block)
    RelativeLayout winnotifBlock;
    @BindView(R.id.settings_long_startendsound_block)
    RelativeLayout startendsoundBlock;
    @BindView(R.id.settings_long_notifbar_block)
    RelativeLayout notifbarBlock;
    @BindView(R.id.settings_short_break_block)
    LinearLayout shortBreakBlock;
    @BindView(R.id.settings_long_break_block)
    LinearLayout longBreakBlock;
    @BindView(R.id.settings_long_winnotif_switch)
    ToggleButton winnotifSwitch;
    @BindView(R.id.settings_long_startendsound_switch)
    ToggleButton startendsoundSwitch;
    @BindView(R.id.settings_long_notifbar_switch)
    ToggleButton notifbarSwitch;
    @BindView(R.id.settings_short_break_tv)
    TextView shortBreakTV;
    @BindView(R.id.settings_long_break_tv)
    TextView longBreakTV;


    private MainActivity mainActivity;
    private SettingsViewModel settingsViewModel;


    public static final String TAG = "BreakChooseDialog";
    private int BREAK_REQUEST_CODE = 123;

    @Override
    public void onAttach(Context context) {
        mainActivity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.onCreate();

        settingsViewModel.getMShortBreakChangedEvent().observe(this, integer -> {
            shortBreakTV.setText(integer + " " + getResources().getString(R.string.minute_short));
        });

        settingsViewModel.getMLongBreakChangedEvent().observe(this, integer -> {
            longBreakTV.setText(integer + " " + getResources().getString(R.string.minute_short));
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, null);

        ButterKnife.bind(this, view);

        if (settingsViewModel.getIsWinNotifEnabled())
            winnotifSwitch.toggle();

        if (settingsViewModel.getIsNotifBarEnabled())
            notifbarSwitch.toggle();

        if (settingsViewModel.getIsStartEndSoundEnabled())
            startendsoundSwitch.toggle();

        winnotifSwitch.setOnToggleChanged(on -> {
            settingsViewModel.onWinOnNotifToggleChanged(on);
        });

        startendsoundSwitch.setOnToggleChanged(on -> {
            settingsViewModel.onStartEndSoundToggleChanged(on);
        });

        notifbarSwitch.setOnToggleChanged(on -> {
            settingsViewModel.onInNotifToggleChanged(on);
        });

        longBreakBlock.setOnClickListener(v -> {
            settingsViewModel.onLongBreakClicked();
            showBreakDialog(BreakChooseDialog.BreakType.LONG);
        });

        shortBreakBlock.setOnClickListener(v -> {
            settingsViewModel.onShortBreakClicked();
            showBreakDialog(BreakChooseDialog.BreakType.SHORT);
        });

        winnotifBlock.setOnClickListener(this);
        startendsoundBlock.setOnClickListener(this);
        notifbarBlock.setOnClickListener(this);

        shortBreakTV.setText(settingsViewModel.getShortBreakValue() + " мин");
        longBreakTV.setText(settingsViewModel.getLongBreakValue() + " мин");

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BREAK_REQUEST_CODE) {
            settingsViewModel.onDialogFinished(data);
        }
    }

    public void showBreakDialog(BreakChooseDialog.BreakType breakType) {
        BreakChooseDialog dialog = new BreakChooseDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(selectedTag, settingsViewModel.getSelectedBreak());
        bundle.putString(breakTypeTag, breakType.toString());
        dialog.setTargetFragment(this, BREAK_REQUEST_CODE);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), TAG);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backButtomIV.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });

        mainActivity.hideBottomNav();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_long_winnotif_block:
                winnotifSwitch.toggle();
                break;
            case R.id.settings_long_startendsound_block:
                startendsoundSwitch.toggle();
                break;
            case R.id.settings_long_notifbar_block:
                notifbarSwitch.toggle();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        mainActivity.showBottomNav();
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.hideBottomNav();
    }
}
