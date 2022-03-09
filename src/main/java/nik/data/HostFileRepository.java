package nik.data;

import nik.models.Host;
import nik.models.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository {
    ReservationFileRepository repository = new ReservationFileRepository("./data/reservations");
    private final String filePath;

    public HostFileRepository(String filePath) {
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

    /**
     * Given a host's email, returns that host's Id. used by service->UI->View Reservations
     * @param  email
     * @return Id
     */
    public String getIdFromEmail(String email) {
        List<Host> allHosts = getAllHosts();
        String iD = "";
        for (Host h : allHosts){
            if(h.email.equals(email)){
                 iD = h.getiD();
            }
        }
        return iD;
    }

    /**
     * Given a host's email, return the Reservation for that email. Uses ReservationFileRepository
     * @param email
     * @return
     */
    public Reservation findReservationByEmail(String email) {
        Reservation res = new Reservation();
        List<Reservation> reservationList = repository.findAll();
        for (Reservation r : reservationList) {
            if (r.getId().equals(getIdFromEmail(email))) {
                res = r;
            }
        }
        return  res;
    }



    private Host deserialize (String[]fields){
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