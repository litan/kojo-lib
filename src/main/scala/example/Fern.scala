package example

import net.kogics.kojo.lite.KojoFrame

object Fern {
  def main(args: Array[String]): Unit = {
    val kojo = KojoFrame.create()
    val builtins = kojo.builtins

    import builtins._
    import TSCanvas._
    import Tw._

    // `Fern` sample from Kojo Showcase menu

    def fern(x: Double): Unit = {
      if (x > 1) {
        saveStyle()
        setPenThickness(x / 10 + 1)
        setPenColor(Color(0, math.abs(200 - x * 3).toInt, 40))
        forward(x)
        right(100)
        fern(x * 0.4)
        left(200)
        fern(x * 0.4)
        right(95)
        fern(x * 0.8)
        right(5)
        back(x)
        restoreStyle()
      }
    }

    def fernp = Picture {
      fern(50)
    }

    cleari()
    setAnimationDelay(10)
    setBackgroundV(Color(255, 255, 150), white)
    val pic = picCol(
      flipX -> (fade(230) * blur(2) -> fernp),
      trans(-20, 0) * penColor(Color(234, 234, 234)) * penWidth(1) -> Picture.hline(40),
      trans(0, 3) -> fernp
    )
    draw(pic)
  }
}
