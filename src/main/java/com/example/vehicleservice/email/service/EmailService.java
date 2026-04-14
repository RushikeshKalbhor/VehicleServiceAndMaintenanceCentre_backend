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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Properties;

@Service
public class EmailService {

    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();
    private final UserOtpRepository userOtpRepository;
    private final VehiclePreferenceRepository  vehiclePreferenceRepository;
    private final PasswordEncoder passwordEncoder;

    public EmailService(UserRepository userRepository, UserOtpRepository userOtpRepository,
                        VehiclePreferenceRepository vehiclePreferenceRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userOtpRepository = userOtpRepository;
        this.vehiclePreferenceRepository = vehiclePreferenceRepository;
        this.passwordEncoder = passwordEncoder;
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

        String emailSetting = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("email_setting");

        if ("true".equals(emailSetting)) {
            JavaMailSender javaMailSender = getMailSenderFromDb();
            SimpleMailMessage message = getEmailContent(user, otp);
            javaMailSender.send(message);
        }
        return new ResponseJson("otp.sent.on.registerd.email");
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

    public JavaMailSender getMailSenderFromDb() {
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

    public ResponseJson otpVerify(String username, String otp) {
        UserOtp userOtp = userOtpRepository.findUserOtpByUoUseUsername(username);
        if (userOtp == null) {
            return new ResponseJson("user.not.found");
        }

        if (userOtp.getUoExpiryTime().isBefore(LocalDateTime.now()) || !userOtp.getUoOtp().equals(otp)) {
            return new ResponseJson("otp.not.matched");
        }

        return new ResponseJson("otp.matched");
    }

    @Transactional
    public ResponseJson confirmPassword(String username, String password) {
        String usePassword = passwordEncoder.encode(password);
        int updateCount = userRepository.updateUsePasswordByUseUsername(username, usePassword);
        return new ResponseJson(updateCount > 0 ? "password.updated" : "password.update.fail");
    }

    public SimpleMailMessage getAppointmentConfirmationEmail(User user, Integer appointmentId, String date) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getUseEmail());
        message.setSubject("Appointment Booked Successfully");

        message.setText(
                "Dear " + user.getUseFullName() + ",\n\n" +

                        "Your appointment has been successfully booked with the Vehicle Service and Maintenance Centre.\n\n" +

                        "Appointment Details:\n" +
                        "Appointment ID : " + appointmentId + "\n" +
                        "Date           : " + date + "\n" +

                        "Please arrive 10 minutes before your scheduled time.\n\n" +

                        "If you need to reschedule or cancel your appointment, please contact us.\n\n" +

                        "Thank you for choosing our service.\n\n" +

                        "Regards,\n" +
                        "Vehicle Service and Maintenance Centre Team"
        );
        return message;
    }

    public SimpleMailMessage getAppointmentApprovedEmail(User user,
                                                         Integer appointmentId,
                                                         String date) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getUseEmail());
        message.setSubject("Appointment Approved");

        message.setText(
                "Dear " + user.getUseFullName() + ",\n\n" +

                        "We are pleased to inform you that your appointment has been approved.\n\n" +

                        "Appointment Details:\n" +
                        "Appointment ID : " + appointmentId + "\n" +
                        "Date           : " + date + "\n" +

                        "Please ensure that you arrive 10 minutes before the scheduled time.\n\n" +

                        "If you have any questions or need to make changes, feel free to contact us.\n\n" +

                        "We look forward to serving you.\n\n" +

                        "Regards,\n" +
                        "Vehicle Service and Maintenance Centre Team"
        );

        return message;
    }

    public SimpleMailMessage getAppointmentDeliveredEmail(User user, Integer appointmentId, String date) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getUseEmail());
        message.setSubject("Vehicle Delivered Successfully");

        message.setText(
                "Dear " + user.getUseFullName() + ",\n\n" +

                        "We are pleased to inform you that your vehicle has been delivered.\n\n" +

                        "Appointment Details:\n" +
                        "Appointment ID : " + appointmentId + "\n" +
                        "Date           : " + date + "\n" +


                        "If you have any questions or need to make changes, feel free to contact us.\n\n" +

                        "We look forward to serving you.\n\n" +

                        "Regards,\n" +
                        "Vehicle Service and Maintenance Centre Team"
        );

        return message;
    }
}
