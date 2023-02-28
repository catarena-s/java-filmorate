INSERT INTO user_info (name, login, email, birthday) VALUES ('est dolore','dolore','mail@yandex.ru','2001-05-05');
INSERT INTO user_info (name, login, email, birthday) VALUES ('user2','user2','mail2@yandex.ru','1976-09-20');
INSERT INTO user_info (name, login, email, birthday) VALUES ('user3','user3','mail3@yandex.ru','1999-10-02');

INSERT INTO film (name,description,duration,release_date,rating_id) VALUES
	 ('Один дома','Мальчик-озорник задает жару грабителям.',103,'1990-11-10',2),
	 ('Аватар','АВАТАР – – Действие «Аватара» разворачивается на далекой планете Пандора',162,'2009-12-17',3);


INSERT INTO film_genre VALUES(1, 1);
INSERT INTO film_genre VALUES(2, 2);

INSERT INTO friendship (user_id,friend_id) VALUES (1,2);
INSERT INTO friendship (user_id,friend_id) VALUES (2,1);
INSERT INTO friendship (user_id,friend_id) VALUES (1,3);
--INSERT INTO friendship (user_id,friend_id) VALUES (3,1);
INSERT INTO friendship (user_id,friend_id) VALUES (3,2);

INSERT INTO likes VALUES(1, 1);
INSERT INTO likes VALUES(1, 2);
INSERT INTO likes VALUES(2, 1);
