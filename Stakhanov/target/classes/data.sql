insert into person(username, password, enabled, role, fullname, education, birth_date, pos) values ('admin', '$2a$04$YDiv9c./ytEGZQopFfExoOgGlJL6/o0er0K.hiGb5TGKHUL8Ebn..', true, 'ROLE_ADMIN', 'Horthy Miklos', 'az elet iskolaja', '1868-06-18', 'kormanyzo');
insert into person(username, password, enabled, role, fullname, education, birth_date, pos) values ('user1', '$2a$04$YDiv9c./ytEGZQopFfExoOgGlJL6/o0er0K.hiGb5TGKHUL8Ebn..', true, 'ROLE_USER', 'Jeno', 'erettsegi', '1967-02-01', 'muszakvezeto');
insert into person(username, password, enabled, role, fullname, education, birth_date, pos) values ('user2', '$2a$04$YDiv9c./ytEGZQopFfExoOgGlJL6/o0er0K.hiGb5TGKHUL8Ebn..', true, 'ROLE_USER', 'Bela', '8 altalanos', '1969-09-01', 'CRC-maros');
insert into person(username, password, enabled, role, fullname, education, birth_date, pos) values ('dummy', '$2a$04$YDiv9c./ytEGZQopFfExoOgGlJL6/o0er0K.hiGb5TGKHUL8Ebn..', true, 'ROLE_USER', 'Dummy', 'asd', '1969-09-01', 'asd');

insert into team(boss_id, team_name, workplace) values (2, 'CRC-marosok', 'Weiss-muvek');

insert into calendar(team_id, created_at) values(1, CURRENT_TIMESTAMP());

insert into registry(calendar_id, owner_id, start_time, end_time, created_at, comment) values (1, 2, '2018-11-17 09:00:00', '2018-11-17 17:00:00', CURRENT_TIMESTAMP(), 'Lehet kesek, fogorvostol jovok');

insert into PERSON_CO_TEAMS(team_mates_id, co_teams_id) values (2,1);
insert into PERSON_CO_TEAMS(team_mates_id, co_teams_id) values (3,1);