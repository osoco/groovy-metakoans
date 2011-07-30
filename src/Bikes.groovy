class BikeBrokenException extends RuntimeException {
}

class Bike {
    def gears = 24

    def ring(times = 1) {
        'ring!' * times
    }

    def ride = {
        'riding!'
    }
}

class BikeWithMethodMissing extends Bike {
    def methodMissing(String name, args) {
        if (name.startsWith('ring')) {
            "ring ${name.substring('ring'.length()).toLowerCase()}!"
        } else {
            throw new MissingMethodException(name, this.class, args)
        }
    }
}

class BikeWithMethodMissingThrowingEx extends Bike {
    @Override
    def invokeMethod(String name, args) {
        throw new BikeBrokenException()
    }
}

class BikeWithInvokeMethod extends Bike {
    @Override
    def invokeMethod(String name, args) {
        if (name.startsWith('ride')) {
            "riding ${name.substring('ride'.length()).toLowerCase()}!"
        } else {
            super.invokeMethod(name, args)
        }
    }
}

class BikeWithMethodMissingAndInvokeMethod extends Bike {
    def methodMissing(String name, args) {
        if (name.startsWith('ring')) {
            "ring ${name.substring('ring'.length()).toLowerCase()}!"
        } else {
            throw new MissingMethodException(name, this.class, args)
        }
    }

    @Override
    def invokeMethod(String name, args) {
        if (name.startsWith('ride')) {
            "riding ${name.substring('ride'.length()).toLowerCase()}!"
        } else {
            super.invokeMethod(name, args)
        }
    }
}

class InterceptableBike extends Bike implements GroovyInterceptable {
    @Override
    def invokeMethod(String name, args) {
        "won't do anything"
    }
}

class BikeWithPropertyMissing extends Bike {
     def propertyMissing(String name) {
         if (name.length() >= 3) {
             name
         } else {
             throw new MissingPropertyException(name, this.class)
         }
     }
}

class BikeWithGetSetProperty extends Bike {
    private static final SUPPORTED_PROPS = ['spokes', 'chainLength', 'wheelSize']

    private dynamicProperties = [:]

    @Override
    Object getProperty(String property) {
        if (property in SUPPORTED_PROPS) {
            dynamicProperties[property]
        } else {
            super.getProperty(property)
        }
    }

    @Override
    void setProperty(String property, newValue) {
        if (property in SUPPORTED_PROPS) {
            dynamicProperties[property] = newValue
        } else {
            super.setProperty(property, newValue)
        }
    }
}

class BikeWithPropertyMissingAndGetSetProperty extends Bike {
    def propertyMissing(String name) {
        if (name.length() >= 3) {
            name
        } else {
            throw new MissingPropertyException(name, this.class)
        }
    }

    private static final SUPPORTED_PROPS = ['spokes', 'chainLength', 'wheelSize']

    private dynamicProperties = [:]

    @Override
    Object getProperty(String property) {
        if (property in SUPPORTED_PROPS) {
            dynamicProperties[property]
        } else {
            super.getProperty(property)
        }
    }

    @Override
    void setProperty(String property, newValue) {
        if (property in SUPPORTED_PROPS) {
            dynamicProperties[property] = newValue
        } else {
            super.setProperty(property, newValue)
        }
    }
}
