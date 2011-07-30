import org.codehaus.groovy.runtime.HandleMetaClass
import org.junit.Test

class POGOIntro {
    @Test
    void 'any object defined in Groovy (POGO) implements implicitly GroovyObject interface'() {
        setup:
        def bike = new Bike()

        assert bike instanceof Object
        assert bike instanceof /*koanify*/GroovyObject/**/
    }

    @Test
    void 'you can invoke any method on a POGO using the GroovyObject interface'() {
        def bike = new Bike()

        assert bike.invokeMethod(/*koanify*/'ring'/**/, /*koanify*/3/**/) == 'ring!ring!ring!'
    }

    @Test
    void 'you can get or set any POGO property using the GroovyObject interface'() {
        def bike = new Bike()

        assert bike.getProperty(/*koanify*/'gears'/**/) == 24
        bike.setProperty(/*koanify*/'gears'/**/, 18)
        assert bike.getProperty(/*koanify*/'gears'/**/) == 18
    }

    @Test
    void 'every POGO has a metaclass with a default implementation'() {
        def bike = new Bike()

        assert bike.metaClass instanceof /*koanify*/MetaClass/**/
        assert bike.metaClass.class == /*koanify*/HandleMetaClass/**/
    }
}
