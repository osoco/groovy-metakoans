import org.junit.After
import org.junit.Before

class MetaKoan {
    protected Map savedMetaClasses

    @Before
    public void setUp() {
        savedMetaClasses = [:]
    }

    @After
    public void tearDown() {
        savedMetaClasses.each { clazz, metaClass ->
            GroovySystem.metaClassRegistry.removeMetaClass(clazz)
            GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
        }
    }

    // Borrowed from Grails 1.3.7
    protected void registerMetaClass(Class clazz) {
        if (savedMetaClasses.containsKey(clazz)) return

        savedMetaClasses[clazz] = clazz.metaClass

        def emc = new ExpandoMetaClass(clazz, true, true)
        emc.initialize()
        GroovySystem.metaClassRegistry.setMetaClass(clazz, emc)
    }
}
