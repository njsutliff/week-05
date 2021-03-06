package nik.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {

    public String id;
    public LocalDate startDate;
    public LocalDate endDate;
    public BigDecimal total;
    private Host host;
    private Guest guest;

    public Reservation() {
    }

    public Reservation(Host host, Guest guest, LocalDate startDate, LocalDate endDate) {
        this.host = host;
        this.guest = guest;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Reservation(Host host, Guest guest) {
        this.host = host;
        this.guest = guest;
    }

    public String getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }


    public BigDecimal getTotal() {
        return total;
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

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }


}
