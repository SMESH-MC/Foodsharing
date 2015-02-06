package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;


public class BrowseCreateEdit extends Activity {
    PopupMenu pop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_create_edit);
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


    public void browsePopUp(View view) {
        pop = new PopupMenu(this, view);
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search_existing_offer_item:
                        fillIntent(OfferDisplayActivity.class);

                        return true;
                    case R.id.search_category_item:
                        fillIntent(ResultActivity.class);

                        return true;
                    case R.id.search_location_item:
                        fillIntent(ResultActivity.class);

                        return true;
                    case R.id.search_profile_item:
                        fillIntent(ProfileDisplayActivity.class);

                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inf = pop.getMenuInflater();
        inf.inflate(R.menu.menu_btn_browse_pop, pop.getMenu());
        pop.show();
    }

    public void browseEditPopUp(View view){
        pop = new PopupMenu(this, view);
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
                    case R.id.new_profile_item:
                        fillIntent(ProfileEditActivity.class);

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

    protected void fillIntent(Class activity){
        Intent i;
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }

}

