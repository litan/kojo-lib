/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.kojo
package lite

import java.awt.geom.GeneralPath
import java.awt.image.{BufferedImage, BufferedImageOp}
import java.awt.{Paint, Toolkit}
import java.net.URL

import com.jhlabs.image.AbstractBufferedImageOp
import com.jhlabs.image.LightFilter.Light
import javax.swing.JComponent
import net.kogics.kojo.core.{VertexShape, Voice}
import net.kogics.kojo.picture.{DslImpl, PicCache, PicDrawingDsl}
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.{Throttler, Utils}
import net.kogics.kojo.xscala.{CodeCompletionUtils, Help, RepeatCommands}

import scala.language.implicitConversions
import scala.swing.Graphics2D

// a static instance is needed for the compiler prefix code 
object Builtins {
  @volatile var instance: Builtins = _
}

class Builtins(
                val TSCanvas: DrawingCanvasAPI,
                val Tw: TurtleWorldAPI,
                val Staging: staging.API,
                mp3player: music.KMp3,
                fuguePlayer: music.FuguePlayer,
                val kojoCtx: core.KojoCtx,
              ) extends CoreBuiltins with RepeatCommands {
  builtins =>
  Builtins.instance = this

  import language.implicitConversions

  val tCanvas = TSCanvas.tCanvas

  val Costume = new Tw.Costume
  val Background = new Tw.Background
  val Sound = new Tw.Sound

  def retainSingleLineCode() = {}

  def clearSingleLineCode() = {}

  def readln(prompt: String): String = kojoCtx.readInput(prompt)

  def readInt(prompt: String): Int = readln(prompt).toInt

  def readDouble(prompt: String): Double = readln(prompt).toDouble
  def setAstStopPhase(phase: String): Unit = kojoCtx.setAstStopPhase(phase)

  def astStopPhase = kojoCtx.astStopPhase
  type Melody = core.Melody
  val Melody = core.Melody

  type Rhythm = core.Rhythm
  val Rhythm = core.Rhythm

  val MusicScore = core.Score

  def playMusic(voice: Voice, n: Int = 1): Unit = {
    fuguePlayer.playMusic(voice, n)
  }

  def playMusicUntilDone(voice: Voice, n: Int = 1): Unit = {
    fuguePlayer.playMusicUntilDone(voice, n)
  }

  def playMusicLoop(voice: Voice): Unit = {
    fuguePlayer.playMusicLoop(voice)
  }

  def runInBackground(code: => Unit) = Utils.runAsyncMonitored(code)

  def runInGuiThread(code: => Unit) = Utils.runInSwingThread(code)

  def runInDrawingThread(code: => Unit) = Utils.runInSwingThread(code)

  def evalInDrawingThread[T](fn: => T) = Utils.runInSwingThreadAndWait(fn)

  def schedule(seconds: Double)(code: => Unit) = Utils.schedule(seconds)(code)

  def scheduleN(n: Int, seconds: Double)(code: => Unit) = Utils.scheduleRecN(n, seconds)(code)

  @deprecated("Use Color instead", "2.7")
  def color(rgbHex: Int) = new Color(rgbHex)

  type Painter = picture.Painter
  type Pic = picture.Pic
  type Pic0 = picture.Pic0
  val picRow = HPics
  val picCol = VPics
  val picStack = GPics
  val picBatch = BatchPics
  @deprecated("Use picRowCentered instead of picRow2", "2.7.08")
  val picRow2 = HPics2
  @deprecated("Use picColCentered instead of picCol2", "2.7.08")
  val picCol2 = VPics2
  @deprecated("Use picStackCentered instead of picStack2", "2.7.08")
  val picStack2 = GPics2
  val picRowCentered = HPics2
  val picColCentered = VPics2
  val picStackCentered = GPics2

  val rotp = picture.rotp _
  val opac = picture.opac _
  val hue = picture.hue _
  val sat = picture.sat _
  val brit = picture.brit _
  val light = picture.brit _
  val offset = picture.offset _
  val flip = picture.flipY
  val flipY = picture.flipY
  val flipX = picture.flipX
  val axes = picture.axesOn
  val fillColor = picture.fill _
  val penColor = picture.stroke _
  val penWidth = picture.strokeWidth _
  val penThickness = picture.strokeWidth _

  def noPen() = transform(_.setNoPen())

  val spin = picture.spin _
  val reflect = picture.reflect _
  val row = picture.row _
  val col = picture.col _

  val fade = picture.fade _
  val blur = picture.blur _
  val pointLight = picture.pointLight _
  val spotLight = picture.spotLight _

  def lights(lights: Light*) = picture.lights(lights: _*)

  val PointLight = picture.PointLight _
  val SpotLight = picture.SpotLight _
  val noise = picture.noise _
  val weave = picture.weave _

  def effect(name: Symbol, props: Tuple2[Symbol, Any]*) = picture.effect(name, props: _*)

  def effect(filter: BufferedImageOp) = picture.ApplyFilterc(filter)

  type ImageOp = picture.ImageOp

  def effect(filterOp: ImageOp) = {
    val filterOp2 = new AbstractBufferedImageOp {
      def filter(src: BufferedImage, dest: BufferedImage) = filterOp.filter(src)
    }
    picture.ApplyFilterc(filterOp2)
  }

  // put api functions here to enable code completion right from function definitions
  def transform(fn: Picture => Unit) = preDrawTransform(fn)

  def preDrawTransform(fn: Picture => Unit) = picture.PreDrawTransformc(fn)

  def postDrawTransform(fn: Picture => Unit) = picture.PostDrawTransformc(fn)

  implicit val _picCanvas = tCanvas

  def pict(painter: Painter) = picture.Pic(painter)

  def PictureT(painter: Painter) = picture.Pic(painter)

  def Picture(fn: => Unit) = picture.Pic0 { t =>
    fn
  }

  def drawAndHide(pictures: Picture*) = pictures.foreach { p => p.draw(); p.invisible() }

  def drawCentered(pic: Picture): Unit = {
    pic.invisible()
    pic.draw()
    center(pic)
    pic.visible()
  }

  def center(pic: Picture): Unit = {
    val cb = canvasBounds;
    val pb = pic.bounds
    val xDelta = cb.getMinX - pb.getMinX + (cb.width - pb.width) / 2
    val yDelta = cb.getMinY - pb.getMinY + (cb.height - pb.height) / 2
    pic.offset(xDelta, yDelta)
  }

  def show(pictures: Picture*): Unit = {
    throw new UnsupportedOperationException("Use draw(pic/s) instead of show(pic/s)")
  }

  def setRefreshRate(fps: Int): Unit = {
    require(fps >= 1 && fps <= 200, "FPS needs to be in the range: 1 to 200")
    kojoCtx.fps = fps
  }

  def isKeyPressed(key: Int) = staging.Inputs.isKeyPressed(key)

  def filterPicture(p: Picture, filter: BufferedImageOp): Picture = {
    drawCentered(p)
    p.scale(1, -1)
    val img = p.toImage
    p.erase()
    Picture.image(filterImage(img, filter))
  }

  def filterImage(img: BufferedImage, filter: BufferedImageOp): BufferedImage = {
    filter.filter(img, null)
  }

  def setDrawingCanvasAspectRatio(r: Double): Unit = Utils.runLaterInSwingThread {
  }

  def setDrawingCanvasSize(width: Int, height: Int): Unit = Utils.runLaterInSwingThread {
  }

  def setDrawingCanvasToA4(): Unit = {
    setDrawingCanvasAspectRatio(A4.aspectRatio)
  }

  def setDrawingCanvasToA4Landscape(): Unit = {
    setDrawingCanvasAspectRatio(A4Landscape.aspectRatio)
  }

  def setDrawingCanvasToA3(): Unit = {
    setDrawingCanvasAspectRatio(A3.aspectRatio)
  }

  def setDrawingCanvasToA3Landscape(): Unit = {
    setDrawingCanvasAspectRatio(A3Landscape.aspectRatio)
  }

  def setDrawingCanvasToA2(): Unit = {
    setDrawingCanvasAspectRatio(A2.aspectRatio)
  }

  def setDrawingCanvasToA2Landscape(): Unit = {
    setDrawingCanvasAspectRatio(A2Landscape.aspectRatio)
  }

  def setDrawingCanvasToA1(): Unit = {
    setDrawingCanvasAspectRatio(A1.aspectRatio)
  }

  def setDrawingCanvasToA1Landscape(): Unit = {
    setDrawingCanvasAspectRatio(A1Landscape.aspectRatio)
  }

  def setDrawingCanvasToA0(): Unit = {
    setDrawingCanvasAspectRatio(A0.aspectRatio)
  }

  def setDrawingCanvasToA0Landscape(): Unit = {
    setDrawingCanvasAspectRatio(A0Landscape.aspectRatio)
  }

  val hueMod = Utils.hueMod _
  val satMod = Utils.satMod _
  val britMod = Utils.britMod _
  val lightMod = Utils.lightMod _

  type Vector2D = util.Vector2D
  val Vector2D = util.Vector2D

  def preloadMp3(mp3File: String): Unit = {
    mp3player.preloadMp3(mp3File)
  }

  def playMp3(mp3File: String): Unit = {
    mp3player.playMp3(mp3File)
  }


  def playMp3Sound(mp3File: String): Unit = {
    mp3player.playMp3Sound(mp3File)
  }


  def playMp3Loop(mp3File: String): Unit = {
    mp3player.playMp3Loop(mp3File)
  }


  def canvasBounds = tCanvas.cbounds

  def setBackground(c: Paint) = tCanvas.setCanvasBackground(c)

  def isMp3Playing = mp3player.isMp3Playing

  def isMusicPlaying = fuguePlayer.isMusicPlaying

  def stopMp3() = mp3player.stopMp3()

  def stopMp3Loop() = mp3player.stopMp3Loop()

  def stopMusic() = fuguePlayer.stopMusic()

  def newMp3Player = new music.KMp3(kojoCtx)

  def addCodeTemplates(lang: String, templates: Map[String, String]): Unit = {
    CodeCompletionUtils.addTemplates(lang, templates)
  }

  def addHelpContent(lang: String, content: Map[String, String]): Unit = {
    Help.addContent(lang, content)
  }

  //  def bounceVecOffStage(v: Vector2D, p: Picture): Vector2D =
  //    picture.bounceVecOffStage(v, p)
  def bouncePicVectorOffStage(p: Picture, v: Vector2D): Vector2D = bouncePicVectorOffPic(p, v, TSCanvas.stageBorder)

  def bouncePicVectorOffPic(pic: Picture, v: Vector2D, obstacle: Picture): Vector2D =
    picture.bouncePicVectorOffPic(pic, v, obstacle, Random)

  def bouncePicOffStage(pic: Picture, vel: Vector2D): Vector2D = picture.bounceVecOffStage(vel, pic)

  def bouncePicOffPic(pic: Picture, vel: Vector2D, obstacle: Picture): Vector2D =
    picture.bouncePicVectorOffPic(pic, vel, obstacle, Random)

  def mouseX = staging.Inputs.mousePos.x

  def mouseY = staging.Inputs.mousePos.y

  def mousePosition = staging.Inputs.mousePos

  def isMousePressed: Boolean = staging.Inputs.mousePressedFlag

  def isMousePressed(button: Int): Boolean = {
    staging.Inputs.mousePressedFlag && mouseButton == button
  }

  def mouseButton: Int = staging.Inputs.mouseBtn

  def screenDPI = kojoCtx.screenDPI

  def setScreenDPI(dpi: Int): Unit = {
    kojoCtx.screenDPI = dpi
  }

  def screenSize = Toolkit.getDefaultToolkit.getScreenSize

  def hiDpiFontIncrease = kojoCtx.hiDpiFontIncrease

  def baseFontSize = kojoCtx.baseFontSize

  def isTracing = false

  def TexturePaint(file: String, x: Double, y: Double) =
    cm.texture(file, x, y)

  val PShapes = Picture
  val PicShape = Picture

  def url(url: String) = new URL(url)

  object Picture {
    def text(content: Any, fontSize: Int = 15) = picture.textu(content, fontSize, red)

    def textu(content: Any, fontSize: Int = 15, color: Color = red) = picture.textu(content, fontSize, color)

    def rect(h: Double, w: Double) = picture.rect(h, w)

    def rectangle(width: Double, height: Double) = picture.rect2(width, height)

    // def rectangle(x: Double, y: Double, w: Double, h: Double) = picture.offset(x, y) -> picture.rect2(w, h)
    def vline(l: Double) = picture.vline(l)

    def hline(l: Double) = picture.hline(l)

    def line(width: Double, height: Double) = picture.line(width, height)

    // def line(x1: Double, y1: Double, x2: Double, y2: Double) = picture.offset(x1, y1) -> picture.line(x2 - x1, y2 - y1)
    def fromPath(fn: GeneralPath => Unit) = picture.fromPath {
      val path = new GeneralPath();
      fn(path)
      path
    }

    def fromVertexShape(fn: VertexShape => Unit) = picture.fromPath {
      val path = new GeneralPath();
      fn(new VertexShape(path))
      path
    }

    def fromTurtle(fn: Turtle => Unit) = PictureT(fn)

    def fromCanvas(width: Double, height: Double)(fn: Graphics2D => Unit) = picture.fromJava2d(width, height, fn)

    def circle(radius: Double) = picture.circle(radius)

    // def circle(x: Double, y: Double, r: Double) = picture.offset(x, y) -> picture.circle(r)
    def ellipse(xRadius: Double, yRadius: Double) = picture.ellipse(xRadius, yRadius)

    def ellipseInRect(width: Double, height: Double) = picture.trans(width / 2, height / 2) -> picture.ellipse(width / 2, height / 2)

    // def ellipse(x: Double, y: Double, rx: Double, ry: Double) = picture.offset(x, y) -> picture.ellipse(rx, ry)
    def arc(radius: Double, angle: Double) = picture.arc(radius, angle)

    def point = picture.trans(0, -0.01 / 2) -> line(0, 0.01)

    def image(fileName: String): Picture = {
      if (fileName.startsWith("http")) {
        image(url(fileName))
      }
      else {
        picture.image(fileName, None)
      }
    }

    def image(fileName: String, envelope: Picture): Picture = {
      if (fileName.startsWith("http")) {
        image(url(fileName), envelope)
      }
      else {
        picture.image(fileName, Some(envelope))
      }
    }

    def image(url: URL) = picture.image(url, None)

    def image(url: URL, envelope: Picture) = picture.image(url, Some(envelope))

    def image(image: Image) = picture.image(image, None)

    def image(image: Image, envelope: Picture) = picture.image(image, Some(envelope))

    def widget(component: JComponent) = picture.widget(component)

    def button(label: String)(fn: => Unit) = widget(Button(label)(fn))

    def effectablePic(pic: Picture) = picture.effectablePic(pic)

    def hgap(gap: Double) = penColor(noColor) * penThickness(0.001) -> Picture.rectangle(gap, 0.001)

    def vgap(gap: Double) = penColor(noColor) * penThickness(0.001) -> Picture.rectangle(0.001, gap)

    def showBounds(pics: Picture*) = Utils.runInSwingThread {
      pics.foreach { pic =>
        val b = pic.tnode.getGlobalFullBounds
        val bpic = trans(b.x, b.y) -> rectangle(b.width, b.height)
        draw(bpic)
      }
    }

    def showAxes(pics: Picture*) = {
      pics.foreach { pic =>
        pic.axesOn()
      }
    }
  }

  object PictureMaker {
    private def placeAndDraw(pic: Picture, x: Double, y: Double) = {
      pic.setPosition(x, y)
      pic.draw()
      pic
    }

    def rectangle(x: Double, y: Double, width: Double, height: Double) = {
      val pic = Picture.rectangle(width, height)
      placeAndDraw(pic, x, y)
    }

    def ellipse(x: Double, y: Double, width: Double, height: Double) = {
      val pic = Picture.ellipse(width / 2, height / 2)
      placeAndDraw(pic, x, y)
    }

    def line(x1: Double, y1: Double, x2: Double, y2: Double) = {
      val pic = Picture.line(x2 - x1, y2 - y1)
      placeAndDraw(pic, x1, y1)
    }

    def fromPath(fn: GeneralPath => Unit) = {
      val pic = Picture.fromPath(fn)
      placeAndDraw(pic, 0, 0)
    }

    def fromVertexShape(fn: VertexShape => Unit) = {
      val pic = Picture.fromVertexShape(fn)
      placeAndDraw(pic, 0, 0)
    }
  }

  val pm = PictureMaker
  type Widget = JComponent
  type TextField[A] = widget.TextField[A]
  type TextArea = widget.TextArea
  type Label = widget.Label
  type Button = widget.Button
  type ToggleButton = widget.ToggleButton
  type DropDown[A] = widget.DropDown[A]
  type Slider = widget.Slider
  type RowPanel = widget.RowPanel
  type ColPanel = widget.ColPanel
  val TextField = widget.TextField
  val TextArea = widget.TextArea
  val Label = widget.Label
  val Button = widget.Button
  val ToggleButton = widget.ToggleButton
  val DropDown = widget.DropDown
  val Slider = widget.Slider
  val RowPanel = widget.RowPanel
  val ColPanel = widget.ColPanel

  def showFps(color: Color = black, fontSize: Int = 15): Unit = {
    val cb = canvasBounds
    @volatile var frameCnt = 0
    val fpsLabel = Picture.textu("Fps: ", fontSize, color)
    fpsLabel.setPosition(cb.x + 10, cb.y + cb.height - 10)
    draw(fpsLabel)
    fpsLabel.forwardInputTo(TSCanvas.stageArea)

    TSCanvas.timer(1000) {
      fpsLabel.update(s"Fps: $frameCnt")
      frameCnt = 0
    }
    fpsLabel.react { self =>
      frameCnt += 1
    }
  }

  def drawCenteredMessage(message: String, color: Color = black, fontSize: Int = 15): Unit = {
    val cb = canvasBounds
    val te = textExtent(message, fontSize)
    val pic = penColor(color) *
      trans(cb.x + (cb.width - te.width) / 2, cb.y + (cb.height - te.height) / 2 + te.height) ->
      PicShape.text(message, fontSize)
    draw(pic)
  }

  def stopAnimation() = {
    Utils.stopMonitoredThreads()
    tCanvas.stopAnimation()
    fuguePlayer.stopMusic()
    fuguePlayer.stopBgMusic()
    mp3player.stopMp3()
    mp3player.stopMp3Loop()
  }

  def showGameTime(limitSecs: Int, endMsg: String, color: Color = black, fontSize: Int = 15, dx: Double = 10, dy: Double = 50): Unit = {
    val cb = canvasBounds
    @volatile var gameTime = 0
    val timeLabel = trans(cb.x + dx, cb.y + dy) -> PicShape.textu(gameTime, fontSize, color)
    draw(timeLabel)
    timeLabel.forwardInputTo(TSCanvas.stageArea)

    TSCanvas.timer(1000) {
      gameTime += 1
      timeLabel.update(gameTime)

      if (gameTime == limitSecs) {
        drawCenteredMessage(endMsg, color, fontSize * 2)
        stopAnimation()
      }
    }
  }

  type Shape = PicDrawingDsl

  object Shape {
    def clear() = TSCanvas.cleari()

    def rectangle(w: Double, h: Double): Shape = DslImpl(picture.rect(h, w))

    def square(l: Double): Shape = rectangle(l, l)

    def circle(r: Double): Shape = DslImpl(picture.circle(r)).translated(r, r)

    def gap(w: Double, h: Double) = rectangle(w, h) outlined (noColor)

    def vline(l: Double): Shape = DslImpl(picture.vline(l))

    def hline(l: Double): Shape = DslImpl(picture.hline(l))

    def text(string: Any, fontSize: Int = 15): Shape =
      DslImpl(picture.textu(string, fontSize, black)).translated(0, textExtent(string.toString, fontSize).height)

    def image(file: String) = DslImpl(picture.image(file, None))

    def turtleMade(fn: => Unit): Shape = DslImpl(Picture(fn))

    def stack(shapes: Shape*): Shape = DslImpl(picture.GPics2(shapes.map(s => PicCache.freshPic(s.pic)).toList))

    def row(shapes: Shape*): Shape = DslImpl(picture.HPics2(shapes.map(s => PicCache.freshPic(s.pic)).toList))

    def col(shapes: Shape*): Shape = DslImpl(picture.VPics2(shapes.map(s => PicCache.freshPic(s.pic)).toList))

    def stack2(shapes: Shape*): Shape = DslImpl(picture.GPics(shapes.map(s => PicCache.freshPic(s.pic)).toList))

    def row2(shapes: Shape*): Shape = DslImpl(picture.HPics(shapes.map(s => PicCache.freshPic(s.pic)).toList))

    def col2(shapes: Shape*): Shape = DslImpl(picture.VPics(shapes.map(s => PicCache.freshPic(s.pic)).toList))

    def draw2(shapes: Shape*) = shapes.foreach {
      _.draw()
    }

    def draw(shapes: Shape*): Unit = {
      def center(shape: Shape) = {
        val cb = canvasBounds;
        val sb = shape.pic.bounds
        val xDelta = cb.getMinX - sb.getMinX + (cb.width - sb.width) / 2
        val yDelta = cb.getMinY - sb.getMinY + (cb.height - sb.height) / 2
        shape.pic.offset(xDelta, yDelta)
      }

      if (shapes.size > 1) {
        val shapeStack = stack(shapes: _*)
        shapeStack.pic.invisible()
        shapeStack.draw()
        center(shapeStack)
        shapeStack.pic.visible()
      }
      else {
        val shape = shapes(0)
        shape.pic.invisible()
        shape.draw()
        center(shape)
        shape.pic.visible()
      }
    }
  }

  type TileWorld = tiles.TileWorld
  type SpriteSheet = tiles.SpriteSheet
  val SpriteSheet = tiles.SpriteSheet
  type TileXY = tiles.TileXY
  val TileXY = tiles.TileXY

  val PictureDraw = new PictureDraw(this)
  @volatile var cwidth = 0
  @volatile var cheight = 0

  def resetPictureDraw(): Unit = {
    PictureDraw.reset()
    val cb = canvasBounds
    cwidth = cb.width.toInt
    cheight = cb.height.toInt
  }

  def size(width: Int, height: Int): Unit = {
    cwidth = width
    cheight = height
    setDrawingCanvasSize(width, height)
  }

  def setup(fn: => Unit) = runInGuiThread {
    fn
  }

  def drawLoop(fn: => Unit) = TSCanvas.animate {
    fn
  }

  private def wh = {
    lazy val cb = canvasBounds
    val w = if (cwidth == 0) cb.width else cwidth
    val h = if (cheight == 0) cb.height else cheight
    (w, h)
  }

  def originTopLeft(): Unit = {
    val (w, h) = wh

    def work = TSCanvas.zoomXY(1, -1, w / 2, h / 2)

    work
    Utils.schedule(0.5) {
      work
    }
  }

  def originBottomLeft(): Unit = {
    val (w, h) = wh

    def work = TSCanvas.zoomXY(1, 1, w / 2, h / 2)

    work
    Utils.schedule(0.5) {
      work
    }
  }

  def rangeTo(start: Int, end: Int, step: Int = 1) = start to end by step

  def rangeTill(start: Int, end: Int, step: Int = 1) = start until end by step

  def rangeTo(start: Double, end: Double, step: Double) = Range.BigDecimal.inclusive(start, end, step)

  def rangeTill(start: Double, end: Double, step: Double) = Range.BigDecimal(start, end, step)

  import scala.language.implicitConversions

  implicit def bd2double(bd: BigDecimal) = bd.doubleValue

  type CanvasDraw = net.kogics.kojo.lite.CanvasDraw

  import scala.language.reflectiveCalls

  def canvasSketch(sketch: {
    def setup(cd: CanvasDraw): Unit
    def drawLoop(cd: CanvasDraw): Unit
  }, scaleFactor: Double = 1): Unit = {

    @volatile var inited = false
    @volatile var initStarted = false // to support breakpoints in setup
    @volatile var cd: CanvasDraw = null
    val pic = Picture.fromCanvas(cwidth * scaleFactor, cheight * scaleFactor) { g2d =>
      if (!inited) {
        if (!initStarted) {
          initStarted = true
          cd = new CanvasDraw(g2d, cwidth * scaleFactor, cheight * scaleFactor, builtins)
          sketch.setup(cd)
          inited = true
        }
      }
      else {
        sketch.drawLoop(cd)
      }
    }
    draw(pic)
    pic.scale(1 / scaleFactor)
    TSCanvas.animate {
      pic.update()
      if (!cd.loop) {
        stopAnimation()
      }
    }
  }

  type PictureDraw = net.kogics.kojo.lite.PictureDraw

  def pictureSketch(sketch: {
    def setup(cd: PictureDraw): Unit
    def drawLoop(cd: PictureDraw): Unit
  }): Unit = {

    setup(sketch.setup(PictureDraw))
    drawLoop(sketch.drawLoop(PictureDraw))
  }

  def timeit(fn: => Unit): Unit = {
    val t0 = epochTime
    fn
    val delta = epochTime - t0
    println(f"Timed code took $delta%.3f seconds")
  }

  def joystick(radius: Double) = new JoyStick(radius)(this)

  LoadProgress.init(this)

  def preloadImage(file: String): Unit = {
    LoadProgress.showLoading()
    Utils.loadUrlImageC(url(file))
    LoadProgress.hideLoading()
  }
}
