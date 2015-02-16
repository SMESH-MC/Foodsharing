package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class OfferDisplayActivity extends Activity {
    private static final String LOG=OfferDisplayActivity.class.getName();

    private ImageView photoImageView;
    private File photoFile;
    private final Handler handler = new Handler();
    private TextView titleDisplayField;
    private TextView bestBeforeDateDisplayField;
    private TextView longDescriptionDisplayField;
    private TextView dateAddedDisplayField;
    private Offer currentOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_display);

        titleDisplayField = (TextView) findViewById(R.id.offerDisplayTitle);
        bestBeforeDateDisplayField = (TextView) findViewById(R.id.offer_display_best_before_tv);
        longDescriptionDisplayField = (TextView) findViewById(R.id.detailed_description_tv);
        dateAddedDisplayField = (TextView) findViewById(R.id.offerDisplayDateAdded);


        photoImageView = (ImageView)findViewById(R.id.offerDisplayPicture);


        currentOffer=new Offer(OfferDisplayActivity.this);
        currentOffer.setOfferID(getIntent().getIntExtra(Constants.keyOfferID, -1));
        Log.i(LOG, Constants.OFFER_ID + currentOffer.getOfferID());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (currentOffer.fillObjectFromDatabase()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            titleDisplayField.setText(currentOffer.getShortDescription());
                            longDescriptionDisplayField.setText(currentOffer.getLongDescription());
                            bestBeforeDateDisplayField.setText(String.format("%tF", currentOffer.getMhd()));
                            dateAddedDisplayField.setText(String.format("%1$tF %1$tT", currentOffer.getDateAdded()));
                            Log.i(LOG, currentOffer.getPicture().toString());
                            photoImageView.setImageURI(null);
                            photoImageView.setImageURI(Uri.fromFile(currentOffer.getPicture()));
                            Toast.makeText(getBaseContext(), Constants.OFFER_FETCHED, Toast.LENGTH_LONG).show();
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


    public void showContactInfo(View view)
    {
        final int currentTransactionID=currentOffer.getTransactID();
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String errorMessage = Constants.EMPTY_STRING;
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("tid", String.valueOf(currentTransactionID)));

                JSONParser jsonParser = new JSONParser();
                JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + "get_transaction_details.php", Constants.JSON_GET, nameValuePairs);
                boolean infoFetchedSuccessfully=true;

                int offerer_id=-1;
                if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) infoFetchedSuccessfully=false;
                else
                {
                    JSONArray transactionJSONArray=returnObject.optJSONArray("transaction");
                    JSONObject transactionJSONObject=transactionJSONArray.optJSONObject(0);
                    if (transactionJSONObject != null)
                    {
                        offerer_id=transactionJSONObject.optInt("offerer_id", -1);
                    }
                    else
                    {
                        infoFetchedSuccessfully=false;
                    }
                }
                final int finalOffererID=offerer_id;

                if (infoFetchedSuccessfully) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(LOG, "Offerer ID  " + finalOffererID);
                            Intent intent = new Intent(OfferDisplayActivity.this, ProfileDisplayActivity.class);
                            intent.putExtra(Constants.keyUserID, finalOffererID);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Could not retrieve offerer ID!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
        thread.start();

    }

}
