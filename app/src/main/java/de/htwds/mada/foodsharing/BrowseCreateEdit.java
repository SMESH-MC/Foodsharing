package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;


public class BrowseCreateEdit extends Activity { // implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private Intent i;
    private View browseLayout;
    private ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_create_edit_new);
        //spinner = (android.widget.Spinner) findViewById(R.id.browse_spinner);
        browseLayout = findViewById(R.id.browse_layout);

        /* if you want to use an array adapter instead of a button */
        /*ArrayAdapter<CharSequence> adapter;
        if (Locale.getDefault().getDisplayLanguage().equals("Deutsch")) {
            // Creates an ArrayAdapter using the string array and customized spinner layout
            // German language
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.items_array_german, R.layout.spinner_layout);
        } else {
            // default language English
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.items_array_english, R.layout.spinner_layout);
        }
        // Adds customised layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_items_layout);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        // Add new progress bar
        progress = new ProgressDialog(this);*/
    }
       /*
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getSelectedItemPosition()) {

                case 0:
                     break;

                case 1:
//                    tests if user input is empty
//                    testInput(view);

                    //shows progress bar
                    showProgress(view);
                    //fills intent with params
                    fillIntent(ResultActivity.class);
                    break;

                case 2:
//                    testInput(view);

                    //shows progress bar
                    showProgress(view);
                    //fills intent with params
                    fillIntent(ResultActivity.class);
                    break;

                case 3:
//                    testInput(view);

                    //shows progress bar
                    showProgress(view);
                    //fills intent with params
                    fillIntent(ResultActivity.class);
                    break;

                case 4:
//                    testInput(view);
                    //shows progress bar
                    showProgress(view);
                    //fills intent with params
                    fillIntent(ResultActivity.class);
                    break;

                default:
                    break;
            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_create, menu);
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


    public void createEdit(View view) {
        Button btn = (Button) view;

                //switch to pass an intent for distinct search
                switch (btn.getId()) {
                    //opens offering browser
                    case R.id.browse_browse_btn:
                        fillIntent(ResultActivity.class);
                        break;
                    // Opens OfferEdit to create new offer
                    case R.id.browse_create_new_offer_btn:
                        fillIntent(OfferEditActivity.class);
                        break;
                    // Opens EditSearch to enter offer which will be edited
                    case R.id.browse_create_edit_offer_btn:
                        fillIntent(EditSearchActivity.class);
                        break;
                    // Opens EditSearch to enter wanted profile
                    case R.id.browse_edit_profile_btn:
                        fillIntent(ProfileEditActivity.class);
                        break;
                    //
                    case R.id.browse_history_btn:
                        fillIntent(TransactionHistoryActivity.class);
                        break;
                    case R.id.browse_exit_btn:

                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.USER_ID, 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        moveTaskToBack(true);
                        break;
                    default:
                        break;
                }
            }

    protected void fillIntent(Class activity) {
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }

    private void testInput(View view){
        EditText et = (EditText) view;

        String input = et.getText().toString().trim();
        if (input.isEmpty()){
            Toast.makeText(getBaseContext(), Constants.NO_ARGUMENT, Toast.LENGTH_LONG).show();
        }
        showProgress(view);
        Toast.makeText(getBaseContext(), Constants.WAIT_INFO, Toast.LENGTH_LONG).show();

    }

    public void showProgress(View view){
        progress.setMessage(Constants.PLEASE_WAIT);
        progress.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.show();

        final int totalProgressTime = 600;

        final Thread t = new Thread(){

            @Override
            public void run(){

                int jumpTime = 0;
                while(jumpTime < totalProgressTime){
                    try {
                        jumpTime ++;
                        progress.setProgress(jumpTime);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        };
        t.start();

    }
}

