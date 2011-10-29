import org.junit.Test

class Tutorial extends MetaKoan {
    @Test
    void 'substitute __ with a value or method that makes the koan pass'() {
        // Hint: the string is empty
        assert ''.empty == /*koanify*/true/**/
        // Hint: use length() method
        assert '123'./*koanify*/length/**/() == 3
    }

    @Test
    void 'substitute FillMeIn with a class name required to pass'() {
        assert 'a string' instanceof /*koanify_as_class*/String/**/
    }

    @Test
    void 'substitute __should_this_block_fail_or_not__ with shouldFail or shouldNotFail depending on the exception and the code in the block'() {
        // Hint: 0 / 0 is not supported in Groovy, so this block shouldFail
        /*koanify_as_should_fail_or_not*/shouldFail/**/(ArithmeticException) {
            0 / 0
        }
        // Hint: 1 / 2 is fine, so this block shouldNotFail
        /*koanify_as_should_fail_or_not*/shouldNotFail/**/(ArithmeticException) {
            1 / 2
        }
    }
}
