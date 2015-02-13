package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class BrowseCreateEdit extends Activity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_create_edit);
        spinner = (android.widget.Spinner) findViewById(R.id.browse_spinner);

        // Creates an ArrayAdapter using the string array and customized spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.items_array, R.layout.spinner_layout);
        // Adds customised layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_items_layout);


        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


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
                    case R.id.browse_create_new_offer_btn:
                        fillIntent(OfferEditActivity.class);
//                        this.getIntent().putExtra();
                        break;

                    case R.id.browse_create_edit_offer_btn:
                        fillIntent(EditSearchActivity.class);
//                        this.getIntent().putExtra();
                        break;

                    case R.id.browse_edit_profile_btn:
                        fillIntent(EditSearchActivity.class);
//                        this.getIntent().putExtra();
                        break;

                    default:
                        break;
                }
            }



    protected void fillIntent(Class activity) {
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getSelectedItemPosition()) {
                case 0:
                     break;
                case 1:
                    i = new Intent(this, OfferDisplayActivity.class);
                    startActivity(i);
                    break;
                case 2:
                    i = new Intent(this, ResultActivity.class);
                    startActivity(i);
                    break;
                case 3:
                    i = new Intent(this, ResultActivity.class);
                    startActivity(i);
                    break;
                case 4:
                    i = new Intent(this, ProfileDisplayActivity.class);
                    startActivity(i);
                    break;
                default:
                    break;
            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }


}

