package com.pcgeekbrain.subwayfarecalculator;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText existingFundsView, numOfTripsView, totalToAddView;
    private TextView amountRemainingView, amountOnCardView;
    public static final String TAG = "MainActivity";
    //calculation variables
    private double fare = 2.75;
    private double rate = 1.11;
    private double min = 5.50;
    double remaining_funds = 0;
    double amountOnCard = 0;
    TextWatcher ExistingFundsWatcher, numOfTripsWatcher, totalToAddWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get the UI objects
        existingFundsView = (EditText)findViewById(R.id.existing_funds);
        numOfTripsView = (EditText)findViewById(R.id.num_of_trips);
        totalToAddView = (EditText)findViewById(R.id.total_to_add);
        amountRemainingView = (TextView)findViewById(R.id.amount_remaining);
        amountOnCardView = (TextView)findViewById(R.id.amount_on_card);
        //assign the listeners/watchers
        ExistingFundsWatcher = new MetroTextWatcher(R.id.existing_funds);
        numOfTripsWatcher = new MetroTextWatcher(R.id.num_of_trips);
        totalToAddWatcher = new MetroTextWatcher(R.id.total_to_add);
        //assign them
        existingFundsView.addTextChangedListener(ExistingFundsWatcher);
        numOfTripsView.addTextChangedListener(numOfTripsWatcher);
        totalToAddView.addTextChangedListener(totalToAddWatcher);

        //set the icon TextViews to fontAwesome
        Typeface fontAwesome = Typefaces.get(this, "fonts/fontawesome-webfont.ttf");
        TextView icon_MetroCard = (TextView)findViewById(R.id.icon_MetroCard);
        TextView icon_metro = (TextView)findViewById(R.id.icon_metro);
        TextView icon_money = (TextView)findViewById(R.id.icon_money);

        icon_MetroCard.setTypeface(fontAwesome);
        icon_metro.setTypeface(fontAwesome);
        icon_money.setTypeface(fontAwesome);
    }
    private void runCalculation(int id){
        double existing = getDoubleFromEditText(existingFundsView);
        double trips = getDoubleFromEditText(numOfTripsView);
        double total_adding = getDoubleFromEditText(totalToAddView);

        if (id == R.id.existing_funds) {
            if (total_adding == 0 || amountOnCard - existing < 0) {//if user has not entered an amount they wish to add. count up the number of rides
                double on_card = existing; // making names clear
                int total_rides = (int) Math.floor(on_card / fare); //total rides is equal to the amount on card divided by the fare. rounded down (so 2.45 rides is just 2)
                setWatchedEditText(numOfTripsView, numOfTripsWatcher, String.format(Locale.US, "%d", total_rides)); //Set number of rides
                setWatchedEditText(totalToAddView, totalToAddWatcher, String.format(Locale.US, "%.2f", 0.00));  //set the amount we need to add down
                remaining_funds = on_card - (fare * total_rides); //amount left is equal to the amount on the card minus the number of rides multiplied by the fare
                amountRemainingView.setText(getString(R.string.amount_left_test, remaining_funds)); //set $ Extra
                amountOnCard = on_card;     //set amount on card (ser entered. but making things look nice
            } else {    //amountOnCard - existing < 0
                total_adding = (trips * fare) - existing; //total needed to add is what is needed minus what we have
                if (total_adding > min) {total_adding /= rate;} //If we still qualify for the bonus apply it
                setWatchedEditText(totalToAddView, totalToAddWatcher, String.format(Locale.US, "%.2f", total_adding));  //set the amount we need to add down
                amountRemainingView.setText(getString(R.string.amount_left_test, 0.00)); //set $ Extra
                amountOnCard = trips*fare;
            }
        } else if (id == R.id.num_of_trips) {
            //TODO get rid of this code duplication. does same as else above.
            total_adding = (trips * fare) - existing; //total needed to add is what is needed minus what we have
            if (total_adding > min) {total_adding /= rate;} //If we still qualify for the bonus apply it
            setWatchedEditText(totalToAddView, totalToAddWatcher, String.format(Locale.US, "%.2f", Math.ceil(total_adding*100)/100));  //set the amount we need to add down
            amountRemainingView.setText(getString(R.string.amount_left_test, 0.00)); //set $ Extra
            amountOnCard = trips*fare;
        } else if (id == R.id.total_to_add) {
            double on_card = total_adding;
            if (on_card > min) {on_card *= rate;} //If we still qualify for the bonus apply it (in this case add it)
            on_card += existing;   //add the amount we have already
            int total_rides = (int) Math.floor(on_card / fare);    //get the amount of rides we have
            setWatchedEditText(numOfTripsView, numOfTripsWatcher, String.format(Locale.US, "%d", total_rides)); //Set number of rides
            remaining_funds = on_card - (fare * total_rides); //amount left is equal to the amount on the card minus the number of rides multiplied by the fare
            amountRemainingView.setText(getString(R.string.amount_left_test, remaining_funds)); //set $ Extra
            amountOnCard = on_card;
        }
        amountOnCardView.setText(getString(R.string.amount_on_card, amountOnCard));
    }
    private class MetroTextWatcher implements TextWatcher {
        int id;
        MetroTextWatcher(int id){
            this.id = id;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.d(TAG, "onTextChanged: Ran on ID" + id);
            runCalculation(id);
        }
    }
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