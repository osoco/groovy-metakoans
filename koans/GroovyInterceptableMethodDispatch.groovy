import org.junit.Before
import org.junit.Test
import support.InterceptableBike

class GroovyInterceptableMethodDispatch extends MetaKoan {
    @Before
    void registerModifiedMetaClasses() {
        storeOriginalMetaClass(InterceptableBike)
    }

    @Test
    void 'GroovyInterceptable is a special case of GroovyObject'() {
        assert /*koanify_as_class*/GroovyObject/**/ in GroovyInterceptable.class.interfaces
    }

    @Test
    void 'Groovy interceptor always delegates all method calls to invokeMethod'() {
        InterceptableBike.metaClass.win = { 'won!' }
        def bike = new InterceptableBike()

        assert bike.ring() == /*koanify*/"won't do anything"/**/
        assert bike.ride() == /*koanify*/"won't do anything"/**/
        assert bike.win() == /*koanify*/"won't do anything"/**/
    }
}
