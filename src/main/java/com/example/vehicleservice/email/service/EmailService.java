package com.example.vehicleservice.email.service;

import com.example.vehicleservice.admin.model.User;
import com.example.vehicleservice.admin.repository.UserRepository;
import com.example.vehicleservice.admin.repository.VehiclePreferenceRepository;
import com.example.vehicleservice.email.model.UserOtp;
import com.example.vehicleservice.email.repository.UserOtpRepository;
import com.example.vehicleservice.general.json.ResponseJson;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Properties;

@Service
public class EmailService {

    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();
    private final UserOtpRepository userOtpRepository;
    private final VehiclePreferenceRepository  vehiclePreferenceRepository;

    public EmailService(UserRepository userRepository, UserOtpRepository userOtpRepository,  VehiclePreferenceRepository vehiclePreferenceRepository) {
        this.userRepository = userRepository;
        this.userOtpRepository = userOtpRepository;
        this.vehiclePreferenceRepository = vehiclePreferenceRepository;
    }

    public ResponseJson otpSend(String username) {
        User user = userRepository.findByUseUsername(username);
        if (user == null) {
            return new ResponseJson("user.not.found");
        }
        String otp = String.valueOf(100000 + this.random.nextInt(899999));

        UserOtp userOtp = new UserOtp();
        userOtp.setUoUseUsername(username);
        userOtp.setUoOtp(otp);
        userOtp.setUoExpiryTime(LocalDateTime.now().plusMinutes(10));
        userOtp.setUoCreated(LocalDateTime.now());
        userOtpRepository.save(userOtp);

        JavaMailSender javaMailSender = getMailSenderFromDb();

        SimpleMailMessage message = getEmailContent(user, otp);
        javaMailSender.send(message);
        return new ResponseJson("otp.sent");
    }

    public SimpleMailMessage getEmailContent(User user, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUseEmail());
        message.setSubject("Password Reset Request");
        message.setText(
                "Dear "+ user.getUseFullName()+",\n\n" +
                        "We received a request to reset your password for the Vehicle Service and Maintenance Centre.\n\n" +
                        "Your One-Time Password (OTP) is: " + otp + "\n\n" +
                        "This OTP is valid for the next 10 minutes. Please do not share this OTP with anyone for security reasons.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Regards,\n" +
                        "Vehicle Service and Maintenance Centre Team"
        );
        return message;
    }

    private JavaMailSender getMailSenderFromDb() {
        String emailUsername = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("email_username");
        String emailAppPassword = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("email_app_password");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailAppPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

}
