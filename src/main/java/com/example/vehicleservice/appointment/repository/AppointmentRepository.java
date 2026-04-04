package com.example.vehicleservice.appointment.repository;

import com.example.vehicleservice.appointment.AppointmentRecord;
import com.example.vehicleservice.appointment.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname, jc.jcId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN JobCard jc ON jc.jcAptId = a.aptId AND jc.jcRecordStatus = 'approved'
    LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptCustomer = :aptCustomer AND a.aptRecordStatus = 'approved' ORDER BY a.aptId DESC
    """)
    List<AppointmentRecord> findAppointmentRecordByAptCustomer(String aptCustomer);

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname, jc.jcId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN JobCard jc ON jc.jcAptId = a.aptId AND jc.jcRecordStatus = 'approved'
    LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptCustomer = :aptCustomer AND a.aptDate = :aptDate AND a.aptRecordStatus = 'approved' ORDER BY a.aptId DESC
    """)
    List<AppointmentRecord> findAppointmentRecordByAptCustomerAndAptDate(LocalDate aptDate, String aptCustomer);

    @Modifying
    @Query("UPDATE Appointment SET aptStatus = :aptStatus WHERE aptId = :aptId AND aptRecordStatus = 'approved'")
    int updateAptStatusByAptId(Integer aptId, String aptStatus);

    @Modifying
    @Query("UPDATE Appointment SET aptStatus = :aptStatus, aptMechanic = :aptMechanic WHERE aptId = :aptId AND aptRecordStatus = 'approved' AND aptStatus != 'REJECTED'")
    int updateAptMechanicAndAptStatusByAptId(Integer aptId, String aptMechanic, String aptStatus);

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname, jc.jcId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN JobCard jc ON jc.jcAptId = a.aptId AND jc.jcRecordStatus = 'approved'
    LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptMechanic = :aptMechanic AND a.aptDate = :aptDate AND a.aptRecordStatus = 'approved' ORDER BY a.aptId DESC
    """)
    List<AppointmentRecord> findAppointmentByAptMechanicAndAptDate(LocalDate aptDate, String aptMechanic, Pageable pageable);

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname, jc.jcId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN JobCard jc ON jc.jcAptId = a.aptId AND jc.jcRecordStatus = 'approved'
    LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptMechanic = :aptMechanic AND a.aptRecordStatus = 'approved' ORDER BY a.aptId DESC
    """)
    List<AppointmentRecord> findAppointmentByAptMechanic(String aptMechanic, Pageable pageable);

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname, jc.jcId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN JobCard jc ON jc.jcAptId = a.aptId AND jc.jcRecordStatus = 'approved'
    LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptMechanic = :aptMechanic AND a.aptRecordStatus = 'approved'
    AND v.vehVehicleNumber like %:vehVehicleNumber% ORDER BY a.aptId DESC
    """)
    List<AppointmentRecord> findAppointmentByAptMechanicAndVehicleNumber(String vehVehicleNumber, String aptMechanic, Pageable pageable);

    @Query("""
    SELECT COUNT(a.aptId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptMechanic = :aptMechanic AND a.aptRecordStatus = 'approved'
    """)
    Integer findAppointmentCountByAptMechanic(String aptMechanic);

    @Query("""
    SELECT COUNT(a.aptId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptMechanic = :aptMechanic AND a.aptRecordStatus = 'approved'
    AND v.vehVehicleNumber like %:vehVehicleNumber%
    """)
    Integer findAppointmentCountByAptMechanicAndVehVehicleNumber(String vehVehicleNumber, String aptMechanic);

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname, jc.jcId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN User m ON m.useUsername = a.aptMechanic
    LEFT JOIN JobCard jc ON jc.jcAptId = a.aptId AND jc.jcRecordStatus = 'approved'
    WHERE a.aptRecordStatus = 'approved' ORDER BY a.aptId DESC
    """)
    List<AppointmentRecord> findAppointment(Pageable pageable);

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname, jc.jcId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN User m ON m.useUsername = a.aptMechanic
    LEFT JOIN JobCard jc ON jc.jcAptId = a.aptId AND jc.jcRecordStatus = 'approved'
    WHERE a.aptRecordStatus = 'approved' AND a.aptDate = :aptDate ORDER BY a.aptId DESC
    """)
    List<AppointmentRecord> findAppointmentByAptDate(LocalDate aptDate, Pageable pageable);

    @Query("""
    SELECT new com.example.vehicleservice.appointment.AppointmentRecord(a.aptId, a.aptStatus, a.aptProblemDescription, a.aptMechanic, a.aptVehId,
    a.aptDate, a.aptCustomer, a.aptCreated, v.vehVehicleNumber, u.useTitle AS custTitle, u.useFirstName AS custFirstName, u.useSurname AS custSurname,
    m.useTitle AS mechanicTitle, m.useFirstName AS mechanicFirstName, m.useSurname AS mechanicSurname, jc.jcId)
    FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
    JOIN User u ON u.useUsername = a.aptCustomer
    LEFT JOIN User m ON m.useUsername = a.aptMechanic
    LEFT JOIN JobCard jc ON jc.jcAptId = a.aptId AND jc.jcRecordStatus = 'approved'
    WHERE a.aptRecordStatus = 'approved' AND v.vehVehicleNumber like %:vehVehicleNumber% ORDER BY a.aptId DESC
    """)
    List<AppointmentRecord> findAppointmentByVehicleNumber(String vehVehicleNumber, Pageable pageable);

    @Query("""
        SELECT COUNT(a.aptId)
        FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
        JOIN User u ON u.useUsername = a.aptCustomer
        LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptRecordStatus = 'approved'
        """)
    Integer findAppointmentCount();

    @Query("""
        SELECT COUNT(a.aptId)
        FROM Appointment a JOIN Vehicle v ON v.vehId = a.aptVehId
        JOIN User u ON u.useUsername = a.aptCustomer
        LEFT JOIN User m ON m.useUsername = a.aptMechanic WHERE a.aptRecordStatus = 'approved' AND v.vehVehicleNumber like %:vehVehicleNumber%
        """)
    Integer findAppointmentVehicleCount(String vehVehicleNumber);

    @Modifying
    @Query("UPDATE Appointment SET aptRecordStatus = 'wrong' WHERE aptId = :aptId AND aptRecordStatus = 'approved'")
    int deleteAppointmentByAptId(Integer aptId);

    @Query("SELECT aptMechanic FROM Appointment WHERE aptId = :aptId AND aptRecordStatus = 'approved'")
    String findAptMechanicByAptId(Integer aptId);

    @Query("FROM Appointment WHERE aptId = :aptId AND aptRecordStatus = 'approved'")
    Appointment findAppointmentByAptId(Integer aptId);
}
