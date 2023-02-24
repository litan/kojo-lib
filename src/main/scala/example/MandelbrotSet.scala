package example

import net.kogics.kojo.lite.KojoFrame

object MandelbrotSet {
  def main(args: Array[String]): Unit = {
    val kojo = KojoFrame.create(true)
    val builtins = kojo.builtins

    import builtins._
    import TSCanvas._
    import Tw._

    // Mandelbrot Set (zoomable)` sample from Kojo Showcase menu

    object code {
      // Zoomable mandelbrot set.
      // Click and drag the left mouse button to specify the zoom-in area.
      // this sample builds upon:
      // http://justindomke.wordpress.com/2008/11/29/mandelbrot-in-scala/
      toggleFullScreenCanvas()

      case class Complex(re: Double, im: Double) {
        def +(other: Complex) =
          Complex(re + other.re, im + other.im)

        def *(other: Complex) =
          Complex(re * other.re - im * other.im, re * other.im + other.re * im)

        def abs = math.sqrt(re * re + im * im)
      }

      val size = 600
      var oxmin, oxmax, oymin, oymax = 0.0
      var pressxy = (0.0, 0.0)
      var dragxy = (0.0, 0.0)
      var level = 102 // should be divisible by 3

      def color(i: Int) = {
        val band = level / 3
        if (i <= band)
          Color((i * 255.0 / band).toInt, 50, 100)
        else if (i <= 2 * band && i > band)
          Color(75, ((i - band) * 255.0 / band).toInt, 25)
        else
          Color(10, 30, ((i - 2 * band) * 255.0 / band).toInt)
      }

      // alternative way of coloring the set
      lazy val colors = Seq.tabulate(level + 1) { n =>
        Color(random(255 - n), random(255 - n), random(255 - n))
      }

      def color2(i: Int) = {
        colors(i)
      }

      def mandel(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Image = {
        oxmin = xmin;
        oxmax = xmax;
        oymin = ymin;
        oymax = ymax
        val img = image(size, size)
        for {
          xi <- 0 until size
          yi <- 0 until size
        } {
          val x = xmin + xi * (xmax - xmin) / size
          val y = ymin + yi * (ymax - ymin) / size
          var z = Complex(0, 0);
          val c = Complex(x, y)
          var i = 0
          while (z.abs < 2 && i < level) {
            z *= z;
            z += c;
            i += 1
          }
          if (z.abs < 2) setImagePixel(img, xi, yi, black)
          else setImagePixel(img, xi, yi, color(i))
        }
        img
      }

      cleari()
      val cDelta = Point(-size / 2, -size / 2)
      var pic = trans(cDelta.x, cDelta.y) -> Picture.image(mandel(-2, 1, -1.5, 1.5))
      draw(pic)
      installMouseHandlers(pic)
      var dragSq: Picture = Picture.rect(0, 0)

      def installMouseHandlers(p: Picture): Unit = {
        p.onMouseDrag { (x, y) =>
          val delx = x - pressxy._1
          val dely = y - pressxy._2
          val del = math.max(delx.abs, dely.abs)
          val newx = pressxy._1 + del * delx.sign
          val newy = pressxy._2 + del * dely.sign
          dragSq.erase()
          dragSq = trans(math.min(newx, pressxy._1), math.min(newy, pressxy._2)) -> Picture.rect(del, del)
          draw(dragSq)
          dragxy = (newx, newy)
        }

        p.onMouseRelease { (x, y) =>
          val bxmin = math.min(dragxy._1, pressxy._1) - cDelta.x
          val bxmax = math.max(dragxy._1, pressxy._1) - cDelta.x
          val bymin = math.min(dragxy._2, pressxy._2) - cDelta.y
          val bymax = math.max(dragxy._2, pressxy._2) - cDelta.y

          val delx = (oxmax - oxmin) / size
          val dely = (oymax - oymin) / size
          dragSq.erase()
          pic.erase()
          pic = trans(cDelta.x, cDelta.y) -> Picture.image(
            mandel(oxmin + delx * bxmin, oxmin + delx * bxmax, oymin + dely * bymin, oymin + dely * bymax)
          )
          pic.draw()
          installMouseHandlers(pic)
        }

        p.onMousePress { (x, y) =>
          pressxy = (x, y)
        }
      }
      def go() = {}
    }
    code.go()
  }
}
