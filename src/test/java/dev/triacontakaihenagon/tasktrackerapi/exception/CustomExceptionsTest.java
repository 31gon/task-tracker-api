package dev.triacontakaihenagon.tasktrackerapi.exception;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CustomExceptionsTest {

    static Stream<Function<String, ? extends RuntimeException>> constructors() {
        return Stream.of(
                TaskNotFoundException::new,
                UserNotFoundException::new,
                CategoryNotFoundException::new,
                LabelNotFoundException::new
        );
    }

    @ParameterizedTest
    @MethodSource("constructors")
    void getMessage_returnsConstructorArgument(Function<String, ? extends RuntimeException> ctor) {
        RuntimeException ex = ctor.apply("not found");

        assertThat(ex.getMessage()).isEqualTo("not found");
    }
}