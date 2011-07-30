import org.junit.Test
import org.junit.Assert

class POGOMethodDispatch {

    private shouldFail = new GroovyTestCase().&shouldFail

    @Test
    void 'method declared directly in the class is executed (no surprise :))'() {
        def bike = new Bike()

        assert bike./*koanify*/ring()/**/ == 'ring!'
    }

    @Test
    void 'method can be added to the POGOs metaclass and executed as if were defined in the POGO'() {
        def bike = new Bike()
        bike.metaClass.win = { 'won!' }

        assert bike./*koanify*/win()/**/ == 'won!'
    }

    @Test
    void 'property that is a closure is executed like a normal method'() {
        def bike = new Bike()

        assert bike./*koanify*/ride()/**/ == 'riding!'
    }

    @Test
    void 'MissingMethodException is raised if a method is not found neither in the class nor in the metaclass'() {
        def bike = new Bike()

        shouldFail(/*koanify*/MissingMethodException/**/) { bike.consumePetrol() }
    }

    @Test
    void 'methodMissing is implemented and executed if the called method is not found'() {
        def bike = new RingingBike()

        assert bike./*koanify*/ringLoudly()/**/ == 'ring loudly!'
    }

    @Test
    void 'methodMissing is implemented but it throws a MissingMethodException if it cannot handle the unknown method'() {
        def bike = new RingingBike()

        shouldFail(/*koanify*/MissingMethodException/**/) { bike.bell() }
    }

    @Test
    void 'invokeMethod is implemented and executed if the called method is not found'() {
        def bike = new VersatileBike()

        assert bike./*koanify*/rideOnTheMoon()/**/ == 'riding onthemoon!'
    }

    @Test
    void 'invokeMethod is implemented but it throws a MissingMethodException if it cannot handle the unknown method'() {
        def bike = new VersatileBike()

        shouldFail(/*koanify*/MissingMethodException/**/) { bike.beRover() }
    }

    @Test
    void 'invokeMethod is never called if the method is implemented directly or in the metaclass'() {
        def bike = new BrittleBike()
        bike.metaClass.win = { 'won!' }

        shouldFail(/*koanify*/BikeBrokenException/**/) { bike.beRover() }
        try {
            bike.ring()
            bike.win()
        } catch (/*koanify*/BikeBrokenException/**/ e) {
            Assert.fail('Bike broken...')
        }
    }

    @Test
    void 'methodMissing takes precedence if both methodMissing and invokeMethod are implemented'() {
        def bike = new HybridBike()

        assert bike./*koanify*/ringLoud()/**/ == 'ring loud!'
        shouldFail(/*koanify*/MissingMethodException/**/) {
            bike.rideOnTheMoon()
        }
    }
}
