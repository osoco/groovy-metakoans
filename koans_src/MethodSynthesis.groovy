import org.junit.Before
import org.junit.Test
import support.Bike

class BikeWithCountingMethodMissing {
    def numberOfCreatedMethods = 0

    def methodMissing(String name, args) {
        if (name.startsWith('goTo')) {
            numberOfCreatedMethods++
            def impl = {
                "going to ${name.substring('goTo'.length())}"
            }
            BikeWithCountingMethodMissing.metaClass./*koanify*/"${name}"/**/ = impl
            return impl()
        }

        throw new MissingMethodException(name, Bike, args);
    }
}

class MethodSynthesis extends MetaKoan {
    @Before
    void registerModifiedMetaClasses() {
        storeOriginalMetaClass(Bike)
    }

    def bikeMethodMissing = { String name, args ->
        if (name.startsWith('goTo')) {
            return "going to ${name.substring('goTo'.length())}"
        }

        throw new MissingMethodException(name, Bike, args);
    }

    @Test
    void 'you can create methods on the fly using methodMissing'() {
        Bike.metaClass.methodMissing = bikeMethodMissing

        def bike = new Bike()
        /*koanify*/shouldFail/**/(MissingMethodException) {
            bike.goingToMadrid()
        }

        /*koanify*/shouldNeverFail/**/(MissingMethodException) {
            bike.goToMadrid()
        }
    }

    @Test
    void 'you can create methods on the fly by substituting the metaclass and passing a prepared ExpandoMetaClass'() {
        def emc = new ExpandoMetaClass(Bike)
        emc.methodMissing = bikeMethodMissing
        // Don't forget to initialize ExpandoMetaClass after adding methods!
        emc.initialize()

        def bike = new Bike()
        def bike2 = new Bike()
        bike.metaClass = emc

        /*koanify*/shouldNeverFail/**/(MissingMethodException) {
            bike.goToMadrid()
        }
        /*koanify*/shouldFail/**/(MissingMethodException) {
            bike2.goToMadrid()
        }
    }

    @Test
    void 'caching of newly created methods with methodMissing improves performance'() {
        def numberOfCreatedMethods = 0

        Bike.metaClass.methodMissing = { String name, args ->
            if (name.startsWith('goTo')) {
                numberOfCreatedMethods++
                def impl = {
                    "going to ${name.substring('goTo'.length())} with ${delegate.gears} gears"
                }
                Bike.metaClass./*koanify*/"${name}"/**/ = impl
                return impl()
            }

            throw new MissingMethodException(name, Bike, args);
        }
        def bike = new Bike()

        assert bike.goToMadrid() == 'going to Madrid with 24 gears'
        bike.goToMadrid()
        bike.goToWarsaw()

        assert numberOfCreatedMethods == /*koanify*/2/**/
    }

    @Test
    void 'caching works only if the metaclass is an instance of ExpandoMetaClass'() {
        def bike = new BikeWithCountingMethodMissing()

        // What is the 'global' metaclass of BikeWithCountingMethodMissing?
        assert BikeWithCountingMethodMissing.metaClass instanceof ExpandoMetaClass == /*koanify*/false/**/

        bike.goToMadrid()
        bike.goToMadrid()

        // Did the metaclass of BikeWithCountingMethodMissing change?
        assert BikeWithCountingMethodMissing.metaClass instanceof ExpandoMetaClass == /*koanify*/true/**/
        assert bike.numberOfCreatedMethods == /*koanify*/2/**/

        def anotherBike = new BikeWithCountingMethodMissing()
        anotherBike.goToMadrid()
        anotherBike.goToMadrid()
        assert anotherBike.numberOfCreatedMethods == /*koanify*/0/**/

        // Think: what type of metaclas enables method caching?
        // Do you know why we got this number of created methods for anotherBike?
    }
}
