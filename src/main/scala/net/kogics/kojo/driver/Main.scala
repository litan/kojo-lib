package net.kogics.kojo.driver

import net.kogics.kojo.lite.KojoFrame
import net.kogics.kojo.util.Utils

object Main {
  def main(args: Array[String]): Unit = {

    val kojo = new KojoFrame
    val builtins = kojo.builtins

    import builtins._
    import CanvasAPI._
    import TurtleAPI._

    size(600, 600)
    cleari()
    originBottomLeft()
    setSpeed(superFast)
    setBackground(white)
    setPenColor(black)

    val tileCount = 10
    val tileSize = cwidth / tileCount
    println(cwidth, cheight)

    def shape() {
      repeat(4) {
        forward(tileSize)
        right(90)
      }
    }

    def block(posX: Double, posY: Double) {
      setPosition(posX, posY)
      shape()
    }

    repeatFor(rangeTill(0, cheight, tileSize)) { posY =>
      repeatFor(rangeTill(0, cwidth, tileSize)) { posX =>
        block(posX, posY)
      }
    }
  }
}
