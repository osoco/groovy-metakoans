import org.junit.Test
import static org.junit.Assert.assertThat
import static org.junit.matchers.JUnitMatchers.containsString

class FirstKoan {

    @Test
    void "first koan, first question"() {
        assertThat('koans are the way to enlightment', containsString('enlightment'))
    }

    @Test
    void "first koan, second question"() {
        assertThat('koans are the way to enlightment', containsString('enlightment'))
    }
}
