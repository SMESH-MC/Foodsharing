package de.htwds.mada.foodsharing;

import java.io.File;
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
    private static final String NOT_NEGATIVE = "No negative numbers!";
    private static final String NO_ARGUMENT = "No or empty object given!";

    public Offer() {
        mhd = new GregorianCalendar();
    }

    public int getOfferID() {        return offerID;    }
    public void setOfferID(int offerID) {
        if (offerID < 0) {
            throw new NumberFormatException(NOT_NEGATIVE);
        }
        this.offerID = offerID;
    }

    public int getTransactID() {        return transactID;    }
    public void setTransactID(int transactID) {
        if (transactID < 0) {
            throw new NumberFormatException(NOT_NEGATIVE);
        }
        this.transactID = transactID;
    }

    public int getCategory() {        return category;    }
    public void setCategory(int category) {
        if (category < 0) {
            throw new NumberFormatException(NOT_NEGATIVE);
        }
        this.category = category;
    }

    public String getShortDescription() {        return shortDescription;    }
    public void setShortDescription(String shortDescription) {
        if (shortDescription.trim().isEmpty()) {
            throw new IllegalArgumentException(NO_ARGUMENT);
        }
        this.shortDescription = shortDescription.trim();
    }

    public String getLongDescription() {        return longDescription;    }
    public void setLongDescription(String longDescription) {
        if (longDescription.trim().isEmpty()) {
            throw new IllegalArgumentException(NO_ARGUMENT);
        }
        this.longDescription = longDescription.trim();
    }

    public File getPicture() {        return picture;    }
    public void setPicture(File picture) {
        if (picture == null) {
            throw new IllegalArgumentException(NO_ARGUMENT);
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
            throw new IllegalArgumentException(NO_ARGUMENT);
        }
        this.pickupTimes = pickupTimes.trim();
    }
}
