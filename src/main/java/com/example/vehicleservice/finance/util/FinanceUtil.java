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
import java.util.Objects;

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
        bill.setBAptId(addBillJson.getBAptId());
        bill.setBJcId(addBillJson.getBJcId());
        bill.setBTotal(addBillJson.getBTotal());
        bill.setBDiscount(addBillJson.getBDiscount());
        bill.setBFinalTotal(addBillJson.getBFinalTotal());
        bill.setBStatus("pending");
        bill.setBRecordStatus("approved");
        bill.setBCreated(LocalDateTime.now());
        bill.setBCreatedBy(userDetails.getUsername());
        bill = billRepository.save(bill);
        addBillItem(addBillJson.getAddBillItemJson(), bill.getBId(), userDetails.getUsername());
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

    public void updateBill(AddBillJson addBillJson, Bill bill) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bill.setBTotal(addBillJson.getBTotal());
        bill.setBDiscount(addBillJson.getBDiscount());
        bill.setBFinalTotal(addBillJson.getBFinalTotal());
        bill.setBUpdated(LocalDateTime.now());
        bill.setBUpdatedBy(userDetails.getUsername());
        billRepository.save(bill);
    }

    public void updateBillItem(AddBillJson addBillJson) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // delete existing bill item
        List<Integer> billItemIds = addBillJson.getAddBillItemJson().stream().map(AddBillItemJson :: getBiId).filter(Objects::nonNull).toList();
        billItemRepository.deleteAllById(billItemIds);

        // add new bill item
        addBillItem(addBillJson.getAddBillItemJson(), addBillJson.getBId(), userDetails.getUsername());

    }
}
