import org.junit.Test

class MOPReflection {
    @Test
    void 'every metaclass implements MetaObjectProtocol (MOP)'()  {
        assert MetaObjectProtocol.isAssignableFrom(/*koanify*/MetaClass/**/)
    }

    @Test
    void 'MOP provides information about declared instance methods'() {
        def bike = new Bike()

        MetaMethod mm = bike.metaClass.getMetaMethod('ring', Object)
        assert mm.declaringClass.theClass == /*koanify*/Bike/**/
        assert mm.name == /*koanify*/'ring'/**/
        assert mm.public == /*koanify*/true/**/
        assert mm.private == /*koanify*/false/**/
        assert mm.abstract == /*koanify*/false/**/
        assert mm.static == /*koanify*/false/**/
        assert mm.returnType == /*koanify*/Object/**/
        assert mm.parameterTypes*.theClass == /*koanify*/[Object]/**/
        assert mm.invoke(bike, 3) == /*koanify*/'ring!ring!ring!'/**/

        assert bike.metaClass.methods.findAll({ it.name == 'ring' }).size() == /*koanify*/2/**/
        // Notice that you get a declared method using getMetaMethod
        // However, you use getMethods to get the list of declared methods
    }

    @Test
    void 'closure is not treated as a MOP meta method'() {
        def bike = new Bike()

        assert bike.metaClass.getMetaMethod('ride') == /*koanify*/null/**/
    }

    @Test
    void 'method added to metaclass is a MOP meta method (although it is closure)'() {
        def bike = new Bike()
        bike.metaClass.win = {'won!'}

        MetaMethod mm = bike.metaClass.getMetaMethod('win')
        mm.declaringClass.theClass == /*koanify*/Bike/**/
        assert mm.returnType == /*koanify*/Object/**/
        assert mm.parameterTypes*.theClass == /*koanify*/[]/**/

        assert bike.metaClass.methods.findAll({ it.name == 'win' }).size() == /*koanify*/2/**/
        assert bike.metaClass.metaMethods.findAll({ it.name == 'win' }).size() == /*koanify*/0/**/
        // getMetaMethods yields the list of methods added from the DefaultGroovyMethods
    }

    @Test
    void 'MOP provides information about declared class (static) methods'() {
        def bike = new Bike()

        MetaMethod mm = bike.metaClass.getStaticMetaMethod('hasHandleBar', [] as Object[])
        assert mm.static == /*koanify*/true/**/
    }

    @Test
    void 'respondsTo finds out if an object would respond to a method call'() {
        def bike = new Bike()

        assert bike.metaClass.respondsTo(bike, 'ring').size() == /*koanify*/2/**/
        assert bike.metaClass.respondsTo(/*koanify*/bike/**/, /*koanify*/'ring'/**/, [] as Object[]).size() == /*koanify*/1/**/
        assert bike.metaClass.respondsTo(/*koanify*/bike/**/, /*koanify*/'ring'/**/, Object).size() == /*koanify*/1/**/
    }

    /*
    TODO getMetaMethod and respondsTo don't require Class'es as parameter descriptors; they work with concrete values

    TODO koans for these methods:

    java.lang.Object invokeConstructor(java.lang.Object[] objects);

    java.lang.Object invokeMethod(java.lang.Object o, java.lang.String s, java.lang.Object[] objects);

    java.lang.Object invokeMethod(java.lang.Object o, java.lang.String s, java.lang.Object o1);

    java.lang.Object invokeStaticMethod(java.lang.Object o, java.lang.String s, java.lang.Object[] objects);



    java.lang.Object getProperty(java.lang.Object o, java.lang.String s);

    void setProperty(java.lang.Object o, java.lang.String s, java.lang.Object o1);

    java.util.List<groovy.lang.MetaProperty> getProperties();

    groovy.lang.MetaProperty hasProperty(java.lang.Object o, java.lang.String s);

    groovy.lang.MetaProperty getMetaProperty(java.lang.String s);

    java.lang.Object getAttribute(java.lang.Object o, java.lang.String s);

    void setAttribute(java.lang.Object o, java.lang.String s, java.lang.Object o1);


    java.lang.Class getTheClass();

    // TODO MOP reflection for POJOs
    */
}
