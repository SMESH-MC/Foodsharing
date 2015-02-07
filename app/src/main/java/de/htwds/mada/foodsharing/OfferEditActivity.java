package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class OfferEditActivity extends Activity {
    public static final String LOG=OfferEditActivity.class.getName();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView photo;
    private Bitmap bitmap;

    final Handler handler = new Handler();

    private EditText titleInputField;
    private EditText bestBeforeDateInputField;
    private EditText longDescriptionInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_create);
        photo = (ImageView)findViewById(R.id.offeringPhoto);

        titleInputField = (EditText) findViewById(R.id.title_tv);
        bestBeforeDateInputField = (EditText) findViewById(R.id.best_before_et);
        longDescriptionInputField = (EditText) findViewById(R.id.detailed_description_tv);


        Button publishOfferBtn = (Button) findViewById(R.id.publish_offer_btn);
        publishOfferBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /*
                Intent i = new Intent(getApplicationContext(), OfferDisplayActivity.class);
                startActivity(i);
                */


                boolean querySuccessful=makeQuery();

                /*
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Successful!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_offer, menu);
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

    private boolean makeQuery()
    {
       boolean querySuccessful=false;
       Thread thread=new Thread(new Runnable() {
           @Override
           public void run() {
               String result = "";

               /*
               ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
               nameValuePairs.add(new BasicNameValuePair("transaction_id", 2));
               nameValuePairs.add(new BasicNameValuePair("image_id",4));
               nameValuePairs.add(new BasicNameValuePair("category",1));
               nameValuePairs.add(new BasicNameValuePair("title",titleInputField.getText().toString().trim()));
               nameValuePairs.add(new BasicNameValuePair("descr",longDescriptionInputField.getText().toString().trim()));
               nameValuePairs.add(new BasicNameValuePair("bbd",bestBeforeDateInputField.getText().toString().trim()));
               nameValuePairs.add(new BasicNameValuePair("date","0000-00-00 00:00:00"));
               nameValuePairs.add(new BasicNameValuePair("valid_date",1423216493));
               */


               JSONObject keyValuePairsJson=new JSONObject();
               try {
                   keyValuePairsJson.put("transaction_id", 2);
                   keyValuePairsJson.put("image_id",4);
                   keyValuePairsJson.put("category","hmmm");
                   keyValuePairsJson.put("title",titleInputField.getText().toString().trim());
                   keyValuePairsJson.put("descr",longDescriptionInputField.getText().toString().trim());
                   keyValuePairsJson.put("bbd",Integer.parseInt(bestBeforeDateInputField.getText().toString().trim()));
                   keyValuePairsJson.put("date","0000-00-00 00:00:00");
                   keyValuePairsJson.put("valid_date",1423216493);
               } catch (Exception e) {
                   final String errorMessage=e.getLocalizedMessage();
                   Log.e(LOG, errorMessage);
                   handler.post(new Runnable (){
                       @Override
                       public void run() {
                           Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                       }
                   });
                   return;
               }



               InputStream inputStream;

               //http post
               try{
                   HttpClient httpclient = new DefaultHttpClient();
                   HttpPost httpPost = new HttpPost("http://odin.htw-saarland.de/create_offer.php");

                   //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                   StringEntity entityForPost=new StringEntity(keyValuePairsJson.toString());
                   httpPost.setHeader("content-type", "application/json");
                   httpPost.setEntity(entityForPost);

                   HttpResponse response = httpclient.execute(httpPost);
                   Log.i(LOG, "httpclient.execute");
                   HttpEntity entity = response.getEntity();
                   inputStream = entity.getContent();
               }catch(Exception e){
                   final String errorMessage=e.getLocalizedMessage();
                   Log.e(LOG, "Error in http connection " + errorMessage);
                   handler.post(new Runnable (){
                       @Override
                       public void run() {
                           Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                       }
                   });
                   return;
               }
               //convert response to string
               try{
                   BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
                   StringBuilder sb = new StringBuilder();
                   String line;
                   while ((line = reader.readLine()) != null) {
                       sb.append(line + "\n");
                   }
                   inputStream.close();

                   result=sb.toString();
               }catch(Exception e){
                   final String errorMessage=e.getLocalizedMessage();
                   Log.e(LOG, "Error converting result "+errorMessage);
                   handler.post(new Runnable (){
                       @Override
                       public void run() {
                           Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                       }
                   });
                   return;
               }

               Log.i(LOG, result);

               //parse json data
               /*
               try{
                   JSONArray jArray = new JSONArray(result);
                   for(int i=0;i<jArray.length();i++){
                       JSONObject json_data = jArray.getJSONObject(i);
                       Log.i(LOG,"id: "+json_data.getInt("id")+
                                       ", name: "+json_data.getString("name")
                       );
                   }
               }catch(JSONException e){
                   Log.e(LOG, "Error parsing data "+e.toString());
               }
               */

               handler.post(new Runnable (){
                   @Override
                   public void run() {
                       Toast.makeText(getBaseContext(), "Offering placed successfully!", Toast.LENGTH_LONG).show();
                       finish();
                   }
               });

           }
       });
       thread.start();

        return querySuccessful;
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //makes sure any app can handle the Intent:
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //launch an activity with a desired result
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap)extras.get("data");
            photo.setImageBitmap(bitmap);
        }
    }


}
