import org.junit.Before
import org.junit.Test

class POGOPropertyAccess extends MetaKoan {

    @Before
    void registerModifiedMetaClasses() {
        storeOriginalMetaClass(Bike)
    }

    @Test
    void 'property declared directly in the class is accessed (no surprise :))'() {
        def bike = new Bike()

        assert bike./*koanify*/gears/**/ == 24
    }

    @Test
    void 'property can be added to the POGOs instance metaclass and accessed as if it was defined in the POGO'() {
        def bike = new Bike()
        bike.metaClass.vendor = 'Orbea'

        assert bike./*koanify*/vendor/**/ == 'Orbea'
    }

    @Test
    void 'if a property is added to the global metaclass, all instances share this property (as if it was static)'() {
        storeOriginalMetaClass(Bike)

        Bike.metaClass.vendor = 'Orbea'
        def bike = new Bike()
        def anotherBike = new Bike()

        assert bike.vendor == /*koanify*/'Orbea'/**/
        assert anotherBike.vendor == /*koanify*/'Orbea'/**/
    }

    @Test
    void 'MissingPropertyException is raised if a property is not found neither in the class nor in the metaclass'() {
        def bike = new Bike()

        shouldFail(/*koanify*/MissingPropertyException/**/) { bike.frameSize }
    }

    @Test
    void 'propertyMissing is implemented and executed if the accessed property is not found'() {
        def bike = new BikeWithPropertyMissing()

        assert bike./*koanify*/veryLongProperty/**/ == 'veryLongProperty'
    }

    @Test
    void 'propertyMissing is implemented but it should throw a MissingPropertyException if it cannot generate the property'() {
        def bike = new BikeWithPropertyMissing()

        shouldFail(/*koanify*/MissingPropertyException/**/) { bike.i }
        // Think: must propertyMissing throw a MissingPropertyException?
        // What would be the result if the propertyMissing implementation hadn't thrown MissingPropertyException?
    }

    @Test
    void 'getProperty and setProperty are overridden and executed if the accessed property is not found'() {
        def bike = new BikeWithGetSetProperty()

        bike.spokes = 24
        assert bike./*koanify*/spokes/**/ == 24
    }

    @Test
    void 'getProperty and setProperty are overridden but they throw a MissingPropertyException if they cannot handle the unknown property'() {
        def bike = new BikeWithGetSetProperty()

        shouldFail(/*koanify*/MissingPropertyException/**/) { bike.horsepower }
        shouldFail(/*koanify*/MissingPropertyException/**/) { bike.horsepower = 170 }
    }

    @Test
    void 'propertyMissing takes precedence over getProperty is the latter is overridden'() {
        def bike = new BikeWithPropertyMissingAndGetSetProperty()

        bike.spokes = 24
        assert bike.spokes == /*koanify*/24/**/
        assert bike.veryLongProperty == /*koanify*/'veryLongProperty'/**/
    }
}
