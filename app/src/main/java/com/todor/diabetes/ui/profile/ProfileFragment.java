package com.todor.diabetes.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todor.diabetes.R;
import com.todor.diabetes.ui.BaseFragment;

/**
 * Created by todor on 20.12.15
 */
public class ProfileFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        return v;
    }

    @Override
    public String getFragmentTitle() {
        return getResources().getString(R.string.title_profile);
    }
}