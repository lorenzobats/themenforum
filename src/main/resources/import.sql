INSERT INTO user_table (id, user_name) VALUES ('78188b10-4733-4fca-8dfd-e07aee389383', 'oschluet');
INSERT INTO user_table (id, user_name) VALUES ('efac4cf6-4188-4a18-8489-4fc98f3633d6', 'lbattist');
INSERT INTO user_table (id, user_name) VALUES ('fafaaa75-64eb-4b5b-96f3-79721048b85f', 'admin');

INSERT INTO auth_user_table (id, password, role, user_id, username) VALUES ('6b05e5b1-1859-418f-97ba-dd5c8a4f02bf', '$2a$10$DwD7QAQ8xBxjG7B3A2I5s.bzU/1bt4YYvR91V1Ob33F4Olb.SnYwi', 'member', '78188b10-4733-4fca-8dfd-e07aee389383', 'oschluet');
INSERT INTO auth_user_table (id, password, role, user_id, username) VALUES ('c91b0e85-abcb-48d4-b344-3229099c84ba', '$2a$10$HTWkZDXvW.laYCCBzWo7Qu1xZL2fKWe.EgEeGly9TCLUa0xzqPuS2', 'member', 'efac4cf6-4188-4a18-8489-4fc98f3633d6', 'lbattist');
INSERT INTO auth_user_table (id, password, role, user_id, username) VALUES ('89445af1-beb3-43bf-a80a-ff17119ccbf7', '$2a$10$QLCuFmy3kh2stifgZnM9BOpADJ3KPx13xQ9I2O16iN3gxpQSzaoIe', 'admin', 'fafaaa75-64eb-4b5b-96f3-79721048b85f', 'admin');

INSERT INTO topic_table (id, createdat, description, title, user_id) VALUES ('a70d18cf-7b53-43fe-86de-1277fa476864', '2023-01-21 20:49:37.855505', 'Deutschland das Autoland', 'Autos', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO topic_table (id, createdat, description, title, user_id) VALUES ('897efe45-a06f-4d3a-9579-5df3fc279beb', '2023-01-22 20:49:37.855505', 'SWA bestes Fach!', 'SWA', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO topic_table (id, createdat, description, title, user_id) VALUES ('5b541bd3-2a87-4cbc-ad95-ed9343831972','2023-01-22 21:45:37.855505', 'Alles rund um das Thema Sport', 'Sport', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');

INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('55a7409f-c0eb-453e-b7ef-89f905963ce9', 'Das hier ist Olivers erster Post', '2023-01-20 21:06:14.563721', 'Olivers Post 1', 'a70d18cf-7b53-43fe-86de-1277fa476864', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('4cfe8a99-f71a-476c-a2cb-dae7cb86872e', 'Das hier ist Olivers zweiter Post', '2023-01-21 21:06:14.563721', 'Olivers Post 2', 'a70d18cf-7b53-43fe-86de-1277fa476864', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('c75d5bba-a840-4ce2-998c-07a2f00cc49b', 'Das hier ist Lorenzos erster Post', '2023-01-20 21:06:14.563721', 'Lorenzos Post 1', 'a70d18cf-7b53-43fe-86de-1277fa476864', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('959b81a0-35b3-4f9d-a08b-4879173a1e2c', 'Das hier ist Lorenzos zweiter Post', '2023-01-21 21:06:14.563721', 'Lorenzos Post 2', 'a70d18cf-7b53-43fe-86de-1277fa476864', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('06f21fbb-1730-446d-b3c2-44dd8a839c12', 'Das hier ist Lorenzos dritter Post', '2023-01-22 21:06:14.563721', 'Lorenzos Post 3', 'a70d18cf-7b53-43fe-86de-1277fa476864', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');


INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('9ac4c6fd-68e1-4b41-97fd-817284290411', true, '2023-01-22 19:29:59.697731', 'Olivers Direktkommentar zu Olivers Post 1', null, '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('ee774407-a71f-4d4e-b829-f26d38e2ee81', true, '2023-01-22 20:29:59.697731', 'Olivers Direktkommentar zu Lorenzos Post 2', null, '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('5c39bb7f-0ff4-4bff-a89f-03adc03bb72c', true, '2023-01-22 20:30:59.697731', 'Olivers Reply 1 zu Olivers Direktkommentar zu Olivers Post 1', '9ac4c6fd-68e1-4b41-97fd-817284290411', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('2c210dd8-ff07-4adb-ad4c-67625ae7c295', true, '2023-01-22 20:31:59.697731', 'Olivers Reply 2 zu Olivers Direktkommentar zu Olivers Post 1', '9ac4c6fd-68e1-4b41-97fd-817284290411', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('11b17c89-1c3b-4951-9bd4-7c42119534e7', true, '2023-01-22 20:33:59.697731', 'Olivers Reply 1 zu Olivers Reply 2 zu Olivers Direktkommentar zu Olivers Post 1', '2c210dd8-ff07-4adb-ad4c-67625ae7c295', '78188b10-4733-4fca-8dfd-e07aee389383');

INSERT INTO post_table_comment_table (post_id, comments_id) VALUES ('55a7409f-c0eb-453e-b7ef-89f905963ce9', '9ac4c6fd-68e1-4b41-97fd-817284290411');
INSERT INTO post_table_comment_table (post_id, comments_id) VALUES ('959b81a0-35b3-4f9d-a08b-4879173a1e2c', 'ee774407-a71f-4d4e-b829-f26d38e2ee81');