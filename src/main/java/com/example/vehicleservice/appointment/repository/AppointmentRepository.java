package com.example.vehicleservice.appointment.repository;

import com.example.vehicleservice.appointment.AppointmentRecord;
import com.example.vehicleservice.appointment.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptCustomer = :aptCustomer AND a.aptRecordStatus = 'approved'
    """)
    List<AppointmentRecord> findAppointmentRecordByAptCustomer(String aptCustomer);

    @Modifying
    @Query("UPDATE Appointment SET aptStatus = :aptStatus WHERE aptId = :aptId AND aptRecordStatus = 'approved'")
    int updateAptStatusByAptId(Integer aptId, String aptStatus);

    @Modifying
    @Query("UPDATE Appointment SET aptStatus = :aptStatus, aptMechanic = :aptMechanic WHERE aptId = :aptId AND aptRecordStatus = 'approved' AND aptStatus != 'REJECTED'")
    int updateAptMechanicAndAptStatusByAptId(Integer aptId, String aptMechanic, String aptStatus);

    @Query("""
    FROM Appointment WHERE aptMechanic = :aptMechanic AND aptRecordStatus = 'approved'
    """)
    List<Appointment> findAppointmentByAptMechanic(String aptMechanic);

    @Query("FROM Appointment WHERE aptRecordStatus = 'approved'")
    List<Appointment> findAppointment(Pageable pageable);

    @Query("SELECT COUNT(aptId) FROM Appointment WHERE aptRecordStatus = 'approved'")
    Integer findAppointmentCount();

    @Modifying
    @Query("UPDATE Appointment SET aptRecordStatus = 'wrong' WHERE aptId = :aptId AND aptRecordStatus = 'approved'")
    int deleteAppointmentByAptId(Integer aptId);

    @Query("SELECT aptMechanic FROM Appointment WHERE aptId = :aptId AND aptRecordStatus = 'approved'")
    String findAptMechanicByAptId(Integer aptId);
}
