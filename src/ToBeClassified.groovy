import org.junit.Test

class ToBeClassified {

    @Test
    void 'every metaclass implements MetaObjectProtocol (MOP)'()  {
        assert MetaObjectProtocol.isAssignableFrom(/*koanify*/MetaClass/**/)
    }
}
