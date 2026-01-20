INSERT INTO `vehicle_preferences` (`vep_name`, `vep_value`) VALUES ('turn_off_ip_blocking', '0');
INSERT INTO `vehicle_preferences` (`vep_name`, `vep_value`) VALUES ('login_amount_of_hours_blocked', '12');
INSERT INTO `vehicle_preferences` (`vep_name`, `vep_value`) VALUES ('password_active_duration', '90');

INSERT INTO `vsamc`.`user_groups` (`usg_name`, `usg_description`, `usg_type`, `usg_created`) VALUES ('admin', 'This role have access of whole application', 'all module access', '2026-01-16 00:00:00');

INSERT INTO `vsamc`.`user_groups` (`usg_name`, `usg_description`, `usg_type`, `usg_created`) VALUES ('customer', 'This role is have all access for the customer', 'customer module access', '2026-01-19');
