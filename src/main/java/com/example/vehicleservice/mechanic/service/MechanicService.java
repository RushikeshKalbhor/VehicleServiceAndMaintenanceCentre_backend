package com.example.vehicleservice.mechanic.service;

import com.example.vehicleservice.admin.repository.UserRepository;
import com.example.vehicleservice.general.json.ResponseJson;
import com.example.vehicleservice.mechanic.records.MechanicRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MechanicService {

    private final UserRepository userRepository;

    public MechanicService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseJson getMechanicList() {
        List<MechanicRecord> mechanicRecordList = userRepository.findMechanicRecordUseType("mechanic", Byte.valueOf("1"));
        if (mechanicRecordList.isEmpty()) {
            return new ResponseJson("mechanic.list.not.found");
        }
        return new ResponseJson("mechanic.list.found",  mechanicRecordList);
    }
}
