import org.codehaus.groovy.runtime.HandleMetaClass
import org.junit.Test

class POJOIntro extends MetaKoan {
    @Test
    void 'every POJO has a metaclass too'() {
        def velocipede = new Velocipede()

        assert velocipede.metaClass instanceof /*koanify*/MetaClass/**/
        assert velocipede.metaClass.class == /*koanify*/HandleMetaClass/**/
    }

    @Test
    void 'POJO methods can be invoked dynamically'() {
        setup:
        def velocipede = new Velocipede()
        def method = /*koanify*/"ring"/**/

        expect:
        velocipede."${method}"() == 'ring!'
    }

    @Test
    void 'POJO properties can be accessed dynamically with dot or map notation'() {
        setup:
        def velocipede = new Velocipede()
        def property = /*koanify*/"gears"/**/

        expect:
        velocipede."${property}" == 1
        velocipede["${property}"] == 1
    }
}
