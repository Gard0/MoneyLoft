package com.loftschool.fomin.moneyloft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BalanceFragment extends Fragment {
   private DiagramView mDiagramView;

    static BalanceFragment newInstance() {
        BalanceFragment fragment = new BalanceFragment();


        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_balance, container, false);
        mDiagramView = fragmentView.findViewById(R.id.diagramView);
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDiagramView.update(90, 270);
    }
}
