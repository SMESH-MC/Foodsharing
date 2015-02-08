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
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.HashMap;


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
        setContentView(R.layout.activity_offer_edit);
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

                //Log.i(LOG,"Title: "+titleInputField.getText().toString().trim());

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Offer newOffer=new Offer();
                        newOffer.setOfferID(6);
                        newOffer.setTransactID(5);
                        newOffer.setCategory(1); // exchanged "Obst" 2 test app
                        newOffer.setShortDescription(titleInputField.getText().toString().trim());
                        newOffer.setLongDescription(longDescriptionInputField.getText().toString().trim());
                        newOffer.setMhd(2015, 3, 3);
                        newOffer.setDateAdded();
                        newOffer.setPickupTimes("bla");
                        final HashMap<String,String> returnObject=makeQuery(newOffer);
                        if (returnObject.get("success").equals("true"))
                        {
                            handler.post(new Runnable (){
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Offering placed successfully!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                        else
                        {
                            final String errorMessage=returnObject.get("message");
                            handler.post(new Runnable (){
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                });
                thread.start();
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

    private HashMap<String,String> makeQuery(Offer newOffer)
    {
               HashMap<String,String> returnObject=new HashMap<String,String>();
               String resultString = "";



               JSONObject keyValuePairsJson=new JSONObject();
               try {
                   keyValuePairsJson.put("transaction_id", newOffer.getTransactID());
                   keyValuePairsJson.put("image_id", 4);
                   keyValuePairsJson.put("category", newOffer.getCategory());
                   keyValuePairsJson.put("title",newOffer.getShortDescription());
                   keyValuePairsJson.put("descr",newOffer.getLongDescription());
                   keyValuePairsJson.put("bbd",Integer.parseInt(bestBeforeDateInputField.getText().toString().trim()));
                   Timestamp timestamp=new Timestamp(newOffer.getDateAdded().getTimeInMillis());
                   keyValuePairsJson.put("date", timestamp.toString());
                   //keyValuePairsJson.put("date", timestamp.toString().replaceFirst("\\..*$", ""));
                   keyValuePairsJson.put("valid_date",1423216493);
               } catch (Exception e) {
                   String errorMessage=e.getLocalizedMessage();
                   returnObject.put("success", "false");
                   returnObject.put("message", errorMessage);
                   return returnObject;
               }



               InputStream inputStream;

               //http post
               try{
                   HttpClient httpclient = new DefaultHttpClient();
                   //HttpPost httpPost = new HttpPost("http://odin.htw-saarland.de/create_offer.php");
                   HttpPost httpPost = new HttpPost("http://odin.htw-saarland.de/create_offer_json.php");
                   //HttpPost httpPost = new HttpPost("http://odin.htw-saarland.de/showPostEntities.php");

                   StringEntity entityForPost=new StringEntity(keyValuePairsJson.toString(), HTTP.UTF_8);
                   httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
                   httpPost.setHeader("Accept", "application/json");
                   httpPost.setEntity(entityForPost);

                   Log.i(LOG, "Json: " + keyValuePairsJson.toString());

                   HttpResponse response = httpclient.execute(httpPost);
                   HttpEntity responseEntity = response.getEntity();
                   inputStream = responseEntity.getContent();
               }catch(Exception e){
                   String errorMessage=e.getLocalizedMessage();
                   Log.e(LOG, "Error in http connection " + errorMessage);
                   returnObject.put("success", "false");
                   returnObject.put("message", errorMessage);
                   return returnObject;
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

                   resultString=sb.toString();
               }catch(Exception e){
                   String errorMessage=e.getLocalizedMessage();
                   Log.e(LOG, "Error converting result " + errorMessage);
                   returnObject.put("success", "false");
                   returnObject.put("message", errorMessage);
                   return returnObject;
               }

               Log.i(LOG, "Result: " + resultString);

               //parse json data
               try{
                   JSONObject resultJsonObject=new JSONObject(resultString);
                   Log.i(LOG,"Success: "+resultJsonObject.getInt("success")+
                           ", message: "+resultJsonObject.getString("message"));
                   if (resultJsonObject.getInt("success") == 1) {
                       returnObject.put("success", "true");
                   }
                   else
                   {
                       String errorMessage=resultJsonObject.getString("message");
                       returnObject.put("success", "false");
                       returnObject.put("message", errorMessage);
                   }
               }catch(Exception e){
                   String errorMessage=e.getLocalizedMessage();
                   Log.e(LOG, "Error parsing result string " + resultString + " " + e.getLocalizedMessage());
                   returnObject.put("success", "false");
                   returnObject.put("message", errorMessage);
                   return returnObject;
               }


               return returnObject;

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
