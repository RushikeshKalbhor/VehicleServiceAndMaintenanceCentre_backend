package com.example.vehicleservice.email.service;

import com.example.vehicleservice.admin.model.User;
import com.example.vehicleservice.admin.repository.UserRepository;
import com.example.vehicleservice.admin.repository.VehiclePreferenceRepository;
import com.example.vehicleservice.appointment.model.Appointment;
import com.example.vehicleservice.appointment.repository.AppointmentRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CommunicationAsyncService {

    private final VehiclePreferenceRepository vehiclePreferenceRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public CommunicationAsyncService(VehiclePreferenceRepository vehiclePreferenceRepository, EmailService emailService,
                                     UserRepository userRepository,  AppointmentRepository appointmentRepository) {
        this.vehiclePreferenceRepository = vehiclePreferenceRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Async("asyncExecutor")
    public void sendBookAppointmentEmail(String username, Integer aptId, String date) {
        String emailSetting = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("email_setting");
        User user = userRepository.findByUseUsername(username);
        if ("true".equals(emailSetting)) {
            JavaMailSender javaMailSender = emailService.getMailSenderFromDb();
            SimpleMailMessage message = emailService.getAppointmentConfirmationEmail(user, aptId, date);
            javaMailSender.send(message);
        }
    }

    @Async("asyncExecutor")
    public void appointmentApproveEmail(Integer aptId) {
        String emailSetting = vehiclePreferenceRepository.findVehiclePreferenceCecValueByCecName("email_setting");

        Appointment appointment = appointmentRepository.findAppointmentByAptId(aptId);

        User user = userRepository.findByUseUsername(appointment.getAptCustomer());
        if ("true".equals(emailSetting)) {
            JavaMailSender javaMailSender = emailService.getMailSenderFromDb();
            SimpleMailMessage message = emailService.getAppointmentApprovedEmail(user, aptId, appointment.getAptDate().toString());
            javaMailSender.send(message);
        }
    }
}
