package com.loftschool.fomin.moneyloft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private List<Item> mItemList = new ArrayList<>();

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View itemView = View.inflate(viewGroup.getContext(), R.layout.item_view, null);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsAdapter.ItemViewHolder viewHolder, final int i) {
        final Item item = mItemList.get(i);
        viewHolder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
    public void addItem(final Item item) {
        mItemList.add(item);
        notifyItemInserted(mItemList.size());
    }
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private TextView mPriceView;

        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.item_name);
            mPriceView = itemView.findViewById(R.id.item_price);
        }

        public void bindItem(final Item item) {
            mNameView.setText(item.getName());
            mPriceView.setText(
                (mPriceView.getContext().getResources().getString(R.string.prise_template, String.valueOf(item.getPrice()))));

        }

    }
}
