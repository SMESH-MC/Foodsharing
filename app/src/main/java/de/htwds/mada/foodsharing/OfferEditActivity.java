package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class OfferEditActivity extends Activity {
    private static final String LOG=OfferEditActivity.class.getName();

    private TextView activityTitle;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView photoImageView;
    private Bitmap bitmap;
    private File photoFile;

    private final Handler handler = new Handler();
    private final FragmentManager fragMan = getFragmentManager();

    private EditText titleInputField;

    private static Calendar bestBeforeDate;
    private EditText editDateField;
    private EditText longDescriptionInputField;

    private Button publishOfferButton;

    private Offer currentOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_edit);

        activityTitle=(TextView)findViewById(R.id.offerEditActivityTitle);

        photoImageView = (ImageView)findViewById(R.id.offerPicture);

        titleInputField = (EditText) findViewById(R.id.title_tv);

        bestBeforeDate = Calendar.getInstance();
        editDateField = (EditText)findViewById(R.id.best_before_date_edit);
        changeDateEditOnClickListener();
        changeDateEditFocusChangeListener();
        
        longDescriptionInputField = (EditText) findViewById(R.id.detailed_description_tv);

        publishOfferButton = (Button) findViewById(R.id.publish_offer_btn);

        photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "foodSharingPhoto.jpg");
        photoImageView.setImageURI(null);
        photoImageView.setImageURI(Uri.fromFile(photoFile));

        currentOffer=new Offer(OfferEditActivity.this);
        currentOffer.setOfferID(getIntent().getIntExtra(Constants.keyOfferID, -1));
        activityTitle.setText(Constants.CREATE_OFFER);

        if (currentOffer.getOfferID() >= 0) {
            //currentOffer.setOfferID(1);
            activityTitle.setText(Constants.EDIT_OFFER);
            //editDateField.setText(bestBeforeDate.toString());
            publishOfferButton.setEnabled(false);
            final Handler handler = new Handler();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (currentOffer.fillObjectFromDatabase()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                titleInputField.setText(currentOffer.getShortDescription());
                                longDescriptionInputField.setText(currentOffer.getLongDescription());

                                editDateField.setText(String.format("%tF", currentOffer.getMhd()));
                                publishOfferButton.setEnabled(true);
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

        //TODO: http://stackoverflow.com/questions/17173435/send-image-file-using-java-http-post-connections

    }


    private void changeDateEditOnClickListener() {
        editDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateEditClick();
            }
        });
    }

    private void changeDateEditFocusChangeListener() {
        editDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onDateEditClick();
                }
            }
        });
    }

    private void onDateEditClick() {
        DialogFragment dateFragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                bestBeforeDate.set(year, monthOfYear, dayOfMonth);
                editDateField.setText(Constants.BEST_BEFORE + String.format("%tF", bestBeforeDate));
            }
        };
        dateFragment.show(fragMan, "datePicker");
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


    public void makePictureButtonClicked(View view) {
        //TODO: hasSystemFeature(PackageManager.FEATURE_CAMERA)
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //makes sure any app can handle the Intent:
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "foodSharingPhoto.jpg");
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG, "In onActivityResult");
        Log.i(LOG, "In onActivityResult " + requestCode + " " + resultCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i(LOG, "In onActivityResult: result ok");
            Bundle extras = data.getExtras();
            bitmap = (Bitmap)extras.get(Constants.DATA_WORD);
            photoImageView.setImageBitmap(bitmap);
        }
    }

    public void publishOfferButtonClicked(View view)
    {
        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                currentOffer.setPicture(photoFile);
                currentOffer.setShortDescription(titleInputField.getText().toString().trim());
                currentOffer.setLongDescription(longDescriptionInputField.getText().toString().trim());
                currentOffer.setTransactID(1);
                currentOffer.setCategory(1); // exchanged "Obst" 2 test app
                currentOffer.setMhd(2015, 3, 3);
                currentOffer.setDateAdded();
                currentOffer.setPickupTimes(Constants.BLA_WORD); //uebergabe muss ausgelesen werden von wo?

                if (currentOffer.saveObjectToDatabase())
                {
                    handler.post(new Runnable (){
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), Constants.OFFER_EDITED, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
                else
                {
                    final String errorMessage=currentOffer.getErrorMessage();
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

    }

}
