import org.junit.runner.RunWith
import org.junit.runners.Suite.SuiteClasses

@RunWith(KoansSuite)
@SuiteClasses([POGOIntro, POGOMethodDispatch, GroovyInterceptableMethodDispatch, POJOMethodDispatch, POGOPropertyAccess,
    MOPReflection, GroovyAOP, Categories, CompileTimeMixins, RuntimeMixins, MetaClassMethodInjection, MethodSynthesis])
class MetaKoans {
    // In progress

    // Backlog:
    // TODO Run koans with Groovy 1.8
    // TODO Koans - 12.3 Dynamically Accessing Objects
    // TODO Koans - 14.6, method synthesis for specific instances
    // TODO Koans - 15, complete chapter (if applies)
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
