package net.kogics.kojo.lite

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame
import javax.swing.plaf.FontUIResource
import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.WindowConstants

import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.music.FuguePlayer
import net.kogics.kojo.music.KMp3
import net.kogics.kojo.staging
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.Utils

object KojoHeadless {
  @volatile var instanceCount = 0
  def incrementInstanceCount(): Unit = {
    instanceCount += 1
    if (instanceCount > 1) {
      assert(false, "Only one instance of KojoHeadless is allowed per process")
    }
  }

  def create(): KojoHeadless = {
    incrementInstanceCount()
    val kf = new KojoHeadless(950, 700)
    kf.show()
    kf
  }

  def create(width: Int, height: Int, showLoading: Boolean): KojoHeadless = {
    incrementInstanceCount()
    val kf = new KojoHeadless(width, height)
    kf.show()
    kf
  }
}

class KojoHeadless private (width: Int, height: Int) {
  System.setProperty("java.awt.headless", "true")

  if (Utils.isLinux) {
    System.setProperty("sun.java2d.xrender", "false")
  }

  val kojoCtx = new KojoCtx // context needs to be created right up front to set user language

  def show(): Unit = {
    Utils.runInSwingThreadAndWait {
      val spriteCanvas = new SpriteCanvas(kojoCtx)
      val Tw = new TurtleWorldAPI(spriteCanvas.turtle0)
      val TSCanvas = new DrawingCanvasAPI(spriteCanvas)
      val Staging = new staging.API(spriteCanvas)
      val mp3player = new KMp3(kojoCtx)
      val fuguePlayer = new FuguePlayer(kojoCtx)

      val builtins = new Builtins(
        TSCanvas,
        Tw,
        Staging,
        mp3player,
        fuguePlayer,
        kojoCtx
      )

      val statusBar = new StatusBar
      kojoCtx.statusBar = statusBar
      statusBar.showText("   ")

      kojoCtx.canvas = spriteCanvas

      spriteCanvas.setPreferredSize(new Dimension(width, height))
      spriteCanvas.setSize(new Dimension(width, height))
    }
  }

  def builtins = {
    net.kogics.kojo.lite.Builtins.instance
  }
}
