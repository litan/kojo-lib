package example

import net.kogics.kojo.lite.KojoFrame

object ParticleLife {
  def main(args: Array[String]): Unit = {
    val kojo = KojoFrame.create()
    val builtins = kojo.builtins

    import builtins._
    import DCanvas._
    import TurtleWorld._

    object code {
      // written in OO style

      cleari()
      originBottomLeft()
      drawStage(black)
      setRefreshRate(8)
      initRandomGenerator()
      // initRandomGenerator(1676480160006L)
      // initRandomGenerator(1676915063713L)

      val randomInteraction = true
      val cb = canvasBounds

      setNoteInstrument(Instrument.PIANO)
      playNote(50, 200)
      playNote(45, 300)
      pauseMillis(600)

      // val PSeq = Array; type PSeq[A] = Array[A]
      // val PSeq = ArrayBuffer; type PSeq[A] = ArrayBuffer[A]
      val PSeq = Vector; type PSeq[A] = Vector[A]

      val psize = 4
      val N = 3000
      val Nc = N / 3

      val colors: PSeq[Color] = PSeq(cm.red, cm.lightBlue, cm.yellow)

      val interactionMatrix =
        if (randomInteraction)
          PSeq(
            PSeq.fill(3)(randomDouble(-1, 1)),
            PSeq.fill(3)(randomDouble(-1, 1)),
            PSeq.fill(3)(randomDouble(-1, 1)),
          )
        else
          PSeq(
            PSeq(-0.1, -0.34, 0),
            PSeq(-0.17, -0.32, 0.34),
            PSeq(0, -0.2, 0.15),
          )

      class Particle(x0: Double, y0: Double, color: Color) {
        var location = Vector2D(x0, y0)
        val colorIdx = colors.indexOf(color)
        private var velocity = Vector2D(0, 0)
        private var stepForce = Vector2D(0, 0)

        def stepStart(): Unit = {
          stepForce = Vector2D(0, 0)
          particles.foreach { p =>
            if (p != this) {
              val d = location.distance(p.location)
              if (d > 0 && d < 80) {
                val g = interactionMatrix(colorIdx)(p.colorIdx)
                if (g != 0) {
                  val f = g / d
                  MusicMaker.addDistance(d)
                  val delta = location - p.location
                  stepForce += delta * f / 1
                }
              }
              else {
                MusicMaker.addSpace(d)
              }
            }
          }
        }

        def stepEnd(): Unit = {
          velocity = (velocity + stepForce) / 2
          location += velocity
          var newx = if (location.x < 0) cwidth - psize else location.x
          newx = if (location.x > cwidth) psize else newx
          var newy = if (location.y < 0) cheight - psize else location.y
          newy = if (location.y > cheight) psize else newy
          if (newx != location.x || newy != location.y) {
            location = Vector2D(newx, newy)
          }
        }

        def show(canvas: CanvasDraw): Unit = {
          canvas.stroke(color)
          canvas.point(location.x, location.y)
        }
      }

      object MusicMaker {
        private var totalDistance = 0.0
        private var distanceN = 0
        private var avgDistance = 0.0
        private var totalSpace = 0.0
        private var spaceN = 0
        private var avgSpace = 0.0
        private var prevNoteIdx = 0
        val raagPitches =
          PSeq(48, 49, 52, 53, 55, 56, 59, 60, 61, 64, 65, 67, 68, 71, 72)

        def stepStart(): Unit = {
          totalDistance = 0
          distanceN = 0
          totalSpace = 0
          spaceN = 0
        }

        def addDistance(d: Double): Unit = {
          totalDistance += d
          distanceN += 1
        }

        def addSpace(s: Double): Unit = {
          totalSpace += s
          spaceN += 1
        }

        def stepEnd(): Unit = {
          avgDistance = totalDistance / distanceN
          avgSpace = totalSpace / spaceN
          //                println(noteNum)
        }

        // play with these to generate more expressive music
        def noteNumRangeMin = 5
        def noteNumRangeMax = 60
        def noteNum = {
          val f1 = distanceN.toDouble / (N * (N - 1)) * 100
          f1 + avgDistance
        }

        def show(): Unit = {
          var noteIdx = mathx
            .map(
              noteNum,
              noteNumRangeMin,
              noteNumRangeMax,
              0,
              raagPitches.length - 1
            )
            .toInt
          noteIdx = mathx.constrain(noteIdx, 0, raagPitches.length - 1).toInt
          if (noteIdx == prevNoteIdx) {
            if (noteIdx == 0) {
              noteIdx = 2
            }
            else if (noteIdx == raagPitches.length - 1) {
              noteIdx = raagPitches.length - 3
            }
            else {
              val idxDelta = if (randomBoolean) 2 else -2
              noteIdx += idxDelta
            }
          }
          noteIdx = mathx.constrain(noteIdx, 0, raagPitches.length - 1).toInt
          prevNoteIdx = noteIdx
          playNote(raagPitches(noteIdx), 30)
        }
      }

      def makeParticle(c: Color) = {
        new Particle(random(psize, cwidth - psize), random(psize, cheight - psize), c)
      }

      val particles = PSeq.fill(Nc)(makeParticle(colors(0))) ++
        PSeq.fill(Nc)(makeParticle(colors(1))) ++
        PSeq.fill(Nc)(makeParticle(colors(2)))

      def setupCanvas(canvas: CanvasDraw): Unit = {
        canvas.strokeWeight(4)
      }

      animateWithSetupCanvasDraw(setupCanvas) { canvas =>
        MusicMaker.stepStart()
        particles.foreach { p =>
          p.stepStart()
        }

        MusicMaker.stepEnd()
        canvas.background(black)
        particles.foreach { p =>
          p.stepEnd()
          p.show(canvas)
        }
        MusicMaker.show()
      }
      showFps(white, 15)

      def go(): Unit = {}
    }
    code.go()
  }
}
