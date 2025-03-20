insert into users (name, email)
values ('test1', 'test1@yandex.ru'),
('test2', 'test2@yandex.ru');

insert into requests (description, requestor_id, created)
values ('description1', 2, '2025-03-24 21:24:16'),
('description2', 1, '2025-03-24 21:24:16'),
('description3', 1, '2025-03-25 08:24:50');;

insert into items (name, description, is_available, owner_id, request_id)
values ('name1', 'description1', true, 1, 1),
('test2', 'description2', false, 1, null),
('name3', 'test3', true, 2, null);

insert into bookings (start_date, end_date, item_id, booker_id, status)
values ('2025-03-23 21:00:00', '2025-03-24 21:00:00', 1, 2, 'APPROVED'),
('2025-04-23 21:00:00', '2025-04-24 21:00:00', 1, 2, 'APPROVED');

insert into comments (text, item_id, author_id, created)
values ('text1', 1, 2, '2025-03-25 12:00:00');