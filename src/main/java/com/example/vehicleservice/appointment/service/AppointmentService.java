package com.example.vehicleservice.appointment.service;

import com.example.vehicleservice.appointment.model.Appointment;
import com.example.vehicleservice.appointment.repository.AppointmentRepository;
import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.general.util.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    private final DateUtils dateUtils;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(DateUtils dateUtils,  AppointmentRepository appointmentRepository) {
        this.dateUtils = dateUtils;
        this.appointmentRepository = appointmentRepository;
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
        List<Appointment> appointmentList = appointmentRepository.findAppointmentByAptCustomer(userDetails.getUsername());
        if (appointmentList.isEmpty()) {
            return new  ResponseJson("customer.appointment.not.found");
        }
        return new ResponseJson("customer.appointment.found",  appointmentList);
    }

    // MECHANIC
    public ResponseJson mechanicAppointments() {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Appointment> appointmentList = appointmentRepository.findAppointmentByAptMechanic(userDetails.getUsername());
        if (appointmentList.isEmpty()) {
            return new ResponseJson("mechanic.appointment.details.not.found");
        }
        return new ResponseJson("mechanic.appointment.details.found",  appointmentList);
    }

    public ResponseJson getAdminAppointmentList(Integer pageNumber) {
        Map<String, Object> entityMap = new HashMap<>();
        pageNumber = pageNumber == null ? 1 : pageNumber;
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        List<Appointment> appointmentList = appointmentRepository.findAppointment(pageable);
        if (appointmentList.isEmpty()) {
            return new  ResponseJson("appointment.list.not.found");
        }
        entityMap.put("appointmentList", appointmentList);
        if (pageNumber == 1) {
            Integer appointmentCount = appointmentRepository.findAppointmentCount();
            entityMap.put("appointmentCount", appointmentCount);
        }
        return new ResponseJson("appointment.list.found",  entityMap);
    }
}
