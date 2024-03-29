/*DROP DATABASE IF EXISTS ticketingsystem;
create database ticketingsystem;

use ticketingsystem;*/

CREATE TABLE IF NOT EXISTS `users` (
   `user_id` int AUTO_INCREMENT PRIMARY KEY,
   `first_name` varchar(100) NOT NULL,
    `last_name` varchar(100) NOT NULL,
    `job_title` varchar(100) NOT NULL,
    `email` varchar(100) NOT NULL,
    `manager` varchar(100) NOT NULL,
    `password` varchar(300) NOT NULL,
    `role` varchar(100) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL
    );

CREATE TABLE IF NOT EXISTS `tickets` (
                                         `ticket_id` int AUTO_INCREMENT PRIMARY KEY,
                                         `status` varchar(100) NOT NULL,
    `subject` varchar(100) NOT NULL,
    `priority` varchar(100) NOT NULL,
    `message` varchar(500) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL,
    `creator_id` int NOT NULL,
    `assignee_id` int DEFAULT NULL,
    `assignee_derpartment_id` int DEFAULT NULL,
    FOREIGN KEY (`creator_id`) REFERENCES users(`user_id`),
    FOREIGN KEY (`assignee_id`) REFERENCES users(`user_id`)
    );

CREATE TABLE IF NOT EXISTS `departments`(
                                            `department_id` int AUTO_INCREMENT PRIMARY KEY,
                                            `name` varchar(100) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL,
    `super_user_id` int DEFAULT NULL,
    FOREIGN KEY (`super_user_id`) REFERENCES users(`user_id`)
    );

ALTER TABLE `users` ADD `fk_department_id` INT NOT NULL;
ALTER TABLE `users` ADD CONSTRAINT `FK_USER_DEPARTMENT_ID` FOREIGN KEY (`fk_department_id`) REFERENCES departments(`department_id`);
ALTER TABLE `tickets` ADD CONSTRAINT `FK_TICKET_DEPARTMENT_ID` FOREIGN KEY (`assignee_derpartment_id`) REFERENCES departments(`department_id`);