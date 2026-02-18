package com.example.vehicleservice.appointment.service;

import com.example.vehicleservice.appointment.AppointmentRecord;
import com.example.vehicleservice.appointment.model.Appointment;
import com.example.vehicleservice.appointment.record.AdminAppointmentRecord;
import com.example.vehicleservice.appointment.repository.AppointmentRepository;
import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.general.util.DateUtils;
import com.example.vehicleservice.jobcard.repository.JobCardRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    private final DateUtils dateUtils;
    private final AppointmentRepository appointmentRepository;
    private final JobCardRepository jobCardRepository;

    public AppointmentService(DateUtils dateUtils,  AppointmentRepository appointmentRepository,
                              JobCardRepository jobCardRepository) {
        this.dateUtils = dateUtils;
        this.appointmentRepository = appointmentRepository;
        this.jobCardRepository = jobCardRepository;
    }

    // CUSTOMER
    public ResponseJson bookAppointment(Integer aptVehId, String aptDate, String aptProblemDescription) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Appointment appointment = new Appointment();
        appointment.setAptDate(dateUtils.convertStringToLocalDate(aptDate));
        appointment.setAptProblemDescription(aptProblemDescription);
        appointment.setAptStatus("pending");
        appointment.setAptCustomer(userDetails.getUsername());
        appointment.setAptVehId(aptVehId);
        appointment.setAptCreated(LocalDateTime.now());
        appointment.setAptRecordStatus("approved");
        appointmentRepository.save(appointment);
        return new  ResponseJson("appointment.book.success");
    }

    // ADMIN
    @Transactional
    public ResponseJson approveAppointment(Integer aptId) {
        int updatedAppointment = appointmentRepository.updateAptStatusByAptId(aptId, "approved");
        if (updatedAppointment == 0) {
            return new ResponseJson("appointment.approval.failed");
        }
        return new ResponseJson("appointment.approved");
    }

    // ADMIN
    @Transactional
    public ResponseJson rejectAppointment(Integer aptId) {
        int updatedAppointment = appointmentRepository.updateAptStatusByAptId(aptId, "rejected");
        if (updatedAppointment == 0) {
            return new ResponseJson("appointment.reject.failed");
        }
        return new ResponseJson("appointment.rejected");
    }

    // ADMIN
    @Transactional
    public ResponseJson assignMechanic(Integer aptId, String username) {
        int updatedAppointment = appointmentRepository.updateAptMechanicAndAptStatusByAptId(aptId, username, "assigned");
        return new ResponseJson(updatedAppointment == 0 ? "mechanic.assigned.failed" : "mechanic.assigned.success");
    }

    // CUSTOMER
    public ResponseJson myAppointments() {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AppointmentRecord> appointmentList = appointmentRepository.findAppointmentRecordByAptCustomer(userDetails.getUsername());
        if (appointmentList.isEmpty()) {
            return new  ResponseJson("customer.appointment.not.found");
        }
        return new ResponseJson("customer.appointment.found",  appointmentList);
    }

    // MECHANIC
    public ResponseJson mechanicAppointments(Integer pageNumber) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> entityMap = new HashMap<>();

        pageNumber = pageNumber == null ? 1 : pageNumber;
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);

        List<AppointmentRecord> appointmentList = appointmentRepository.findAppointmentByAptMechanic(userDetails.getUsername(), pageable);
        if (appointmentList.isEmpty()) {
            return new ResponseJson("mechanic.appointment.details.not.found");
        }
        entityMap.put("appointmentList", appointmentList);
        if (pageNumber == 1) {
            Integer appointmentCount = appointmentRepository.findAppointmentCountByAptMechanic(userDetails.getUsername());
            entityMap.put("appointmentCount", appointmentCount);
        }
        return new ResponseJson("mechanic.appointment.details.found",  entityMap);
    }

    public ResponseJson getAdminAppointmentList(Integer pageNumber) {
        Map<String, Object> entityMap = new HashMap<>();
        pageNumber = pageNumber == null ? 1 : pageNumber;
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        List<AppointmentRecord> appointmentList = appointmentRepository.findAppointment(pageable);
        if (appointmentList.isEmpty()) {
            return new  ResponseJson("appointment.list.not.found");
        }

        List<AdminAppointmentRecord> finalAppointmentList = new ArrayList<>();
        for (AppointmentRecord appointmentRecord : appointmentList) {
            Integer jcId = jobCardRepository.findJcIdByJcAptId(appointmentRecord.aptId());
            AdminAppointmentRecord  adminAppointmentRecord = new AdminAppointmentRecord(appointmentRecord.aptId(), appointmentRecord.aptStatus(), appointmentRecord.aptProblemDescription(),
                    appointmentRecord.aptMechanic(), appointmentRecord.aptVehId(), appointmentRecord.aptDate(), appointmentRecord.aptCustomer(),
                    appointmentRecord.aptCreated(), appointmentRecord.vehVehicleNumber(), appointmentRecord.custTitle(), appointmentRecord.custFirstName(),
                    appointmentRecord.custSurname(), appointmentRecord.mechanicTitle(), appointmentRecord.mechanicFirstName(), appointmentRecord.mechanicSurname(),
                    jcId);
            finalAppointmentList.add(adminAppointmentRecord);
        }

        entityMap.put("appointmentList", finalAppointmentList);
        if (pageNumber == 1) {
            Integer appointmentCount = appointmentRepository.findAppointmentCount();
            entityMap.put("appointmentCount", appointmentCount);
        }
        return new ResponseJson("appointment.list.found",  entityMap);
    }

    @Transactional
    public ResponseJson deleteAppointment(Integer aptId) {
        int deletedAppointment = appointmentRepository.deleteAppointmentByAptId(aptId);
        return new ResponseJson(deletedAppointment == 0 ? "appointment.delete.fail" : "appointment.delete.success");
    }
}
