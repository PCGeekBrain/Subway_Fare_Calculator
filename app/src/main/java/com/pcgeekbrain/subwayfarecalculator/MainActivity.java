package com.pcgeekbrain.subwayfarecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText existingFunds, numOfTrips, totalToAdd;
    public static final String TAG = "MainActivity";
    //calculation variables
    private double fare = 2.75;
    private double rate = 1.11;
    private double Min = 5.50;

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
        double existing = getDoubleFromEditText(existingFunds);
        double trips = getDoubleFromEditText(numOfTrips);
        double total = getDoubleFromEditText(totalToAdd);

        if (id == R.id.existing_funds){
            //run calculation on existing amount
            total = trips * fare - existing;
            setWatchedEditText(totalToAdd, totalToAddWatcher, String.valueOf(total));
        }
        else if (id == R.id.num_of_trips){
            total = trips * fare - existing;
            setWatchedEditText(totalToAdd, totalToAddWatcher, String.valueOf(total));

        }
        else if (id == R.id.total_to_add){
            trips = total / fare;
            setWatchedEditText(numOfTrips, numOfTripsWatcher, String.valueOf(trips));
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
            runCalculation(R.id.total_to_add);
        }
    };

    private TextWatcher totalToAddWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   //runs every time another number is put in
            runCalculation(R.id.num_of_trips);
        }
    };

    private double getDoubleFromEditText(EditText field){
        try {
            return Double.parseDouble(field.getText().toString());
        } catch (NumberFormatException e){
            //blank edittext
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
