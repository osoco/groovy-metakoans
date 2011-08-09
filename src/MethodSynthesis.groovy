import org.junit.Before
import org.junit.Test

class MethodSynthesis extends MetaKoan {
    @Before
    void registerModifiedMetaClasses() {
        storeOriginalMetaClass(Bike)
    }

    @Test
    void 'you can create methods on the fly using methodMissing'() {
        Bike.metaClass.methodMissing = { String name, args ->
            if (name.startsWith('goTo')) {
                return "going to ${name.substring('goTo'.length())}"
            }

            throw new MissingMethodException(name, Bike, args);
        }

        def bike = new Bike()
        /*koanify*/shouldFail/**/(MissingMethodException) {
            bike.goingToMadrid()
        }

        /*koanify*/shouldNeverFail/**/(MissingMethodException) {
            assert bike.goToMadrid() == 'a'
        }
    }

    @Test
    void 'caching of newly created methods with methodMissing improves performance'() {
        def numberOfCreatedMethods = 0

        Bike.metaClass.methodMissing = { String name, args ->
            if (name.startsWith('goTo')) {
                numberOfCreatedMethods++
                def impl = {
                    "going to ${name.substring('goTo'.length())}"
                }
                Bike.metaClass./*koanify*/"${name}"/**/ = impl
                return impl()
            }

            throw new MissingMethodException(name, Bike, args);
        }
        def bike = new Bike()

        bike.goToMadrid()
        bike.goToMadrid()
        bike.goToWarsaw()

        assert numberOfCreatedMethods == /*koanify*/2/**/
    }
}
