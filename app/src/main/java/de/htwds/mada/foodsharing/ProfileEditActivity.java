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
    public static final String LOG=ProfileEditActivity.class.getName();

    private EditText firstNameInputField;
    private EditText lastNameInputField;
    private EditText emailInputField;
    private EditText phoneInputField;
    private EditText streetInputField;
    private EditText houseNumberInputField;
    private EditText zipcodeInputField;
    private EditText countryInputField;
    private EditText cityInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);


        firstNameInputField = (EditText) findViewById(R.id.profile_edit_first_name_et);
        lastNameInputField = (EditText) findViewById(R.id.profile_edit_last_name_et);
        emailInputField=(EditText) findViewById(R.id.profile_edit_email_et);
        phoneInputField=(EditText) findViewById(R.id.profile_edit_phone_et);
        cityInputField=(EditText) findViewById(R.id.profile_edit_place_of_res_et);
        streetInputField=(EditText) findViewById(R.id.profile_edit_street_address_et);
        houseNumberInputField=(EditText) findViewById(R.id.profile_edit_street_address_no_et);
        zipcodeInputField=(EditText) findViewById(R.id.profile_edit_zipcode_et);
        countryInputField=(EditText) findViewById(R.id.profile_edit_country_et);


        firstNameInputField.setText("Willi", TextView.BufferType.EDITABLE);
        lastNameInputField.setText("Winzig", TextView.BufferType.EDITABLE);
        emailInputField.setText("williwinzig@mailinator.com", TextView.BufferType.EDITABLE);
        phoneInputField.setText("123456789", TextView.BufferType.EDITABLE);
        cityInputField.setText("Hintertupfingen", TextView.BufferType.EDITABLE);
        streetInputField.setText("Winzigestrasse", TextView.BufferType.EDITABLE);
        houseNumberInputField.setText("23", TextView.BufferType.EDITABLE);
        zipcodeInputField.setText("23542", TextView.BufferType.EDITABLE);
        countryInputField.setText("DE", TextView.BufferType.EDITABLE);
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
                    return;
            }
        }

    protected void fillIntent(Class activity){
        Intent i;
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }

    private void updateAccount()
    {
        Log.i(LOG, "hallo");
        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                User accountUser=new User();
                accountUser.setVorname(firstNameInputField.getText().toString().trim());
                accountUser.setNachname(lastNameInputField.getText().toString().trim());
                accountUser.setUsername("williwinzig");
                int passwordLength=8;
                char[] password=new char[passwordLength];
                //passwordInputField.getText().getChars(0,passwordLength,password,0);
                Arrays.fill(password, 'a');
                accountUser.setPassword(password);
                accountUser.setEmail(emailInputField.getText().toString().trim());
                accountUser.setStreet(streetInputField.getText().toString().trim());
                accountUser.setHouseNumber(houseNumberInputField.getText().toString().trim());
                accountUser.setAdditional("Nichts ist wie es scheint.");
                accountUser.setPlz(Integer.parseInt(zipcodeInputField.getText().toString().trim()));
                accountUser.setCity(cityInputField.getText().toString().trim());
                accountUser.setCountry(countryInputField.getText().toString().trim());

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", accountUser.getEmail()));
                nameValuePairs.add(new BasicNameValuePair("password", accountUser.getPassword().toString()));
                nameValuePairs.add(new BasicNameValuePair("username", accountUser.getUsername()));
                nameValuePairs.add(new BasicNameValuePair("vorname", accountUser.getVorname()));
                nameValuePairs.add(new BasicNameValuePair("nachname", accountUser.getNachname()));
                nameValuePairs.add(new BasicNameValuePair("strasse", accountUser.getStreet()));
                nameValuePairs.add(new BasicNameValuePair("hausnummer", accountUser.getHouseNumber()));
                nameValuePairs.add(new BasicNameValuePair("zusatz", accountUser.getAdditional()));
                nameValuePairs.add(new BasicNameValuePair("plz", String.valueOf(accountUser.getPlz())));
                nameValuePairs.add(new BasicNameValuePair("ort", accountUser.getCity()));
                nameValuePairs.add(new BasicNameValuePair("land", accountUser.getCountry()));


                JSONParser jsonParser = new JSONParser();
                JSONObject returnObject = jsonParser.makeHttpRequest("http://odin.htw-saarland.de/create_user.php", "POST", nameValuePairs);
                if (returnObject.optBoolean("success"))
                {
                    handler.post(new Runnable (){
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Updated account successfully!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
                else
                {
                    final String errorMessage=returnObject.optString("message", "Unknown error!");
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
