package nik.models;

import java.math.BigDecimal;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return iD.equals(host.iD) && lastName.equals(host.lastName) && email.equals(host.email)
                && phone.equals(host.phone) && address.equals(host.address)
                && city.equals(host.city) && state.equals(host.state)
                && postalCode.equals(host.postalCode) && standardRate.equals(host.standardRate)
                && weekendRate.equals(host.weekendRate) && Objects.equals(reservation, host.reservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iD, lastName, email, phone, address, city,
                state, postalCode, standardRate, weekendRate, reservation);
    }
}
