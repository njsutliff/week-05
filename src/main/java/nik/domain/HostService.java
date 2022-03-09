package nik.domain;

import nik.data.HostFileRepository;
import nik.data.ReservationFileRepository;
import nik.models.Host;
import nik.models.Reservation;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HostService {

    private final HostFileRepository hostRepository;

    public HostService(HostFileRepository hostRepository ) {
        this.hostRepository = hostRepository;
    }
    public String getIdFromEmail(String email){
        List<Host> hostList = hostRepository.getAllHosts();
        for (Host h : hostList){
            if(h.getEmail().equals(email)){
                return hostRepository.getIdFromEmail(email);
            }
        }
            return "Email not found";

    }
    public Host getHostFromEmail(String email) {
        return hostRepository.getHostFromEmail(email);
    }
        public Reservation findByEmail(String email) {
        return hostRepository.findReservationByEmail(email);
    }

   // private Result<Host> validate(Host host) {


   // }
    }
