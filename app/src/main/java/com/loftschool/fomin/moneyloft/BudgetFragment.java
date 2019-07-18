package com.loftschool.fomin.moneyloft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.loftschool.fomin.moneyloft.MainActivity.AUTH_TOKEN;

public class BudgetFragment extends Fragment implements ItemAdapterListener, ActionMode.Callback {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PRICE_COLOR = "price_color";
    private static final String TYPE = "type";
    static final int REQUEST_CODE;
    public static android.view.ActionMode mode;
    public static ActionMode actionMode;

    static {
        REQUEST_CODE = 1001;
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ItemsAdapter mItemsAdapter;
    private Api mApi;
    private ActionMode mActionMode;

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
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        RecyclerView recyclerView = fragmentView.findViewById((R.id.recycler_view));
        mSwipeRefreshLayout = fragmentView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this::loadItems);

        assert getArguments() != null;
        mItemsAdapter = new ItemsAdapter(getArguments().getInt(PRICE_COLOR));
        mItemsAdapter.setListener(this);

        recyclerView.setAdapter(mItemsAdapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));

        return fragmentView;

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AUTH_TOKEN, "");
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

    @Override
    public void onItemClick(Item item, int position) {
        if (mItemsAdapter.isSelected(position)) {
            mItemsAdapter.toggleItem(position);
            mItemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemLongClick(final Item item, final int position) {
        mItemsAdapter.toggleItem(position);
        mItemsAdapter.notifyDataSetChanged();
        if (mActionMode == null) {
            ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).startSupportActionMode(this);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mActionMode = mode;
        return true;

    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.item_menu_remove, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.delete_menu_item) {
            showDialog();

        }
        return false;
    }

    private void showDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.remove_conformation)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> removeItems())
                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {

                }).show();
    }

    private void removeItems() {
        List<Integer> selectedIds = mItemsAdapter.getSelectedItemsIds();
        for (int selectedId : selectedIds) {
            removeItem(selectedId);
        }
    }

    private void removeItem(int selectedId) {
        final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AUTH_TOKEN, "");
        assert getArguments() != null;
        Call<Status> itemsRemoveCall = mApi.removeItem(selectedId, token);
        itemsRemoveCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {

                loadItems();
                mItemsAdapter.clearSelections();

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mItemsAdapter.clearSelections();
        mItemsAdapter.notifyDataSetChanged();
        mActionMode = null;
    }
}