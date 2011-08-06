import org.junit.runner.RunWith
import org.junit.runners.Suite.SuiteClasses

@RunWith(KoansSuite)
@SuiteClasses([POGOIntro, POGOMethodDispatch, GroovyInterceptableMethodDispatch, POJOMethodDispatch, POGOPropertyAccess,
    MOPReflection, GroovyAOP, Categories, CompileTimeMixins, RuntimeMixins])
class MetaKoans {
    // Backlog:
    // TODO Koans - 12.3 Dynamically Accessing Objects
    // TODO Koans - 14.2, method injection with EMC
    // TODO Koans - 14.3, method injection into specific instances
    // TODO Koans - 14.4, method synthesis using methodMissing
    // TODO Koans - 14.5, method synthesis using EMC
    // TODO Koans - 14.6, method synthesis for specific instances
    // TODO Koans - 15, complete chapter
    // TODO Transform koans into cloze tests (substitute /*koanify*//**/ markers with __)
    // TODO Koans - AST transformations

    // Questions:
    // how HandleMetaClass and DelegatingMetaClass work? Why are they used instead of ExpandoMetaClass?
    // groovy.lang.MetaClassRegistry? How it works? What is it for?
    // why internal classes dont have metaClass?

    // Reference:
    // http://en.wikipedia.org/wiki/Metaobject
    // http://groovy.codehaus.org/ExpandoMetaClass
}
