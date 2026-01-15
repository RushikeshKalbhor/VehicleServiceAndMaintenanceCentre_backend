package com.example.vehicleservice.admin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {

    @Column(name = "usr_id", precision = 8, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usrId;

    @Column(name = "usr_use_username", length=40, nullable = false)
    private String usrUseUsername;

    @Column(name = "usr_usg_id", precision = 8, nullable = false)
    private Integer usrUsgId;

    @Column(name = "usr_name", length=20)
    private String usrName;

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public String getUsrUseUsername() {
        return usrUseUsername;
    }

    public void setUsrUseUsername(String usrUseUsername) {
        this.usrUseUsername = usrUseUsername;
    }

    public Integer getUsrUsgId() {
        return usrUsgId;
    }

    public void setUsrUsgId(Integer usrUsgId) {
        this.usrUsgId = usrUsgId;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }
}
