import org.junit.runners.Suite
import org.junit.runners.model.RunnerBuilder
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runner.notification.RunListener
import org.junit.runner.notification.Failure

/*
TODO:
    - print the progress bar
    - print zen encouragements
    - move messages to external templates
 */

class ProgressIO {
    private static final FILE_NAME = '.progress'

    def readProgress() {
        def f = new File(FILE_NAME)
        f.exists() ? f.text as Integer : 0
    }

    def writeProgress(progress) {
        def f = new File(FILE_NAME)
        if (!f.exists()) f.createNewFile()
        f.text = progress
    }
}

class MessageRenderer {

    def printWelcomeMessage(koanCount) {
        println """
Groovy MetaProgramming Koans
${koanCount} tough questions are waiting for you...
"""
    }

    def printFinalMessage() {
        println """
You reached the enlightment
"""
    }

    def printPassedMessage(Description description) {
        println "${description.className} ${description.methodName} has expanded your awareness."
    }

    def printFailureMessage(Failure failure) {
        println "${failure.description.className}.${failure.description.methodName} damaged your karma."
        println failure.message
        println failure.trace
    }
}

class Sensei extends RunListener {
    private RunNotifier notifier
    private ProgressIO progressIO = new ProgressIO()
    private MessageRenderer messageRenderer = new MessageRenderer()
    private koanCount
    private koansPassed = 0
    private progress
    private failed = false
    private boolean isSuccess() { !failed }
    private lastFailure

    Sensei(koanCount, RunNotifier notifier) {
        this.koanCount = koanCount
        this.notifier = notifier
    }

    def initialize() {
        progress = progressIO.readProgress()
    }

    def printWelcomeMessage() {
        messageRenderer.printWelcomeMessage(koanCount)
    }

    def printFinalMessageIfCompleted() {
        if (koanCount == koansPassed) {
            messageRenderer.printFinalMessage()
        }
    }

    @Override
    void testFailure(Failure failure) {
        markKoanAsFailed(failure)
    }

    @Override
    void testAssumptionFailure(Failure failure) {
        markKoanAsFailed(failure)
    }

    private markKoanAsFailed(Failure failure) {
        failed = true
        lastFailure = failure
    }

    @Override
    void testFinished(Description description) {
        success ? onKoanSuccess(description) : onKoanFailure()
        progressIO.writeProgress(koansPassed)
    }

    private onKoanSuccess(Description description) {
        koansPassed++
        if (koansPassed > progress) {
            messageRenderer.printPassedMessage(description)
        }
    }

    private onKoanFailure() {
        messageRenderer.printFailureMessage(lastFailure)
        notifier.pleaseStop()
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
        Sensei sensei = new Sensei(testCount(), notifier)
        sensei.initialize()
        sensei.printWelcomeMessage()

        notifier.addListener(sensei)
        super.run(notifier)

        sensei.printFinalMessageIfCompleted()
    }
}
