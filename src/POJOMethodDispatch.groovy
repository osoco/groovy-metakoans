import org.codehaus.groovy.runtime.HandleMetaClass
import org.junit.Test

class POJOMethodDispatch {
    @Test
    void 'every POJO has a metaclass too'() {
        def velocipede = new Velocipede()

        assert velocipede.metaClass instanceof /*koanify*/MetaClass/**/
        assert velocipede.metaClass.class == /*koanify*/HandleMetaClass/**/
    }

    @Test
    void 'method declared directly in the class is executed (no surprise :))'() {
        def velocipede = new Velocipede()

        assert velocipede./*koanify*/ring()/**/ == 'ring!'
    }

    @Test
    void 'method can be added to the POJOs metaclass and executed as if it was defined in the POJO'() {
        def velocipede = new Velocipede()
        velocipede.metaClass.win = { 'won!' }

        assert velocipede./*koanify*/win()/**/ == 'won!'
    }
}
