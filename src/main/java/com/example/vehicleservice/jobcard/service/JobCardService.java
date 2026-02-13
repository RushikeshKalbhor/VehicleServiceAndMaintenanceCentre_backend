package com.example.vehicleservice.jobcard.service;

import com.example.vehicleservice.appointment.repository.AppointmentRepository;
import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.jobcard.model.JobCard;
import com.example.vehicleservice.jobcard.repository.JobCardRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobCardService {

    private final JobCardRepository jobCardRepository;
    private final AppointmentRepository appointmentRepository;

    public JobCardService(JobCardRepository jobCardRepository, AppointmentRepository appointmentRepository) {
        this.jobCardRepository = jobCardRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ADMIN – create job card after assigning mechanic
    public ResponseJson createJobCard(Integer aptId) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String aptMechanic = appointmentRepository.findAptMechanicByAptId(aptId);
        if (aptMechanic == null || aptMechanic.isEmpty()) {
            return new ResponseJson("mechanic.not.assigned");
        }

        JobCard jobCard = new JobCard();
        jobCard.setJcAptId(aptId);
        jobCard.setJcStatus("ASSIGNED");
        jobCard.setJcProgressPercentage(10);
        jobCard.setJcCreated(LocalDateTime.now());
        jobCard.setJcCreatedBy(userDetails.getUsername());
        jobCard.setJcRecordStatus("approved");
        jobCardRepository.save(jobCard);
        return new ResponseJson("job.card.create.success");
    }

    // MECHANIC – update service progress
    public ResponseJson updateService(Integer jcId, String jcStatus, String jcInspectionNotes) {
        JobCard jobCard = jobCardRepository.findById(jcId).orElse(null);
        if (jobCard ==  null) {
            return new ResponseJson("job.card.not.found");
        }
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobCard.setJcStatus(jcStatus);
        jobCard.setJcUpdated(LocalDateTime.now());
        jobCard.setJcUpdatedBy(userDetails.getUsername());

        switch (jcStatus) {
            case "INSPECTION" -> {
                jobCard.setJcInspectionNotes(jcInspectionNotes);
                jobCard.setJcProgressPercentage(25);
            }
            case "IN_PROGRESS" -> {
                jobCard.setJcWorkDone(jcInspectionNotes);
                jobCard.setJcProgressPercentage(60);
            }
            case "QUALITY_CHECK" -> jobCard.setJcProgressPercentage(80);
            case "READY_FOR_DELIVERY" -> jobCard.setJcProgressPercentage(95);
            case "DELIVERED" -> jobCard.setJcProgressPercentage(100);
            default -> {
                break;
            }
        }

        jobCardRepository.save(jobCard);
        return new ResponseJson("job.card.update.success");
    }

    // ADMIN / CUSTOMER / MECHANIC
    public ResponseJson getJobCard(Integer aptId) {

        JobCard jobCard = jobCardRepository.findJobCardByJcAptId(aptId);
        if (jobCard == null) {
            return new ResponseJson("job.card.not.found");
        }
        return new ResponseJson("job.card.found", jobCard);
    }
}
