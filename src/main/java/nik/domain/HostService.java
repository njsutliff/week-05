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

    public HostService(HostFileRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public String getIdFromEmail(String email) {
                return hostRepository.getIdFromEmail(email);

    }
    public List<Host> getHostsFromState(String stateAbbrev) {
        return hostRepository.getHostsFromState(stateAbbrev);
    }

    public Host getHostFromEmail(String email) {
    return hostRepository.getHostFromEmail(email);
    }
}
