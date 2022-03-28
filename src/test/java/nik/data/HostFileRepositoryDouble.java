package nik.data;

import nik.models.Host;
import nik.models.Reservation;

import java.util.List;

public class HostFileRepositoryDouble implements HostRepository{

    public HostFileRepositoryDouble(){
        final String path = "./data/test.csv";
    }
    @Override
    public String getIdFromEmail(String email) {
        return null;
    }

    @Override
    public List<Host> getAllHosts() {
        return null;
    }

    @Override
    public Host getHostFromEmail(String email) {
        return null;
    }


    public Host getHostFromLastName(String lastName) {
        return null;
    }
}
