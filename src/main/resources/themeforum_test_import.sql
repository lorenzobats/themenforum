INSERT INTO user_table (id, user_name, active) VALUES ('fafaaa75-64eb-4b5b-96f3-79721048b85f', 'admin', true);
INSERT INTO user_table (id, user_name, active) VALUES ('78188b10-4733-4fca-8dfd-e07aee389383', 'oschluet', true);
INSERT INTO user_table (id, user_name, active) VALUES ('efac4cf6-4188-4a18-8489-4fc98f3633d6', 'lbattist', true);
INSERT INTO user_table (id, user_name, active) VALUES ('c8f79ffc-7ce0-4673-a22c-5cf210b83f13', 'afuchs', true);
INSERT INTO user_table (id, user_name, active) VALUES ('7adc0aa2-5532-4233-a8b4-2d0debd6a053', 'bdierken', true);
INSERT INTO user_table (id, user_name, active) VALUES ('8be3a431-5b87-4223-ab76-bd3308da4cfd', 'ckaiser', true);
INSERT INTO user_table (id, user_name, active) VALUES ('6194c256-473d-4fe1-9841-41d999885a23', 'dbacher', true);
INSERT INTO user_table (id, user_name, active) VALUES ('9fe9d31c-71ac-45fc-b236-f8d53dfd00bb', 'emammer', true);
INSERT INTO user_table (id, user_name, active) VALUES ('75588120-9b33-4484-acbc-cf902a9f9ac8', 'fwecher', true);
INSERT INTO user_table (id, user_name, active) VALUES ('77cc0520-c6d0-4f06-964f-9af0834fc6db', 'gluehrs', true);

INSERT INTO topic_table (id, createdat, description, title, user_id) VALUES ('a70d18cf-7b53-43fe-86de-1277fa476864', '2023-01-21 20:49:37.855505', 'Deutschland das Autoland', 'Autos', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO topic_table (id, createdat, description, title, user_id) VALUES ('897efe45-a06f-4d3a-9579-5df3fc279beb', '2023-01-22 20:49:37.855505', 'Softwarearchitektur bestes Fach!', 'Softwarearchitektur', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');
INSERT INTO topic_table (id, createdat, description, title, user_id) VALUES ('9b0d4d55-9c40-4f7e-5c5a-9dbf9e957f38', '2023-01-25 16:37:42.234567', 'Die Geschichte der deutschen Sprache', 'Sprachwissenschaft', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');
INSERT INTO topic_table (id, createdat, description, title, user_id) VALUES ('9b0d4d55-9c40-4f7e-6c5a-9dbf9e967f38', '2023-01-25 17:37:42.234567', 'Ein Überblick über die deutsche Wirtschaft', 'Wirtschaft', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');
INSERT INTO topic_table (id, createdat, description, title, user_id) VALUES ('9b0d4d55-9c40-4f7e-8c5a-9dbf9e987f38', '2023-01-25 19:37:42.234567', 'Eine Einführung in die Welt der deutschen Naturwissenschaften', 'Naturwissenschaften', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');

INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('55a7409f-c0eb-453e-b7ef-89f905963ce9', 'Das hier ist Olivers erster Post', '2023-01-20 21:06:14.563721', 'Olivers Post 1', 'a70d18cf-7b53-43fe-86de-1277fa476864', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('4cfe8a99-f71a-476c-a2cb-dae7cb86872e', 'Das hier ist Olivers zweiter Post', '2023-01-21 21:06:14.563721', 'Olivers Post 2', 'a70d18cf-7b53-43fe-86de-1277fa476864', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('c75d5bba-a840-4ce2-998c-07a2f00cc49b', 'Das hier ist Lorenzos erster Post', '2023-01-20 21:06:14.563721', 'Lorenzos Post 1', 'a70d18cf-7b53-43fe-86de-1277fa476864', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('959b81a0-35b3-4f9d-a08b-4879173a1e2c', 'Das hier ist Lorenzos zweiter Post', '2023-01-21 21:06:14.563721', 'Lorenzos Post 2', 'a70d18cf-7b53-43fe-86de-1277fa476864', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('06f21fbb-1730-446d-b3c2-44dd8a839c12', 'Das hier ist Lorenzos dritter Post', '2023-01-22 21:06:14.563721',  'Lorenzos Post 3', 'a70d18cf-7b53-43fe-86de-1277fa476864', 'efac4cf6-4188-4a18-8489-4fc98f3633d6');
INSERT INTO post_table (id, content, createdat, title, topic_id, user_id) VALUES ('08f21fbb-1730-446d-b3c2-44dd8a839c12', 'Das hier ist Olivers vierter Post', '2022-12-22 21:06:14.563721',  'Ollis Post 3', '9b0d4d55-9c40-4f7e-5c5a-9dbf9e957f38', '78188b10-4733-4fca-8dfd-e07aee389383');

INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('9ac4c6fd-68e1-4b41-97fd-817284290411', true, '2023-01-22 19:29:59.697731', 'Olivers Direktkommentar zu Olivers Post 1', null, '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('ee774407-a71f-4d4e-b829-f26d38e2ee81', true, '2023-01-22 20:29:59.697731', 'Olivers Direktkommentar zu Lorenzos Post 2', null, '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('5c39bb7f-0ff4-4bff-a89f-03adc03bb72c', true, '2023-01-22 20:30:59.697731', 'Olivers Reply 1 zu Olivers Direktkommentar zu Olivers Post 1', '9ac4c6fd-68e1-4b41-97fd-817284290411', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('2c210dd8-ff07-4adb-ad4c-67625ae7c295', true, '2023-01-22 20:31:59.697731', 'Olivers Reply 2 zu Olivers Direktkommentar zu Olivers Post 1', '9ac4c6fd-68e1-4b41-97fd-817284290411', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, active, createdat, text, parent_comment_id, user_id) VALUES ('11b17c89-1c3b-4951-9bd4-7c42119534e7', true, '2023-01-22 20:33:59.697731', 'Olivers Reply 1 zu Olivers Reply 2 zu Olivers Direktkommentar zu Olivers Post 1', '2c210dd8-ff07-4adb-ad4c-67625ae7c295', '78188b10-4733-4fca-8dfd-e07aee389383');

INSERT INTO post_table_comment_table (post_id, comments_id) VALUES ('55a7409f-c0eb-453e-b7ef-89f905963ce9', '9ac4c6fd-68e1-4b41-97fd-817284290411');
INSERT INTO post_table_comment_table (post_id, comments_id) VALUES ('959b81a0-35b3-4f9d-a08b-4879173a1e2c', 'ee774407-a71f-4d4e-b829-f26d38e2ee81');






INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('6b05e5b1-1859-418f-97ba-dd5c8a4f02bf', '$2a$10$DwD7QAQ8xBxjG7B3A2I5s.bzU/1bt4YYvR91V1Ob33F4Olb.SnYwi', 'member', '78188b10-4733-4fca-8dfd-e07aee389383', 'oschluet');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('c91b0e85-abcb-48d4-b344-3229099c84ba', '$2a$10$HTWkZDXvW.laYCCBzWo7Qu1xZL2fKWe.EgEeGly9TCLUa0xzqPuS2', 'member', 'efac4cf6-4188-4a18-8489-4fc98f3633d6', 'lbattist');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('89445af1-beb3-43bf-a80a-ff17119ccbf7', '$2a$10$QLCuFmy3kh2stifgZnM9BOpADJ3KPx13xQ9I2O16iN3gxpQSzaoIe', 'admin', 'fafaaa75-64eb-4b5b-96f3-79721048b85f', 'admin');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('e9bd912c-07b4-490d-b2b4-35586b89d5f7', '$2a$10$lhn3XvixVCon5Rm3arJqAOZK/EP4WRWYOVchlCm0v6TAwWTNq882i', 'member', 'c8f79ffc-7ce0-4673-a22c-5cf210b83f13', 'afuchs');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('258c6eef-56ac-45db-a40f-b4c0a56305ac', '$2a$10$x1ICBN/pbZRIF7/XGnB8lOras.hg7iyElwvOkOdUa7RvdKaGAVlG6', 'member', '7adc0aa2-5532-4233-a8b4-2d0debd6a053', 'bdierken');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('93d404b4-6dfc-4597-9896-b7e36d574523', '$2a$10$asza7gMOtJ.rDAgQ4XwGPOEw2QYelbUYfC.AQQV0lNlsHvdccGZFa', 'member', '8be3a431-5b87-4223-ab76-bd3308da4cfd', 'ckaiser');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('ff0c3fed-dd6a-4737-96ef-d89a27348042', '$2a$10$kO1PXcFmP5o3JZ37j0O9h.IIEsZZsZRMpCNC3SRdLPn5L9nu85f3G', 'member', '6194c256-473d-4fe1-9841-41d999885a23', 'dbacher');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('ecec7edc-d9f2-4a5a-8968-75f5c024c916', '$2a$10$3PCL3NwPbAez2/WMqFK.Su6LeGEKyJ2ScI898t9pqQ0yY4YVA1R.K', 'member', '9fe9d31c-71ac-45fc-b236-f8d53dfd00bb', 'emammer');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('a1a7e8b8-6f68-47b0-804f-cef584854782', '$2a$10$AbH4TmXXU7U6c2TyXiz2d./8/5BqxkqAN2JYJozgBh9n3fW6sTTxK', 'member', '75588120-9b33-4484-acbc-cf902a9f9ac8', 'fwecher');
INSERT INTO auth.auth_user_table (id, password, role, user_id, username) VALUES ('fd14b0f7-e3ff-45d3-b64f-6b6fac2f5b98', '$2a$10$AWM.m7oJfQqFVaIGY3QTlev4747pIqTyb3zdbYQvkJ8989QIHwYcK', 'member', '77cc0520-c6d0-4f06-964f-9af0834fc6db', 'gluehrs');


INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (1, 'a70d18cf-7b53-43fe-86de-1277fa476864',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (2, '897efe45-a06f-4d3a-9579-5df3fc279beb',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (11, '9b0d4d55-9c40-4f7e-5c5a-9dbf9e957f38',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (12, '9b0d4d55-9c40-4f7e-6c5a-9dbf9e967f38',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (14, '9b0d4d55-9c40-4f7e-8c5a-9dbf9e987f38',  'c91b0e85-abcb-48d4-b344-3229099c84ba');

INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (15, '55a7409f-c0eb-453e-b7ef-89f905963ce9',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (16, '4cfe8a99-f71a-476c-a2cb-dae7cb86872e',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (17, 'c75d5bba-a840-4ce2-998c-07a2f00cc49b',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (18, '959b81a0-35b3-4f9d-a08b-4879173a1e2c',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (19, '06f21fbb-1730-446d-b3c2-44dd8a839c12',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (20, '08f21fbb-1730-446d-b3c2-44dd8a839c12',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');

INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (21, '9ac4c6fd-68e1-4b41-97fd-817284290411',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (22, 'ee774407-a71f-4d4e-b829-f26d38e2ee81',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (23, '5c39bb7f-0ff4-4bff-a89f-03adc03bb72c',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (24, '2c210dd8-ff07-4adb-ad4c-67625ae7c295',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (25, '11b17c89-1c3b-4951-9bd4-7c42119534e7',  '6b05e5b1-1859-418f-97ba-dd5c8a4f02bf');

INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (26, '09f21fbb-1130-446d-b3c2-44dd8a839c12',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (27, '09f21fbb-1230-446d-b3c2-44dd8a839c12',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (28, '09f21fbb-1330-446d-b3c2-44dd8a839c12',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
INSERT INTO auth.owner_of_table (id, ressource_id, owner_id) VALUES (29, '09f21fbb-1430-446d-b3c2-44dd8a839c12',  'c91b0e85-abcb-48d4-b344-3229099c84ba');
