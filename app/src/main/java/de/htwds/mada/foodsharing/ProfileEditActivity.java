package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class ProfileEditActivity extends Activity {
    private static final String LOG=ProfileEditActivity.class.getName();


    private Button profileEditSaveButton;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        profileEditSaveButton=(Button)findViewById(R.id.profile_edit_save_btn);
        profileEditSaveButton.setEnabled(false);

        final EditText firstNameInputField = (EditText) findViewById(R.id.profile_edit_first_name_et);
        final EditText lastNameInputField = (EditText) findViewById(R.id.profile_edit_last_name_et);
        final EditText emailInputField=(EditText) findViewById(R.id.profile_edit_email_et);
        final EditText phoneInputField=(EditText) findViewById(R.id.profile_edit_phone_et);
        final EditText cityInputField=(EditText) findViewById(R.id.profile_edit_place_of_res_et);
        final EditText streetInputField=(EditText) findViewById(R.id.profile_edit_street_address_et);
        final EditText houseNumberInputField=(EditText) findViewById(R.id.profile_edit_street_address_no_et);
        final EditText zipcodeInputField=(EditText) findViewById(R.id.profile_edit_zipcode_et);
        final EditText countryInputField=(EditText) findViewById(R.id.profile_edit_country_et);


        currentUser=new User(this);
        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if (currentUser.fillObjectFromDatabase())
                {
                    handler.post(new Runnable (){
                        @Override
                        public void run() {
                            firstNameInputField.setText(currentUser.getVorname());
                            lastNameInputField.setText(currentUser.getNachname());
                            emailInputField.setText(currentUser.getEmail());
                            cityInputField.setText(currentUser.getCity());
                            streetInputField.setText(currentUser.getStreet());
                            houseNumberInputField.setText(currentUser.getHouseNumber());
                            //TODO: zipcodeInputField.setText(currentUser.getPlz());
                            countryInputField.setText(currentUser.getCountry());
                            profileEditSaveButton.setEnabled(true);
                            Toast.makeText(getBaseContext(), Constants.USER_FETCHED, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Log.e(LOG, currentUser.getErrorMessage());
                    final String errorMessage=currentUser.getErrorMessage();

                    handler.post(new Runnable (){
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
        thread.start();

        /*
        firstNameInputField.setText("Willi", TextView.BufferType.EDITABLE);
        lastNameInputField.setText("Winzig", TextView.BufferType.EDITABLE);
        emailInputField.setText("williwinzig@mailinator.com", TextView.BufferType.EDITABLE);
        phoneInputField.setText("123456789", TextView.BufferType.EDITABLE);
        cityInputField.setText("Hintertupfingen", TextView.BufferType.EDITABLE);
        streetInputField.setText("Winzigestrasse", TextView.BufferType.EDITABLE);
        houseNumberInputField.setText("23", TextView.BufferType.EDITABLE);
        zipcodeInputField.setText("23542", TextView.BufferType.EDITABLE);
        countryInputField.setText("DE", TextView.BufferType.EDITABLE);
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        public void editProfile(View view){
            Button btn = (Button) view;
            switch (btn.getId()) {
                case R.id.profile_edit_offer_btn:
                    fillIntent(OfferEditActivity.class);
                    break;
                case R.id.profile_edit_save_btn:
                    //fillIntent(ProfileDisplayActivity.class);
                    updateAccount();
                    break;

                default:
            }
        }

    protected void fillIntent(Class activity){
        Intent i;
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }

    private void updateAccount()
    {
        final EditText firstNameInputField = (EditText) findViewById(R.id.profile_edit_first_name_et);
        final EditText lastNameInputField = (EditText) findViewById(R.id.profile_edit_last_name_et);
        final EditText emailInputField=(EditText) findViewById(R.id.profile_edit_email_et);
        final EditText phoneInputField=(EditText) findViewById(R.id.profile_edit_phone_et);
        final EditText cityInputField=(EditText) findViewById(R.id.profile_edit_place_of_res_et);
        final EditText streetInputField=(EditText) findViewById(R.id.profile_edit_street_address_et);
        final EditText houseNumberInputField=(EditText) findViewById(R.id.profile_edit_street_address_no_et);
        final EditText zipcodeInputField=(EditText) findViewById(R.id.profile_edit_zipcode_et);
        final EditText countryInputField=(EditText) findViewById(R.id.profile_edit_country_et);


        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                currentUser.setVorname(firstNameInputField.getText().toString().trim());
                currentUser.setNachname(lastNameInputField.getText().toString().trim());
                currentUser.setUsername("williwinzig");
                int passwordLength=8;
                char[] password=new char[passwordLength];
                //passwordInputField.getText().getChars(0,passwordLength,password,0);
                Arrays.fill(password, 'a');
                currentUser.setPassword(password);
                currentUser.setEmail(emailInputField.getText().toString().trim());
                currentUser.setStreet(streetInputField.getText().toString().trim());
                currentUser.setHouseNumber(houseNumberInputField.getText().toString().trim());
                currentUser.setAdditional("Nichts ist wie es scheint.");
                //TODO: currentUser.setPlz(Integer.parseInt(zipcodeInputField.getText().toString().trim()));
                currentUser.setCity(cityInputField.getText().toString().trim());
                currentUser.setCountry(countryInputField.getText().toString().trim());

                if (currentUser.saveObjectToDatabase())
                {
                    handler.post(new Runnable (){
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), Constants.ACCOUNT_UPDATED, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
                else
                {
                    final String errorMessage=currentUser.getErrorMessage();
                    handler.post(new Runnable (){
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
        thread.start();
    }
}
