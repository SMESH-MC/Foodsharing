package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Lists all offers in the database and provides the functionality to sort and filter them
 */

public class ResultActivity extends Activity {
    private static final String LOG=ResultActivity.class.getName();
    private ArrayAdapter<Offer> offerArrayAdapter;
    private ListView resultListView;

    private Spinner sortSpinner;
    private EditText filterInputField;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        sortSpinner=(Spinner) findViewById(R.id.resultActivitySortSpinner);
        filterInputField=(EditText) findViewById(R.id.resultActivityFilter);

        resultListView=(ListView) findViewById(R.id.activity_result_listview);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Offer offer = (Offer) parent.getItemAtPosition(position);
                Log.i(LOG, Constants.OFFER_ID + offer.getOfferID());
                Intent intent = new Intent(ResultActivity.this, OfferDisplayActivity.class);
                intent.putExtra(Constants.keyOfferID, offer.getOfferID());
                startActivity(intent);
            }
        });


        spinnerAdapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        //spinnerAdapter.add("Not sorted");
        spinnerAdapter.add("Short Description");
        spinnerAdapter.add("Long Description");
        spinnerAdapter.add("Best Before Date");
        spinnerAdapter.add("Date Added");
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectionString=(String)parent.getItemAtPosition(position);
                final Collator collator=Collator.getInstance();
                switch(selectionString)
                {
                    /*
                    case "Not sorted":
                        //offerArrayAdapter=new ArrayAdapter<>(ResultActivity.this, android.R.layout.simple_list_item_1);
                        //new RetrieveOffersTask().execute();
                        offerArrayAdapter.sort(null);
                        break;
                        */
                    case "Short Description":
                        offerArrayAdapter.sort(new Comparator<Offer>() {
                            @Override
                            public int compare(Offer lhs, Offer rhs) {
                                return collator.compare(lhs.getShortDescription(), rhs.getShortDescription());
                            }
                        });
                        break;
                    case "Long Description":
                        offerArrayAdapter.sort(new Comparator<Offer>() {
                            @Override
                            public int compare(Offer lhs, Offer rhs) {
                                return collator.compare(lhs.getLongDescription(), rhs.getLongDescription());
                            }
                        });
                        break;
                    case "Best Before Date":
                        offerArrayAdapter.sort(new Comparator<Offer>() {
                            @Override
                            public int compare(Offer lhs, Offer rhs) {
                                return lhs.getMhd().compareTo(rhs.getMhd());
                            }
                        });
                        break;
                    case "Date Added":
                        offerArrayAdapter.sort(new Comparator<Offer>() {
                            @Override
                            public int compare(Offer lhs, Offer rhs) {
                                return lhs.getDateAdded().compareTo(rhs.getDateAdded());
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Log.i(LOG, "Nothing selected.");
            }
        });

        filterInputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                ResultActivity.this.offerArrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        offerArrayAdapter=new ArrayAdapter<>(ResultActivity.this, android.R.layout.simple_list_item_1);
        new RetrieveOffersTask().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

    private class RetrieveOffersTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(ResultActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Retrieving all offers ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair(Constants.JSON_TRANS_ID,Constants.BLA_WORD));

            JSONParser jsonParser = new JSONParser();
            JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + Constants.URL_GET_ALL_OFFERS, Constants.JSON_GET, nameValuePairs);


            if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
                errorOccurred=true;
                errorMessage="Failed to fetch offers!";
                return null;
            }

            JSONArray offerJSONArray=returnObject.optJSONArray(Constants.OFFERS_WORD);
            if (offerJSONArray == null)
            {
                errorOccurred=true;
                errorMessage="Failed to fetch offers!";
                return null;
            }
            JSONObject offerJSONObject;
            for (int i=0; i<offerJSONArray.length(); i++) {
                offerJSONObject = offerJSONArray.optJSONObject(i);
                if (offerJSONObject != null) {
                    offerArrayAdapter.add(new Offer(ResultActivity.this, offerJSONObject));
                }
                else
                {
                    errorOccurred=true;
                    errorMessage="Could not retrieve offer info " + i;
                    return null;
                }
            }

            return null;
        }


        protected void onPostExecute(Void param)
        {
            if (errorOccurred) {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                return;
            }

            offerArrayAdapter.notifyDataSetChanged();
            resultListView.setAdapter(offerArrayAdapter);
            progressDialog.dismiss();
            Toast.makeText(getBaseContext(), Constants.OFFER_FETCHED, Toast.LENGTH_LONG).show();
        }
    }

}
