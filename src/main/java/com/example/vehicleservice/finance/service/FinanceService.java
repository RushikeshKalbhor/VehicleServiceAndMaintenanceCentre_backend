package com.example.vehicleservice.finance.service;

import com.example.vehicleservice.appointment.repository.AppointmentRepository;
import com.example.vehicleservice.finance.json.AddBillJson;
import com.example.vehicleservice.finance.model.Bill;
import com.example.vehicleservice.finance.model.BillItem;
import com.example.vehicleservice.finance.records.FinaceBillListRecord;
import com.example.vehicleservice.finance.repository.BillItemRepository;
import com.example.vehicleservice.finance.repository.BillRepository;
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
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;

    public FinanceService(AppointmentRepository appointmentRepository, FinanceUtil financeUtil,
                          BillRepository billRepository, BillItemRepository billItemRepository) {
        this.appointmentRepository = appointmentRepository;
        this.financeUtil = financeUtil;
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
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

    public ResponseJson addBillExtraDetails(Integer bId) {
        Bill bill = billRepository.findBillByBId(bId);
        if (bill == null) {
            return new ResponseJson("finance.bill.details.not.found");
        }

        List<BillItem> billItemList = billItemRepository.findBillItemByBiBId(bId);
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put("bill", bill);
        entityMap.put("billItemList", billItemList);

        return new ResponseJson("finance.bill.details.found", entityMap);
    }
}
