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
}
