import org.junit.Test
import org.junit.BeforeClass

@Category(MetaKoan)
class DateOperations {
    Date today() {
        def today = new Date().toCalendar()
        today.clearTime()
        return today.time
    }
}

@Mixin(DateOperations)
class CompileTimeMixins extends MetaKoan {
    @Test
    void 'you add new behaviour to a class on the compile time with the Mixin transformation'() {
        def now = new Date()

        assert now >= /*koanify*/today()/**/
        assert now <= /*koanify*/today()/**/ + 1
        // Think: can you add today() method to Date class with the Mixin annotation?
    }
}

class RuntimeMixins extends MetaKoan {
    @BeforeClass
    static void mixinBehaviour() {
        RuntimeMixins.mixin(DateOperations)
    }

    @Test
    void 'you add new behaviour to a class on the compile time with the Mixin transformation'() {
        def now = new Date()

        assert now >= /*koanify*/today()/**/
        assert now <= /*koanify*/today()/**/ + 1
    }
}
