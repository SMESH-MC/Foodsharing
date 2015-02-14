package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class OfferEditActivity extends Activity  {
    private static final String LOG=OfferEditActivity.class.getName();

    private TextView activityTitle;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView photo;
    private Bitmap bitmap;

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

        photo = (ImageView)findViewById(R.id.offeringPhoto);

        titleInputField = (EditText) findViewById(R.id.title_tv);

        bestBeforeDate = Calendar.getInstance();
        editDateField = (EditText)findViewById(R.id.best_before_date_edit);
        changeDateEditOnClickListener();
        changeDateEditFocusChangeListener();
        
        longDescriptionInputField = (EditText) findViewById(R.id.detailed_description_tv);

        publishOfferButton = (Button) findViewById(R.id.publish_offer_btn);


        currentOffer=new Offer();
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

        publishOfferButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Log.i(LOG,"Title: "+titleInputField.getText().toString().trim());

               final Handler handler = new Handler();
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        currentOffer.setShortDescription(titleInputField.getText().toString().trim());
                        currentOffer.setLongDescription(longDescriptionInputField.getText().toString().trim());
                        currentOffer.setOfferID(6);
                        currentOffer.setTransactID(1);
                        currentOffer.setCategory(1); // exchanged "Obst" 2 test app
                        currentOffer.setMhd(2015, 3, 3);
                        //currentOffer.setDateAdded();
                        currentOffer.setPickupTimes(Constants.BLA_WORD); //uebergabe muss ausgelesen werden von wo?

                        if (currentOffer.saveObjectToDatabase())
                            //ToDo speichern in die DB
                        {
                            handler.post(new Runnable (){
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), Constants.OFFER_EDITED, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                            // Going back to browser
//                            Intent i = new Intent(getApplicationContext(), BrowseCreateEdit.class);
//                            startActivity(i);

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
        });
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
            bitmap = (Bitmap)extras.get(Constants.DATA_WORD);
            photo.setImageBitmap(bitmap);
        }
    }


}
