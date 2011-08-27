import org.junit.Test

class ClassicCategoryStringUtils {
    static underscorize(String self) {
        self.replaceAll(/(.)([A-Z])/) {
            "${it[1]}_${it[2]}"
        }.toLowerCase()
    }
}

class BrokenStringUtils {
    static underscorize(String self) {
        self
    }
}

class AutoCloseableReader {
    static withStream(Reader self, Closure closure) {
        /*koanify*/closure(self)
        self.close()/**/
    }
}

class ToStringAugmenter {
    static String toString(self) {
        "||${self.toString()}||"
    }
}

@Category(String)
class ASTTransformationStringUtils {
    def underscorize()  {
        this.replaceAll(/(.)([A-Z])/) {
            "${it[1]}_${it[2]}"
        }.toLowerCase()
    }
}

class Categories extends MetaKoan {
    @Test
    void 'you add new behaviour with classic categories'() {
        use(/*koanify*/ClassicCategoryStringUtils/**/) {
            assert 'CamelCaseString'./*koanify*/underscorize()/**/ == 'camel_case_string'
        }
        // Note: use adds category methods to the metaclass of the class given as use parameter.
        // They are only valid in the use block. Category methods must be static;
        // first method parameter is the enriched object.
    }

    @Test
    void 'you can use multiple categories simultaneously'() {
        use(/*koanify*/ClassicCategoryStringUtils/**/, /*koanify*/BrokenStringUtils/**/) {
            assert 'CamelCaseString'.underscorize() == 'CamelCaseString'
        }
        // Note: if categories have methods with the same signature, the last category in use clause wins
    }

    @Test
    void 'categories can be nested'() {
        use(/*koanify*/BrokenStringUtils/**/) {
            assert 'CamelCaseString'.underscorize() == 'CamelCaseString'

            use(/*koanify*/ClassicCategoryStringUtils/**/) {
                assert 'CamelCaseString'.underscorize() == 'camel_case_string'
            }
        }
    }

    @Test
    void 'you can use a category to implement a template method'() {
        use(AutoCloseableReader) {
            // Implement AutoCloseableReader.withStream so it performs operations from the closure and finally closes the stream
            def reader = new StringReader('abc')
            def c

            reader.withStream {
                c = reader.read()
            }
            assert c == 'a'
            shouldFail(IOException) {
                reader.read()
            }
        }
    }

    @Test
    void 'categories can override existing methods and thus implement AOP advices'() {
        def s = 'a string'

        assert s.toString() == /*koanify*/'a string'/**/

        use(ToStringAugmenter) {
            s.toString() == /*koanify*/'||a string||'/**/
        }
    }

    @Test
    void 'you can use _use_ method to add new behaviour with AST transformation categories like with classic categories'() {
        use(/*koanify*/ASTTransformationStringUtils/**/) {
            assert 'CamelCaseString'./*koanify*/underscorize()/**/ == 'camel_case_string'
        }
    }
}
