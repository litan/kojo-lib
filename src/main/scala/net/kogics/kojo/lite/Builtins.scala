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

import java.awt.geom.{Ellipse2D, GeneralPath, Rectangle2D}
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Paint
import java.awt.Toolkit
import java.net.URL
import javax.swing.JComponent
import scala.language.implicitConversions
import com.jhlabs.image.AbstractBufferedImageOp
import com.jhlabs.image.LightFilter.Light
import net.kogics.kojo.core.Rich2DPath
import net.kogics.kojo.core.VertexShape
import net.kogics.kojo.core.Voice
import net.kogics.kojo.kmath.KEasing
import net.kogics.kojo.music.RealtimeNotePlayer
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.Utils
import net.kogics.kojo.xscala.RepeatCommands

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
) extends CoreBuiltins
    with RepeatCommands { builtins =>
  Builtins.instance = this
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

  val Instrument = music.Instrument
  @volatile private var rtnp: Option[RealtimeNotePlayer] = None

  private def checkNotePlayer(): Unit = {
    if (rtnp.isEmpty) {
      rtnp = Some(new RealtimeNotePlayer())
    }
  }

  def playNote(pitch: Int, durationMillis: Int, volume: Int = 80): Unit = {
    checkNotePlayer()
    rtnp.get.playNote(pitch, durationMillis, volume)
  }

  def setNoteInstrument(instrumentCode: Int): Unit = {
    checkNotePlayer()
    rtnp.get.setInstrument(instrumentCode)
  }

  def stopNotePlayer(): Unit = {
    rtnp.foreach(_.stop())
  }

  def resetNotePlayer(): Unit = {
    rtnp.foreach(_.close())
    rtnp = None
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
  val flipAroundY = flipY
  val flipX = picture.flipX
  val flipAroundX = flipX
  val axes = picture.axesOn
  val bounds = picture.bounds
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
  //  val pointLight = picture.pointLight _
  //  val spotLight = picture.spotLight _
  def distantLight(direction: Double, elevation: Double) = picture.distantLight(direction, elevation)
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

  def shear(shearX: Double, shearY: Double) = preDrawTransform { pic => pic.shear(shearX, shearY) }
  def zIndex(idx: Int) = postDrawTransform { pic => pic.setZIndex(idx) }
  def clip(clipShape: java.awt.Shape) = picture.Clippedc(clipShape)

  // some core picture transformations as regular functions - for educative use
  def withRotation(pic: Picture, angle: Double) = pic.withRotation(angle)
  def withTranslation(pic: Picture, x: Double, y: Double) = pic.withTranslation(x, y)
  def withScaling(pic: Picture, factor: Double) = pic.withScaling(factor)
  def withFillColor(pic: Picture, color: Color) = pic.withFillColor(color)
  def withPenColor(pic: Picture, color: Color) = pic.withPenColor(color)

  implicit val _picCanvas = tCanvas
  def pict(painter: Painter) = picture.Pic(painter)
  def PictureT(painter: Painter) = picture.Pic(painter)
  def Picture(fn: => Unit) = picture.Pic0 { t =>
    fn
  }
  def drawAndHide(pictures: Picture*) = pictures.foreach { p => p.draw(); p.invisible() }
  def drawCentered(pic: Picture): Unit = {
    checkForLargeDrawing()
    pic.invisible()
    pic.draw()
    center(pic)
    pic.visible()
  }
  def center(pic: Picture): Unit = {
    val cb = canvasBounds; val pb = pic.bounds
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

  def stopAnimations() = stopAnimation()
  def stopAnimation() = {
    Utils.stopMonitoredThreads()
    tCanvas.stopAnimation()
    fuguePlayer.stopMusic()
    fuguePlayer.stopBgMusic()
    mp3player.stopMp3()
    mp3player.stopMp3Loop()
    stopNotePlayer()
  }

  def isKeyPressed(key: Int) = staging.Inputs.isKeyPressed(key)
  def pressedKeys: collection.Set[Int] = staging.Inputs.pressedKeys
  def activateCanvas() = tCanvas.activate()

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
    val frame = kojoCtx.frame
    val b = frame.getBounds()
    val newWidth = b.height * r
    setDrawingCanvasSize(math.round(newWidth).toInt, b.height)
  }

  def setDrawingCanvasSize(width: Int, height: Int): Unit = Utils.runLaterInSwingThread {
    kojoCtx.canvas.setPreferredSize(new Dimension(width, height))
    kojoCtx.frame.pack();
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

  private val fullScreenAction = kojoCtx.fullScreenCanvasAction()
  def toggleFullScreenCanvas() = fullScreenAction.actionPerformed(null)

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
  def setScreenDPI(dpi: Int): Unit = { kojoCtx.screenDPI = dpi }
  def screenSize = Toolkit.getDefaultToolkit.getScreenSize
  def hiDpiFontIncrease = kojoCtx.hiDpiFontIncrease
  def baseFontSize = kojoCtx.baseFontSize
  def isTracing = false

  def TexturePaint(file: String, x: Double, y: Double) =
    cm.texture(file, x, y)

  def url(url: String) = new URL(url)

  val PShapes = Picture
  val PicShape = Picture
  implicit def p2rp(path: GeneralPath): Rich2DPath = new Rich2DPath(path)
  object Picture {
    def text(content: Any, fontSize: Int = 15) = picture.text(content, fontSize, red)
    def text(content: Any, font: Font) = picture.text(content, font, red)
    def textu(content: Any, fontSize: Int = 15, color: Color = red) = picture.text(content, fontSize, color)
    def textu(content: Any, font: Font, color: Color) = picture.text(content, font, color)
    def rect(h: Double, w: Double) = picture.rect2(w, h)
    def rectangle(width: Double, height: Double) = picture.rect2(width, height)
    // def rectangle(x: Double, y: Double, w: Double, h: Double) = picture.offset(x, y) -> picture.rect2(w, h)
    def vline(length: Double) = picture.vline(length)
    def hline(length: Double) = picture.hline(length)
    def line(width: Double, height: Double) = picture.line(width, height)
    // def line(x1: Double, y1: Double, x2: Double, y2: Double) = picture.offset(x1, y1) -> picture.line(x2 - x1, y2 - y1)
    def fromPath(fn: GeneralPath => Unit) = picture.fromPath {
      val path = new GeneralPath(); fn(path)
      path
    }
    def fromVertexShape(fn: VertexShape => Unit) = picture.fromPath {
      val path = new GeneralPath(); fn(new VertexShape(path))
      path
    }
    def fromTurtle(fn: Turtle => Unit) = PictureT(fn)
    def fromCanvas(width: Double, height: Double)(fn: Graphics2D => Unit) = picture.fromJava2d(width, height, fn)

    private[lite] def fromCanvasDynamic(width: Double, height: Double, scaleOutFactor: Double)(fn: Graphics2D => Unit)(
        stopCheck: => Boolean
    ) =
      picture.fromJava2dDynamic(width, height, scaleOutFactor, fn, stopCheck)

    def fromSketch(
        sketch: {
          def setup(cd: CanvasDraw): Unit
          def drawLoop(cd: CanvasDraw): Unit
        },
        scaleOutFactor: Double = 1
    ): Picture = {
      val sr = new SketchRunner(sketch, scaleOutFactor)
      sr.pic
    }

    def circle(radius: Double) = picture.circle(radius)
    // def circle(x: Double, y: Double, r: Double) = picture.offset(x, y) -> picture.circle(r)
    def ellipse(xRadius: Double, yRadius: Double) = picture.ellipse(xRadius, yRadius)
    def ellipseInRect(width: Double, height: Double) =
      picture.trans(width / 2, height / 2) -> picture.ellipse(width / 2, height / 2)
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

    def showGlobalBounds(pics: Picture*) = Utils.runInSwingThread {
      pics.foreach { pic =>
        val b = pic.tnode.getGlobalFullBounds
        val bpic = penWidth(4) * penColor(black) * trans(b.x, b.y) -> rectangle(b.width, b.height)
        draw(bpic)
      }
    }

    def showLocalBounds(pics: Picture*) = Utils.runInSwingThread {
      pics.foreach { pic =>
        val tnode = pic.tnode
        val b = tnode.getUnionOfChildrenBounds(null)
        b.add(tnode.getBoundsReference)
        val bpic = penWidth(1) * penColor(black) * trans(b.x, b.y) -> rectangle(b.width, b.height)
        draw(bpic)
        tnode.addChild(bpic.tnode)
        tnode.repaint()
      }
    }

    def showBounds(pics: Picture*) = Utils.runInSwingThread {
      pics.foreach { pic =>
        val tnode = pic.tnode
        assert(tnode.getParent != null, s"Picture does not have a parent - $pic")
        val b = tnode.getFullBounds
        val bpic = penWidth(2) * penColor(black) * trans(b.x, b.y) -> rectangle(b.width, b.height)
        draw(bpic)
        tnode.getParent.addChild(bpic.tnode)
        tnode.getParent.repaint()
      }
    }

    def showAxes(pics: Picture*) = {
      pics.foreach { pic =>
        pic.axesOn()
      }
    }
  }

  object ClipShape {
    def ellipse(x: Double, y: Double, width: Double, height: Double) = new Ellipse2D.Double(x, y, width, height)
    def rectangle(x: Double, y: Double, width: Double, height: Double) = new Rectangle2D.Double(x, y, width, height)
    def emptyPath = new GeneralPath()
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

    def fromSketch(
        sketch: {
          def setup(cd: CanvasDraw): Unit
          def drawLoop(cd: CanvasDraw): Unit
        },
        scaleOutFactor: Double = 1
    ): Picture = {
      val pic = Picture.fromSketch(sketch, scaleOutFactor)
      placeAndDraw(pic, 0, 0)
    }
  }
  val pm = PictureMaker

  type Animation = animation.Animation
  def Transition(
      durationSeconds: Double,
      fromState: Seq[Double],
      toState: Seq[Double],
      easer: KEasing,
      picMaker: Seq[Double] => Picture,
      hideOnDone: Boolean
  ): Animation =
    animation.Animation(durationSeconds, fromState, toState, easer, picMaker, hideOnDone)
  def Timeline(
      duration: Double,
      keyFrames: animation.KeyFrames,
      easer: KEasing,
      picMaker: Seq[Double] => Picture,
      hideOnDone: Boolean
  ): Animation =
    animation.Animation(duration, keyFrames, Seq.fill(keyFrames.frames.length - 1)(easer), picMaker, hideOnDone)
  def Timeline(
      duration: Double,
      keyFrames: animation.KeyFrames,
      easers: Seq[KEasing],
      picMaker: Seq[Double] => Picture,
      hideOnDone: Boolean
  ): Animation =
    animation.Animation(duration, keyFrames, easers, picMaker, hideOnDone)
  implicit def iis2dds(is: (Int, Seq[Int])): (Double, Seq[Double]) = is match {
    case (i, si) => (i.toDouble, si.map(_.toDouble))
  }
  implicit def ids2dds(is: (Int, Seq[Double])): (Double, Seq[Double]) = is match {
    case (i, sd) => (i.toDouble, sd)
  }
  def KeyFrames(frames: (Double, Seq[Double])*) = animation.KeyFrames(frames)
  def animSeq(as: Animation*): Animation = animSeq(as)
  def animSeq(as: collection.Seq[Animation]): Animation = animation.animSeq(as.toSeq)
  def animPar(as: Animation*): Animation = animPar(as)
  def animPar(as: collection.Seq[Animation]): Animation = animation.animPar(as.toSeq)
  def run(anim: Animation) = anim.run()

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

  def makeCenteredMessage(message: String, color: Color = black, fontSize: Int = 15): Picture = {
    val cb = canvasBounds
    val te = textExtent(message, fontSize)
    penColor(color) *
      trans(cb.x + (cb.width - te.width) / 2, cb.y + (cb.height - te.height) / 2 + te.height) ->
      PicShape.text(message, fontSize)
  }

  def drawCenteredMessage(message: String, color: Color = black, fontSize: Int = 15): Unit = {
    val pic = makeCenteredMessage(message, color, fontSize)
    draw(pic)
  }

  @volatile var gameTimeLabel: Option[Picture] = None
  @volatile var gameTimeEndMsg: Option[Picture] = None
  @volatile private var gameTimeRunning = false

  private def clearGameTime(): Unit = {
    gameTimeLabel = None
    gameTimeEndMsg = None
    gameTimeRunning = false
  }

  def showGameTimeCountdown(
      limitSecs: Int,
      endMsg: => String,
      color: Color = black,
      fontSize: Int = 15,
      dx: Double = 10,
      dy: Double = 50
  ) = showGameTime(limitSecs, endMsg, color, fontSize, dx, dy, true)

  def showGameTime(
      limitSecs: Int,
      endMsg: => String,
      color: Color = black,
      fontSize: Int = 15,
      dx: Double = 10,
      dy: Double = 50,
      countDown: Boolean = false
  ): Unit = {
    if (gameTimeRunning) {
      return
    }
    gameTimeRunning = true

    val cb = canvasBounds
    @volatile var gameTime = if (countDown) limitSecs else 0
    val incr = if (countDown) -1 else 1
    val endTime = if (countDown) 0 else limitSecs

    gameTimeLabel.foreach(_.erase())
    gameTimeEndMsg.foreach(_.erase())

    gameTimeLabel = Some(trans(cb.x + dx, cb.y + dy) -> PicShape.textu(gameTime, fontSize, color))
    gameTimeLabel.foreach { label =>
      draw(label)
      label.forwardInputTo(TSCanvas.stageArea)
    }

    TSCanvas.timer(1000) {
      gameTime += incr
      gameTimeLabel.foreach(_.update(gameTime))
      if (gameTime == endTime) {
        gameTimeEndMsg = Some(makeCenteredMessage(endMsg, color, fontSize * 2))
        gameTimeEndMsg.foreach(draw(_))
        stopAnimation()
        gameTimeRunning = false
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

  private[lite] def onClear(): Unit = {
    PictureDraw.reset()
    val cb = canvasBounds
    cwidth = cb.width.toInt
    cheight = cb.height.toInt
    clearGameTime()
    currGame = null
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

  implicit def bd2double(bd: BigDecimal) = bd.doubleValue

  type CanvasDraw = net.kogics.kojo.lite.CanvasDraw
  import scala.language.reflectiveCalls

  class SketchRunner private[lite] (
      sketch: {
        def setup(cd: CanvasDraw): Unit
        def drawLoop(cd: CanvasDraw): Unit
      },
      scaleFactor: Double = 1
  ) {
    @volatile var inited = false
    @volatile var initStarted = false // to support breakpoints in setup
    @volatile var cd: CanvasDraw = null
    def shouldStop: Boolean = {
      if (cd == null) false else !cd.loop
    }
    val pic = Picture.fromCanvasDynamic(cwidth, cheight, scaleFactor) { g2d =>
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
    }(shouldStop)

    def draw() = {
      pic.draw()
    }
  }

  def canvasSketch(
      sketch: {
        def setup(cd: CanvasDraw): Unit
        def drawLoop(cd: CanvasDraw): Unit
      },
      scaleOutFactor: Double = 1
  ): Unit = {
    val sr = new SketchRunner(sketch, scaleOutFactor)
    sr.draw()
  }

  type PictureDraw = net.kogics.kojo.lite.PictureDraw
  def pictureSketch(sketch: {
    def setup(cd: PictureDraw): Unit
    def drawLoop(cd: PictureDraw): Unit
  }): Unit = {

    setup(sketch.setup(PictureDraw))
    drawLoop(sketch.drawLoop(PictureDraw))
  }

  def timeit[T](msg: String)(fn: => T): T = {
    val t0 = epochTime
    val ret = fn
    val delta = epochTime - t0
    println(f"$msg took $delta%.3f seconds")
    ret
  }

  def timeit[T](fn: => T): T = timeit("Timed code")(fn)

  def joystick(radius: Double) = new JoyStick(radius)(this)
  LoadProgress.init(this)
  def preloadImage(file: String): Unit = {
    LoadProgress.showLoading()
    Utils.loadUrlImageC(url(file))
    LoadProgress.hideLoading()
  }

  def animateWithRedraw[S](initState: S, nextState: S => S, stateView: S => Picture): Unit = {
    import edu.umd.cs.piccolo.activities.PActivity

    import java.util.concurrent.Future
    val initPic = stateView(initState)
    initPic.draw()
    lazy val anim: Future[PActivity] = tCanvas.animateWithState((initState, initPic)) {
      case (state, pic) =>
        val newState = nextState(state)
        val pic2 = stateView(state)
        pic.erase()
        pic2.draw()
        if (newState == state) {
          tCanvas.stopAnimationActivity(anim)
        }
        (newState, pic2)
    }
    anim
  }

  def animateWithSetupCanvasDraw(setupCanvas: CanvasDraw => Unit)(drawFrame: CanvasDraw => Unit): Unit = {
    class Sketch {
      def setup(canvas: CanvasDraw): Unit = {
        setupCanvas(canvas)
      }

      def drawLoop(canvas: CanvasDraw): Unit = {
        drawFrame(canvas)
      }
    }

    val sketch = new Sketch
    val pic = Picture.fromSketch(sketch, 1)
    draw(pic)
  }

  def animateWithCanvasDraw(drawFrame: CanvasDraw => Unit): Unit = {
    animateWithSetupCanvasDraw { canvas => }(drawFrame)
  }

  type Sub[M] = gaming.Sub[M]
  type CmdQ[M] = gaming.CmdQ[M]
  val Subscriptions = gaming.Subscriptions
  lazy val CollisionDetector = new gaming.CollisionDetector()
  @volatile private var currGame: Option[gaming.Game[_, _]] = None

  def runGame[S, M](init: S, update: (S, M) => S, view: S => Picture, subscriptions: S => Seq[Sub[M]]): Unit = {
    currGame = Some(new gaming.Game(init, update, view, subscriptions))
  }

  def runCommandQuery[M](cmdQ: CmdQ[M]): Unit = currGame.get.asInstanceOf[gaming.Game[_, M]].runCommandQuery(cmdQ)
}
