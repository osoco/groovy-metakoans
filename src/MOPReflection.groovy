import org.junit.Test

class MOPReflection {
    @Test
    void 'every metaclass implements MetaObjectProtocol (MOP)'()  {
        assert MetaObjectProtocol.isAssignableFrom(/*koanify*/MetaClass/**/)
    }

    @Test
    void 'object Class is not MetaClass but it can be obtained from'() {
        assert (Bike.class == Bike.metaClass) == /*koanify*/false/**/
        assert (Bike.class == Bike.metaClass.theClass) == /*koanify*/true/**/
    }

    @Test
    void 'MOP provides information about declared instance methods as MetaMethod'() {
        MetaMethod mm = Bike.metaClass.getMetaMethod('ring', Object)
        assert mm.declaringClass.theClass == /*koanify*/Bike/**/
        assert mm.name == /*koanify*/'ring'/**/
        assert mm.public == /*koanify*/true/**/
        assert mm.private == /*koanify*/false/**/
        assert mm.abstract == /*koanify*/false/**/
        assert mm.static == /*koanify*/false/**/
        assert mm.returnType == /*koanify*/Object/**/
        assert mm.parameterTypes*.theClass == /*koanify*/[Object]/**/
        assert mm.invoke(new Bike(), 3) == /*koanify*/'ring!ring!ring!'/**/

        assert Bike.metaClass.methods.findAll({ it.name == 'ring' }).size() == /*koanify*/2/**/
        // Notice that you get a declared method using getMetaMethod
        // However, you use getMethods to get the list of declared methods
    }

    @Test
    void 'closure is not treated as a MOP meta method'() {
        assert Bike.metaClass.getMetaMethod('ride') == /*koanify*/null/**/
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
        MetaMethod mm = Bike.metaClass.getStaticMetaMethod('hasHandleBar', [] as Object[])
        assert mm.static == /*koanify*/true/**/
    }

    @Test
    void 'respondsTo finds out if an object would respond to a method call'() {
        def bike = new Bike()
        bike.metaClass.win = {'won!'}

        assert Bike.metaClass.respondsTo(bike, 'ring').size() == /*koanify*/2/**/
        assert Bike.metaClass.respondsTo(/*koanify*/bike/**/, /*koanify*/'ring'/**/, [] as Object[]).size() == /*koanify*/1/**/
        assert Bike.metaClass.respondsTo(/*koanify*/bike/**/, /*koanify*/'ring'/**/, Object).size() == /*koanify*/1/**/
    }

    @Test
    void 'getMetaMethod and respondsTo work also with concrete values as parameter descriptors'() {
        assert Bike.metaClass.getMetaMethod(/*koanify*/'ring'/**/, 3) != /*koanify*/null/**/
        assert Bike.metaClass.respondsTo(new Bike(), /*koanify*/'ring'/**/, 3).size() == /*koanify*/1/**/
    }

    @Test
    void 'you can create a new object instance using MOP'() {
        assert Bike.metaClass.invokeConstructor().gears == /*koanify*/24/**/
    }

    @Test
    void 'you can invoke an instance method through MOP without the reference to the meta method'() {
        def bike = new Bike()

        assert Bike.metaClass.invokeMethod(/*koanify*/bike/**/, /*koanify*/'ring'/**/) == 'ring!'
        assert Bike.metaClass.invokeMethod(/*koanify*/bike/**/, /*koanify*/'ring'/**/, /*koanify*/3/**/) == 'ring!ring!ring!'
    }

    @Test
    void 'you can invoke a class (static) method through MOP without the reference to the meta method'() {
        assert Bike.metaClass.invokeStaticMethod(/*koanify*/Bike/**/, /*koanify*/'hasHandleBar'/**/) == true
    }

    @Test
    void 'MOP allows you to get a set a property'() {
        def bike = new Bike()

        assert Bike.metaClass.getProperty(bike, 'gears') == /*koanify*/24/**/
        Bike.metaClass.setProperty(bike, /*koanify*/'gears'/**/, 18)
        assert Bike.metaClass.getProperty(bike, 'gears') == /*koanify*/18/**/
    }

    @Test
    void 'MOP provides information about object properties as MetaProperty'() {
        def bike = new Bike()

        MetaProperty mp = Bike.metaClass.hasProperty(bike, 'gears')
        assert mp.name == /*koanify*/'gears'/**/
        assert mp.type == /*koanify*/Object/**/
        assert mp.getProperty(bike) == /*koanify*/24/**/

        assert /*koanify*/'gears'/**/ in Bike.metaClass.getProperties()*.name
    }

    @Test
    void 'closure is treated as a MOP meta property'() {
        def bike = new Bike()

        assert Bike.metaClass.hasProperty(bike, 'ride').getProperty(bike) instanceof /*koanify*/Closure/**/
    }

    @Test
    void 'hasProperty and getMetaProperty are same methods and differ only due to historical reasons'() {
        assert Bike.metaClass.hasProperty(null, 'gears').is(Bike.metaClass.getMetaProperty('gears')) == /*koanify*/true/**/
    }

    @Test
    void 'MOP allows you to get a set a member variable of an object (without going through the getter and setter)'() {
        def bike = new Bike()

        assert Bike.metaClass.getAttribute(bike, 'raeder') == /*koanify*/2/**/
        Bike.metaClass.setAttribute(bike, /*koanify*/'raeder'/**/, 4)
        assert Bike.metaClass.getAttribute(bike, 'raeder') == /*koanify*/4/**/
    }

    @Test
    void 'since POJOs have a metaclass, MOP reflection works for POJOs too'() {
        def velocipede = new Velocipede()

        MetaMethod mm = velocipede.metaClass.getMetaMethod('ring')
        assert mm.declaringClass.theClass == /*koanify*/Velocipede/**/
        assert mm.returnType == /*koanify*/String/**/
        assert mm.parameterTypes*.theClass == /*koanify*/[]/**/
        assert mm.invoke(velocipede) == /*koanify*/'ring!'/**/

        assert Velocipede.metaClass.methods.findAll({ it.name == 'ring' }).size() == /*koanify*/1/**/

        assert Velocipede.metaClass.respondsTo(velocipede, 'ring').size() == /*koanify*/1/**/
    }
}
