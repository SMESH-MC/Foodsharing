package de.htwds.mada.foodsharing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private int uid;
    private String email;
    private char[] password; //char[] because of a better security (nullable)
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
    //Exceptions
    private static final String NO_NEGATIVE_NUMBER = "Only positive numbers allowed!";
    private static final String NO_VALID_EMAIL = "No valid email address!";
    private static final String NO_ARGUMENT = "No argument given!";
    private static final String NO_VALID_PLZ = "PLZ must be between 00000 and 99999!";
    private static final String NO_VALID_COUNTRY = "The given country code is too long!";

    public User() {    }

    public int getUid() {        return uid;    }
    public void setUid(int uid) {
        //test if positive
        if (uid >= 0) {
            this.uid = uid;
        } else {
            throw new NumberFormatException(NO_NEGATIVE_NUMBER);
        }
    }

    public String getEmail() {        return email;    }
    public void setEmail(String email) {
        //test if not empty
        if (email.trim().isEmpty()) {
            throw new NullPointerException(NO_ARGUMENT);
        }
        //test if it is a correct email address
        pattern = Pattern.compile(EMAIL_REGEXP);
        matcher = pattern.matcher(email);
        if (matcher.matches()) {
            this.email = email;
        } else {
            throw new IllegalArgumentException(NO_VALID_EMAIL);
        }
    }

    public char[] getPassword() {        return password;    }
    public void setPassword(char[] password) {
        //test if not empty
        if (password[0] == '\0') {
            throw new NullPointerException(NO_ARGUMENT);
        }
        this.password = password;
    }

    public String getUsername() {        return username;    }
    public void setUsername(String username) {
        //test if not empty
        if (username.trim().isEmpty()) {
            throw new NullPointerException(NO_ARGUMENT);
        }
        this.username = username.trim();
    }

    public String getStreet() {        return street;    }
    public void setStreet(String street) {
        //test if not empty
        if (street.trim().isEmpty()) {
            throw new NullPointerException(NO_ARGUMENT);
        }
        this.street = street.trim();
    }

    public String getHouseNumber() {        return houseNumber;    }
    public void setHouseNumber(String houseNumber) {
        //test if not empty
        if (houseNumber.trim().isEmpty()) {
            throw new NullPointerException(NO_ARGUMENT);
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
            throw new NumberFormatException(NO_VALID_PLZ);
        }
        this.plz = plz;
    }

    public String getCity() {        return city;    }
    public void setCity(String city) {
        if (city.trim().isEmpty()) {
            throw new NullPointerException(NO_ARGUMENT);
        }
        this.city = city.trim();
    }

    public String getCountry() {        return country;    }
    public void setCountry(String country) {
        /*if (country.trim().isEmpty()) {
            throw new NullPointerException(NO_ARGUMENT);
        } else if (country.trim().length() > 2) {
            throw new IllegalArgumentException(NO_VALID_COUNTRY);
        }
        this.country = country.trim();*/
        this.country = "DE";
    }

    public String getVorname() {        return vorname;    }
    public void setVorname(String vorname) {
        if (vorname.trim().isEmpty()) {
            throw new NullPointerException(NO_ARGUMENT);
        }
        this.vorname = vorname.trim();
    }

    public String getNachname() {        return nachname;    }
    public void setNachname(String nachname) {
        if (nachname.trim().isEmpty()) {
            throw new NullPointerException(NO_ARGUMENT);
        }
        this.nachname = nachname.trim();
    }
}
