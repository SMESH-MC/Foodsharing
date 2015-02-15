package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ResultActivity extends Activity {
    private static final String LOG=ResultActivity.class.getName();
    private ArrayAdapter<Offer> listAdapter;
    private ListView resultListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        resultListView = (ListView) findViewById(R.id.activity_result_listview);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Offer offer = (Offer) parent.getItemAtPosition(position);
                Log.i(LOG, Constants.OFFER_ID + offer.getOfferID());
                //TODO: if offer belongs to current user start OfferEditActivity
                Intent intent = new Intent(ResultActivity.this, OfferDisplayActivity.class);
                intent.putExtra(Constants.keyOfferID, offer.getOfferID());
                startActivity(intent);
            }
        });


        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair(Constants.JSON_TRANS_ID,Constants.BLA_WORD));

                JSONParser jsonParser = new JSONParser();
                JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + Constants.URL_GET_ALL_OFFERS, Constants.JSON_GET, nameValuePairs);

                if (returnObject.optBoolean(Constants.SUCCESS_WORD))
                {
                    JSONArray offerJSONArray=returnObject.optJSONArray(Constants.OFFERS_WORD);
                    //TODO: if (offerJSONArray == null)
                    JSONObject offerJSONObject;
                    for (int i=0; i<offerJSONArray.length(); i++) {
                        offerJSONObject = offerJSONArray.optJSONObject(i);
                        if (offerJSONObject != null) {
                            listAdapter.add(new Offer(ResultActivity.this, offerJSONObject));
                        }
                        //TODO: else {
                            //errorMessage = "Could not retrieve offer info!";
                        //}
                    }
                }
                //TODO: else
                handler.post(new Runnable (){
                    @Override
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                        resultListView.setAdapter(listAdapter);
                        Toast.makeText(getBaseContext(), Constants.OFFER_FETCHED, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        thread.start();

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
}
