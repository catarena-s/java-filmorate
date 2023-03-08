INSERT INTO genre(name)
SELECT name
FROM(
    SELECT 1 AS id,'Комедия' AS name
    UNION
    SELECT 2 AS id,'Драма' AS name
    UNION
    SELECT 3 AS id,'Мультфильм' AS name
    UNION
    SELECT 4 AS id,'Триллер' AS name
    UNION
    SELECT 5 AS id,'Документальный' AS name
    UNION
    SELECT 6 AS id,'Боевик' AS name
    ORDER BY id
    ) AS g
WHERE g.id NOT IN (SELECT genre_id FROM genre);

INSERT INTO rating (name, description)
SELECT name, description
FROM (
    SELECT 1 AS id,'G' AS name,'нет возрастных ограничений' AS description
    UNION
    SELECT 2 AS id,'PG','детям рекомендуется смотреть фильм с родителями'
    UNION
    SELECT 3 AS id,'PG-13','детям до 13 лет просмотр не желателен'
    UNION
    SELECT 4 AS id,'R','лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
    UNION
    SELECT 5 AS id,'NC-17','лицам до 18 лет просмотр запрещён'
    ORDER BY id
    )AS r
WHERE r.id NOT IN (SELECT rating_id FROM rating);

