package com.loftschool.fomin.moneyloft;

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

    ItemsAdapter(int priceColor) {
        mPriceColor = priceColor;

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
        viewHolder.bindItem(item);
        viewHolder.setListener(item, position);
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

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private TextView mPriceView;

        ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.item_name);
            mPriceView = itemView.findViewById(R.id.item_price);


        }

        void bindItem(final Item item) {
            mNameView.setText(item.getName());
            mPriceView.setText(
                    (mPriceView.getContext().getResources().getString(R.string.prise_template, String.valueOf(item.getPrice()))));

        }
        public void  setListener (Item item, int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemView.setSelected(!itemView.isSelected());
                    return false;
                }
            });
        }

    }
}