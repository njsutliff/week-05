package nik.data;

import nik.models.Host;
import nik.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository


public class HostFileRepository implements HostRepository {
    ReservationFileRepository repository = new ReservationFileRepository("./data/reservations");
    private final String filePath;
@Autowired
    public HostFileRepository(@Value("./data/hosts.csv") String filePath) {
        this.filePath = filePath;
    }
    /**
     * Gets a list of all Hosts (i.e. in hosts.csv)
     * @return List of hosts.
     */
    @Override
    public List<Host> getAllHosts() {

        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;

    }
    public Host getHostFromId(String Id){
        return  getAllHosts().stream()
                .filter(i -> i.getiD().equals(Id))
                .findFirst()
                .orElse(null);
    }
    /**
     * @param stateAbbrev state to find
     * @return list of hosts with that state
     */
    public List<Host> getHostsFromState(String stateAbbrev) {
        return getAllHosts().stream()
                .filter(i -> i.getState().equalsIgnoreCase(stateAbbrev))
                .collect(Collectors.toList());

    }
    /**
     * @param email email to get Host from
     * @return Host returned
     */
    public Host getHostFromEmail(String email) {
        return getAllHosts().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public Host getHostFromLastName(String lastName) {
        return getAllHosts().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }
    /**
     * Given a host's email, returns that host's Id. used by service->UI->View Reservations
     * @param email email to get Id from
     * @return Id of host
     */
    public String getIdFromEmail(String email) {
        List<Host> allHosts = getAllHosts();
        return  getAllHosts().stream().filter(host -> host.getEmail().equals(email))
                .findFirst().orElse(null).getiD();

    }
    private Host deserialize(String[] fields) {
        //id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate

        Host result = new Host();
        result.setiD(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhone(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostalCode(fields[7]);
        result.setStandardRate(BigDecimal.valueOf(Double.parseDouble(fields[8])));
        result.setWeekendRate(BigDecimal.valueOf(Double.parseDouble(fields[9])));

        return result;
    }
}
