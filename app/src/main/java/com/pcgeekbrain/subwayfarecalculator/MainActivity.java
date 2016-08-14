package com.pcgeekbrain.subwayfarecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText existingFunds, numOfTrips, totalToAdd;
    private TextView amountRemaining;
    public static final String TAG = "MainActivity";
    //calculation variables
    private double fare = 2.75;
    private double rate = 1.11;
    private double min = 5.50;
    double remaining_funds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        existingFunds = (EditText)findViewById(R.id.existing_funds);
        numOfTrips = (EditText)findViewById(R.id.num_of_trips);
        totalToAdd = (EditText)findViewById(R.id.total_to_add);
        amountRemaining = (TextView)findViewById(R.id.amount_remaining);
        amountRemaining.setText(getString(R.string.amount_left_test, remaining_funds));

        existingFunds.addTextChangedListener(ExistingFundsWatcher);
        numOfTrips.addTextChangedListener(numOfTripsWatcher);
        totalToAdd.addTextChangedListener(totalToAddWatcher);
    }

    private void runCalculation(int id){
        //TODO calculate remaining funds and add to bottom of activity
        double existing = getDoubleFromEditText(existingFunds);
        double trips = getDoubleFromEditText(numOfTrips);
        double total = getDoubleFromEditText(totalToAdd);
        int total_trips;

        switch (id){
            case R.id.existing_funds:
            case R.id.num_of_trips:
                //total to add is num of trips multiplied by the fare. then for good measure remove existing funds
                total = (trips * fare) - existing;
                if (total > min * rate){    //if we qualify for the discount (are adding more then min (after bonus is applied)
                    total = total / rate;   //divide by rate because x = ( x / y ) * y (division is the reverse of multiplication
                }
                //set the total to the amount we need
                setWatchedEditText(totalToAdd, totalToAddWatcher, String.format(Locale.US, "%.2f", total));
                break;
            case R.id.total_to_add:
                //add bonus if over min
                if (total > min){
                    total = total * rate;
                }
                trips = (total + existing) / fare;              //trips available is the amount of times you have the fare on the card
                total_trips = (int)trips;                       //round that down to avoid rounding up errors (e.g. $5 = 2 trips)
                remaining_funds = (trips - (int)trips) * fare;  //get whatever is left (remainder of trips times fare. Since trips is the total divided by fare this should work.

                //debug
                Log.d(TAG, "runCalculation: trips = " + trips);
                Log.d(TAG, "runCalculation: total_trips = " + total_trips);
                Log.d(TAG, "runCalculation: remaining_funds = " + remaining_funds);
                //set text fields
                setWatchedEditText(numOfTrips, numOfTripsWatcher, String.format(Locale.US, "%d", total_trips));
                amountRemaining.setText(getString(R.string.amount_left_test, remaining_funds));
                //amountRemaining.setText(getString(R.string.amount_left) + Double.toString(remaining_funds));
                break;
        }
    }

    private TextWatcher ExistingFundsWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   //runs every time another number is put in
            runCalculation(R.id.existing_funds);
        }
    };

    private TextWatcher numOfTripsWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   //runs every time another number is put in
            runCalculation(R.id.num_of_trips);
        }
    };

    private TextWatcher totalToAddWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   //runs every time another number is put in
            runCalculation(R.id.total_to_add);
        }
    };

    private double getDoubleFromEditText(EditText field){
        try {
            return Double.parseDouble(field.getText().toString());
        } catch (NumberFormatException e){
            //blank EditText
        } catch (Exception e){
            Log.e(TAG, "getDoubleFromEditText: ", e);
        }
        return 0;
    }

    private void setWatchedEditText(EditText view, TextWatcher watcher, String text){
        view.removeTextChangedListener(watcher);
        view.setText(text);
        view.addTextChangedListener(watcher);
    }
}