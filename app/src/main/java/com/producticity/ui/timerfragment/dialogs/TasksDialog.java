package com.producticity.ui.timerfragment.dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.producticity.App;
import com.producticity.R;
import com.producticity.model.db.models.Pomodoro;
import com.producticity.ui.FixedMaxHeightRecylerView;
import com.producticity.ui.TaskListAdapter;
import com.producticity.ui.timerfragment.ITaskDialog;
import com.producticity.utils.commonutils.UIUtils;
import com.producticity.viewmodels.TasksDialogViewModel;
import com.producticity.viewmodels.interfaces.ITasksDialogViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksDialog extends DialogFragment implements ITaskDialog {

    private Context context;
    @BindView(R.id.tasks_dialog_rv)
    FixedMaxHeightRecylerView recyclerView;
    @BindView(R.id.tasks_dialog_closeiv)
    ImageView closeIV;
    @BindView(R.id.tasks_dialog_edittext)
    EditText editText;
    @BindView(R.id.tasks_dialog_sendiv)
    ImageView sendIV;
    @BindView(R.id.tasks_dialog_settings)
    ImageView settingIV;
    @BindView(R.id.tasks_dialog_no_tasks_tv)
    TextView noTasksTV;
    @BindView(R.id.tasks_dialog_title)
    TextView titleTV;

    public static final String TAG = "TasksDialog";

    private UIUtils UIUtils;

    private ITasksDialogViewModel iTasksDialogViewModel;
    private TaskListAdapter taskListAdapter;

    public static TasksDialog getInstance() {
        return new TasksDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UIUtils = App.getsAppComponent().getFontUtils();
        context = App.getsAppComponent().getApplicationContext();

        iTasksDialogViewModel = ViewModelProviders.of(this).get(TasksDialogViewModel.class);
        iTasksDialogViewModel.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_dialog, container, true);
        ButterKnife.bind(this, view);

        setFonts();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void updateList(List<Pomodoro> pomodoroList) {

    }

    @Override
    public void onStop() {
        super.onStop();
        iTasksDialogViewModel.onStop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));

        iTasksDialogViewModel.getPomodoroLoadedEvent().observe(this, pomodoros -> {

            taskListAdapter = new TaskListAdapter(context, pomodoros, task -> {
                iTasksDialogViewModel.onTaskClicked(task);
            });

            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(taskListAdapter);
            recyclerView.getLayoutManager().scrollToPosition(taskListAdapter.getPomodoros().size() - 1);

        });

        iTasksDialogViewModel.getUpdatePomodoroListEvent().observe(this, position -> {
            taskListAdapter.notifyItemChanged(position);
        });

        iTasksDialogViewModel.getNoTasksEvent().observe(this, empty -> {
            if (empty) {
                noTasksTV.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                noTasksTV.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        closeIV.setOnClickListener(v -> {
            dismiss();
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    editText.setTypeface(UIUtils.getLight());
                }
                iTasksDialogViewModel.onTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendIV.setOnClickListener(v -> {
            iTasksDialogViewModel.onAddClicked();
            taskListAdapter.notifyItemChanged(0);
            recyclerView.getLayoutManager().scrollToPosition(taskListAdapter.getPomodoros().size() - 1);
            editText.setText("");
        });

        settingIV.setOnClickListener(v -> {
            dismiss();
            Navigation.findNavController(getActivity(), R.id.toolbar_settings_iv).navigate(R.id.action_timerFragment_to_settingsFragment);
        });

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        iTasksDialogViewModel.onDismiss();
    }

    private void setFonts() {
        titleTV.setTypeface(UIUtils.getLight());
        editText.setTypeface(UIUtils.getLight());
        noTasksTV.setTypeface(UIUtils.getLight());
    }
}
