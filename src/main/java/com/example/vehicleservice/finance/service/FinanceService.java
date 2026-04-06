package com.example.vehicleservice.finance.service;

import com.example.vehicleservice.appointment.repository.AppointmentRepository;
import com.example.vehicleservice.finance.json.AddBillJson;
import com.example.vehicleservice.finance.records.FinaceBillListRecord;
import com.example.vehicleservice.finance.util.FinanceUtil;
import com.example.vehicleservice.general.json.ResponseJson;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceService {

    private final AppointmentRepository appointmentRepository;
    private final FinanceUtil financeUtil;

    public FinanceService(AppointmentRepository appointmentRepository, FinanceUtil financeUtil) {
        this.appointmentRepository = appointmentRepository;
        this.financeUtil = financeUtil;
    }

    public ResponseJson getFinanceBillList(String vehicleNumber, Integer pageNumber) {

        pageNumber = pageNumber == null ? 1 : pageNumber;
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        List<FinaceBillListRecord> financeBillListRecordList;
        if (vehicleNumber != null &&  !vehicleNumber.isEmpty()) {
            financeBillListRecordList = appointmentRepository.findFinanceBillListRecordByAptStatus(pageable, vehicleNumber);
        } else {
            financeBillListRecordList = appointmentRepository.findFinanceBillListRecordByAptStatus(pageable);
        }

        if (financeBillListRecordList.isEmpty()) {
            return new ResponseJson("finance.bill.list.not.found");
        }
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put("financeBillListRecordList", financeBillListRecordList);
        if (pageNumber == 1) {
            Integer financeBillListCount = 0;
            if (vehicleNumber != null &&  !vehicleNumber.isEmpty()) {
                financeBillListCount = appointmentRepository.findFinanceBillListRecordCountByAptStatusAndVehicleNumber(vehicleNumber);
            } else {
                financeBillListCount = appointmentRepository.findFinanceBillListRecordCountByAptStatus();
            }
            entityMap.put("financeBillListCount", financeBillListCount);
        }
        return new ResponseJson("finance.bill.list.found", entityMap);
    }

    public ResponseJson addBill(AddBillJson addBillJson) {
        financeUtil.addBillAndBillItem(addBillJson);
        return new ResponseJson("finance.bill.add.success");
    }
}
