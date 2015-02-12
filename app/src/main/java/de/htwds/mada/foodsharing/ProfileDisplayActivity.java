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
import android.widget.TextView;
import android.widget.Toast;


public class ProfileDisplayActivity extends Activity {
    private static final String LOG=ProfileDisplayActivity.class.getName();

    private TextView firstNameDisplayField;
    private TextView lastNameDisplayField;
    private TextView emailDisplayField;
    private TextView phoneDisplayField;
    private TextView cityDisplayField;
    private TextView streetDisplayField;
    private TextView houseNumberDisplayField;
    private TextView zipcodeDisplayField;
    private TextView countryDisplayField;

    private User displayedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        firstNameDisplayField = (TextView) findViewById(R.id.profile_edit_first_name_tv);
        lastNameDisplayField = (TextView) findViewById(R.id.profile_edit_last_name_tv);
        emailDisplayField=(TextView) findViewById(R.id.profile_edit_email_et);
        phoneDisplayField=(TextView) findViewById(R.id.profile_edit_phone_et);
        cityDisplayField=(TextView) findViewById(R.id.profile_edit_place_of_res_et);
        streetDisplayField=(TextView) findViewById(R.id.profile_edit_street_address_et);
        houseNumberDisplayField=(TextView) findViewById(R.id.profile_edit_street_address_no_et);
        //zipcodeDisplayField=(TextView) findViewById(R.id.profile_edit_zipcode_et);
        countryDisplayField=(TextView) findViewById(R.id.profile_displ_country_tv);

        displayedUser=new User(this, getIntent().getIntExtra(Constants.keyUserID, -1));
        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if (displayedUser.fillObjectFromDatabase())
                {
                    handler.post(new Runnable (){
                        @Override
                        public void run() {
                            firstNameDisplayField.setText(displayedUser.getVorname());
                            lastNameDisplayField.setText(displayedUser.getNachname());
                            emailDisplayField.setText(displayedUser.getEmail());
                            cityDisplayField.setText(displayedUser.getCity());
                            streetDisplayField.setText(displayedUser.getStreet());
                            houseNumberDisplayField.setText(displayedUser.getHouseNumber());
                            //TODO: zipcodeDisplayField.setText(displayedUser.getPlz());
                            countryDisplayField.setText(displayedUser.getCountry());
                            Toast.makeText(getBaseContext(), Constants.USER_FETCHED, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Log.e(LOG, displayedUser.getErrorMessage());
                    final String errorMessage=displayedUser.getErrorMessage();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_display, menu);
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
    public void profileDisplay(View view){
        Button btn = (Button) view;
        switch (btn.getId()) {
            case R.id.profile_display_edit_btn:
                fillIntent(ProfileEditActivity.class);
                break;
            case R.id.profile_edit_show_tr_history:
                fillIntent(TransactionHistoryActivity.class);
                break;

            default:
        }
    }

    protected void fillIntent(Class activity){
        Intent i;
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }
}
