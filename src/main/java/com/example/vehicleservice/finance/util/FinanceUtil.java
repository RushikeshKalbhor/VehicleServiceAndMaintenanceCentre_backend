package com.example.vehicleservice.finance.util;

import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.finance.json.AddBillItemJson;
import com.example.vehicleservice.finance.json.AddBillJson;
import com.example.vehicleservice.finance.model.Bill;
import com.example.vehicleservice.finance.model.BillItem;
import com.example.vehicleservice.finance.repository.BillItemRepository;
import com.example.vehicleservice.finance.repository.BillRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class FinanceUtil {

    private final BillItemRepository billItemRepository;
    
    private final BillRepository billRepository;
    
    public FinanceUtil(BillItemRepository billItemRepository, BillRepository billRepository) {
        this.billItemRepository = billItemRepository;
        this.billRepository = billRepository;
    }
    
    
    public void addBillAndBillItem(AddBillJson addBillJson) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Bill bill = new Bill();
        bill.setbAptId(addBillJson.getBAptId());
        bill.setbJcId(addBillJson.getBJcId());
        bill.setbTotal(addBillJson.getBTotal());
        bill.setbDiscount(addBillJson.getBDiscount());
        bill.setbFinalTotal(addBillJson.getBFinalTotal());
        bill.setbStatus("pending");
        bill.setbRecordStatus("approved");
        bill.setbCreated(LocalDateTime.now());
        bill.setbCreatedBy(userDetails.getUsername());
        bill = billRepository.save(bill);
        addBillItem(addBillJson.getAddBillItemJson(), bill.getbId(), userDetails.getUsername());
    }

    public void addBillItem(List<AddBillItemJson> addBillItemJsonList, Integer billId, String username) {
        List<BillItem> billItemList = new ArrayList<>();
        for(AddBillItemJson addBillItemJson : addBillItemJsonList) {
            BillItem billItem = new BillItem();
            billItem.setBiBId(billId);
            billItem.setBiServiceName(addBillItemJson.getBiServiceName());
            billItem.setBiQuantity(addBillItemJson.getBiQuantity());
            billItem.setBiRate(addBillItemJson.getBiRate());
            billItem.setBiTotal(addBillItemJson.getBiTotal());
            billItem.setBiRecordStatus("approved");
            billItem.setBiCreated(LocalDateTime.now());
            billItem.setBiCreatedBy(username);
            billItemList.add(billItem);
        }
        billItemRepository.saveAll(billItemList);
    }
}
