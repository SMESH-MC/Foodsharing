package de.htwds.mada.foodsharing;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Offer {

    private int offerID;
    private int transactID;
    private int category;
    private String shortDescription;
    private String longDescription;
    private File picture;
    private Calendar mhd;
    private Calendar dateAdded;
    private String pickupTimes; //too complex to use date or time types


    //Exceptions

    public Offer() {
        mhd = new GregorianCalendar();
    }

    public Offer(JSONObject offerJSONObject)
    {
        mhd = new GregorianCalendar();

        this.setOfferID(offerJSONObject.optInt(Constants.ID_ABK, -1));
        this.setTransactID(offerJSONObject.optInt(Constants.JSON_TRANS_ID, -1));
        //TODO: this.setPicture();
        this.setShortDescription(offerJSONObject.optString(Constants.TITLE_WORD));
        this.setLongDescription(offerJSONObject.optString(Constants.DESCRIPTION_ABK));
        //TODO: this.setMhd(userJSONObject.optString("bbd"));
        //TODO: this.setDateAdded(userJSONObject.optString("date"));
        //TODO: this.setValidDate(userJSONObject.optString("valid_date"));
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
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
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
                //TODO: this.setPicture();
                this.setShortDescription(offerJSONObject.optString(Constants.TITLE_WORD));
                this.setLongDescription(offerJSONObject.optString(Constants.DESCRIPTION_ABK));
                //TODO: this.setMhd(userJSONObject.optString("bbd"));
                //TODO: this.setDateAdded(userJSONObject.optString("date"));
                //TODO: this.setValidDate(userJSONObject.optString("valid_date"));
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

    public boolean saveObjectToDatabase()
    {
        errorMessage= Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_TRANS_ID,String.valueOf(this.getTransactID())));
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_IMAGE_ID,"4")); //was macht die 4 hier?
        nameValuePairs.add(new BasicNameValuePair(Constants.TITLE_WORD, this.getShortDescription()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DESCRIPTION_ABK, this.getLongDescription()));
        nameValuePairs.add(new BasicNameValuePair("bbd", this.getMhd().toString())); // was ist bbd?
        Timestamp timestamp=new Timestamp(this.getDateAdded().getTimeInMillis());
        nameValuePairs.add(new BasicNameValuePair(Constants.DATE_WORD, timestamp.toString()));
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_VALID_DATE, "1423216493")); //was kommt hier rein?
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_VALID_DATE, "1423216493")); //ja eher keine hardcgecodete Zeit^^

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + Constants.URL_CREATE_OFFER, Constants.JSON_POST, nameValuePairs);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD, Constants.UNKNOWN_ERROR);

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }



    public String toString()
    {
        return getShortDescription() + Constants.NEWLINE + getLongDescription();
    }
}
