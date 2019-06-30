package com.loftschool.fomin.moneyloft;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class BudgetFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 1001;


    private String mParam1;
    private String mParam2;

    private ItemsAdapter mItemsAdapter;

    public BudgetFragment() {
        // Required empty public constructor
    }

    static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        RecyclerView recyclerView = fragmentView.findViewById((R.id.recycler_view));

        mItemsAdapter = new ItemsAdapter();

        recyclerView.setAdapter(mItemsAdapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));


        mItemsAdapter.addItem(new Item("Молоко",70));
        mItemsAdapter.addItem(new Item("Зубная щётка",70));
        mItemsAdapter.addItem(new Item("Сковородка с антипригарным покрытием",1670));

        Button openAddScreenButton = fragmentView.findViewById(R.id.open_add_screen);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(new Intent(getContext(), AddItemActivity.class), REQUEST_CODE);
            }

        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(final int requestCode,  final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Item item = new Item(data.getStringExtra("name"), Integer.parseInt(data.getStringExtra("price")));
            mItemsAdapter.addItem(item);
        }
    }
}