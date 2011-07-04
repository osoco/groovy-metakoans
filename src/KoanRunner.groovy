import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.Statement
import org.junit.runners.model.FrameworkMethod

class KoanRunner extends BlockJUnit4ClassRunner {

    KoanRunner(Class<?> klass) {
        super(klass)
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method)
    {
        println method.getName()
        super.methodBlock(method)
    }
}
