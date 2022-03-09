package nik.domain;

import nik.data.HostFileRepository;
import nik.data.ReservationFileRepository;
import nik.models.Reservation;

import java.util.List;

public class HostService {

    private final HostFileRepository hostRepository;

    public HostService(HostFileRepository hostRepository) {
        this.hostRepository = hostRepository;
    }
    public String getIdFromEmail(String email){
        return hostRepository.getIdFromEmail(email);
    }
    public Reservation findByEmail(String email) {
        return hostRepository.findReservationByEmail(email);
    }
}
