package nik.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {

    public  String id;
    public LocalDate startDate;
    public LocalDate endDate;
    public int guestId;
    public BigDecimal total;
    public Guest guest;
    public Host host;

    public String getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getGuestId() {
        return guestId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Guest getGuest() {
        return guest;
    }

    public Host getHost() {
        return host;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setHost(Host host) {
        this.host = host;
    }


    public Reservation(){

    }
}
