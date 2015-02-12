package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class AccountEditActivity extends Activity {
    public static final String LOG=AccountEditActivity.class.getName();

    private EditText usernameInputField;
    private EditText passwordInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        usernameInputField = (EditText) findViewById(R.id.username_et);
        passwordInputField = (EditText) findViewById(R.id.password_et);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_edit, menu);
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

    public void accountEdit(View view){
        Button btn = (Button) view;
        switch (btn.getId()) {
            case R.id.account_edit_save_btn:
                updateAccount();
                //fillIntent(AccountDisplayActivity.class);
                break;
            case R.id.account_edit_cancel_btn:
                fillIntent(BrowseCreateEdit.class);
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
        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                User accountUser=new User(AccountEditActivity.this);
                accountUser.setVorname("Willi");
                accountUser.setNachname("Winzig");
                accountUser.setUsername(usernameInputField.getText().toString().trim());
                int passwordLength=passwordInputField.length();
                char[] password=new char[passwordLength];
                passwordInputField.getText().getChars(0,passwordLength,password,0);
                accountUser.setPassword(password);
                Arrays.fill(password, ' ');
                accountUser.setEmail("williwinzig@mailinator.com");
                accountUser.setStreet("Winzigstraße");
                accountUser.setHouseNumber("23");
                accountUser.setAdditional("Nichts ist wie es scheint.");
                accountUser.setPlz(Constants.EXAMPLE_PLZ);
                accountUser.setCity("Hintertupfingen");
                accountUser.setCountry(Constants.COUNTRY_CODE_STANDARD);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair(Constants.EMAIL_WORD, accountUser.getEmail()));
                nameValuePairs.add(new BasicNameValuePair(Constants.PASSWORD_WORD, accountUser.getPassword().toString()));
                nameValuePairs.add(new BasicNameValuePair(Constants.USERNAME_WORD, accountUser.getUsername()));
                nameValuePairs.add(new BasicNameValuePair(Constants.VORNAME_WORD, accountUser.getVorname()));
                nameValuePairs.add(new BasicNameValuePair(Constants.NACHNAME_WORD, accountUser.getNachname()));
                nameValuePairs.add(new BasicNameValuePair(Constants.STRASSE_WORD, accountUser.getStreet()));
                nameValuePairs.add(new BasicNameValuePair(Constants.HAUSNUMMER_WORD, accountUser.getHouseNumber()));
                nameValuePairs.add(new BasicNameValuePair(Constants.ZUSATZ_WORD, accountUser.getAdditional()));
                nameValuePairs.add(new BasicNameValuePair(Constants.PLZ_WORD, String.valueOf(accountUser.getPlz())));
                nameValuePairs.add(new BasicNameValuePair(Constants.ORT_WORD, accountUser.getCity()));
                nameValuePairs.add(new BasicNameValuePair(Constants.LAND_WORD, accountUser.getCountry()));


                JSONParser jsonParser = new JSONParser();
                JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + Constants.URL_CREATE_USER, Constants.JSON_POST, nameValuePairs);
                if (returnObject.optBoolean(Constants.SUCCESS_WORD))
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
                    final String errorMessage=returnObject.optString(Constants.MESSAGE_WORD, Constants.UNKNOWN_ERROR);
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
