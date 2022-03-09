package nik.data;

import com.google.common.collect.ImmutableList;
import nik.models.Guest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository {
    private  final String filePath;
    public GuestFileRepository(String filePath){ this.filePath = filePath;}
    /**
     * returns all Guests
     *
     * @return list of all Guests in guests.csv
     */
    public ArrayList<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./data/guests.csv"))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Guest getGuestByLastName(String lastName) {
        return findAll().stream()
                .filter(i -> i.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        //guest_id,first_name,last_name,email,phone,state
        result.setGuestId(fields[0]);
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone(fields[4]);
        result.setState(fields[5]);
        return result;
    }


}
