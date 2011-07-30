import org.junit.Test

class ToBeClassified {

    @Test
    void 'GroovyInterceptable extends from GroovyObject is a marker interface and intercepts every method call to invoke method'() {
        assert GroovyObject.isAssignableFrom(/*koanify*/GroovyInterceptable/**/)
    }

    @Test
    void 'every metaclass implements MetaObjectProtocol (MOP)'()  {
        assert MetaObjectProtocol.isAssignableFrom(/*koanify*/MetaClass/**/)
    }
}
