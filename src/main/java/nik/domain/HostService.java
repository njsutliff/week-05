package nik.domain;

import nik.data.HostFileRepository;
import nik.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
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

    public Result<Host> getHostFromEmail(String email) {
        Result<Host> hostResult = validate(email);
        if (!hostResult.isSuccess()) {
            return hostResult;
        }
        hostResult.setPayload(hostRepository.getHostFromEmail(email));
        return hostResult;
    }

    private Result<Host> validate(String email) {
        Result<Host> result = new Result<>();

        if (email == null){
            result.addErrorMessage("Host not found");
        }
         if (hostRepository.getHostFromEmail(email) == null) {
            result.addErrorMessage("Host has no reservations.");
        }
        return result;
    }
}
