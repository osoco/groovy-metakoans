import org.junit.runner.RunWith
import org.junit.runners.Suite.SuiteClasses

@RunWith(KoansSuite)
@SuiteClasses([POGOIntro, POGOMethodDispatch, GroovyInterceptableMethodDispatch, POJOMethodDispatch, POGOPropertyAccess,
    ToBeClassified])
class MetaKoans {
    // TODO Koan - querying methods and properties through MOP and MetaClass
    // TODO Koans - categories and mixins

    // TODO how HandleMetaClass and DelegatingMetaClass work? Why are they used instead of ExpandoMetaClass?
    // TODO groovy.lang.MetaClassRegistry? How it works? What is it for?
    // TODO why internal classes dont have metaClass?
    // TODO koans on http://groovy.codehaus.org/ExpandoMetaClass
    // http://en.wikipedia.org/wiki/Metaobject
}
