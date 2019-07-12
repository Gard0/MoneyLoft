package com.loftschool.fomin.moneyloft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PRICE_COLOR = "price_color";
    private static final String TYPE = "type";
    static final int REQUEST_CODE;

    static {
        REQUEST_CODE = 1001;
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ItemsAdapter mItemsAdapter;
    private Api mApi;

    public BudgetFragment() {
        // Required empty public constructor
    }

    static BudgetFragment newInstance(FragmentType fragmentType) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(PRICE_COLOR, fragmentType.getPriceColor());
        args.putString(TYPE, fragmentType.name());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((LoftMoneyApp) Objects.requireNonNull(getActivity()).getApplication()).getApi();

    }

    @Override
    public void onStart() {
        super.onStart();
        loadItems();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        RecyclerView recyclerView = fragmentView.findViewById((R.id.recycler_view));
        mSwipeRefreshLayout = fragmentView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        assert getArguments() != null;
        mItemsAdapter = new ItemsAdapter(getArguments().getInt(PRICE_COLOR));

        recyclerView.setAdapter(mItemsAdapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));

        return fragmentView;

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("auth_token", "");
            final int price = Integer.parseInt(data.getStringExtra("price"));
            final String name = data.getStringExtra("name");
            assert getArguments() != null;
            Call<Status> call = mApi.addItems(new AddItemsRequest(price, name, getArguments().getString(TYPE)), token);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    loadItems();

                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {

                }
            });
        }

    }

    private void loadItems() {
        final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("auth_token", "");
        assert getArguments() != null;
        Call<List<Item>> itemsResponseCall = mApi.getItems(getArguments().getString(TYPE), token);
        itemsResponseCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                mSwipeRefreshLayout.setRefreshing(false);
                mItemsAdapter.clear();
                List<Item> itemsList = response.body();
                assert itemsList != null;
                for (Item item : itemsList) {
                    mItemsAdapter.addItem(item);
                }

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();


            }
        });
    }

}