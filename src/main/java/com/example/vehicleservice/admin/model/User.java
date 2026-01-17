package com.example.vehicleservice.admin.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "use_username", unique = true, nullable = false)
    private String useUsername;

    @Column(name = "use_title")
    private String useTitle;

    @Column(name = "use_firstname", nullable = false)
    private String useFirstName;

    @Column(name = "use_surname", nullable = false)
    private String useSurname;

    @Column(name = "use_fullname", nullable = false)
    private String useFullName;

    @Column(name = "use_active", precision = 1)
    private Byte useActive;

    @Column(name = "use_email")
    private String useEmail;

    @Column(name = "use_mobile")
    private String useMobile;

    @Column(name = "use_password", nullable = false)
    private String usePassword;

    @Column(name = "use_password_last_modified")
    private java.time.LocalDate usePasswordLastModified;

    @Column(name = "use_last_loggedin_date")
    private java.time.LocalDate useLastLoggedinDate;

    @Column(name = "use_logged_in", precision = 1)
    private Byte useLoggedIn;

    @Column(name = "use_login_attempts", length=10)
    private Integer useLoginAttempts;

    @Column(name = "use_created_by", length=40, nullable = false)
    private String useCreatedBy;

    @Column(name = "use_created", nullable = false)
    private java.time.LocalDateTime useCreated;

    public String getUseUsername() {
        return useUsername;
    }

    public void setUseUsername(String useUsername) {
        this.useUsername = useUsername;
    }

    public String getUseTitle() {
        return useTitle;
    }

    public void setUseTitle(String useTitle) {
        this.useTitle = useTitle;
    }

    public String getUseFirstName() {
        return useFirstName;
    }

    public void setUseFirstName(String useFirstName) {
        this.useFirstName = useFirstName;
    }

    public String getUseSurname() {
        return useSurname;
    }

    public void setUseSurname(String useSurname) {
        this.useSurname = useSurname;
    }

    public String getUseFullName() {
        return useFullName;
    }

    public void setUseFullName(String useFullName) {
        this.useFullName = useFullName;
    }

    public Byte getUseActive() {
        return useActive;
    }

    public void setUseActive(Byte useActive) {
        this.useActive = useActive;
    }

    public String getUseEmail() {
        return useEmail;
    }

    public void setUseEmail(String useEmail) {
        this.useEmail = useEmail;
    }

    public String getUseMobile() {
        return useMobile;
    }

    public void setUseMobile(String useMobile) {
        this.useMobile = useMobile;
    }

    public String getUsePassword() {
        return usePassword;
    }

    public void setUsePassword(String usePassword) {
        this.usePassword = usePassword;
    }

    public LocalDate getUsePasswordLastModified() {
        return usePasswordLastModified;
    }

    public void setUsePasswordLastModified(LocalDate usePasswordLastModified) {
        this.usePasswordLastModified = usePasswordLastModified;
    }

    public LocalDate getUseLastLoggedinDate() {
        return useLastLoggedinDate;
    }

    public void setUseLastLoggedinDate(LocalDate useLastLoggedinDate) {
        this.useLastLoggedinDate = useLastLoggedinDate;
    }

    public Byte getUseLoggedIn() {
        return useLoggedIn;
    }

    public void setUseLoggedIn(Byte useLoggedIn) {
        this.useLoggedIn = useLoggedIn;
    }

    public Integer getUseLoginAttempts() {
        return useLoginAttempts;
    }

    public void setUseLoginAttempts(Integer useLoginAttempts) {
        this.useLoginAttempts = useLoginAttempts;
    }

    public String getUseCreatedBy() {
        return useCreatedBy;
    }

    public void setUseCreatedBy(String useCreatedBy) {
        this.useCreatedBy = useCreatedBy;
    }

    public LocalDateTime getUseCreated() {
        return useCreated;
    }

    public void setUseCreated(LocalDateTime useCreated) {
        this.useCreated = useCreated;
    }
}
