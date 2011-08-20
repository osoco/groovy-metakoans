import org.junit.runner.RunWith
import org.junit.runners.Suite.SuiteClasses

@RunWith(KoansSuite)
@SuiteClasses([POGOIntro,
    POGOMethodDispatch,
    GroovyInterceptableMethodDispatch,
    POJOIntro,
    POJOMethodDispatch,
    POGOPropertyAccess,
    MOPReflection,
    GroovyAOP,
    Categories,
    CompileTimeMixins,
    RuntimeMixins,
    MetaClassMethodInjection,
    MetaClassPropertyInjection,
    MethodSynthesis,
    DynamicClasses])
class MetaKoans {
    // Backlog:
    // TODO Run koans with Groovy 1.8
    // TODO Transform koans into cloze tests (substitute /*koanify*//**/ markers with __)

    // Maybe:
    // Koans about groovy.lang.MetaClassRegistry? How it works? What is it for?
    // Koans - AST transformations

    // Questions:
    // how HandleMetaClass and DelegatingMetaClass work? Why are they used instead of ExpandoMetaClass?
    // why internal classes dont have metaClass?

    // Reference:
    // http://en.wikipedia.org/wiki/Metaobject
    // http://groovy.codehaus.org/ExpandoMetaClass
}
