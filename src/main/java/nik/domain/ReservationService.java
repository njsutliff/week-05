package nik.domain;

import nik.data.DataException;
import nik.data.ReservationFileRepository;
import nik.models.Host;
import nik.models.Reservation;

import java.util.List;

public class ReservationService {
    private final ReservationFileRepository reservationRepository;
    public ReservationService(ReservationFileRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }
    public List<Reservation> findByHostId(String Id){
        return reservationRepository.findByHostId(Id);
     }
     public List<Reservation> getFutureReservations(Host h){
        return  reservationRepository.getFutureReservations(h);
     }
     public Result<Reservation> add(Reservation r) throws DataException {
        Result<Reservation> result = validate(r);
        result.setPayload(reservationRepository.add(r));
        return  result;
     }
     public Result<Reservation> validate(Reservation r){
         Result<Reservation> result = new Result<>();
        if(r == null){
            result.addErrorMessage("Reservation is null");
            return  result;
        }else{
            return result; //TODO more validation
        }
     }
}
