import org.codehaus.groovy.runtime.ScriptBytecodeAdapter
import org.junit.Test
import static org.junit.Assert.fail

class POGOMethodDispatch {

    private shouldFail = new GroovyTestCase().&shouldFail

    private shouldNeverFail(Class clazz, Closure code) {
        Throwable th = null;
        try {
            code.call();
        } catch (GroovyRuntimeException gre) {
            th = ScriptBytecodeAdapter.unwrap(gre);
        } catch (Throwable e) {
            th = e;
        }

        if (clazz.isInstance(th)) {
            fail("Closure " + code + " should have never failed with an exception of type " + clazz.getName());
        }
    }

    @Test
    void 'method declared directly in the class is executed (no surprise :))'() {
        def bike = new Bike()

        assert bike./*koanify*/ring()/**/ == 'ring!'
    }

    @Test
    void 'method can be added to the POGOs metaclass and executed as if it was defined in the POGO'() {
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
        def bike = new BikeWithMethodMissing()

        assert bike./*koanify*/ringLoudly()/**/ == 'ring loudly!'
    }

    @Test
    void 'methodMissing is implemented but it throws a MissingMethodException if it cannot handle the unknown method'() {
        def bike = new BikeWithMethodMissing()

        shouldFail(/*koanify*/MissingMethodException/**/) { bike.bell() }
        // Think: must methodMissing throw a MissingMethodException?
        // What would be the result if the methodMissing implementation hadn't thrown MissingMethodException?
    }

    @Test
    void 'invokeMethod is overridden and executed if the called method is not found'() {
        def bike = new BikeWithInvokeMethod()

        assert bike./*koanify*/rideOnTheMoon()/**/ == 'riding onthemoon!'
    }

    @Test
    void 'invokeMethod is overridden but it should throw a MissingMethodException if it cannot handle the unknown method'() {
        def bike = new BikeWithInvokeMethod()

        shouldFail(/*koanify*/MissingMethodException/**/) { bike.beRover() }
    }

    @Test
    void 'invokeMethod is never called if the method is implemented directly or in the metaclass'() {
        def bike = new BikeWithMethodMissingThrowingEx()
        bike.metaClass.win = { 'won!' }

        shouldFail(/*koanify*/BikeBrokenException/**/) { bike.beRover() }
        shouldNeverFail(/*koanify*/BikeBrokenException/**/) {
            bike.ring()
            bike.win()
        }
    }

    @Test
    void 'only methodMissing is executed if both methodMissing and invokeMethod are implemented'() {
        def bike = new BikeWithMethodMissingAndInvokeMethod()

        assert bike./*koanify*/ringLoud()/**/ == 'ring loud!'
        shouldFail(/*koanify*/MissingMethodException/**/) {
            bike.rideOnTheMoon()
        }
    }

    // TODO Koan - method overriden in the metaclass is called first - or implement it in the MethonSynthesis koan?
    // TODO Koan - vide, invokeMethod implemented on the metaClass is called first
}
