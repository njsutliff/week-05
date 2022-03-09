package nik.models;

import java.math.BigDecimal;

//id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate
public class Host {
    public String iD;
    public String lastName;
    public String email;

    public void setiD(String iD) {
        this.iD = iD;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStandardRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    public void setWeekendRate(BigDecimal weekendRate) {
        this.weekendRate = weekendRate;
    }

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
