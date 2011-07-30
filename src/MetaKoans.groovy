import org.junit.runner.RunWith
import org.junit.runners.Suite.SuiteClasses

@RunWith(KoansSuite)
@SuiteClasses([POGOIntro, POGOMethodDispatch, ToBeClassified])
class MetaKoans {
    // TODO method calls with GroovyInterceptable and POJO
    // TODO Koan - querying methods and properties
    // TODO Koan with propertyMissing

    // http://en.wikipedia.org/wiki/Metaobject
    // TODO how HandleMetaClass and DelegatingMetaClass work? Why are they used instead of ExpandoMetaClass?
    // TODO groovy.lang.MetaClassRegistry? How it works? What is it for?
    // TODO categories and mixins
    // TODO koans on http://groovy.codehaus.org/ExpandoMetaClass
    // TODO internal classes dont have metaClass - investigate it

}
