package net.kogics.kojo.driver

import net.kogics.kojo.lite.KojoFrame

object Main {
  def main(args: Array[String]): Unit = {

    val kojo = new KojoFrame
    val builtins = kojo.builtins

    import builtins.TSCanvas._
    import builtins.Tw._
    import builtins._

    def spiral(size: Int, angle: Int) {
      if (size <= 300) {
        forward(size)
        right(angle)
        spiral(size + 2, angle)
      }
    }
    clear()
    setPenColor(darkGray)
    setFillColor(green)
    setBackgroundH(red, yellow)
    setPenThickness(1)
    setSpeed(fast)
    spiral(0, 91)
  }
}
