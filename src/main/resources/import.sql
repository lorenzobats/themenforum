INSERT INTO user_table (id, user_name) VALUES ('78188b10-4733-4fca-8dfd-e07aee389383', 'oschluet');
INSERT INTO user_table (id, user_name) VALUES ('efac4cf6-4188-4a18-8489-4fc98f3633d6', 'lbattist');

INSERT INTO keycloack_user_table (id, password, role, userid, username) VALUES ('6b05e5b1-1859-418f-97ba-dd5c8a4f02bf', '$2a$10$DwD7QAQ8xBxjG7B3A2I5s.bzU/1bt4YYvR91V1Ob33F4Olb.SnYwi', 'member', '78188b10-4733-4fca-8dfd-e07aee389383', 'oschluet');
INSERT INTO keycloack_user_table (id, password, role, userid, username) VALUES ('c91b0e85-abcb-48d4-b344-3229099c84ba', '$2a$10$HTWkZDXvW.laYCCBzWo7Qu1xZL2fKWe.EgEeGly9TCLUa0xzqPuS2', 'member', 'efac4cf6-4188-4a18-8489-4fc98f3633d6', 'lbattist');

INSERT INTO post_table (id, title, userpersistenceentity_id) VALUES ('55a7409f-c0eb-453e-b7ef-89f905963ce9', 'Olivers Post 1', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO post_table (id, title, userpersistenceentity_id) VALUES ('4cfe8a99-f71a-476c-a2cb-dae7cb86872e', 'Olivers Post 2', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO post_table (id, title, userpersistenceentity_id) VALUES ('c75d5bba-a840-4ce2-998c-07a2f00cc49b', 'Lorenzos Post 2', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO post_table (id, title, userpersistenceentity_id) VALUES ('959b81a0-35b3-4f9d-a08b-4879173a1e2c', 'Lorenzos Post 1', '78188b10-4733-4fca-8dfd-e07aee389383');


INSERT INTO comment_table (id, text, parentcomment_id, userpersistenceentity_id) VALUES ('9ac4c6fd-68e1-4b41-97fd-817284290411', 'Parentcomment 1 Olivers Post 1', null, '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, text, parentcomment_id, userpersistenceentity_id) VALUES ('ee774407-a71f-4d4e-b829-f26d38e2ee81', 'Parentcomment 1 Lorenzos Post 1', null, '78188b10-4733-4fca-8dfd-e07aee389383');

INSERT INTO comment_table (id, text, parentcomment_id, userpersistenceentity_id) VALUES ('5c39bb7f-0ff4-4bff-a89f-03adc03bb72c', 'Reply 1 to Parentcomment 1 of Olivers Post 1', '9ac4c6fd-68e1-4b41-97fd-817284290411', '78188b10-4733-4fca-8dfd-e07aee389383');
INSERT INTO comment_table (id, text, parentcomment_id, userpersistenceentity_id) VALUES ('2c210dd8-ff07-4adb-ad4c-67625ae7c295', 'Reply 2 to Parentcomment 1 of Olivers Post 1', '9ac4c6fd-68e1-4b41-97fd-817284290411', '78188b10-4733-4fca-8dfd-e07aee389383');

INSERT INTO comment_table (id, text, parentcomment_id, userpersistenceentity_id) VALUES ('11b17c89-1c3b-4951-9bd4-7c42119534e7', 'Reply 1 to Reply 2 to Parentcomment 1 of Olivers Post 1', '2c210dd8-ff07-4adb-ad4c-67625ae7c295', '78188b10-4733-4fca-8dfd-e07aee389383');

INSERT INTO post_table_comment_table (post_id, comments_id) VALUES ('55a7409f-c0eb-453e-b7ef-89f905963ce9', '9ac4c6fd-68e1-4b41-97fd-817284290411');
INSERT INTO post_table_comment_table (post_id, comments_id) VALUES ('959b81a0-35b3-4f9d-a08b-4879173a1e2c', 'ee774407-a71f-4d4e-b829-f26d38e2ee81');
