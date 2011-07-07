import org.junit.Test
import static org.junit.Assert.assertThat
import static org.junit.matchers.JUnitMatchers.containsString

class SecondKoan {
    @Test
    void "second koan, first question"() {
        assertThat('koans are the way to enlightment', containsString('enlightment'))
    }
}
