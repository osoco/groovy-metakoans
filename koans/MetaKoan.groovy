import org.codehaus.groovy.runtime.ScriptBytecodeAdapter
import org.junit.After
import org.junit.Before
import static org.junit.Assert.fail

class MetaKoan {
    // TODO add it to Koan classes with an AST transformation?
    protected static final __ = 'fill_me_in'
    protected static final __should_this_block_fail_or_not__(Class exClass, Closure code) {
        fail("should this block fail or not fail with the exception ${exClass.simpleName}?")
    }

    protected Map originalMetaClasses

    @Before
    void clearOriginalMetaClasses() {
        originalMetaClasses = [:]
    }

    @After
    void restoreOriginalMetaClasses() {
        originalMetaClasses.each { clazz, metaClass ->
            GroovySystem.metaClassRegistry.removeMetaClass(clazz)
            GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
        }
    }

    // Borrowed from Grails 1.3.7
    protected void storeOriginalMetaClass(Class clazz) {
        if (originalMetaClasses.containsKey(clazz)) return

        originalMetaClasses[clazz] = clazz.metaClass

        def emc = new ExpandoMetaClass(clazz, true, true)
        emc.initialize()
        GroovySystem.metaClassRegistry.setMetaClass(clazz, emc)
    }

    protected void shouldFail(Class exClass, Closure code) {
        Throwable th = null
        try {
            code.call()
        } catch (GroovyRuntimeException gre) {
            th = ScriptBytecodeAdapter.unwrap(gre)
        } catch (Throwable e) {
            th = e
        }

        if (!th) {
            fail("Closure should have failed with an exception of type ${exClass.name}")
        } else if (!exClass.isInstance(th)) {
            fail("Closure should have failed with an exception of type ${exClass.name}, instead got Exception ${th}")
        }
    }

    protected void shouldNotFail(Class exClass, Closure code) {
        Throwable th = null
        try {
            code.call();
        } catch (GroovyRuntimeException gre) {
            th = ScriptBytecodeAdapter.unwrap(gre)
        } catch (Throwable e) {
            th = e
        }

        if (exClass.isInstance(th)) {
            fail("Closure should have NEVER failed with an exception of type ${exClass.name}")
        }
    }
}
