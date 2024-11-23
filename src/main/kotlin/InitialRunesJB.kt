import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noise.Random
import org.openrndr.math.Polar
import org.openrndr.math.Vector2
import org.openrndr.shape.LineSegment
import org.openrndr.shape.ShapeContour
import org.openrndr.shape.shape

fun main() = application {
    configure { width = 800; height = 800 }
    program {
        val step = (width / 6).toDouble()

        fun position(x: Int, y: Int) = Vector2(x = (x + 0.5) * step, y = y * step)
        fun position(x: Double, y: Double) = Vector2(x = (x + 0.5) * step, y = y * step)

        backgroundColor = ColorRGBa.BLACK
        extend {

            val r = Random
            r.seed = "-fg12"

            drawer.stroke = ColorRGBa.WHITE

            val initials = listOf(
                position(x = 1, y = 4),
                position(x = 2, y = 5),
                position(x = 2, y = 1),
            ).zipWithNext(::LineSegment) + listOf(
                position(x = 1, y = 1),
                position(x = 3, y = 1),
                position(x = 3, y = 5),
                position(x = 4, y = 4),
                position(x = 3, y = 3),
                position(x = 4, y = 2),
                position(x = 3, y = 1),
            ).zipWithNext(::LineSegment)

            val frame = listOf(
                position(x = 0.5, y = 0.5),
                position(x = 4.5, y = 0.5),
                position(x = 4.5, y = 5.5),
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

fun List<LineSegment>.scatterAndRepeat(r: Random) = flatMap { (start, end) ->
    generateSequence {
        LineSegment(
            start = start + Polar(
                theta = r.double(min = 0.0, max = 360.0),
                radius = r.double(min = 0.0, max = 5.0)
            ).cartesian,
            end = end + Polar(
                theta = r.double(min = 0.0, max = 360.0),
                radius = r.double(min = 0.0, max = 5.0)
            ).cartesian,
        )
    }.take(20)
}
