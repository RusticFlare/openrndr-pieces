import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.color.rgba
import org.openrndr.extra.noclear.NoClear
import org.openrndr.extra.noise.Random
import org.openrndr.math.Polar

/**
 * From [Noise fields & leaving trails](https://openrndr.discourse.group/t/openrndr-processing-noise-fields-leaving-trails/)
 */
fun main() = application {
    configure { width = 800; height = 800 }
    program {
        val zoom = 0.03
        backgroundColor = ColorRGBa.WHITE
        extend(NoClear())
        extend {
            drawer.fill = rgb(0.0, 0.0, 0.0, 0.1)
            drawer.points(generateSequence(Random.point(drawer.bounds)) {
                it + Polar(
                    180 * if (it.x < width / 2)
                        Random.value(it * zoom)
                    else
                        Random.simplex(it * zoom)
                ).cartesian
            }.take(500).toList())
        }
    }
}