package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;


public class BrowseCreateEdit extends Activity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_create_edit);

        spinner = (android.widget.Spinner) findViewById(R.id.browse_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_dropdown_item_1line);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


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


    public void browseEditPopUp(View view) {
        PopupMenu pop = new PopupMenu(this, view);
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_offer_item:
                        fillIntent(OfferEditActivity.class);

                        return true;
                    case R.id.edit_profile_item:
                        fillIntent(ProfileDisplayActivity.class);

                        return true;
                    case R.id.new_offer_item:
                        fillIntent(OfferEditActivity.class);

                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inf = pop.getMenuInflater();
        inf.inflate(R.menu.menu_btn_create_pop, pop.getMenu());
        pop.show();
    }

    protected void fillIntent(Class activity) {
        Intent i;
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final Intent i;

            switch (parent.getSelectedItemPosition()) {
                case 0:
                     break;
                case 1:
                    i = new Intent(this, ResultActivity.class);
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

    }
}

