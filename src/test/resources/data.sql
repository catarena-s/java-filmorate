INSERT INTO user_info (name, login, email, birthday) VALUES ('est dolore','dolore','mail@yandex.ru','2001-05-05');
INSERT INTO user_info (name, login, email, birthday) VALUES ('user2','user2','mail2@yandex.ru','1976-09-20');
INSERT INTO user_info (name, login, email, birthday) VALUES ('user3','user3','mail3@yandex.ru','1999-10-02');
INSERT INTO user_info (name, login, email, birthday) VALUES ('user4','user4','mail4@yandex.ru','2005-11-02');

INSERT INTO film (name,description,duration,release_date,rating_id) VALUES
	 ('Один дома','Мальчик-озорник задает жару грабителям',103,'1990-11-10',2),
	 ('Аватар','АВАТАР – – Действие «Аватара» разворачивается на далекой планете Пандора',162,'2009-12-17',3),
	 ('Шрэк','Анимационный мультфильм DreamWorks, выпущенный 18 мая 2001 года, рассказывающий о нелюдимом огре по имени Шрек, который выходит из границ своего мира и тем образом счастье и любовь',90,'2001-10-31',2),
	 ('Законопослушный гражданин','Добропорядочный житель Филадельфии Клайд Шелтон мстит за гибель семьи в результате бандитского нападения',108,'2009-11-04',4);


INSERT INTO film_genre VALUES(1, 1);
INSERT INTO film_genre VALUES(3, 1);
INSERT INTO film_genre VALUES(3, 3);
INSERT INTO film_genre VALUES(2, 2);
INSERT INTO film_genre VALUES(4, 2);
INSERT INTO film_genre VALUES(4, 4);
INSERT INTO film_genre VALUES(4, 6);

INSERT INTO friendship (user_id,friend_id) VALUES (1,2);
INSERT INTO friendship (user_id,friend_id) VALUES (2,1);
INSERT INTO friendship (user_id,friend_id) VALUES (1,3);
INSERT INTO friendship (user_id,friend_id) VALUES (4,1);
INSERT INTO friendship (user_id,friend_id) VALUES (4,2);
INSERT INTO friendship (user_id,friend_id) VALUES (3,2);

INSERT INTO likes VALUES(1, 1);
INSERT INTO likes VALUES(1, 2);
INSERT INTO likes VALUES(1, 4);
INSERT INTO likes VALUES(2, 1);
INSERT INTO likes VALUES(4, 3);
INSERT INTO likes VALUES(4, 4);
INSERT INTO likes VALUES(3, 1);
INSERT INTO likes VALUES(3, 2);
INSERT INTO likes VALUES(3, 4);
INSERT INTO likes VALUES(3, 3);