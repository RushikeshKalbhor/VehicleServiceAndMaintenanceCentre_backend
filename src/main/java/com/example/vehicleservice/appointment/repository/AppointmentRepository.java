package com.example.vehicleservice.appointment.repository;

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
    FROM Appointment WHERE aptCustomer = :aptCustomer AND aptRecordStatus = 'approved'
    """)
    List<Appointment> findAppointmentByAptCustomer(String aptCustomer);

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
}
