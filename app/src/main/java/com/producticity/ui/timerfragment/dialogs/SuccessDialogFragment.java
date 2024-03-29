package com.producticity.ui.timerfragment.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.producticity.App;
import com.producticity.R;
import com.producticity.model.db.models.Building;
import com.producticity.utils.commonutils.UIUtils;
import com.producticity.ui.customviews.BuldingProgressView;
import com.producticity.utils.commonutils.VectorUtils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuccessDialogFragment extends DialogFragment {

    private Context context;

    @BindView(R.id.success_dialog_close_iv)
    ImageView mCloseIV;
    @BindView(R.id.success_dialog_to_city_btn)
    Button mToCityBtn;
    @BindView(R.id.success_dialog_buidling_view)
    BuldingProgressView mBuidlingView;
    @BindView(R.id.success_dialog_congrats_tv)
    TextView mCongratsTV;
    @BindView(R.id.success_dialog_congrats1_tv)
    TextView mCongrats1TV;

    private UIUtils UIUtils;

    private Building building;
    private static final String BUILDING = "buidling";

    public static SuccessDialogFragment newInstance(Building building) {
        SuccessDialogFragment f = new SuccessDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(BUILDING, Parcels.wrap(building));
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UIUtils = App.getsAppComponent().getFontUtils();
        context = App.getsAppComponent().getApplicationContext();

        building = Parcels.unwrap((Parcelable) getArguments().get(BUILDING));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.success_dialog, container, false);

        ButterKnife.bind(this, view);

        mCloseIV.setOnClickListener(v -> dismiss());

        mBuidlingView.setPeopleProgress(building.getPeople_count(), building.getPeople_count());
        mBuidlingView.setImage(VectorUtils.getBitmapFromVectorDrawable(App.getsAppComponent().getApplicationContext(), getResources().getIdentifier(building.getIconName(), "drawable", context.getPackageName())));

        mBuidlingView.setProgress(100);

        mToCityBtn.setOnClickListener(v -> dismiss());

        setFonts();

        return view;
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();

        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // window.setLayout((int) (size.x * 0.80),(int) (size.y * 0.5));
        window.setGravity(Gravity.CENTER);

        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    private void setFonts() {
        mToCityBtn.setTypeface(UIUtils.getRegular());
        mCongrats1TV.setTypeface(UIUtils.getRegular());
        mCongratsTV.setTypeface(UIUtils.getRegular());
    }
}
