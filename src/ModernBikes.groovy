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

class RingingBike extends Bike {
    def methodMissing(String name, args) {
        if (name.startsWith('ring')) {
            return "ring ${name.substring('ring'.length()).toLowerCase()}!"
        }

        throw new MissingMethodException(name, this.class, args)
    }
}

class BrittleBike extends Bike {
    def invokeMethod(String name, args) {
        throw new BikeBrokenException()
    }
}

class VersatileBike extends Bike {
    def invokeMethod(String name, args) {
        if (name.startsWith('ride')) {
            return "riding ${name.substring('ride'.length()).toLowerCase()}!"
        }

        throw new MissingMethodException(name, this.class, args)
    }
}

class HybridBike extends Bike {
    def methodMissing(String name, args) {
        if (name.startsWith('ring')) {
            return "ring ${name.substring('ring'.length()).toLowerCase()}!"
        }

        throw new MissingMethodException(name, this.class, args)
    }

    def invokeMethod(String name, args) {
        if (name.startsWith('ride')) {
            return "riding ${name.substring('ride'.length()).toLowerCase()}!"
        }

        throw new MissingMethodException(name, this.class, args)
    }

}

class StubbornBike extends Bike implements GroovyInterceptable {
    def invokeMethod(String name, args) {
        "won't do anything"
    }
}
