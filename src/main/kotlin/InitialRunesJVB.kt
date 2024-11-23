import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noise.Random
import org.openrndr.math.Polar
import org.openrndr.math.Vector2
import org.openrndr.shape.LineSegment

fun main() = application {
    configure { width = 800; height = 800 }
    program {
        val step = (width / 6).toDouble()

        fun position(x: Int, y: Int) = Vector2(x = x * step, y = y * step)
        fun position(x: Double, y: Double) = Vector2(x = x * step, y = y * step)

        backgroundColor = ColorRGBa.BLACK
        extend {

            val r = Random
            r.seed = "-fg12"

            drawer.stroke = ColorRGBa.WHITE

            val initials = listOf(
                position(x = 1, y = 1),
                position(x = 3, y = 1),
            ).zipWithNext(::LineSegment) + listOf(
                position(x = 1, y = 4),
                position(x = 2, y = 5),
                position(x = 2, y = 1),
                position(x = 3, y = 5),
                position(x = 4, y = 1),
                position(x = 4, y = 5),
                position(x = 5, y = 4),
                position(x = 4, y = 3),
                position(x = 5, y = 2),
                position(x = 4, y = 1),
            ).zipWithNext(::LineSegment)

            val frame = listOf(
                position(x = 0.5, y = 0.5),
                position(x = 5.5, y = 0.5),
                position(x = 5.5, y = 5.5),
                position(x = 0.5, y = 5.5),
                position(x = 0.5, y = 0.5),
            ).zipWithNext(::LineSegment)

            val segments = initials + frame

            val yShift = drawer.bounds.center.y * 0.2

            fun Vector2.scaleY() = copy(y = (y * 0.8) + yShift)

            drawer.lineSegments(
                segments = segments.scatterAndRepeat(r).map { it.extendBy(length = step / 4) }
                    .map { (s, e) -> LineSegment(start = s.scaleY(), end = e.scaleY()) }
            )
        }
    }
}

