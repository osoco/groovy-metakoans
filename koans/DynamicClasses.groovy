import org.junit.Test

class DynamicClasses {
    @Test
    void 'dynamic classes can be created with Expando'() {
        def bike = new Expando(gears: 24, ring: { times = 1 -> 'ring!' * times })

        assert bike./*koanify*/gears/**/ == 24
        assert bike./*koanify*/ring()/**/ == 'ring!'

        bike.frameSize = 56

        assert bike./*koanify*/frameSize/**/ == 56
    }

    @Test
    void 'dynamic classes can be created with maps'() {
        def bike = [gears: 24, ring: { times = 1 -> 'ring!' * times }]

        assert bike./*koanify*/gears/**/ == 24
        assert bike./*koanify*/ring()/**/ == 'ring!'

        bike.frameSize = 56

        assert bike./*koanify*/frameSize/**/ == 56
    }
}
