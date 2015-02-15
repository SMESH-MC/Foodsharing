package de.htwds.mada.foodsharing;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Offer {
    private static final String LOG=Offer.class.getName();

    private int offerID;
    private int transactID;
    private int category;
    private String shortDescription;
    private String longDescription;
    private File picture;

    private Calendar dateAdded;
    private String pickupTimes; //too complex to use date or time types
    private final Calendar mhd = new GregorianCalendar();


    private Context context;

    //Exceptions

    public Offer(Context context) {
        this.context=context;
    }

    public Offer(Context context, JSONObject offerJSONObject)
    {
        this.setOfferID(offerJSONObject.optInt(Constants.ID_ABK, -1));
        this.setTransactID(offerJSONObject.optInt(Constants.JSON_TRANS_ID, -1));
        //TODO: this.setPicture();
        this.setShortDescription(offerJSONObject.optString(Constants.TITLE_WORD));
        this.setLongDescription(offerJSONObject.optString(Constants.DESCRIPTION_ABK));
        //TODO: this.setMhd(userJSONObject.optString("bbd"));
        //TODO: this.setDateAdded(userJSONObject.optString("date"));
        //TODO: this.setValidDate(userJSONObject.optString("valid_date"));

        this.context=context;
    }

    public int getOfferID() {        return offerID;    }
    public void setOfferID(int offerID) {
        /*
        if (offerID < 0) {
            throw new NumberFormatException(NOT_NEGATIVE);
        }
        */
        this.offerID = offerID;
    }

    public int getTransactID() {        return transactID;    }
    public void setTransactID(int transactID) {
        if (transactID < 0) {
            throw new NumberFormatException(Constants.NOT_NEGATIVE);
        }
        this.transactID = transactID;
    }

    public int getCategory() {        return category;    }
    public void setCategory(int category) {
        if (category < 0) {
            throw new NumberFormatException(Constants.NOT_NEGATIVE);
        }
        this.category = category;
    }

    public String getShortDescription() {        return shortDescription;    }
    public void setShortDescription(String shortDescription) {
        if (shortDescription.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        this.shortDescription = shortDescription.trim();
    }

    public String getLongDescription() {        return longDescription;    }
    public void setLongDescription(String longDescription) {
        if (longDescription.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        this.longDescription = longDescription.trim();
    }

    public File getPicture() {        return picture;    }
    public void setPicture(File picture) {
        if (picture == null) {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        this.picture = picture;
    }
    public void setPicture(byte[] picture)
    {
        boolean pictureWrittenSuccessfully=true;

        File photoFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "foodSharingPhoto.jpg");

        try ( FileOutputStream fileOutputStream = new FileOutputStream(photoFile)) {
            fileOutputStream.write(picture);
            fileOutputStream.close();
        } catch (Exception ex) {
            pictureWrittenSuccessfully=false;
            Log.e(LOG, "Error writing image to file!");
        }

        if (pictureWrittenSuccessfully) {
            Log.i(LOG, "Picture written successfully!");
            setPicture(photoFile);
        }
    }

    public Calendar getMhd() {        return mhd;    }
    public void setMhd(int year, int month, int day) {
        mhd.setLenient(false);          //make calendar validating
        mhd.set(year, month, day); //throws exception if date is invalid
    }

    public Calendar getDateAdded() {        return dateAdded;    }
    public void setDateAdded() {
        this.dateAdded = Calendar.getInstance(); //take current time
    }

    public String getPickupTimes() {        return pickupTimes;    }
    public void setPickupTimes(String pickupTimes) {
        if (pickupTimes.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        this.pickupTimes = pickupTimes.trim();
    }

    private String errorMessage;
    public String getErrorMessage() {return errorMessage; }


    public boolean fillObjectFromDatabase() {
        errorMessage = Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(Constants.OFFER_ID_ABK, String.valueOf(this.getOfferID())));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + Constants.URL_GET_OFFER, Constants.JSON_GET, nameValuePairs);

        if (returnObject.optBoolean(Constants.SUCCESS_WORD))
        {
            JSONArray offerJSONArray=returnObject.optJSONArray(Constants.OFFER_WORD);
            JSONObject offerJSONObject=offerJSONArray.optJSONObject(0);
            if (offerJSONObject != null)
            {
                this.setTransactID(offerJSONObject.optInt(Constants.JSON_TRANS_ID, -1));
                this.setShortDescription(offerJSONObject.optString(Constants.TITLE_WORD));
                this.setLongDescription(offerJSONObject.optString(Constants.DESCRIPTION_ABK));
                //TODO: this.setMhd(userJSONObject.optInt("bbd"));
                //TODO: this.setDateAdded(userJSONObject.optString("date"));
                //TODO: this.setValidDate(userJSONObject.optInt("valid_date"));
                Log.i(LOG, "filling");
                //TODO: handle errors
                this.getImage(offerJSONObject.optInt("image_id", -1));
            }
            else
            {
                errorMessage=Constants.OFFER_INFO_RETRIEVING_ERROR;
                return false;
            }
        }

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD);

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }

    private boolean getImage(int imageID)
    {
        errorMessage = Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("pid", String.valueOf(imageID)));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + "get_image.php", Constants.JSON_GET, nameValuePairs);

        Log.i(LOG, "Getting image ...");
        if (returnObject.optBoolean(Constants.SUCCESS_WORD))
        {
            JSONArray imageJSONArray=returnObject.optJSONArray("image");
            JSONObject imageJSONObject=imageJSONArray.optJSONObject(0);
            if (imageJSONObject != null)
            {
                try {
                    this.setPicture(Base64.decode(imageJSONObject.optString("image", ""), Base64.DEFAULT));
                } catch (Exception ex) {
                    Log.e(LOG, "Error decoding image!");
                }
            }
            else
            {
                errorMessage="Failed to fetch image!";
                return false;
            }
        }

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD);

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }

    /*
    public boolean saveObjectToDatabase()
    {
        errorMessage= Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_TRANS_ID,String.valueOf(this.getTransactID())));
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_IMAGE_ID,"4")); //was macht die 4 hier?
        nameValuePairs.add(new BasicNameValuePair(Constants.TITLE_WORD, this.getShortDescription()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DESCRIPTION_ABK, this.getLongDescription()));
        nameValuePairs.add(new BasicNameValuePair("bbd", this.getMhd().toString())); // was ist bbd?
        Timestamp timestamp=new Timestamp(this.getDateAdded().getTimeInMillis());
        nameValuePairs.add(new BasicNameValuePair(Constants.DATE_WORD, timestamp.toString()));
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_VALID_DATE, "1423216493")); //was kommt hier rein?

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + Constants.URL_CREATE_OFFER, Constants.JSON_POST, nameValuePairs);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD, Constants.UNKNOWN_ERROR);

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }
    */


    public boolean saveObjectToDatabase()
    {
        errorMessage= Constants.EMPTY_STRING;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        FileBody fileBody = new FileBody(this.getPicture());
        builder.addPart("image", fileBody);
        builder.addTextBody(Constants.JSON_TRANS_ID, String.valueOf(this.getTransactID()));
        builder.addTextBody("bbd", this.getMhd().toString());
        builder.addTextBody(Constants.TITLE_WORD, this.getShortDescription());
        builder.addTextBody(Constants.DESCRIPTION_ABK, this.getLongDescription());
        Timestamp timestamp=new Timestamp(this.getDateAdded().getTimeInMillis());
        builder.addTextBody(Constants.DATE_WORD, timestamp.toString());
        builder.addTextBody(Constants.JSON_VALID_DATE, "1423216493");
        HttpEntity httpRequestEntity = builder.build();

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeMultipartHttpRequest("http://odin.htw-saarland.de/create_offer_with_image.php", httpRequestEntity);
        //JSONObject returnObject = jsonParser.makeMultipartHttpRequest("http://odin.htw-saarland.de/showMultipartContent.php", httpRequestEntity);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD, Constants.UNKNOWN_ERROR);


        //TODO: setimageid

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }


    /*
    Currently only for the items in ListView
     */
    public String toString()
    {
        return getShortDescription() + Constants.NEWLINE + getLongDescription();
    }
}
