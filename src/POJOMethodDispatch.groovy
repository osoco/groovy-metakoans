import org.codehaus.groovy.runtime.HandleMetaClass
import org.junit.Before
import org.junit.Test

class POJOMethodDispatch extends MetaKoan {
    @Before
    void registerModifiedMetaClasses() {
       storeOriginalMetaClass(Velocipede)
    }

    @Test
    void 'method declared directly in the class is executed (no surprise :))'() {
        def velocipede = new Velocipede()

        assert velocipede./*koanify*/ring()/**/ == 'ring!'
    }

    @Test
    void 'method can be added to the POJOs metaclass and executed as if it was defined in the POJO'() {
        Velocipede.metaClass.win = { 'won!' }
        def velocipede = new Velocipede()

        assert velocipede./*koanify*/win()/**/ == 'won!'
    }
}
