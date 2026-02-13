create schema vsamc;
use vsamc;

-- -----------------------------------------------------
-- Table `authentication_tokens`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `authentication_tokens` (
  `atk_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'This column will hold Primary Key',
  `atk_use_username` VARCHAR(40) NULL COMMENT 'This column will hold username of the user ',
  `atk_jwt_token` VARCHAR(3000) NULL COMMENT 'This column will hold JWT token of the user',
  `atk_refresh_token` VARCHAR(255) NULL COMMENT 'This column will hold  REFRESH token of the user ',
  `atk_uuid` VARCHAR(50) NULL COMMENT 'This column will hold UUID per Login \nFor example - 35ed1ad3-5ef4-43b4-a2d3-8adb2c4088ef',
  `atk_created` DATETIME NULL COMMENT 'This column will hold record created datetime',
  PRIMARY KEY (`atk_id`))
ENGINE = InnoDB
COMMENT = 'Stores the latest authentication tokens (JWT and refresh token) for each user. This table ensures that only one active login session is tracked per user at any time.\n\nThis table maintains a single row per user, storing their current JWT, refresh token, and session UUID. It is useful for single-session models, token revocation, and tracking active sessions securely.';

-- -----------------------------------------------------
-- Table `blocking_ip_address_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `blocking_ip_address_details` (
  `bpa_id` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `bpa_type` ENUM('patient', 'user', 'employee') NOT NULL COMMENT 'Type of login (patient/user/employee)',
  `bpa_blocked_till_time` DATETIME NOT NULL COMMENT 'Blocked till Timestamp',
  `bpa_ip_address` VARCHAR(15) NOT NULL COMMENT 'Blocked IP v4 Address',
  `bpa_created` DATETIME NOT NULL COMMENT 'Created date time',
  PRIMARY KEY (`bpa_id`))
ENGINE = InnoDB
COMMENT = 'If 10 failed login attempts with same IP address and no valid patient or user details in a period of 10 mins will add ip address to blocking ip address table';


-- -----------------------------------------------------
-- Table `failed_logins`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `failed_logins` (
  `flo_id` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `flo_type` ENUM('patient', 'user', 'employee') NOT NULL COMMENT 'Type of login',
  `flo_ip_address` VARCHAR(15) NOT NULL COMMENT 'IP v4 Address',
  `flo_created` DATETIME NOT NULL COMMENT 'Created date time',
  `flo_notes` VARCHAR(225) NOT NULL COMMENT 'username used/ patient details/ employee supplied',
  PRIMARY KEY (`flo_id`))
ENGINE = InnoDB
COMMENT = 'This table will store instances of login , records of every invalid login attempts';


-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `use_username` VARCHAR(40) NOT NULL COMMENT 'This column stores the user username as primary key.',
  `use_title` VARCHAR(10) NULL COMMENT 'This column store the users title',
  `use_firstname` VARCHAR(60) NOT NULL COMMENT 'This column stores the user first name.',
  `use_surname` VARCHAR(60) NOT NULL COMMENT 'This column stores the user sur name.',
  `use_fullname` VARCHAR(150) NOT NULL COMMENT 'This column stores the user full name.',
  `use_active` TINYINT(1) NULL DEFAULT 1 COMMENT 'This column stores the flag if the user is active.(e.g. 1 for active)',
  `use_email` VARCHAR(150) NULL COMMENT 'This column stores the users email address.',
  `use_mobile` VARCHAR(20) NULL COMMENT 'This column stores the users mobile number',
  `use_password` VARCHAR(64) NULL COMMENT 'This column stores the password of user',
  `use_type` ENUM('customer', 'mechanic', 'admin') NOT NULL COMMENT 'This column store the users type', 
  `use_password_last_modified` DATE NULL COMMENT 'This column stores the date of the user\'s last password update.',
  `use_last_loggedin_date` DATE NULL COMMENT 'This column stores the date the user last logged in.',
  `use_logged_in` TINYINT(1) NULL COMMENT 'This column stores the flag if the user is currently logged in. (e.g.  1 if logged in)',
  `use_login_attempts` VARCHAR(10) NULL DEFAULT 0 COMMENT 'This column stores the successive login attempts with your username. Since last successful login.',
  `use_created_by` VARCHAR(40) NOT NULL COMMENT 'This column stores the username of the user that created this user account.',
  `use_created` DATETIME NOT NULL COMMENT 'This column stores the timestamp of user account was created.',
PRIMARY KEY (`use_username`))
ENGINE = InnoDB
COMMENT = 'This table stores user information.';

-- -----------------------------------------------------
-- Table `user_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_roles` (
  `usr_id` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key for the table',
  `usr_use_username` VARCHAR(40) NOT NULL,
  `usr_usg_id` MEDIUMINT(8) UNSIGNED NOT NULL COMMENT 'Foreign key to the user_groups table',
  `usr_name` VARCHAR(20) NULL COMMENT 'The name is copied from the user_groups table (required for JDBCRealsm in Tomcat to work)',
  PRIMARY KEY (`usr_id`))
COMMENT = 'Table created so that a user can have many roles, and a role can have many users.'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

-- -----------------------------------------------------
-- Table `vehicle_glitches`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle_glitches` (
  `veg_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'This column will store primary key.',
  `veg_use_username` VARCHAR(40) NULL COMMENT 'This column will store the username.',
  `veg_cause` TEXT NULL COMMENT 'Cause of exception.',
  `veg_file_name` VARCHAR(255) NULL COMMENT 'This column will store the file name.',
  `veg_line_number` INT(10) UNSIGNED NULL COMMENT 'This column will store the line number.',
  `veg_navigation_area` VARCHAR(100) NULL COMMENT 'The navigation area in which the exception was thrown.',
  `veg_stack_trace` TEXT NULL COMMENT 'The stack trace containing detailed information of the exception.',
  `veg_created` DATETIME NULL COMMENT 'Timestamp when records was added',
  `veg_created_date` DATE NULL COMMENT 'Date when record was added',
  `veg_resolved` TINYINT(1) UNSIGNED NULL COMMENT 'The flag indicating if the exception was fixed by the developer. Set to 0 if not fixed, 1 if fixed.',
  PRIMARY KEY (`veg_id`))
ENGINE = InnoDB
COMMENT = 'This table stores the details of vehicle glitches.';

-- -----------------------------------------------------
-- Table `user_groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_groups` (
  `usg_id` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'It will store the primary key',
  `usg_name` VARCHAR(60) NOT NULL COMMENT 'It will store the user role name',
  `usg_description` VARCHAR(255) NULL COMMENT 'It will store the user role description',
  `usg_type` VARCHAR(60) NULL COMMENT 'It will store the user role type e.g \'module access',
  `usg_created` DATETIME NOT NULL COMMENT 'It will store the when record is created',
  PRIMARY KEY (`usg_id`))
COMMENT = 'This table hold the user group data'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

-- -----------------------------------------------------
-- Table `vehicle_preferences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle_preferences` (
  `vep_id` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT,
  `vep_name` VARCHAR(60) NULL,
  `vep_value` VARCHAR(255) NULL,
  PRIMARY KEY (`vep_id`))
COMMENT = 'This table will contain preferences for default texting username and password , url and link to snomed coding for questions, and texting country code'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

-- -----------------------------------------------------
-- Table `vehicles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicles` (
  `veh_id` MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key for the table',
  `veh_vehicle_number` VARCHAR(255)  NOT NULL COMMENT 'Vehicle registration number',
  `veh_vehicle_type` VARCHAR(50) NOT NULL COMMENT 'Type of vehicle (e.g., Car, Bike)',
  `veh_brand` VARCHAR(100) NOT NULL COMMENT 'Brand of the vehicle (e.g., Toyota, Honda)',
  `veh_model` VARCHAR(100) NOT NULL COMMENT 'Model of the vehicle (e.g., Corolla, City)',
  `veh_manufacturing_year` MEDIUMINT NULL COMMENT 'Manufacturing year of the vehicle',
  `veh_use_username` VARCHAR(40) NOT NULL COMMENT 'This column will store the owner username of the vehicle',
  `veh_created` DATETIME NOT NULL COMMENT 'This column stores the timestamp of user account was created.',
  `veh_record_status` ENUM('approved', 'wrong') NOT NULL COMMENT 'This column will store the record status of the record',
PRIMARY KEY (`veh_id`))
ENGINE = InnoDB
COMMENT = 'Stores vehicle details such as number, type, brand, model, and manufacturing year.';

CREATE TABLE IF NOT EXISTS `appointments` (
  `apt_id` MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary Key for the appointment table',
  `apt_date` DATE NOT NULL COMMENT 'Appointment date selected by the customer',
  `apt_problem_description` VARCHAR(500) NULL COMMENT 'Problem description provided by the customer',
  `apt_status` ENUM('PENDING', 'APPROVED', 'REJECTED', 'ASSIGNED') NOT NULL COMMENT 'Current status of the appointment',
  `apt_customer` VARCHAR(40) NOT NULL COMMENT 'Username of the customer who booked the appointment',
  `apt_veh_id` MEDIUMINT UNSIGNED NOT NULL COMMENT 'Vehicle ID linked to this appointment',
  `apt_mechanic` VARCHAR(40) NULL COMMENT 'Username of the mechanic assigned to the appointment',
  `apt_created` DATETIME NOT NULL COMMENT 'Timestamp when the appointment was created',
  `apt_record_status` ENUM('approved', 'wrong') NOT NULL COMMENT 'Record status of the appointment',
  PRIMARY KEY (`apt_id`))
ENGINE = InnoDB
COMMENT = 'Stores appointment details including date, problem description, status, customer, vehicle, and assigned mechanic.';

CREATE TABLE IF NOT EXISTS `job_cards` (
    `jc_id` MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key for the job card table',
    `jc_apt_id` MEDIUMINT UNSIGNED NOT NULL COMMENT 'Appointment ID linked to this job card',
    `jc_status` VARCHAR(50) NOT NULL DEFAULT 'ASSIGNED' COMMENT 'Current status of the job card (e.g., ASSIGNED, IN_PROGRESS, DELIVERED)',
    `jc_progress_percentage` MEDIUMINT UNSIGNED NULL COMMENT 'Progress percentage of the job card (0–100)',
    `jc_inspection_notes` VARCHAR(2000) NULL COMMENT 'Inspection notes recorded by the service engineer',
    `jc_work_done` VARCHAR(2000) NULL COMMENT 'Details of the work completed on the vehicle',
    `jc_remarks` VARCHAR(2000) NULL COMMENT 'Additional remarks related to the job card',
    `jc_created` DATETIME NOT NULL COMMENT 'Timestamp when the job card was created',
    `jc_created_by` VARCHAR(40) NOT NULL COMMENT 'Username of the user who created the job card',
    `jc_updated` DATETIME NULL COMMENT 'Timestamp when the job card was last updated',
    `jc_updated_by` VARCHAR(40) NULL COMMENT 'Username of the user who last updated the job card',
    `jc_record_status` ENUM('approved', 'wrong', 'updated') NOT NULL COMMENT 'record status of the job card',
PRIMARY KEY (`jc_id`))
ENGINE = InnoDB
COMMENT = 'Stores job card details including status, progress, inspection notes, and audit information.';
