package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ResultActivity extends Activity {
    private ArrayAdapter<Offer> listAdapter;
    private ListView resultListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listAdapter = new ArrayAdapter<>(this);
        resultListView = (ListView) findViewById(R.id.activity_result_listview);
        ViewStub vs = (ViewStub) findViewById(R.id.todoview_empty);
        resultListView.setEmptyView(vs);

        listAdapter.clear();
        List<Offer> offerList = todoDao.queryForAll();
        listAdapter.addAll(offerList);
        listAdapter.notifyDataSetChanged();
        if (listAdapter.getCount() != 0) {
            resultListView.setAdapter(listAdapter);
        }

        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                JSONParser jsonParser = new JSONParser();
                JSONObject returnObject = jsonParser.makeHttpRequest("http://odin.htw-saarland.de/get_all_offers.php", "GET", nameValuePairs);

                if (returnObject.optBoolean("success"))
                {
                    JSONArray offerJSONArray=returnObject.optJSONArray("offer");
                    JSONObject offerJSONObject=offerJSONArray.optJSONObject(0);
                    if (offerJSONObject != null)
                    {
                        this.setTransactID(offerJSONObject.optInt("transaction_id", -1));
                        //TODO: this.setPicture();
                        this.setShortDescription(offerJSONObject.optString("title"));
                        this.setLongDescription(offerJSONObject.optString("descr"));
                        //TODO: this.setMhd(userJSONObject.optString("bbd"));
                        //TODO: this.setDateAdded(userJSONObject.optString("date"));
                        //TODO: this.setValidDate(userJSONObject.optString("valid_date"));
                    }
                    else
                    {
                        errorMessage="Could not retrieve offer info!";
                        return false;
                    }
                }
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
