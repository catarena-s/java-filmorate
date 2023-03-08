package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RatingMPAStorageTest {
    private final RatingMPAStorage mpaStorage;
    @Test
    @DisplayName("RatingMPAStorage - getById")
    void getById() {
        Optional<RatingMPA> ratingMPAOptional = Optional.ofNullable(mpaStorage.getById(1L));
        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        {
                            assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                        }
                );
    }

    @Test
    @DisplayName("RatingMPAStorage - getAll")
    void getAllRatingMPA() {
        Optional<Collection<RatingMPA>> ratingMPAOptional = Optional.ofNullable(mpaStorage.getAll());
        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                {
                    assertThat(mpa).isNotEmpty();
                    assertThat(mpa).hasSize(5);
                    assertThat(mpa).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(mpa).element(0).hasFieldOrPropertyWithValue("name", "G");
                    assertThat(mpa).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(mpa).element(1).hasFieldOrPropertyWithValue("name", "PG");
                    assertThat(mpa).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(mpa).element(2).hasFieldOrPropertyWithValue("name", "PG-13");
                    assertThat(mpa).element(3).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(mpa).element(3).hasFieldOrPropertyWithValue("name", "R");
                    assertThat(mpa).element(4).hasFieldOrPropertyWithValue("id", 5L);
                    assertThat(mpa).element(4).hasFieldOrPropertyWithValue("name", "NC-17");
                });
    }
}