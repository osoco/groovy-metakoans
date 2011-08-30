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
            def answer = delegate.toString()
            if (delegate % 3 == 0) answer = 'Fizz'
            if (delegate % 5 == 0) answer = 'Buzz'
            answer
        }

        assert 1.fizzBuzz() == /*koanify*/'1'/**/
        assert 3.fizzBuzz() == /*koanify*/'Fizz'/**/
        assert 5.fizzBuzz() == /*koanify*/'Buzz'/**/
    }

    @Test
    void 'delegate in a method added to the metaclass refers to the current instance'() {
        Integer.metaClass.delegate = {
            delegate
        }

        assert 1.delegate() == /**koanify*/1/**/
    }

    @Test
    void 'owner in a method added to the metaclass refers to the instance where the closure was declared'() {
        Integer.metaClass.owner = {
            owner
        }

        assert 1.owner() == /**koanify*/this/**/
    }

    @Test
    void 'an existing instance method can be replaced by overriding it in the metaclass' () {
        assert 2.power(2) == /*koanify*/4/**/

        Integer.metaClass.power = { Integer exponent ->
            /*koanify*/delegate/**/ // Hint: return the number
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
    void 'a new static method can be added through the metaclass' () {
        Double.metaClass.static.floor = { n -> /*koanify*/Math.floor(n)/**/ } // Hint: use java.lang.Math

        assert Double.floor(Math.PI) == 3
    }

    @Test
    void 'method can be added to a specific object instance' () {
        1.metaClass.isEven = { delegate % 2 == 0 }

        assert 1.isEven() == /*koanify*/false/**/
        shouldFail(/*koanify_as_ex*/MissingMethodException/**/) {
            2.isEven()
        }

        Integer.metaClass.isEven = { delegate % 2 == 0 }
        assert 1.isEven() == /*koanify*/false/**/
        assert 2.isEven() == /*koanify*/true/**/
    }

    @Test
    void 'a new constructor can be added through the metaclass' () {
        String.metaClass.constructor << { int n ->
            /*koanify*/new String(n.toString());/**/
        }

        assert new String(1) == '1'
    }

    @Test
    void 'an existing constructor can be replaced by overriding it in the metaclass (yes it is possible in Groovy)' () {
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
        /*koanify_as_should_fail_or_not*/shouldFail/**/(MissingMethodException) {
            1L.isEven()
        }

        /*koanify*/Number/**/.metaClass.isEven = { delegate % 2 == 0 }

        /*koanify_as_should_fail_or_not*/shouldNeverFail/**/(MissingMethodException) {
            2L.isEven()
            1.0D.isEven()
        }
    }

    @Test
    void 'a new instance method can be added to an interface so it is available in all implementors' () {
        List.metaClass.swap = { pos1, pos2 ->
            def elem = delegate[pos1]
            delegate[pos1] = delegate[pos2]
            delegate[pos2] = elem
        }

        /*koanify_as_should_fail_or_not*/shouldNeverFail/**/(MissingMethodException) {
            [1, 2].swap(0, 1)
        }

        /*koanify_as_should_fail_or_not*/shouldNeverFail/**/(MissingMethodException) {
            new LinkedList([0, 1]).swap(0, 1)
        }
    }
}
