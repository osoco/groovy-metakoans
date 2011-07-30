import org.codehaus.groovy.runtime.HandleMetaClass
import org.junit.Test

class ToBeClassified {

    @Test
    void 'GroovyInterceptable extends from GroovyObject is a marker interface and intercepts every method call to invoke method'() {
        assert GroovyObject.isAssignableFrom(/*koanify*/GroovyInterceptable/**/)
    }

    @Test
    void 'every POJO has a metaclass too'() {
        def velocipede = new Velocipede()

        assert velocipede.metaClass instanceof /*koanify*/MetaClass/**/
        assert velocipede.metaClass.class == /*koanify*/HandleMetaClass/**/
    }

    @Test
    void 'every metaclass implements MetaObjectProtocol (MOP)'()  {
        assert MetaObjectProtocol.isAssignableFrom(/*koanify*/MetaClass/**/)
    }
}
