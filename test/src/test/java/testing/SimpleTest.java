package testing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleTest {

    @Test
    public void test() {
        assertThat(StringShortener.shorten("foo!bar123", 7)).isEqualTo("foo!...");
    }

}
