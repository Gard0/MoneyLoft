package com.loftschool.fomin.moneyloft;

import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private List<Item> mItemList = new ArrayList<>();
    private int mPriceColor;
    private ItemAdapterListener mListener;

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();

    ItemsAdapter(int priceColor) {
        mPriceColor = priceColor;

    }

    void setListener(ItemAdapterListener listener) {
        mListener = listener;

    }
    boolean isSelected(final int position){
           return mSelectedItems.get(position);

    }
    void toggleItem(int position) {
        mSelectedItems.put(position, !mSelectedItems.get(position));
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View itemView = View.inflate(viewGroup.getContext(), R.layout.item_view, null);
        TextView priceView = itemView.findViewById(R.id.item_price);
        priceView.setTextColor(itemView.getContext().getResources().getColor(mPriceColor));
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsAdapter.ItemViewHolder viewHolder, final int position) {
        final Item item = mItemList.get(position);
        viewHolder.bindItem(item, mSelectedItems.get(position));
        viewHolder.setListener(item, mListener, position);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    void addItem(final Item item) {
        mItemList.add(item);
        notifyItemInserted(mItemList.size());
    }

    void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private TextView mPriceView;
        private View mItemView;

        ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            mItemView = itemView;
            mNameView = itemView.findViewById(R.id.item_name);
            mPriceView = itemView.findViewById(R.id.item_price);


        }

        void bindItem(final Item item, final boolean selected) {
            mItemView.setSelected(selected);
            mNameView.setText(item.getName());
            mPriceView.setText(
                    (mPriceView.getContext().getResources().getString(R.string.prise_template, String.valueOf(item.getPrice()))));

        }

        void setListener(final Item item, final ItemAdapterListener listener, final int position) {
            mItemView.setOnClickListener(view -> listener.onItemClick(item, position));

            mItemView.setOnLongClickListener(view -> {
                listener.onItemLongClick(item, position);
                return false;
            });
        }

    }
}