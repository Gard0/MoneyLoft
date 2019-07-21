package com.loftschool.fomin.moneyloft;

import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.loftschool.fomin.moneyloft.MainActivity.AUTH_TOKEN;


public class BalanceFragment extends Fragment {
    private DiagramView mDiagramView;
    private TextView mTotalMoney;
    private TextView mExpensesMoney;
    private TextView mIncomeMoney;
    private Api mApi;

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
        mTotalMoney = fragmentView.findViewById(R.id.total_money);
        mIncomeMoney = fragmentView.findViewById(R.id.total_income);
        mExpensesMoney = fragmentView.findViewById(R.id.total_expenses);

        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((LoftMoneyApp) (getActivity()).getApplication()).getApi();

    }

    @Override
    public void onStart() {
        super.onStart();
        loadBalance();
    }

    private void loadBalance() {
        final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
        assert getArguments() != null;
        Call<BalanceResponse> balanceResponseCall = mApi.getBalance(token);
        balanceResponseCall.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(
                    Call<BalanceResponse> call, Response<BalanceResponse> response) {
                mTotalMoney.setText(getString(R.string.prise_template, String.valueOf(response.body().getTotalIncome() - response.body().getTotalExpenses())));
                mExpensesMoney.setText(getString(R.string.prise_template, String.valueOf(response.body().getTotalExpenses())));
                mIncomeMoney.setText(getString(R.string.prise_template, String.valueOf(response.body().getTotalIncome())));

                mDiagramView.update(response.body().getTotalIncome(), response.body().getTotalExpenses());
            }


            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {

            }
        });
    }

}
