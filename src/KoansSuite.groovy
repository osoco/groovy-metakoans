import org.junit.runners.Suite
import org.junit.runners.model.RunnerBuilder
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runner.notification.RunListener
import org.junit.runner.notification.Failure
import static java.lang.String.format
import org.junit.runner.notification.StoppedByUserException

// TODO print zen encouragements
// TODO move messages to external templates
// TODO print stack traces on demand
class ProgressIO {
    private static final FILE_NAME = '.progress'

    int readProgress() {
        def f = new File(FILE_NAME)
        f.exists() ? f.text as Integer : 0
    }

    def writeProgress(int progress) {
        def f = new File(FILE_NAME)
        if (!f.exists()) f.createNewFile()
        f.text = progress
    }
}

class MessageRenderer {

    def printWelcomeMessage(int koanCount) {
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

class ProgressBarRenderer {
    private static final CONSOLE_WIDTH = 80
    private static final PROGRESS_LINE_START = '['
    private static final PROGRESS_LINE_END = ']'
    private static final COMPLETED_CHAR = '+'
    private static final REMAINING_CHAR = '-'

    def printProgressBar(int completed, int total) {
        def percentageCompleted = format('%3d%%', percentage(completed, total))

        def progressLineWidth = CONSOLE_WIDTH - (PROGRESS_LINE_START.length() + PROGRESS_LINE_END.length()) -
            percentageCompleted.length()
        def completedCharCount = Math.round(completed / total * progressLineWidth)
        def remainingCharCount = progressLineWidth - completedCharCount

        printFormattedProgressBar(completedCharCount, remainingCharCount, percentageCompleted)
    }

    private printFormattedProgressBar(completedCharCount, remainingCharCount, percentageCompleted) {
        println """
${PROGRESS_LINE_START}${COMPLETED_CHAR * completedCharCount}${REMAINING_CHAR * remainingCharCount}${PROGRESS_LINE_END} ${percentageCompleted}
"""
    }

    private percentage(part, total) {
        Math.round part / total * 100
    }
}

class Sensei extends RunListener {
    private RunNotifier notifier
    private ProgressIO progressIO = new ProgressIO()
    private MessageRenderer messageRenderer = new MessageRenderer()
    private int totalKoans
    private int koansPassed = 0
    private int progress
    private boolean failed = false
    private boolean isSuccess() { !failed }
    private Failure lastFailure

    Sensei(int totalKoans, RunNotifier notifier) {
        this.totalKoans = totalKoans
        this.notifier = notifier
    }

    def initialize() {
        progress = progressIO.readProgress()
    }

    def printWelcomeMessage() {
        messageRenderer.printWelcomeMessage(totalKoans)
    }

    def printFinalMessageIfCompleted() {
        if (totalKoans == koansPassed) {
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

    def printProgressBar() {
        new ProgressBarRenderer().printProgressBar(koansPassed, totalKoans)
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
        try {
            super.run(notifier)
        } catch (StoppedByUserException e) {
        } finally {
            sensei.printProgressBar()
            sensei.printFinalMessageIfCompleted()
        }
    }
}
