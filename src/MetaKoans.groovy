import org.junit.runner.RunWith
import org.junit.runners.Suite.SuiteClasses

@RunWith(KoansSuite)
@SuiteClasses([POGOIntro, POGOMethodDispatch, GroovyInterceptableMethodDispatch, POJOMethodDispatch, POGOPropertyAccess,
    MOPReflection])
class MetaKoans {
    // TODO Koans - 12,3 Dynamically Accessing Objects
    // TODO Koans - categories and mixins

    // TODO how HandleMetaClass and DelegatingMetaClass work? Why are they used instead of ExpandoMetaClass?
    // TODO groovy.lang.MetaClassRegistry? How it works? What is it for?
    // TODO why internal classes dont have metaClass?

    // http://en.wikipedia.org/wiki/Metaobject
    // http://groovy.codehaus.org/ExpandoMetaClass
}
