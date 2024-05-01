insert into incident_user (username, password, role) values ('admin', '$2a$10$yVX5eGWSaGdS73RTr1MW7uO/c1.maJ1UaxExmGE7aXe1/Mc.vqaEG', 'ROLE_ADMIN'); --admin
insert into incident_user (username, password, role) values ('usertest', '$2a$12$Mi.W31Kgphee/8NArVc5Cu9HWdRjYaLfeLmOvtFa5N1w9.gMc7OqS', 'ROLE_USER'); --password
insert into incident_user (username, password, role) values ('usertest2', '$2a$12$Mi.W31Kgphee/8NArVc5Cu9HWdRjYaLfeLmOvtFa5N1w9.gMc7OqS', 'ROLE_USER');
insert into incident_user (username, password, role) values ('usertest3', '$2a$12$Mi.W31Kgphee/8NArVc5Cu9HWdRjYaLfeLmOvtFa5N1w9.gMc7OqS', 'ROLE_USER');

insert into incident (type, description, status, priority, date_registered, date_resolved, user_responsible_id)
values ('type1', 'desc1', 'ASSIGNED', 'LOW', '2024-04-19 17:32:32.300311', null, 1);

insert into incident (type, description, status, priority, date_registered, date_resolved, user_responsible_id)
values ('type2', 'desc2', 'IN_PROGRESS', 'MEDIUM', '2024-04-20 17:32:32.300311', null, 2);

insert into incident (type, description, status, priority, date_registered, date_resolved, user_responsible_id)
values ('type3', 'desc3', 'PAUSED', 'MEDIUM', '2024-04-21 17:32:32.300311', null, 3);

insert into incident (type, description, status, priority, date_registered, date_resolved, user_responsible_id)
values ('type4', 'desc4', 'DONE', 'HIGH', '2024-04-22 17:32:32.300311', null, 4);

insert into incident (type, description, status, priority, date_registered, date_resolved, user_responsible_id)
values ('type5', 'desc5', 'CLOSED', 'CRITICAL', '2024-04-23 17:32:32.300311', '2024-04-23 18:32:32.300311', 4);

insert into incident_log (incident_id, date_updated, update_description)
values (1, '2024-04-19 17:32:32.300311', 'Инцидент создан');

insert into incident_log (incident_id, date_updated, update_description)
values (2, '2024-04-20 17:32:32.300311', 'Инцидент создан');

insert into incident_log (incident_id, date_updated, update_description)
values (3, '2024-04-21 17:32:32.300311', 'Инцидент создан');

insert into incident_log (incident_id, date_updated, update_description)
values (4, '2024-04-22 17:32:32.300311', 'Инцидент создан');

insert into incident_log (incident_id, date_updated, update_description)
values (5, '2024-04-23 17:32:32.300311', 'Инцидент создан');
