drop table users;
CREATE TABLE IF NOT EXISTS `users` (
  `use_username` VARCHAR(40) NOT NULL COMMENT 'This column stores the user username as primary key.',
  `use_title` VARCHAR(10) NULL COMMENT 'This column store the users title'
  `use_firstname` VARCHAR(60) NOT NULL COMMENT 'This column stores the user first name.',
  `use_surname` VARCHAR(60) NOT NULL COMMENT 'This column stores the user sur name.',
  `use_fullname` VARCHAR(150) NOT NULL COMMENT 'This column stores the user full name.',
  `use_active` TINYINT(1) NULL DEFAULT 1 COMMENT 'This column stores the flag if the user is active.(e.g. 1 for active)',
  `use_email` VARCHAR(150) NULL COMMENT 'This column stores the users email address.',
  `use_mobile` VARCHAR(20) NULL COMMENT 'This column stores the users mobile number',
  `use_password` VARCHAR(64) NULL COMMENT 'This column stores the password of user',
  `use_password_last_modified` DATE NULL COMMENT 'This column stores the date of the user\'s last password update.',
  `use_last_loggedin_date` DATE NULL COMMENT 'This column stores the date the user last logged in.',
  `use_logged_in` TINYINT(1) NULL COMMENT 'This column stores the flag if the user is currently logged in. (e.g.  1 if logged in)',
  `use_login_attempts` VARCHAR(10) NULL DEFAULT 0 COMMENT 'This column stores the successive login attempts with your username. Since last successful login.',
  `use_created_by` VARCHAR(40) NOT NULL COMMENT 'This column stores the username of the user that created this user account.',
  `use_created` DATETIME NOT NULL COMMENT 'This column stores the timestamp of user account was created.',
PRIMARY KEY (`use_username`))
ENGINE = InnoDB
COMMENT = 'This table stores user information.';

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

INSERT INTO `vsamc`.`user_groups` (`usg_id`, `usg_name`, `usg_description`, `usg_type`, `usg_created`) VALUES ('1', 'admin', 'This role have access of whole application', 'all module access', '2026-01-16 00:00:00');
