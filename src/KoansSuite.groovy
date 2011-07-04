import org.junit.runners.Suite
import org.junit.runners.model.RunnerBuilder
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runner.notification.RunListener
import org.junit.runner.notification.Failure
import com.sun.org.apache.bcel.internal.generic.AALOAD

/*
TODO:
    - print the last failure
    - prin the progress bar
    - print a beautiful final message if all koans are completed
    - print zen encouragements
 */
class Sensei extends RunListener {
    private Runner runner
    private RunNotifier notifier
    private passCount = 0
    private progress
    private failed = false
    private boolean isSuccess() { !failed }
    private lastFailure

    Sensei(Runner runner, RunNotifier notifier) {
        this.runner = runner
        this.notifier = notifier
    }

    def initialize() {
        readProgress()
    }

    private readProgress() {
        def f = new File('.progress')
        progress = f.exists() ? f.text.toInteger() : 0
    }

    private writeProgress() {
        def f = new File('.progress')
        if (!f.exists()) f.createNewFile()
        f.text = passCount
    }

    def printWelcomeMessage() {
        println """Groovy MetaProgramming Koans
${runner.testCount()} tough questions are waiting for you...
"""
    }

    def printFinalMessage() {
        println """
You reached the enlightment
"""
    }

    @Override
    void testFailure(Failure failure) {
        failed = true
        lastFailure = failure
    }

    @Override
    void testAssumptionFailure(Failure failure) {
        failed = true
        lastFailure = failure
    }

    @Override
    void testFinished(Description description) {
        if (success) {
            passCount++
            if (passCount > progress) {
                println "${description.className} ${description.methodName} has expanded your awareness."
            }
            writeProgress()
        } else {
            println "${lastFailure.description.className} ${lastFailure.description.methodName} damaged your karma."
            writeProgress()
            notifier.pleaseStop()
        }
    }
}

class KoansSuite extends Suite {

    KoansSuite(Class<?> klass, RunnerBuilder builder) {
        super(klass, builder)
    }

    KoansSuite(RunnerBuilder builder, Class<?>[] classes) {
        super(builder, classes)
    }

    KoansSuite(Class<?> klass, Class<?>[] suiteClasses) {
        super(klass, suiteClasses)
    }

    KoansSuite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) {
        super(builder, klass, suiteClasses)
    }

    KoansSuite(Class<?> klass, List<Runner> runners) {
        super(klass, runners)
    }

    @Override
    void run(RunNotifier notifier) {
        Sensei sensei = new Sensei(this, notifier)
        sensei.initialize()
        sensei.printWelcomeMessage()

        notifier.addListener(sensei)
        super.run(notifier)

        sensei.printFinalMessage()
    }
}
