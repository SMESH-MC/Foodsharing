package de.htwds.mada.foodsharing;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private int uid;
    private String email;
    private char[] password; //char[] because of a better security (nullable)
    /* http://stackoverflow.com/questions/10393951/getting-a-char-array-from-the-user-without-using-a-string */
    private String username;
    //address infos
    private String street;
    private String houseNumber; //String because of possible house numbering like "109 - 111a"
    private String additional;
    private int plz;  //0-99999
    private String city;
    private String country;

    //evtl. Optional
    private String vorname;
    private String nachname;

    //email regexp test
    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                              + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /*/Exceptions
    private static final String NO_NEGATIVE_NUMBER = "Only positive numbers allowed!";
    private static final String NO_VALID_EMAIL = "No valid email address!";
    private static final String NO_ARGUMENT = "No argument given!";
    private static final String NO_VALID_PLZ = "PLZ must be between 00000 and 99999!";
    private static final String NO_VALID_COUNTRY = "The given country code is too long!";*/

    private static final String currentUserIdKey="de.htwds.mada.foodsharing.currentuserid";

    public User(Context context)
    {
        setUid(PreferenceManager.getDefaultSharedPreferences(context).getInt(currentUserIdKey, -1));
    }

    public User(Context context, int uid)
    {
        setUid(uid);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(currentUserIdKey, getUid()).apply();
    }

    public int getUid() {        return uid;    }
    public void setUid(int uid) {
        //test if positive
        if (uid >= 0) {
            this.uid = uid;
        } else {
            throw new NumberFormatException(Constants.NO_NEGATIVE_NUMBER);
        }
    }

    public String getEmail() {        return email;    }
    public void setEmail(String email) {
        //test if not empty
        if (email.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        //test if it is a correct email address
        pattern = Pattern.compile(EMAIL_REGEXP);
        matcher = pattern.matcher(email);
        if (matcher.matches()) {
            this.email = email;
        } else {
            throw new IllegalArgumentException(Constants.NO_VALID_EMAIL);
        }
    }

    public char[] getPassword() {        return password;    }
    public void setPassword(char[] password) {
        //test if not empty
        if (password[0] == '\0') {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.password = password;
    }

    public String getUsername() {        return username;    }
    public void setUsername(String username) {
        //test if not empty
        if (username.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.username = username.trim();
    }

    public String getStreet() {        return street;    }
    public void setStreet(String street) {
        //test if not empty
        if (street.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.street = street.trim();
    }

    public String getHouseNumber() {        return houseNumber;    }
    public void setHouseNumber(String houseNumber) {
        //test if not empty
        if (houseNumber.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.houseNumber = houseNumber.trim();
    }

    public String getAdditional() {        return additional;    }
    public void setAdditional(String additional) {
        this.additional = additional.trim();
    }

    public int getPlz() {        return plz;    }
    public void setPlz(int plz) {
        if (plz < 0 ||  plz > 99999) {
            throw new NumberFormatException(Constants.NO_VALID_PLZ);
        }
        this.plz = plz;
    }

    public String getCity() {        return city;    }
    public void setCity(String city) {
        if (city.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.city = city.trim();
    }

    public String getCountry() {        return country;    }
    public void setCountry(String country) {
        /*if (country.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        } else if (country.trim().length() > 2) {
            throw new IllegalArgumentException(Constants.NO_VALID_COUNTRY);
        }
        this.country = country.trim();*/
        this.country = "DE";
    }

    public String getVorname() {        return vorname;    }
    public void setVorname(String vorname) {
        if (vorname.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.vorname = vorname.trim();
    }

    public String getNachname() {        return nachname;    }
    public void setNachname(String nachname) {
        if (nachname.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.nachname = nachname.trim();
    }


    private String errorMessage;
    public String getErrorMessage() {return errorMessage; }


    public boolean fillObjectFromDatabase()
    {
        errorMessage="";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(this.getUid())));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + Constants.URL_GET_USER, Constants.JSON_GET, nameValuePairs);

        if (returnObject.optBoolean(Constants.SUCCESS_WORD))
        {
            JSONObject userJSONObject=returnObject.optJSONObject("0");
            if (userJSONObject != null)
            {
                this.setEmail(userJSONObject.optString("email"));
                this.setVorname(userJSONObject.optString("vorname"));
                this.setUsername(userJSONObject.optString("username"));
                this.setNachname(userJSONObject.optString("nachname"));
                this.setStreet(userJSONObject.optString("strasse"));
                this.setHouseNumber(userJSONObject.optString("hausnummer"));
                this.setAdditional(userJSONObject.optString("zusatz"));
                this.setPlz(userJSONObject.optInt("plz"));
                this.setCity(userJSONObject.optString("ort"));
                this.setCountry(userJSONObject.optString("land"));
            }
            else
            {
               errorMessage=Constants.USER_INFO_RETRIEVING_ERROR;
                return false;
            }
        }

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD);

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }

    public boolean saveObjectToDatabase()
    {
        errorMessage="";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email", this.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("password", this.getPassword().toString()));
        nameValuePairs.add(new BasicNameValuePair("username", this.getUsername()));
        nameValuePairs.add(new BasicNameValuePair("vorname", this.getVorname()));
        nameValuePairs.add(new BasicNameValuePair("nachname", this.getNachname()));
        nameValuePairs.add(new BasicNameValuePair("strasse", this.getStreet()));
        nameValuePairs.add(new BasicNameValuePair("hausnummer", this.getHouseNumber()));
        nameValuePairs.add(new BasicNameValuePair("zusatz", this.getAdditional()));
        nameValuePairs.add(new BasicNameValuePair("plz", String.valueOf(this.getPlz())));
        nameValuePairs.add(new BasicNameValuePair("ort", this.getCity()));
        nameValuePairs.add(new BasicNameValuePair("land", this.getCountry()));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.HTTP_BASE_URL + Constants.URL_CREATE_USER, Constants.JSON_POST, nameValuePairs);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD, Constants.UNKNOWN_ERROR);

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }

}
