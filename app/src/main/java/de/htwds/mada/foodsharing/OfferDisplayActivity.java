package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class OfferDisplayActivity extends Activity {
    private static final String LOG=OfferDisplayActivity.class.getName();
    private final Handler handler = new Handler();
    private TextView titleDisplayField;
    private TextView bestBeforeDateDisplayField;
    private TextView longDescriptionDisplayField;
    private Offer currentOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_display);

        titleDisplayField = (TextView) findViewById(R.id.title_tv);
        bestBeforeDateDisplayField = (TextView) findViewById(R.id.offer_display_best_before_tv);
        longDescriptionDisplayField = (TextView) findViewById(R.id.detailed_description_tv);

        currentOffer=new Offer();
        currentOffer.setOfferID(getIntent().getIntExtra(Constants.keyOfferID, -1));
        Log.i(LOG, "Offer id: " + currentOffer.getOfferID());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (currentOffer.fillObjectFromDatabase()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            titleDisplayField.setText(currentOffer.getShortDescription());
                            longDescriptionDisplayField.setText(currentOffer.getLongDescription());
                            Toast.makeText(getBaseContext(), "Offer data fetched successfully!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Log.e(LOG, currentOffer.getErrorMessage());
                    final String errorMessage = currentOffer.getErrorMessage();

                    handler.post(new Runnable() {
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
        getMenuInflater().inflate(R.menu.menu_display_offer, menu);
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

    public void editOffer(View view){
        Intent i = new Intent(getApplicationContext(), OfferEditActivity.class);
        startActivity(i);
    }
}
