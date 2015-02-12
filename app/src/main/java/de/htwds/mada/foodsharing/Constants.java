package de.htwds.mada.foodsharing;

/**
 * Created by Katha on 04.02.2015.
 */
public class Constants {
    public static final String keyOfferID=Constants.class.getName() + "keyOfferID";
    public static final String keyUserID=Constants.class.getName() + "keyUserID";

    //success messages
    public static final String ACCOUNT_UPDATED = "Account updated successfully!";
    public static final String OFFER_FETCHED = "Offer data fetched successfully!";
    public static final String OFFER_EDITED = "Offer edited successfully!";
    public static final String SIGNED_OUT = "Sign out successful!";
    public static final String USER_FETCHED = "User data fetched successfully!";

    //missing failures
    public static final String NO_NEGATIVE_NUMBER = "Only positive numbers allowed!";
    public static final String NO_VALID_EMAIL = "No valid email address!";
    public static final String NO_VALID_PLZ = "PLZ must be between 00000 and 99999!";
    public static final String NO_VALID_COUNTRY = "The given country code is too long!";
    public static final String NOT_NEGATIVE = "No negative numbers!";
    public static final String NO_ARGUMENT = "No or empty object given!";

    //chars
    public static final String SPACE = " ";
    public static final String NEWLINE = "\n";
    public static final String QUESTIONMARK = "?";
    public static final String AT_SIGN = "@";



    //abkuerzings
    public static final String OFFER_ID_ABK = "oid";
    public static final String DESCRIPTION_ABK = "descr";

    public static final String HTTP_BASE_URL = "http://odin.htw-saarland.de/";
    public static final String URL_CREATE_OFFER = "create_offer.php";
    public static final String URL_GET_OFFER = "get_offer_details.php";
    public static final String URL_CREATE_USER = "create_user.php";
    public static final String URL_GET_USER = "get_user_details.php";

    //JSON stuff
    public static final String JSON_POST = "POST";
    public static final String JSON_GET = "GET";
    public static final String JSON_UTF = "utf-8";
    public static final String JSON_ISO = "iso-8859-1";
    public static final String JSON_TRANS_ID = "transaction_id";
    public static final String JSON_IMAGE_ID = "image_id";
    public static final String JSON_VALID_DATE = "valid_date";

    //single words
    public static final String SUCCESS_WORD = "success";
    public static final String MESSAGE_WORD = "message";
    public static final String OFFER_WORD = "offer";
    public static final String TITLE_WORD= "title";
    public static final String DATE_WORD = "date";
    public static final String DATA_WORD = "data";

    //multiple words
    public static final String LOG_SUCCESS = "Success: ";
    public static final String LOG_MESSAGE = ", message: ";
    public static final String CREATE_OFFER = "create offer";
    public static final String EDIT_OFFER =  "Edit offer";

    //Errors
    public static final String HTTP_ERROR = "Error in http connection ";
    public static final String CONVERTING_ERROR = "Error converting result ";
    public static final String STRING_PARSING_ERROR = "Error parsing result string ";
    public static final String OFFER_INFO_RETRIEVING_ERROR = "Could not retrieve offer info!";
    public static final String USER_INFO_RETRIEVING_ERROR = "Could not retrieve user info!";
    public static final String UNKNOWN_ERROR = "Unknown error!";


}
