package com.example.vehicleservice.finance.util;

import com.example.vehicleservice.config.security.UserDetail;
import com.example.vehicleservice.finance.json.AddBillItemJson;
import com.example.vehicleservice.finance.json.AddBillJson;
import com.example.vehicleservice.finance.model.Bill;
import com.example.vehicleservice.finance.model.BillItem;
import com.example.vehicleservice.finance.model.Payment;
import com.example.vehicleservice.finance.repository.BillItemRepository;
import com.example.vehicleservice.finance.repository.BillRepository;
import com.example.vehicleservice.finance.repository.PaymentRepository;
import com.example.vehicleservice.jobcard.repository.JobCardRepository;
import com.example.vehicleservice.jobcard.service.JobCardService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FinanceUtil {

    private final BillItemRepository billItemRepository;
    
    private final BillRepository billRepository;

    private final PaymentRepository paymentRepository;

    private final JobCardService jobCardService;

    private final JobCardRepository jobCardRepository;
    
    public FinanceUtil(BillItemRepository billItemRepository, BillRepository billRepository,
                       PaymentRepository paymentRepository, JobCardService jobCardService,
                       JobCardRepository jobCardRepository) {
        this.billItemRepository = billItemRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.jobCardService = jobCardService;
        this.jobCardRepository = jobCardRepository;
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

    @Transactional
    public void updateBillItem(AddBillJson addBillJson) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // delete existing bill item
//        List<Integer> billItemIds = addBillJson.getAddBillItemJson().stream().map(AddBillItemJson :: getBiId).filter(Objects::nonNull).toList();
        billItemRepository.deleteAllByBiBId(addBillJson.getBId());

        // add new bill item
        addBillItem(addBillJson.getAddBillItemJson(), addBillJson.getBId(), userDetails.getUsername());
    }

    public void addPayment(Integer payAptId, Double payAmount, String payStatus, String payTransactionId) {
        UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Payment payment = new Payment();
        payment.setPayAptId(payAptId);
        payment.setPayAmount(payAmount);
        payment.setPayStatus(payStatus);
        payment.setPayTransactionId(payTransactionId);
        payment.setPayDate(LocalDateTime.now());
        payment.setPayRecordStatus("approved");
        payment.setPayCreated(LocalDateTime.now());
        payment.setPayCreatedBy(userDetails.getUsername());
        paymentRepository.save(payment);
    }

    public void markBillPaid(Integer aptId) {
        Double payAmountSum = paymentRepository.findSumPayAmountByPayAptId(aptId);
        Double bFinalTotal = billRepository.findBFinalTotalByBAptId(aptId);
        if (payAmountSum != null && bFinalTotal != null && payAmountSum >= bFinalTotal) {
            Integer jcId = jobCardRepository.findJcIdByJcAptId(aptId);
            jobCardService.updateService(jcId, "DELIVERED", null);
        }
    }
}
