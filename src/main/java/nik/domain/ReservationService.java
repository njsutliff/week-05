package nik.domain;

import nik.data.ReservationFileRepository;
import nik.models.Host;
import nik.models.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private final ReservationFileRepository reservationRepository;
    public ReservationService(ReservationFileRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }
    public List<Reservation> findByHostId(String Id){
        return reservationRepository.findByHostId(Id);
     }
     public List<Reservation> getFutureReservations(Host h){
        return  reservationRepository.getFutureReservations(h);
     }
}
