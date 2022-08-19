INSERT INTO `departments` (`name`,`created_at`,`created_by`) VALUES ('IT', CURDATE(), 'AUTOMATIC_FILL');
INSERT INTO `departments` (`name`,`created_at`,`created_by`) VALUES ('Finance', CURDATE(), 'AUTOMATIC_FILL');

INSERT INTO `users` (`first_name`, `last_name`, `job_title`, `email`, `manager`, `password`, `role`, `created_at`, `created_by`, `fk_department_id`) VALUES ('John', 'McCain', 'Help Desk', 'jm@gmail.com', 'Marta', '$2a$10$rgxQE//XV8SZrXuCdsl.AOH.DzSIYnknXysKjbLIQZ7khkWI7emI.', 'ROLE_USER', CURDATE(), 'AUTOMATIC_FILL', '1');
INSERT INTO `users` (`first_name`, `last_name`, `job_title`, `email`, `manager`, `password`, `role`,`created_at`, `created_by`, `fk_department_id`) VALUES ('Ana', 'Pereira', 'Accountant', 'ap@gmail.com', 'Natacha', '$2a$10$rgxQE//XV8SZrXuCdsl.AOH.DzSIYnknXysKjbLIQZ7khkWI7emI.', 'ROLE_USER', CURDATE(), 'AUTOMATIC_FILL', '2');

INSERT INTO `tickets` (`status`, `subject`, `priority`, `message`, `created_at`, `created_by`,`creator_id`,`assignee_id`,`assignee_derpartment_id`) VALUES ('OPEN', 'My keyboard is broken', 'URGENT', 'Dear IT, my keyboard is broken. Could you kinldy provide me a replacement ?', CURDATE(), 'AUTOMATIC_FILL', '2', '1', '1');
INSERT INTO `tickets` (`status`, `subject`, `priority`, `message`, `created_at`, `created_by`,`creator_id`,`assignee_id`,`assignee_derpartment_id`) VALUES ('OPEN', 'Video is too long', 'STANDARD', 'Lin, could you please reduce the video from yesterday ? It needs to be less than 5 minutes. Thank you', CURDATE(), 'AUTOMATIC_FILL', '1', '2', '2');
