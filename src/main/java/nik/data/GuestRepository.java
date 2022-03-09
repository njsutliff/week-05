package nik.data;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import nik.models.Guest;

import java.util.ArrayList;

public interface GuestRepository {
    public ArrayList<Guest> findAll();

    public Guest getGuestByLastName(String lastName);

}
