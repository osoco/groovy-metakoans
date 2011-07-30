import org.junit.Test

class GroovyInterceptableMethodDispatch {
    @Test
    void 'GroovyInterceptable is a special case of GroovyObject'() {
        assert GroovyObject.isAssignableFrom(/*koanify*/GroovyInterceptable/**/)
    }

    @Test
    void 'Groovy interceptor always delegates all method calls to invokeMethod'() {
        def bike = new InterceptableBike()
        bike.metaClass.win = { 'won!' }

        assert bike.ring() == /*koanify*/"won't do anything"/**/
        assert bike.ride() == /*koanify*/"won't do anything"/**/
        assert bike.win() == /*koanify*/"won't do anything"/**/
    }
}
