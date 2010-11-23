package wevote_app;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;
import java.util.ArrayList;

/**
 *
 * @author hollgam & NorthernDemon
 */
public class MobileNumber implements Serializable, Comparable<MobileNumber>{
    private String phoneNumber; // phone number with plus, "+3805045643"
    private char gender; // u - unknown, m - male, f - female
    private Date birthDate; //calendar instance, how it works

    /**
     *
     * @param phoneNumber
     * @param gender
     * @param birthDate
     */
    public MobileNumber(String phoneNumber, char gender, Date birthDate) {
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    /**
     *
     * @param phoneNumber
     */
    public MobileNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @param birthDate
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     *
     * @return
     */
    public char getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        //handle adding new numbers to the server
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @param o
     * @return
     */
    public int compareTo(MobileNumber o) {
        return phoneNumber.compareTo(o.getPhoneNumber());
    }

    String getBirthDateString() {
        if(birthDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(birthDate); //using the date object from earlier
        } else {
            return " ";          
        }
    }
}