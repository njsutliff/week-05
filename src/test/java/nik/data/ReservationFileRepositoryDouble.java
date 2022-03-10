package nik.data;

import nik.models.Host;
import nik.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationFileRepositoryDouble implements  ReservationRepository{

    List<Reservation> list = new ArrayList<>();
    public  ReservationFileRepositoryDouble() {
        final String id = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now());
        reservation.setGuestId(1);
        reservation.setTotal(BigDecimal.ONE);

    }
    public List<Reservation> findByHostId(String iD) {
        return  list.stream().filter(reservation -> reservation.id.equals(iD))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getFutureReservations(Host h) {
        return null;
    }

    @Override
    public Host getHostFromList(List<Reservation> reservationList, Host host) {
        return null;
    }

    @Override
    public Reservation add(Reservation r) {
        return null;
    }
}
