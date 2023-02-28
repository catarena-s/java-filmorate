package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString(callSuper = true)
public class Film extends FilmorateObject {
    public static final int MAX_DESCRIPTION_SIZE = 200;

    @NotBlank(message = "Отсутствует название")
    private String name;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    @Size(max = MAX_DESCRIPTION_SIZE,
            message = "Длинна описания не должна быть больше " + MAX_DESCRIPTION_SIZE + " символов")
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;//  дата релиза

    private Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
    private RatingMPA mpa;
    private Set<Long> likes = new HashSet<>();//список лайков(id пользователей поставивших лайк)

    @Builder(toBuilder = true)
    public Film(long id, String name, int duration, String description, LocalDate releaseDate,
                RatingMPA mpa, Set<Genre> genres, Set<Long> likes) {
        super(id);
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.releaseDate = releaseDate;
        if (mpa != null) this.mpa = mpa;
        if (genres != null) this.genres.addAll(genres);
        if (likes != null) this.likes.addAll(likes);
    }

    @JsonIgnore
    public int getCountLikes() {
        return likes.size();
    }

    public void addLike(long userId) {
        likes.add(userId);
    }

    public Film updateData(Film obj) {
        setName(obj.getName());
        setDescription(obj.getDescription());
        setDuration(obj.getDuration());
        setReleaseDate(obj.getReleaseDate());
        return this;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("duration", duration);
        values.put("release_date", releaseDate);
        values.put("rating_id", mpa.getId());
        return values;
    }
}
