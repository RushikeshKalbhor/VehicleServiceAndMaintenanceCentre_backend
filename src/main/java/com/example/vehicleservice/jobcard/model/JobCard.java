package com.example.vehicleservice.jobcard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_cards")
public class JobCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jc_id", precision = 8, nullable = false)
    private Integer jcId;

    @Column(name = "jc_apt_id", nullable = false)
    private Integer jcAptId;

    @Column(name = "jc_status", nullable = false)
    private String jcStatus = "ASSIGNED";

    @Column(name = "jc_progress_percentage")
    private Integer jcProgressPercentage;  // 0–100

    @Column(name = "jc_inspection_notes")
    private String jcInspectionNotes;

    @Column(name = "jc_work_done")
    private String jcWorkDone;

    @Column(name = "jc_remarks")
    private String jcRemarks;

    @Column(name = "jc_created", nullable = false)
    private LocalDateTime jcCreated;

    @Column(name = "jc_created_by", nullable = false)
    private String jcCreatedBy;

    @Column(name = "jc_updated")
    private LocalDateTime jcUpdated;

    @Column(name = "jc_updated_by")
    private String jcUpdatedBy;

    @Column(name = "jc_record_status", nullable = false)
    private String jcRecordStatus;

    public Integer getJcId() {
        return jcId;
    }

    public void setJcId(Integer jcId) {
        this.jcId = jcId;
    }

    public Integer getJcAptId() {
        return jcAptId;
    }

    public void setJcAptId(Integer jcAptId) {
        this.jcAptId = jcAptId;
    }

    public String getJcStatus() {
        return jcStatus;
    }

    public void setJcStatus(String jcStatus) {
        this.jcStatus = jcStatus;
    }

    public Integer getJcProgressPercentage() {
        return jcProgressPercentage;
    }

    public void setJcProgressPercentage(Integer jcProgressPercentage) {
        this.jcProgressPercentage = jcProgressPercentage;
    }

    public String getJcInspectionNotes() {
        return jcInspectionNotes;
    }

    public void setJcInspectionNotes(String jcInspectionNotes) {
        this.jcInspectionNotes = jcInspectionNotes;
    }

    public String getJcWorkDone() {
        return jcWorkDone;
    }

    public void setJcWorkDone(String jcWorkDone) {
        this.jcWorkDone = jcWorkDone;
    }

    public String getJcRemarks() {
        return jcRemarks;
    }

    public void setJcRemarks(String jcRemarks) {
        this.jcRemarks = jcRemarks;
    }

    public LocalDateTime getJcCreated() {
        return jcCreated;
    }

    public void setJcCreated(LocalDateTime jcCreated) {
        this.jcCreated = jcCreated;
    }

    public String getJcCreatedBy() {
        return jcCreatedBy;
    }

    public void setJcCreatedBy(String jcCreatedBy) {
        this.jcCreatedBy = jcCreatedBy;
    }

    public LocalDateTime getJcUpdated() {
        return jcUpdated;
    }

    public void setJcUpdated(LocalDateTime jcUpdated) {
        this.jcUpdated = jcUpdated;
    }

    public String getJcUpdatedBy() {
        return jcUpdatedBy;
    }

    public void setJcUpdatedBy(String jcUpdatedBy) {
        this.jcUpdatedBy = jcUpdatedBy;
    }

    public String getJcRecordStatus() {
        return jcRecordStatus;
    }

    public void setJcRecordStatus(String jcRecordStatus) {
        this.jcRecordStatus = jcRecordStatus;
    }
}
