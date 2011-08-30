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
        /*koanify_as_should_fail_or_not*/shouldFail/**/(MissingMethodException) {
            bike.goingToMadrid()
        }

        /*koanify_as_should_fail_or_not*/shouldNeverFail/**/(MissingMethodException) {
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

        /*koanify_as_should_fail_or_not*/shouldNeverFail/**/(MissingMethodException) {
            bike.goToMadrid()
        }
        /*koanify_as_should_fail_or_not*/shouldFail/**/(MissingMethodException) {
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
        bike.goToWarsaw() == 'going to Warsaw with 24 gears'
        bike.goToWarsaw()

        assert numberOfCreatedMethods == 2
    }

    @Test
    void 'caching works only if the metaclass is an instance of ExpandoMetaClass'() {
        // What is the 'global' metaclass of BikeWithCountingMethodMissing?
        assert BikeWithCountingMethodMissing.metaClass instanceof ExpandoMetaClass == /*koanify*/false/**/

        // Your task: cache generated methods in BikeWithCountingMethodMissing.methodMissing
        def bike = new BikeWithCountingMethodMissing()
        bike.goToMadrid()
        bike.goToMadrid()
        assert bike.numberOfCreatedMethods == /*koanify*/2/**/

        def anotherBike = new BikeWithCountingMethodMissing()
        anotherBike.goToMadrid()
        anotherBike.goToMadrid()
        assert anotherBike.numberOfCreatedMethods == /*koanify*/0/**/

        // Did the metaclass of BikeWithCountingMethodMissing change?
        assert BikeWithCountingMethodMissing.metaClass instanceof ExpandoMetaClass == /*koanify*/true/**/

        // Think: what type of metaclas enables method caching?
        // Do you know why we got this number of created methods for anotherBike?
    }
}
