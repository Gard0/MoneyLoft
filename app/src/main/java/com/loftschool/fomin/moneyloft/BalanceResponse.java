package com.loftschool.fomin.moneyloft;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse {
    @SerializedName("total_income")
    private int totalIncome;
    @SerializedName("total_expenses")
    private int totalExpenses;


    int getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }


    int getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }


}

