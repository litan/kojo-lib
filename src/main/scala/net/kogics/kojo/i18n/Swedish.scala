package net.kogics.kojo

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.lite.KojoFrame
import net.kogics.kojo.xscala.RepeatCommands
import net.kogics.kojo.core.Turtle
import java.awt.Color

/**  The Swedish and English api can be accessed via this one-line import:
  *  import net.kogics.kojo.Swedish.*, padda.*, CanvasAPI.*, TurtleAPI.*
  */
object Swedish {
  val builtins = English.frame.builtins
  val CanvasAPI = builtins.CanvasAPI
  val TurtleAPI = builtins.TurtleAPI

  trait SwedishTurtle { 
    // I know it's controversial not to use empty parens on def sudda: Unit
    // but kids don't like cryptic () and we have tons of exercises with sudda 
    // Scala 3 is more picky about this than Scala 2, which allowed whatever
    def englishTurtle: Turtle
    def sudda = englishTurtle.clear()
    def synlig = englishTurtle.visible()
    def osynlig = englishTurtle.invisible()
    def fram(steg: Double) = englishTurtle.forward(steg)
    def fram = englishTurtle.forward(25)
    def höger(vinkel: Double) = englishTurtle.right(vinkel)
    def höger = englishTurtle.right(90)
    def vänster(vinkel: Double) = englishTurtle.left(vinkel)
    def vänster = englishTurtle.left(90)
    def hoppaTill(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def gåTill(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def hoppa(steg: Double) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(steg) //hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def hoppa: Unit = hoppa(25)
    def hem = englishTurtle.home()
    def mot(x: Double, y: Double) = englishTurtle.towards(x, y)
    def sättVinkel(vinkel: Double) = englishTurtle.setHeading(vinkel)
    def vinkel = englishTurtle.heading
    def öster = englishTurtle.setHeading(0)
    def väster = englishTurtle.setHeading(180)
    def norr = englishTurtle.setHeading(90)
    def söder = englishTurtle.setHeading(-90)
    def sakta(n: Long) = englishTurtle.setAnimationDelay(n)
    def skriv(t: Any) = englishTurtle.write(t)
    def textstorlek(s: Int) = englishTurtle.setPenFontSize(s)
    def båge(radie: Double, vinkel: Double) = englishTurtle.arc(radie, math.round(vinkel).toInt)
    def cirkel(radie: Double) = englishTurtle.circle(radie)
    def läge = englishTurtle.position
    def pennaNer = englishTurtle.penDown()
    def pennaUpp = englishTurtle.penUp()
    def pennanÄrNere = englishTurtle.style.down
    def färg(c: java.awt.Color) = englishTurtle.setPenColor(c)
    def fyll(c: java.awt.Color) = englishTurtle.setFillColor(c)
    def bredd(n: Double) = englishTurtle.setPenThickness(n)
    def sparaStil = englishTurtle.saveStyle()
    def laddaStil = englishTurtle.restoreStyle()
    def sparaLägeRiktning = englishTurtle.savePosHe()
    def laddaLägeRiktning = englishTurtle.restorePosHe()
    def siktePå = englishTurtle.beamsOn()
    def sikteAv = englishTurtle.beamsOff()
    def kostym(filNamn: String) = englishTurtle.setCostume(filNamn)
    def kostymer(filNamn: String*) = englishTurtle.setCostumes(filNamn: _*)
    def nästaKostym = englishTurtle.nextCostume()
  }
  class Padda(override val englishTurtle: Turtle) extends SwedishTurtle {
    def this(startX: Double, startY: Double, kostymFilNamn: String) = this(CanvasAPI.newTurtle(startX, startY, kostymFilNamn))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0,0)
  }
  class Padda0(t0: => Turtle) extends SwedishTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object padda extends Padda0(CanvasAPI.turtle0)
  //def sudda() = builtins.clear()
  //def suddaUtdata() = builtins.clearOutput()
  lazy val blå = builtins.blue 
  lazy val röd = builtins.red 
  lazy val gul = builtins.yellow 
  lazy val grön = builtins.green 
  lazy val lila = builtins.purple
  lazy val rosa = builtins.pink 
  lazy val brun = builtins.brown 
  lazy val svart = builtins.black 
  lazy val vit = builtins.white
  lazy val genomskinlig = builtins.noColor
  def bakgrund(färg: Color) = builtins.setBackground(färg)
  def bakgrund2(färg1: Color, färg2: Color) = CanvasAPI.setBackgroundV(färg1, färg2)
  object KcSwe { //Key codes for Swedish keys
    lazy val VK_Å = 197
    lazy val VK_Ä = 196
    lazy val VK_Ö = 214
  }

  //loops in Swedish
  def upprepa(n: Int)(block: => Unit): Unit = {
    RepeatCommands.repeat(n){ block }
  }

  def räkneslinga(n: Int)(block: Int => Unit): Unit = {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def sålänge(villkor: => Boolean)(block: => Unit): Unit = {
    RepeatCommands.repeatWhile (villkor) { block }
  }  

  //simple IO
  def indata(ledtext: String = "") =  builtins.readln(ledtext)

  //math functions
  def avrunda(tal: Number, antalDecimaler: Int = 0): Double = {
    val faktor = math.pow(10, antalDecimaler).toDouble
    math.round(tal.doubleValue * faktor).toLong / faktor
  }
  def slumptal(n: Int) = builtins.random(n)
  def slumptalMedDecimaler(n: Int) = builtins.randomDouble(n)

  //some type aliases in Swedish
  type Heltal = Int
  type Decimaltal = Double
  type Sträng = String

  //speedTest
  def systemtid = BigDecimal(System.nanoTime) / BigDecimal("1000000000") //sekunder

  @annotation.nowarn def räknaTill(n: BigInt): Unit = {
    var c: BigInt = 1
    print("*** Räknar från 1 till ... ")
    val startTid = systemtid
    while (c < n) { c = c + 1 } //tar tid om n är stort
    val stoppTid = systemtid
    println("" + n + " *** KLAR!")
    val tid = stoppTid - startTid
    print("Det tog ")
    if (tid < 0.1)
      println((tid * 1000).round(new java.math.MathContext(2)).toString + " millisekunder.")
    else println((tid * 10).toLong / 10.0 + " sekunder.")
  }
}
