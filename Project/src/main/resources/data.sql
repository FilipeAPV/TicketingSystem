INSERT INTO `departments` (`name`,`created_at`,`created_by`) VALUES ('IT', CURDATE(), 'AUTOMATIC_FILL');
INSERT INTO `departments` (`name`,`created_at`,`created_by`) VALUES ('Finance', CURDATE(), 'AUTOMATIC_FILL');
INSERT INTO `departments` (`name`,`created_at`,`created_by`) VALUES ('Marketing', CURDATE(), 'AUTOMATIC_FILL');
INSERT INTO `departments` (`name`,`created_at`,`created_by`) VALUES ('Sales', CURDATE(), 'AUTOMATIC_FILL');
INSERT INTO `departments` (`name`,`created_at`,`created_by`) VALUES ('Engineering', CURDATE(), 'AUTOMATIC_FILL');
INSERT INTO `departments` (`name`,`created_at`,`created_by`) VALUES ('Design', CURDATE(), 'AUTOMATIC_FILL');

INSERT INTO `users` (`first_name`, `last_name`, `job_title`, `email`, `manager`, `password`, `created_at`, `created_by`, `department`) VALUES ('John', 'McCain', 'Help Desk', 'jm@gmail.com', 'Marta', '123', CURDATE(), 'AUTOMATIC_FILL', '1');
INSERT INTO `users` (`first_name`, `last_name`, `job_title`, `email`, `manager`, `password`, `created_at`, `created_by`, `department`) VALUES ('Ana', 'Pereira', 'Accountant', 'ap@gmail.com', 'Natacha', '123', CURDATE(), 'AUTOMATIC_FILL', '2');
INSERT INTO `users` (`first_name`, `last_name`, `job_title`, `email`, `manager`, `password`, `created_at`, `created_by`, `department`) VALUES ('Tao', 'Lee', 'Creative', 'tl@gmail.com', 'Leo', '123', CURDATE(), 'AUTOMATIC_FILL', '3');
INSERT INTO `users` (`first_name`, `last_name`, `job_title`, `email`, `manager`, `password`, `created_at`, `created_by`, `department`) VALUES ('Lin', 'Khoo', 'Video Editor', 'lk@gmail.com', 'Steve', '123', CURDATE(), 'AUTOMATIC_FILL', '4');
INSERT INTO `users` (`first_name`, `last_name`, `job_title`, `email`, `manager`, `password`, `created_at`, `created_by`, `department`) VALUES ('Patricia', 'Oneil', 'Social Media Specialist', 'po@gmail.com', 'Toni', '123', CURDATE(), 'AUTOMATIC_FILL', '5');

INSERT INTO `tickets` (`status`, `subject`, `priority`, `message`, `created_at`, `created_by`,`creator_id`) VALUES ('Open', 'My keyboard is broken', 'High', 'Dear IT, my keyboard is broken. Could you kinldy provide me a replacement ?', CURDATE(), 'AUTOMATIC_FILL', '2');
INSERT INTO `tickets` (`status`, `subject`, `priority`, `message`, `created_at`, `created_by`,`creator_id`) VALUES ('Open', 'Video is too long', 'Normal', 'Lin, could you please reduce the video from yesterday ? It needs to be less than 5 minutes. Thank you', CURDATE(), 'AUTOMATIC_FILL', '2');
INSERT INTO `tickets` (`status`, `subject`, `priority`, `message`, `created_at`, `created_by`,`creator_id`) VALUES ('Open', 'Ideas needed', 'Normal', 'Tao, what do you think of the presentation attached ? Thank you', CURDATE(), 'AUTOMATIC_FILL', '5');
INSERT INTO `tickets` (`status`, `subject`, `priority`, `message`, `created_at`, `created_by`,`creator_id`) VALUES ('Open', 'Install Software', 'Normal', 'Dear IT, I need to install a plug-in for Excel. My TV is 123456789', CURDATE(), 'AUTOMATIC_FILL', '2');
INSERT INTO `tickets` (`status`, `subject`, `priority`, `message`, `created_at`, `created_by`,`creator_id`) VALUES ('Open', 'Instagram Views', 'High', 'URGENT - Patricia, correct the last post ASAP.', CURDATE(), 'AUTOMATIC_FILL', '3');