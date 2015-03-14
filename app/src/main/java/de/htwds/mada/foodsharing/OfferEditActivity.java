package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;


public class OfferEditActivity extends Activity {
    private static final String LOG=OfferEditActivity.class.getName();

    private TextView activityTitle;


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView photoImageView;
    private Bitmap bitmap;
    private File photoFile;

    private final Handler handler = new Handler();
    private final FragmentManager fragMan = getFragmentManager();
    /* fields */
    //title field
    private EditText titleInputField;
    //category field
    private EditText editCategoryField;
    //mhd field
    private static Calendar bestBeforeDate;
    private EditText bestBeforeDateInputField;
    //description field
    private EditText longDescriptionInputField;
    //finish button
    private Button publishOfferButton;



    private Offer currentOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_edit);

        activityTitle = (TextView) findViewById(R.id.offerEditActivityTitle);

        photoImageView = (ImageView) findViewById(R.id.offerPicture);

        titleInputField = (EditText) findViewById(R.id.title_tv);


        editCategoryField = (EditText) findViewById(R.id.offer_category_edit);
        changeCategoryEditFocusChangeListener();
        changeCategoryEditOnClickListener();

        bestBeforeDate = Calendar.getInstance();
        bestBeforeDateInputField = (EditText) findViewById(R.id.best_before_date_edit);
        changeDateEditOnClickListener();
        changeDateEditFocusChangeListener();

        longDescriptionInputField = (EditText) findViewById(R.id.detailed_description_tv);
        publishOfferButton = (Button) findViewById(R.id.publish_offer_btn);


        currentOffer = new Offer(OfferEditActivity.this);
        currentOffer.setOfferID(getIntent().getIntExtra(Constants.keyOfferID, -1));
        activityTitle.setText(Constants.CREATE_OFFER);

        if (currentOffer.getOfferID() >= 0) {
            activityTitle.setText(Constants.EDIT_OFFER);
            currentOffer.setEdited(true);
            new RetrieveOfferInfoTask().execute();
        }


    }


    private void changeCategoryEditOnClickListener() {
        editCategoryField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                onCategorieEditClick();
            }
        });
    }


    private void changeDateEditOnClickListener() {
        bestBeforeDateInputField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateEditClick();
            }
        });
    }

    private void changeCategoryEditFocusChangeListener() {
        editCategoryField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onCategorieEditClick();
                }
            }
        });

    }
    private void changeDateEditFocusChangeListener() {
        bestBeforeDateInputField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onDateEditClick();
                }
            }
        });
    }

    private void onCategorieEditClick() {
        DialogFragment dialog = new CategoryFragment();
        dialog.show(getFragmentManager(), Constants.CATEGORY_DIALOG_TAG);
    }


    private void onDateEditClick() {
        DialogFragment dateFragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                bestBeforeDate.set(year, monthOfYear, dayOfMonth);
                bestBeforeDateInputField.setText(String.format("%tF", bestBeforeDate));
            }
        };
        dateFragment.show(fragMan, Constants.DATEPICKER_TAG);
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

            //photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.PHOTO_FILENAME);
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /* Thumbnail */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG, Constants.IN_ONACTIVITY_RESULT);
        Log.i(LOG, Constants.IN_ONACTIVITY_RESULT + requestCode + " " + resultCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i(LOG, Constants.IN_ONACTIVITY_RESULT + Constants.RESULT_OK);
            Bundle extras = data.getExtras();
            bitmap = (Bitmap)extras.get(Constants.DATA_WORD);
            photoImageView.setImageBitmap(bitmap);
            if (currentOffer.getOfferID() >= 0) {
                currentOffer.setPictureEdited(true);
            }
        }
    }

    public void publishOfferButtonClicked(View view)
    {
        new PublishOfferTask().execute();
        /*
        final Handler handler = new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                //currentOffer.setPicture(photoFile);
                currentOffer.setPicture(bitmap);
                currentOffer.setShortDescription(titleInputField.getText().toString().trim());
                currentOffer.setLongDescription(longDescriptionInputField.getText().toString().trim());
                currentOffer.setCategory(1);
                currentOffer.setMhd(bestBeforeDateInputField.getText().toString().trim());
                currentOffer.setPickupTimes(Constants.BLA_WORD);

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
        */

    }

    private class RetrieveOfferInfoTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;
        private File pictureFile;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(OfferEditActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Retrieving offer info...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {
            if (!currentOffer.fillObjectFromDatabase()) {
                Log.e(LOG, currentOffer.getErrorMessage());
                errorOccurred=true;
                errorMessage=currentOffer.getErrorMessage();
                return null;
            }

            pictureFile=currentOffer.getPicture();

            return null;
        }


        protected void onPostExecute(Void param)
        {
            if (errorOccurred) {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                return;
            }

            titleInputField.setText(currentOffer.getShortDescription());
            longDescriptionInputField.setText(currentOffer.getLongDescription());
            bestBeforeDateInputField.setText(String.format("%tF", currentOffer.getMhd()));
            if (pictureFile != null) {
                photoImageView.setImageURI(null);
                photoImageView.setImageURI(Uri.fromFile(pictureFile));
            }

            progressDialog.dismiss();
            Toast.makeText(getBaseContext(), Constants.OFFER_FETCHED, Toast.LENGTH_LONG).show();
        }
    }

    private class PublishOfferTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(OfferEditActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Publishing offer ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {
            //currentOffer.setPicture(photoFile);
            currentOffer.setPicture(bitmap);
            currentOffer.setShortDescription(titleInputField.getText().toString().trim());
            currentOffer.setLongDescription(longDescriptionInputField.getText().toString().trim());
            currentOffer.setCategory(1);
            currentOffer.setMhd(bestBeforeDateInputField.getText().toString().trim());
            currentOffer.setPickupTimes(Constants.BLA_WORD);

            if (!currentOffer.saveObjectToDatabase()) {
                Log.e(LOG, currentOffer.getErrorMessage());
                errorOccurred=true;
                errorMessage=currentOffer.getErrorMessage();
                return null;
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

            progressDialog.dismiss();
            Toast.makeText(getBaseContext(), Constants.OFFER_EDITED, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
