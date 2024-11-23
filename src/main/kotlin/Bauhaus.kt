import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noise.Random
import org.openrndr.math.Vector2
import org.openrndr.shape.*

object VeebyBeeby {

    val PINK = ColorRGBa.fromHex("#f575a6")
    val TEAL = ColorRGBa.fromHex("#43b3ae")
    val BLUE = ColorRGBa.fromHex("#007ba7")
    val PURPLE = ColorRGBa.fromHex("#b482c8")

    private val colors = setOf(PINK, TEAL, BLUE, PURPLE)

    fun color(r: Random = Random): ColorRGBa = r.pick(colors)
}

object Christmas {

    val RED = ColorRGBa.fromHex("#b70d00")
    val GREEN = ColorRGBa.fromHex("#005c01")
    val NAVY = ColorRGBa.fromHex("#132f63")

    private val colors = listOf(RED, GREEN, RED, GREEN, GREEN, NAVY)

    fun color(r: Random = Random): ColorRGBa = r.pick(colors)
}

fun main() = application {
    configure { width = 800; height = width }


    program {
        backgroundColor = ColorRGBa.WHITE

        extend {

            // Options!
            Random.seed = "RusticFlare"
            val count = 50

            val step = (width + 1).toDouble() / count

            drawer.stroke = null
            drawer.strokeWeight = 0.0

            (0..<count).map { it * step }.flatMap { x ->
                (0..<count).map { it * step }.map { y ->
                    Vector2(x = x, y = y)
                }
            }.map { Rectangle(corner = it, width = step - 1) }
                .mapNotNull { Bauhaus.shape(it) }
                .forEach {
                    drawer.fill = VeebyBeeby.color()
                    drawer.shape(it)
                }
        }
    }
}

object Bauhaus {

    val RED = ColorRGBa.fromHex("#be1e2d")
    val YELLOW = ColorRGBa.fromHex("#ffde17")
    val BLUE = ColorRGBa.fromHex("#21409a")

    private val colors = setOf(RED, YELLOW, BLUE)

    fun color(r: Random = Random): ColorRGBa = r.pick(colors)

    private fun Rectangle.corners() = listOf(
        corner,
        corner.copy(x = x + width),
        corner.copy(y = y + height),
        corner.copy(x = x + width, y = y + height),
    )

    fun Rectangle.square() = shape.intersection(shape)
    val Rectangle.circle get() = Circle(center = center, radius = width / 2)
    fun Rectangle.circle() = shape.intersection(circle.shape)
    fun Rectangle.semiCircleNorth() = shape.intersection(circle.copy(center = corner.copy(x = x + (width / 2))).shape)
    fun Rectangle.semiCircleSouth() = shape.intersection(circle.copy(center = corner.copy(x = x + (width / 2), y = y + height)).shape)
    fun Rectangle.semiCircleWest() = shape.intersection(circle.copy(center = corner.copy(y = y + (height / 2))).shape)
    fun Rectangle.semiCircleEast() = shape.intersection(circle.copy(center = corner.copy(x = x + width, y = y + (height / 2))).shape)
    fun Rectangle.triangleNorthWest(): Shape {
        val corners = corners()
        return shape.intersection(Triangle(corners[0], corners[1], corners[2]).shape)
    }
    fun Rectangle.triangleNorthEast(): Shape {
        val corners = corners()
        return shape.intersection(Triangle(corners[0], corners[1], corners[3]).shape)
    }
    fun Rectangle.triangleSouthWest(): Shape {
        val corners = corners()
        return shape.intersection(Triangle(corners[0], corners[3], corners[2]).shape)
    }
    fun Rectangle.triangleSouthEast(): Shape {
        val corners = corners()
        return shape.intersection(Triangle(corners[1], corners[3], corners[2]).shape)
    }

    private val shapes = listOf<(Rectangle) -> Shape?>(
        { it.square() },
        { it.square() },
        { it.square() },
        { it.square() },
        { it.circle() },
        { it.circle() },
        { it.semiCircleNorth() },
        { it.semiCircleSouth() },
        { it.semiCircleEast() },
        { it.semiCircleWest() },
        { it.triangleSouthEast() },
        { it.triangleSouthWest() },
        { it.triangleNorthEast() },
        { it.triangleNorthWest() },
        { null },
        { null },
        { null },
        { null },
    )

    fun shape(rectangle: Rectangle, r: Random = Random) = r.pick(shapes).invoke(rectangle)
}
