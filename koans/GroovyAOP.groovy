import org.junit.Before
import org.junit.Test
import support.Velocipede

class Controller {
    def list() {
        'article list'
    }

    def save = {
        'saving an article'
    }
}

class LoggingController extends Controller implements GroovyInterceptable {
    def logger = []

    @Override
    def invokeMethod(String name, args) {
        logger << "method ${name} started"
        def result
        MetaMethod mm = LoggingController.metaClass.getMetaMethod(name, args)
        if (mm) {
            logger << 'defined'
            result = mm.invoke(this, args)
        } else {
            logger << 'missing'
            result = LoggingController.metaClass.invokeMethod(this, name, args)
        }
        logger << "method ${name} finished"
        logger << "result: ${result}"
        result
    }
}

class GroovyAOP extends MetaKoan {
    @Before
    void registerModifiedMetaClasses() {
        storeOriginalMetaClass(Controller)
        storeOriginalMetaClass(Velocipede)
    }

    @Test
    void 'use GroovyInterceptable to implement an around advice for a POGO'() {
        def controller = new LoggingController()
        assert controller.list() == 'article list'
        assert controller.logger == [/*koanify*/'method list started'/**/, /*koanify*/'defined'/**/, /*koanify*/'method list finished'/**/, /*koanify*/'result: article list'/**/]

        controller.logger = []
        assert controller.save() == 'saving an article'
        assert controller.logger == [/*koanify*/'method save started'/**/, /*koanify*/'missing'/**/, /*koanify*/'method save finished'/**/, /*koanify*/'result: saving an article'/**/]

        controller.logger = []
        Controller.metaClass.remove = { 'article removed' }
        assert controller.remove() == 'article removed'
        assert controller.logger == [/*koanify*/'method remove started'/**/, /*koanify*/'missing'/**/, /*koanify*/'method remove finished'/**/, /*koanify*/'result: article removed'/**/]

        // Hint: if a method has not been found, route the call to the metaclass
    }

    @Test
    void 'you can add an around advice to POGOs metaclass'() {
        Controller.metaClass.invokeMethod = { String name, args ->
            logger << "method ${name} started"
            def result
            MetaMethod mm = LoggingController.metaClass.getMetaMethod(name, args)
            if (mm) {
                logger << 'defined'
                result = mm.invoke(delegate, args)
            } else {
                logger << 'missing'
                // Call invokeMissingMethod to give the chance to methodMissing to handle the call (if implemented, of course)
                result = LoggingController.metaClass.invokeMissingMethod(delegate, name, args)
            }
            logger << "method ${name} finished"
            logger << "result: ${result}"
            result
        }

        def controller = new LoggingController()
        assert controller.list() == 'article list'
        assert controller.logger == [/*koanify*/'method list started'/**/, /*koanify*/'defined'/**/, /*koanify*/'method list finished'/**/, /*koanify*/'result: article list'/**/]

        controller.logger = []
        assert controller.save() == 'saving an article'
        assert controller.logger == [/*koanify*/'method save started'/**/, /*koanify*/'missing'/**/, /*koanify*/'method save finished'/**/, /*koanify*/'result: saving an article'/**/]

        controller.logger = []
        Controller.metaClass.remove = { return 'article removed' }
        assert controller.remove() == 'article removed'
        assert controller.logger == [/*koanify*/'method remove started'/**/, /*koanify*/'missing'/**/, /*koanify*/'method remove finished'/**/, /*koanify*/'result: article removed'/**/]

        // Bonus point: spot 2 differences between the advice implementation as GroovyInterceptable and MetaClass' invokeMethod
    }

    @Test
    void 'adding advices to the metaclass works for POJOs too'() {
        Velocipede.metaClass.invokeMethod = { String name, args ->
            MetaMethod mm = Velocipede.metaClass.getMetaMethod(name, args)
            if (mm) {
                // Hint: delegate points to the current Velocipede instance
                return /*koanify*/"invoked: ${mm.invoke(delegate, args)}"/**/
            }

            'not implemented'
        }

        def velocipede = new Velocipede()

        assert velocipede.ring() == 'invoked: ring!'
        assert velocipede.win() == 'not implemented'
    }
}
