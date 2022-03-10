package nik.domain;

import nik.data.DataException;
import nik.data.ReservationFileRepository;
import nik.models.Guest;
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

    public Result<Reservation> createReservation(Host h, Reservation r) throws DataException {
        Result<Reservation> result = validate(h, r);
        result.setPayload(reservationRepository.createReservation(h, r));

        return result;
    }
     public Result<Reservation> validate(Host h, Reservation r){
         Result<Reservation> result = new Result<>();

         List<Reservation> reservationsWithId = reservationRepository.findByHostId(h.getiD());
         for (Reservation res : reservationsWithId){
             if(r.getStartDate().isAfter(res.getStartDate())&& res.getEndDate().isBefore(r.getEndDate())){
                 result.addErrorMessage("Reservation starts before and is during a reservation");
                 return result;
             }
             if(r.getEndDate().isBefore(res.getEndDate())&& res.getEndDate().isAfter(r.getEndDate())){
                 result.addErrorMessage("Reservation ends before and is during a reservation");
             }

         }

        if(r == null){
            result.addErrorMessage("Reservation is null");
            return  result;
        }else{
            return result; //TODO more validation
        }
     }


}
