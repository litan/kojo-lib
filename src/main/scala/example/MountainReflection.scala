package example

import net.kogics.kojo.lite.KojoFrame

object MountainReflection {
  def main(args: Array[String]): Unit = {
    val kojo = KojoFrame.create()
    val builtins = kojo.builtins

    import builtins._
    import DCanvas._
    import TurtleWorld._

    object code {
      // Mountain reflection usingregular picture transforms and a rondom seed
      size(780, 660)
      cleari()
      zoom(1.005)
      setBackground(black)
      initRandomGenerator(-3846417674807362996L)

      // Background
      val bgColor = cm.linearGradient(0, 0, Color(44, 185, 255), 0, cheight, ColorMaker.hsl(0, 0.40, 0.50), false)
      val bg = fillColor(bgColor) * penColor(noColor) * trans(0, cheight / 2) -> Picture.rectangle(cwidth, cheight / 2)

      // Mountains
      val mountain = Picture.fromVertexShape { s =>
        import s._
        beginShape()
        curveVertex(0, 0)
        curveVertex(0, 0)
        curveVertex(20, 25)
        curveVertex(45, 43)
        curveVertex(65, 45)
        curveVertex(100, 30)
        curveVertex(150, 0)
        curveVertex(225, 50)
        endShape()
      }

      val mColor = cm.linearGradient(0, 0, ColorMaker.hsl(35, 0.76, 0.05), 100, 50, cm.hsl(35, 0.75, 0.30), false)
      val mRevColor = cm.linearGradient(0, 0, cm.hsl(35, 0.75, 0.30), 100, 50, ColorMaker.hsl(35, 0.76, 0.05), false)

      def mountains(n: Int): Picture = {
        val mtn =
          if (randomBoolean)
            penColor(darkGray) * fillColor(mColor) -> mountain
          else
            penColor(darkGray) * fillColor(mRevColor) * flipY * trans(-100, 0) -> mountain

        if (n == 1) {
          mtn
        }
        else {
          picStack(
            mtn,
            trans(85, 0) -> mountains(n - 1)
          )
        }
      }
      // Mountains

      // Moon
      val moon = penColor(noColor) * fillColor(cm.silver) -> Picture {
        forward(50)
        right(90)
        right(90, 50)
        right(90)
        forward(50)
      }
      // Moon

      // Birds
      def bird(s: Double) = penColor(black) -> Picture {
        right(90)
        right(60, s)
        left(120)
        right(60, s)
      }

      def birds(n: Int, s: Double): Picture = {
        if (n == 1) {
          bird(s)
        }
        else {
          picStack(
            bird(s),
            trans(50, s * 0.6) -> birds(n - 1, s * 0.8)
          )
        }
      }
      // Birds

      // Moonlight
      val moonLight = distantLight(215, 5)
      // Moonlight

      val pic1 = picStack(
        bg,
        trans(cwidth, cheight) * rot(180) -> moon,
        trans(cwidth / 2 - 50, cheight / 2 + 100) -> birds(8, 50),
        trans(0, cheight / 2) -> mountains(9)
      )

      val pic = effect(moonLight) -> picStack(
        penColor(noColor) * fillColor(Color(44, 185, 255, 100)) * trans(0, cheight / 2 - 25) ->
          Picture.rectangle(cwidth, 50),
        pic1,
        flipX * trans(0, -cheight + 3) * fade(400) * blur(20) ->
          pic1
      )

      draw(trans(-cwidth / 2, -cheight / 2) -> pic)

      def go(): Unit = {}
    }
    code.go()
  }
}
