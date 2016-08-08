package com.pcgeekbrain.subwayfarecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText existingFunds, numOfTrips, totalToAdd;
    public static final String TAG = "MainActivity";
    //calculation variables
    private double fare = 2.75;
    private double rate = 1.11;
    private double min = 5.50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        existingFunds = (EditText)findViewById(R.id.existing_funds);
        numOfTrips = (EditText)findViewById(R.id.num_of_trips);
        totalToAdd = (EditText)findViewById(R.id.total_to_add);

        existingFunds.addTextChangedListener(ExistingFundsWatcher);
        numOfTrips.addTextChangedListener(numOfTripsWatcher);
        totalToAdd.addTextChangedListener(totalToAddWatcher);
    }

    private void runCalculation(int id){
        //TODO calculate remaining funds and add to bottom of activity
        double existing = getDoubleFromEditText(existingFunds);
        double trips = getDoubleFromEditText(numOfTrips);
        double total = getDoubleFromEditText(totalToAdd);

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
                if (total > min){
                    total = total * rate;
                }
                trips = (total + existing) / fare;
                setWatchedEditText(numOfTrips, numOfTripsWatcher, String.format(Locale.US, "%.0f", trips));
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