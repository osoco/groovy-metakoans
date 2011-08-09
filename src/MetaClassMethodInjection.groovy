import org.junit.After
import org.junit.Before
import org.junit.Test

class MetaClassMethodInjection extends MetaKoan {
    @Before
    void registerModifiedMetaClasses() {
        storeOriginalMetaClass(Integer)
        storeOriginalMetaClass(String)
    }

    @After
    void restoreOriginalMetaClasses() {
        GroovySystem.metaClassRegistry.removeMetaClass(Date)
        super.restoreOriginalMetaClasses()
    }

    @Test
    void 'a new instance method can be added through the metaclass' () {
        Integer.metaClass.fizzBuzz = {
            def answer = ''
            if (delegate % 3 == 0) answer = 'Fizz'
            if (delegate % 5 == 0) answer += 'Buzz'
            answer
        }

        assert 1.fizzBuzz() == /*koanify*/''/**/
        assert 3.fizzBuzz() == /*koanify*/'Fizz'/**/
        assert 5.fizzBuzz() == /*koanify*/'Buzz'/**/
        assert 15.fizzBuzz() == /*koanify*/'FizzBuzz'/**/
    }

    @Test
    void 'an existing instance method can be replaced by overriding it in the metaclass' () {
        assert 2.power(2) == /*koanify*/4/**/

        Integer.metaClass.power = { Integer exponent ->
            /*koanify*/delegate/**/
        }

        assert 4.power(15) == 4
        // Hint: the closure signature must be exactly the same as of the overridden method
    }

    @Test
    void 'an existing instance method can be overloaded by adding an additional method to the metaclass' () {
        String.metaClass.tokenize << { String delimiter, int maxTokens ->
            /*koanify*/delegate.tokenize(delimiter)[0..maxTokens - 1]/**/
        }

        assert '1 2 3 4 5'.tokenize(' ') == ['1', '2', '3', '4', '5']
        assert '1 2 3 4 5'.tokenize(' ', 3) == ['1', '2', '3']
    }

    @Test
    void 'if a method is added to the default metaclass it is automatically replaced by ExpandoMetaClass' () {
        assert Date.metaClass instanceof ExpandoMetaClass == /*koanify*/false/**/

        Date.metaClass.aMethod = {}

        assert Date.metaClass instanceof ExpandoMetaClass == /*koanify*/true/**/
    }

    @Test
    void 'a new class (static) method can be added through the metaclass' () {
        Double.metaClass.static.floor = { n -> /*koanify*/Math.floor(n)/**/ } // Hint: use java.lang.Math

        assert Double.floor(Math.PI) == 3
    }

    @Test
    void 'a new property can be added through the metaclass' () {
        String.metaClass.zero = '0'

        assert 'a string'.zero == /*koanify*/'0'/**/
        assert 'another string'.zero == /*koanify*/'0'/**/

        'a string'.zero = '1'
        assert 'a string'.zero == /*koanify*/'1'/**/
        assert 'another string'.zero == /*koanify*/'0'/**/
    }

    @Test
    void 'if a property is added to the default metaclass it is automatically replaced by ExpandoMetaClass' () {
        assert Date.metaClass instanceof ExpandoMetaClass == /*koanify*/false/**/

        Date.metaClass.daysInYear = 365

        assert Date.metaClass instanceof ExpandoMetaClass == /*koanify*/true/**/
    }

    @Test
    void 'a class (static) attribute must be added as a property through the metaclass' () {
        String.metaClass.static.zero = '0'
        /*koanify*/shouldFail/**/(MissingPropertyException) {
            String.zero
        }

        String.metaClass.static./*koanify*/getZero/**/ = { '0' }
        /*koanify*/shouldNeverFail/**/(MissingPropertyException) {
            String.zero
        }

        /*koanify*/shouldFail/**/(ReadOnlyPropertyException) {
            String.zero = '1'
        }
    }

    @Test
    void 'method can be added to a specific object instance' () {
        1.metaClass.isEven = { delegate % 2 == 0 }

        assert 1.isEven() == /*koanify*/false/**/
        shouldFail(/*koanify*/MissingMethodException/**/) {
            2.isEven()
        }

        Integer.metaClass.isEven = { delegate % 2 == 0 }
        assert 1.isEven() == /*koanify*/false/**/
        assert 2.isEven() == /*koanify*/true/**/
    }

    @Test
    void 'property can be added only to a specific object instance' () {
        def str = 'Marcin'
        str.metaClass.zero = '0'

        /*koanify*/shouldNeverFail/**/(MissingPropertyException) {
            'str.zero'
        }
        /*koanify*/shouldFail/**/(MissingPropertyException) {
            'Gryszko'.zero
        }
    }

    @Test
    void 'a new constructor can be added through the metaclass' () {
        String.metaClass.constructor << { int n ->
            /*koanify*/new String(n.toString());/**/
        }

        assert new String(1) == '1'
    }

    @Test
    void 'an existing construct can be replaced by overriding it in the metaclass (yes it is possible in Groovy)' () {
        String.metaClass.constructor = { String s ->
            // You can still get a reference to the original constructor
            def originalConstructor = String.class.getConstructor(String)
            /*koanify*/originalConstructor.newInstance(s) * 2/**/
        }

        assert new String('source') == 'sourcesource'
    }

    @Test
    void 'a new instance method can be added to a complete class hierarchy' () {
        Integer.metaClass.isEven = { delegate % 2 == 0 }

        assert 1.isEven() == false
        /*koanify*/shouldFail/**/(MissingMethodException) {
            1L.isEven()
        }

        /*koanify*/Number/**/.metaClass.isEven = { delegate % 2 == 0 }

        2L.isEven() == /*koanify*/true/**/
    }

    @Test
    void 'a new instance method can be added to an interface so it is available in all implementors' () {
        List.metaClass.swap = { pos1, pos2 ->
            def elem = delegate[pos1]
            delegate[pos1] = delegate[pos2]
            delegate[pos2] = elem
        }

        /*koanify*/shouldNeverFail/**/(MissingMethodException) {
            [1, 2].swap(0, 1)
        }

        /*koanify*/shouldNeverFail/**/(MissingMethodException) {
            new LinkedList([0, 1]).swap(0, 1)
        }
    }

    @Test
    void 'you can substitute metaclass and pass a prepared ExpandoMetaClass'() {
        // TODO implement it
    }
}
