package nik.data;

import com.google.common.collect.ImmutableList;
import nik.models.Guest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository{
   /** public ImmutableList<Guest> findByDate(LocalDate date) {
        ArrayList<Forage> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(date)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
                    result.add(deserialize(fields, date));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }**/

   /** @Override
    public ImmutableList<Guest> getGuests() {
        return null;
    }**/
}
