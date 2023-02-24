package net.kogics.kojo
package lite

import java.awt.Color
import java.awt.Paint
import java.util.concurrent.Future

import edu.umd.cs.piccolo.activities.PActivity
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.TSCanvasFeatures
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.core.UnitLen

// Turtle and Staging Canvas
class DrawingCanvasAPI(val tCanvas: SCanvas) extends TSCanvasFeatures {
  def turtle0 = tCanvas.turtle0
  def clear() = tCanvas.clear()
  def cleari() = { clear(); turtle0.invisible() }
  def clearStepDrawing() = tCanvas.clearStepDrawing()

  def zoom(factor: Double) = tCanvas.zoom(factor)
  def zoom(factor: Double, cx: Double, cy: Double) = tCanvas.zoom(factor, cx, cy)
  def scroll(x: Double, y: Double) = tCanvas.scroll(x, y)

  def viewRotate(a: Double): Unit = tCanvas.viewRotate(a)
//  def showScale = tCanvas.
  def showGrid() = tCanvas.showGrid()

  def hideGrid() = tCanvas.hideGrid()

  def showAxes() = tCanvas.showAxes()

  def hideAxes() = tCanvas.hideAxes()

  def showProtractor() = tCanvas.showProtractor(-tCanvas.cbounds.getWidth / 2, -tCanvas.cbounds.getHeight / 2)
  def showProtractor(x: Double, y: Double) = tCanvas.showProtractor(x, y)
  def hideProtractor() = tCanvas.hideProtractor()
  def showScale() = tCanvas.showScale(-tCanvas.cbounds.getWidth / 2, tCanvas.cbounds.getHeight / 2)
  def showScale(x: Double, y: Double) = tCanvas.showScale(x, y)
  def hideScale() = tCanvas.hideScale()

  def newTurtle(): Turtle = newTurtle(0, 0)
  def newTurtle(x: Double = 0, y: Double = 0, costume: String = "/images/turtle32.png") =
    tCanvas.newTurtle(x, y, costume)

  def exportImage(filePrefix: String) = tCanvas.exportImage(filePrefix)
  def exportImage(filePrefix: String, width: Int, height: Int) = tCanvas.exportImage(filePrefix, width, height)
  def exportImageH(filePrefix: String, height: Int) = tCanvas.exportImageH(filePrefix, height)
  def exportImageW(filePrefix: String, width: Int) = tCanvas.exportImageW(filePrefix, width)
  def exportThumbnail(filePrefix: String, height: Int) = tCanvas.exportThumbnail(filePrefix, height)
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double) =
    tCanvas.zoomXY(xfactor, yfactor, cx, cy)

  def onKeyPress(fn: Int => Unit) = tCanvas.onKeyPress(fn)
  def onKeyRelease(fn: Int => Unit) = tCanvas.onKeyRelease(fn)
  def onMouseClick(fn: (Double, Double) => Unit) = tCanvas.onMouseClick(fn)
  def onMouseDrag(fn: (Double, Double) => Unit) = tCanvas.onMouseDrag(fn)
  def onMouseMove(fn: (Double, Double) => Unit) = tCanvas.onMouseMove(fn)

  val Pixel = core.Pixel
  val Inch = core.Inch
  val Cm = core.Cm
  def setUnitLength(ul: UnitLen) = tCanvas.setUnitLength(ul)
  def clearWithUL(ul: UnitLen) = tCanvas.clearWithUL(ul)
  def camScale = tCanvas.camScale
  def setBackgroundH(c1: Color, c2: Color) = tCanvas.setBackgroundH(c1, c2)
  def setBackgroundV(c1: Color, c2: Color) = tCanvas.setBackgroundV(c1, c2)
  def wipe() = tCanvas.wipe()
  def drawStage(fillc: Paint) = tCanvas.drawStage(fillc)
  def stage = tCanvas.stage
  def stageLeft = tCanvas.stageLeft
  def stageTop = tCanvas.stageTop
  def stageRight = tCanvas.stageRight
  def stageBot = tCanvas.stageBot
  def stageArea = tCanvas.stageArea
  def stageBorder = tCanvas.stage
  def timer(milliSeconds: Long)(fn: => Unit): Future[PActivity] = tCanvas.timer(milliSeconds)(fn)
  def animate(fn: => Unit) = tCanvas.animate(fn)
  def stopAnimationActivity(a: Future[PActivity]) = tCanvas.stopAnimationActivity(a)
  def onAnimationStart(fn: => Unit) = tCanvas.onAnimationStart(fn)
  def onAnimationStop(fn: => Unit) = tCanvas.onAnimationStop(fn)
  def resetPanAndZoom() = tCanvas.resetPanAndZoom()
  def disablePanAndZoom() = tCanvas.disablePanAndZoom()
}
