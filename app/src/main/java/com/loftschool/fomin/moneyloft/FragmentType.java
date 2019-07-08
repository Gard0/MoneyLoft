package com.loftschool.fomin.moneyloft;

public enum FragmentType {

    expense(R.color.dark_sky_blue),
    income(R.color.income_price_color);

    private int priceColor;

    FragmentType(int priceColor) {
        this.priceColor = priceColor;
    }

    public int getPriceColor() {
        return priceColor;
    }


}
