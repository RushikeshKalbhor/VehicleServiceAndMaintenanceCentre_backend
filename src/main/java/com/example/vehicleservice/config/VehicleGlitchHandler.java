package com.example.vehicleservice.config;

import com.example.vehicleservice.general.json.ExceptionJson;
import com.example.vehicleservice.general.json.ValidationExceptionJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class VehicleGlitchHandler {

    private static final Logger logger = LogManager.getLogger(VehicleGlitchHandler.class);

    private final VehicleGlitchHandlingService vehicleGlitchHandlingService;

    public VehicleGlitchHandler(VehicleGlitchHandlingService vehicleGlitchHandlingService) {
        this.vehicleGlitchHandlingService = vehicleGlitchHandlingService;
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ExceptionJson> handleException(Exception exception) {
        logger.error("An error occurred", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        Map<String, String> errorMap = new HashMap<>();
        if (exception instanceof AccessDeniedException) {
            errorMap.put("validationCode", "insufficient.privileges");
            ValidationExceptionJson validationExceptionJson = new ValidationExceptionJson();
            validationExceptionJson.setParameters(errorMap);
            exceptionJson.setValidationException(validationExceptionJson);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(exceptionJson);
        } else if (!(exception instanceof UsernameNotFoundException) && !(exception instanceof BadCredentialsException) && exception.getStackTrace().length > 0) {
            Integer vegId = vehicleGlitchHandlingService.handleExceptions(exception);
            exceptionJson.setCegId(vegId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionJson);
        }
        System.out.println(exception.getMessage());
        JSONObject getMessage = new JSONObject(exception.getMessage());
        JSONObject validationCode = getMessage.getJSONObject("validationCode");
        String messageKey = validationCode.getString("messageKey");

        if (validationCode.has("parameters")) {
            int parameters = validationCode.getInt("parameters");
            errorMap.put("parameters", String.valueOf(parameters));
        }

        errorMap.put("messageKey", messageKey);

        ValidationExceptionJson validationExceptionJson = new ValidationExceptionJson();
        validationExceptionJson.setParameters(errorMap);
        exceptionJson.setValidationException(validationExceptionJson);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionJson);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionJson> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
        logger.error("An error occurred", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException(
                "Invalid value for parameter '" + exception.getName() +
                        "'. It should be of type " +
                        (exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionJson);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionJson> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        logger.error("An error occurred", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException("Required parameter '" + exception.getParameterName() + "' is missing.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionJson);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionJson handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        logger.error("An error occurred", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException("Invalid JSON format");
        return exceptionJson;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ExceptionJson handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException exception) {
        logger.error("An error occurred", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException("Invalid schema structure, " + exception.getMostSpecificCause().getMessage());
        return exceptionJson;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ExceptionJson> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        logger.error("An error occurred", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        int startIndex = exception.getMessage().indexOf("'") + 1;
        int endIndex = exception.getMessage().lastIndexOf("'");
        exceptionJson.setValidationException("Required parameter '" + exception.getMessage().substring(startIndex, endIndex) + "' is missing.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionJson);
    }

    @ExceptionHandler({
            IncorrectResultSizeDataAccessException.class,
            org.hibernate.NonUniqueResultException.class,
            jakarta.persistence.NonUniqueResultException.class
    })
    public ResponseEntity<ExceptionJson> handleNonUnique(Exception ex) {
        logger.error("An error occurred", ex);
        ExceptionJson body = new ExceptionJson();

        // Prepare ValidationExceptionJson
        ValidationExceptionJson validationMessage = new ValidationExceptionJson();
        validationMessage.setMessageKey("multiple.records");

        // Fill parameters map
        Map<String, String> params = new HashMap<>();
        params.put("reason", "Multiple records found when a single record was expected");

        // find the most relevant stacktrace element (your package)
        StackTraceElement location = Arrays.stream(ex.getStackTrace())
                .filter(st -> st.getClassName().startsWith("com.riomed"))
                .findFirst()
                .orElse(ex.getStackTrace()[0]);

        params.put("fileName", location.getFileName());
        params.put("lineNumber", String.valueOf(location.getLineNumber()));

        validationMessage.setParameters(params);

        // attach to ExceptionJson
        body.getValidationException().add(validationMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ExceptionJson handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        logger.error("An error occurred", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException("Request method not supported");
        return exceptionJson;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            SQLNonTransientConnectionException.class,
            DataAccessResourceFailureException.class,
            CannotGetJdbcConnectionException.class
    })
    public ExceptionJson handleDatabaseConnectionExceptions(DataAccessResourceFailureException ex) {
        logger.error("Database resource failure: ", ex);
        ExceptionJson exceptionJson = new ExceptionJson();
        if (ex.getMessage().contains("Unknown database")) {
            exceptionJson.setValidationException("The specified database does not exist. Please check the database configuration.");
        } else {
            exceptionJson.setValidationException("There was a problem accessing the database. Please try again later.");
        }
        return exceptionJson;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ExceptionJson handleSqlSyntaxErrorException(SQLSyntaxErrorException exception) {
        logger.error("An error occurred", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException("There is an error in your SQL syntax");
        return exceptionJson;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoResourceFoundException.class)
    public ExceptionJson handleNoResourceFoundException(NoResourceFoundException exception) {
        logger.error("An error occurred: Wrong URL passed", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException("No static resource found due to wrong URL.");
        return exceptionJson;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ExceptionJson handleAccessDeniedException(AccessDeniedException exception) {
        logger.error("An error occurred: Access denied ", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException("Access denied for directory");
        return exceptionJson;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ExceptionJson handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        logger.error("An error occurred: Authorization Denied ", exception);
        ExceptionJson exceptionJson = new ExceptionJson();
        exceptionJson.setValidationException("Authorization Denied");
        return exceptionJson;
    }
}
