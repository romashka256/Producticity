package com.citywork.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.citywork.App;
import com.citywork.R;
import com.citywork.viewmodels.TasksDialogViewModel;
import com.citywork.viewmodels.interfaces.ITasksDialogViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksDialog extends DialogFragment {

    private Context context;
    @BindView(R.id.tasks_dialog_rv)
    RecyclerView recyclerView;
    @BindView(R.id.tasks_dialog_closeiv)
    ImageView closeIV;
    @BindView(R.id.tasks_dialog_edittext)
    EditText editText;
    @BindView(R.id.tasks_dialog_sendiv)
    ImageView sendIV;
    @BindView(R.id.tasks_dialog_settings)
    ImageView settingIV;

    private ITasksDialogViewModel iTasksDialogViewModel;
    private TaskListAdapter taskListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = App.getsAppComponent().getApplicationContext();

        iTasksDialogViewModel = ViewModelProviders.of(this).get(TasksDialogViewModel.class);
        iTasksDialogViewModel.onCreate();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_dialog, null, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        iTasksDialogViewModel.getPomodoroLoadedEvent().observe(this, pomodoros -> {
            taskListAdapter = new TaskListAdapter(context, pomodoros);
            recyclerView.setAdapter(taskListAdapter);
        });

        closeIV.setOnClickListener(v -> {
            //    this.dismiss();
        });
    }
}
