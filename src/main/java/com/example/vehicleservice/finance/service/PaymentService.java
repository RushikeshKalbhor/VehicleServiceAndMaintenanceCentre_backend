package com.example.vehicleservice.finance.service;

import com.example.vehicleservice.finance.util.FinanceUtil;
import com.example.vehicleservice.finance.util.RazorpayConfig;
import com.example.vehicleservice.general.json.ResponseJson;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final RazorpayConfig razorpayConfig;
    private final FinanceUtil financeUtil;

    public PaymentService(RazorpayConfig razorpayConfig, FinanceUtil financeUtil) {
        this.razorpayConfig = razorpayConfig;
        this.financeUtil = financeUtil;
    }

    public ResponseJson createOrder(Double amount) throws RazorpayException {

        RazorpayClient client = new RazorpayClient(
                razorpayConfig.getKey(),
                razorpayConfig.getSecret()
        );

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // convert to paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = client.orders.create(options);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", order.get("amount"));

        return new ResponseJson("create.order.success", response);
    }

    public ResponseJson verifyPayment(Map<String, String> payload) throws NoSuchAlgorithmException, InvalidKeyException {
        String orderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");
        String payAptId = payload.get("payAptId");
        String payAmount = payload.get("payAmount");

        if (payAptId == null || payAmount == null) {
            return new ResponseJson("payAptId.and.payAmount.is.required");
        }

        String secret = razorpayConfig.getSecret();
        String generatedSignature = hmacSha256(orderId + "|" + paymentId, secret);
        if (generatedSignature.equals(signature)) {
            financeUtil.addPayment(Integer.valueOf(payAptId), Double.valueOf(payAmount), "paid", paymentId);
            financeUtil.markBillPaid(Integer.valueOf(payAptId));
            return new ResponseJson("payment.verified.success");
        }
        financeUtil.addPayment(Integer.valueOf(payAptId), Double.valueOf(payAmount), "failed", null);
        return new ResponseJson("invalid.signature");
    }

    public String hmacSha256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);

        byte[] rawHmac = mac.doFinal(data.getBytes());

        StringBuilder hex = new StringBuilder(2 * rawHmac.length);
        for (byte b : rawHmac) {
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }
}
