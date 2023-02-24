package net.kogics.kojo
package turtle

import java.awt.Font
import java.awt.Image
import java.awt.Paint

import net.kogics.kojo.core.Point
import net.kogics.kojo.core.Style
import net.kogics.kojo.core.TurtleMover
import net.kogics.kojo.core.Voice

class TurtleWorldAPI(turtle0: => core.Turtle) extends TurtleMover {
  def forward(n: Double) = turtle0.forward(n)
  override def forward() = turtle0.forward()

  // all commands that have a UserCommand entry but no implementation come in via
  // RichTurtleCommands or TurtleMover
  def jumpTo(x: Double, y: Double) = turtle0.jumpTo(x, y)
  def position: Point = turtle0.position

  @deprecated("Use lineTo instead of moveTo", "2.7.08")
  def moveTo(x: Double, y: Double) = turtle0.moveTo(x, y)

  def turn(angle: Double) = turtle0.turn(angle)

  def towards(x: Double, y: Double) = turtle0.towards(x, y)
  def heading: Double = turtle0.heading
  def penDown() = turtle0.penDown()

  def penUp() = turtle0.penUp()

  def setPenColor(color: Paint) = turtle0.setPenColor(color)

  def setFillColor(color: Paint) = turtle0.setFillColor(color)

  def setPenCapJoin(cap: Int, join: Int) = turtle0.setPenCapJoin(cap, join)
  def setPenCapJoin(capJoin: (Int, Int)) = turtle0.setPenCapJoin(capJoin._1, capJoin._2)

  def setPenThickness(t: Double) = turtle0.setPenThickness(t)

  def setPenFontSize(n: Int) = turtle0.setPenFontSize(n)

  def setPenFont(font: Font) = turtle0.setPenFont(font)

  def saveStyle() = turtle0.saveStyle()

  def restoreStyle() = turtle0.restoreStyle()

  def savePosHe() = turtle0.savePosHe()

  def restorePosHe() = turtle0.restorePosHe()

  def beamsOn() = turtle0.beamsOn()

  def beamsOff() = turtle0.beamsOff()

  def invisible() = turtle0.invisible()

  def visible() = turtle0.visible()
  def write(text: String) = turtle0.write(text)

  def setAnimationDelay(d: Long) = turtle0.setAnimationDelay(d)

  def animationDelay = turtle0.animationDelay

  def playSound(voice: Voice) = turtle0.playSound(voice)

  def style: Style = turtle0.style

  override def arc2(r: Double, a: Double) = turtle0.arc2(r, a)
  override def ellipse(r1: Double, r2: Double) = turtle0.ellipse(r1, r2)

  def setCostumeImage(image: Image) = turtle0.setCostumeImage(image)
  def setCostume(costumeFile: String) = turtle0.setCostume(costumeFile)

  def setCostumes(costumeFiles: Vector[String]) = turtle0.setCostumes(costumeFiles)
  def setCostumeImages(images: Vector[Image]) = turtle0.setCostumeImages(images)
  def nextCostume() = turtle0.nextCostume()
  def scaleCostume(factor: Double) = turtle0.scaleCostume(factor)
  def changePosition(x: Double, y: Double) = turtle0.changePosition(x, y)
  def area = turtle0.area
  def perimeter = turtle0.perimeter

  // need to make this a class to get it to show up reliably in completions
  class Costume {
    val car = "/media/costumes/car.png"
    val pencil = "/media/costumes/pencil.png"
    val bat1 = "/media/costumes/bat1-a.png"
    val bat2 = "/media/costumes/bat1-b.png"
    val womanWaving = "/media/costumes/womanwaving.png"
  }

  class Background {
    val trainTrack = "/media/backgrounds/train-tracks3.gif"
  }

  class Sound {
    val medieval1 = "/media/music-loops/Medieval1.mp3"
  }
  def lastLine = turtle0.lastLine
  def lastTurn = turtle0.lastTurn
}
