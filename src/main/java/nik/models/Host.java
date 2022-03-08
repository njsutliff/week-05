package nik.models;

import java.math.BigDecimal;

//id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate
public class Host {
    public String iD;
    public String lastName;
    public String email;
    public String phone;
    public String address;
    public String city;
    public String state;
    public String postalCode;
    public BigDecimal standardRate;
    public BigDecimal weekendRate;

    public void setReservation(Reservation reservation) {//only once
        this.reservation = reservation;
    }

    public Reservation reservation;
    public String getiD() {
        return iD;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public BigDecimal getStandardRate() {
        return standardRate;
    }

    public BigDecimal getWeekendRate() {
        return weekendRate;
    }

    public Reservation getReservation() {
        return reservation;
    }


}
