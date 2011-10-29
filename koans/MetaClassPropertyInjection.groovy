import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class MetaClassPropertyInjection extends MetaKoan {
    @Before
    void registerModifiedMetaClasses() {
        storeOriginalMetaClass(Integer)
        storeOriginalMetaClass(String)
    }

    @After
    void restoreOriginalMetaClasses() {
        GroovySystem.metaClassRegistry.removeMetaClass(Date)
        super.restoreOriginalMetaClasses()
    }

    @Test
    void 'a new property can be added through the metaclass' () {
        String.metaClass.zero = '0'

        assert 'a string'.zero == /*koanify*/'0'/**/
        assert 'another string'.zero == /*koanify*/'0'/**/

        'a string'.zero = '1'
        assert 'a string'.zero == /*koanify*/'1'/**/
        assert 'another string'.zero == /*koanify*/'0'/**/
    }

    @Test
    void 'if a property is added to the default metaclass it is automatically replaced by ExpandoMetaClass' () {
        assert Date.metaClass instanceof ExpandoMetaClass == /*koanify*/false/**/

        Date.metaClass.daysInYear = 365

        assert Date.metaClass instanceof ExpandoMetaClass == /*koanify*/true/**/
    }

    @Test
    void 'a static attribute must be added as a property through the metaclass' () {
        String.metaClass.static.zero = '0'

        /*koanify_as_should_fail_or_not*/shouldFail/**/(MissingPropertyException) {
            String.zero
        }

        String.metaClass.static./*koanify*/getZero/**/ = { '0' }
        shouldNotFail(MissingPropertyException) {
            String.zero
        }

        /*koanify_as_should_fail_or_not*/shouldFail/**/(ReadOnlyPropertyException) {
            String.zero = '1'
        }
    }

    @Test
    void 'property can be added only to a specific object instance' () {
        def str = 'Marcin'
        str.metaClass.zero = '0'

        /*koanify_as_should_fail_or_not*/shouldNotFail/**/(MissingPropertyException) {
            str.zero
        }
        /*koanify_as_should_fail_or_not*/shouldFail/**/(MissingPropertyException) {
            'Gryszko'.zero
        }
    }
}
