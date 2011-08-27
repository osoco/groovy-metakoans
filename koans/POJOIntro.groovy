import org.codehaus.groovy.runtime.HandleMetaClass
import org.junit.Test
import support.Velocipede

class POJOIntro extends MetaKoan {
    @Test
    void 'every POJO has a metaclass too'() {
        def velocipede = new Velocipede()

        assert velocipede.metaClass instanceof /*koanify_as_class*/MetaClass/**/
        assert velocipede.metaClass.class == /*koanify_as_class*/HandleMetaClass/**/
    }

    @Test
    void 'POJO methods can be invoked dynamically'() {
        def velocipede = new Velocipede()
        def method = /*koanify*/"ring"/**/

        velocipede."${method}"() == 'ring!'
    }

    @Test
    void 'POJO properties can be accessed dynamically with dot or map notation'() {
        def velocipede = new Velocipede()
        def property = /*koanify*/"gears"/**/

        velocipede."${property}" == 1
        velocipede["${property}"] == 1
    }
}
