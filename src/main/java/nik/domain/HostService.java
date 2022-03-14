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
                //TODO host service add error here
            }
        }
            return "Email not found";

    }
    public List<Host> getHostsFromState(String stateAbbrev){
        return  hostRepository.getHostsFromState(stateAbbrev);
    }

    public Result<Host> getHostFromEmail(String email) {
        Result<Host> result = new Result<>();
        if(getIdFromEmail(email).equalsIgnoreCase("Email not found")){
            result.addErrorMessage("Host not found! ");
            //TODO host service add error here
        }
        if(hostRepository.getHostFromEmail(email)!=null){
        result.setPayload(hostRepository.getHostFromEmail(email));
        }
        return result;
    }
        public Reservation findByEmail(String email) {
        return hostRepository.findReservationByEmail(email);
    }

   // private Result<Host> validate(Host host) {


   // }
    }
