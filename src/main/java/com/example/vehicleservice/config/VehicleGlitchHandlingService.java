package com.example.vehicleservice.config;

import com.example.vehicleservice.admin.model.VehicleGlitch;
import com.example.vehicleservice.admin.repository.VehicleGlitchRepository;
import com.example.vehicleservice.config.security.UserDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class VehicleGlitchHandlingService {

    private static final Logger logger = LogManager.getLogger(VehicleGlitchHandlingService.class);

    private final VehicleGlitchRepository vehicleGlitchRepository;

    public VehicleGlitchHandlingService(VehicleGlitchRepository vehicleGlitchRepository) {
        this.vehicleGlitchRepository = vehicleGlitchRepository;
    }

    public Integer handleExceptions(Exception exception) {
        UserDetail userDetails = null;
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        if (principal instanceof UserDetail) {
            userDetails = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        Optional<VehicleGlitch> vehicleGlitch = saveVehicleGlitch(exception, userDetails);

        return vehicleGlitch.map(VehicleGlitch::getVegId).orElse(null);
    }

    Optional<VehicleGlitch> saveVehicleGlitch(Exception exception, UserDetail userDetails){

        String cause = exception.toString();
        String fileName = "";
        int lineNumber = 0;

        StringBuilder stackTraceString = new StringBuilder();
        StackTraceElement[] stackTrace = exception.getStackTrace();

        for (StackTraceElement element : stackTrace) {
            try {
                Class<?> exceptionClass = Class.forName(element.getClassName());
                stackTraceString.append(element).append("\n");

                if(exceptionClass.toString().contains("example") && !exceptionClass.toString().contains("filter")){
                    fileName = exceptionClass.toString();
                    lineNumber = element.getLineNumber();
                    if(lineNumber < 0) {
                        lineNumber = 0;
                    }
                    break;
                }

            } catch (ClassNotFoundException e) {
                logger.error("An error occurred", e);
            }
        }


        logger.error("Exception", exception);

        VehicleGlitch veg = new VehicleGlitch();

        //TO DO set estId, cliId, username and navigation area from JWT Token

        if(userDetails != null) {
            veg.setVegUseUsername(userDetails.getUsername());
        }

        veg.setVegFileName(fileName);
        veg.setVegLineNumber(lineNumber);
        veg.setVegCause(cause);
        veg.setVegStackTrace(stackTraceString.toString());
        veg.setVegCreatedDate(LocalDate.now());
        veg.setVegCreated(LocalDateTime.now());
        veg.setVegResolved(Byte.valueOf("0"));

        veg = vehicleGlitchRepository.save(veg);
        return Optional.of(veg);
    }
}
