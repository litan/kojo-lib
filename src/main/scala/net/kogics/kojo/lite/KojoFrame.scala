package net.kogics.kojo.lite

import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.music.{FuguePlayer, KMp3}
import net.kogics.kojo.staging
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.Utils

import java.awt.{BorderLayout, Dimension, Font, Frame}
import javax.swing.plaf.FontUIResource
import javax.swing.{JFrame, UIManager, WindowConstants}

object KojoFrame {
  @volatile var instanceCount = 0
  def incrementInstanceCount(): Unit = {
    instanceCount += 1
    if (instanceCount > 1) {
      assert(false, "Only one instance of KojoFrame is allowed per process")
    }
  }

  def create(): KojoFrame = {
    incrementInstanceCount()
    val kf = new KojoFrame(950, 700, false)
    kf.show()
    kf
  }

  def create(width: Int, height: Int): KojoFrame = {
    incrementInstanceCount()
    val kf = new KojoFrame(width, height, false)
    kf.show()
    kf
  }

  def create(showLoading: Boolean): KojoFrame = {
    incrementInstanceCount()
    val kf = new KojoFrame(950, 700, showLoading)
    kf.show()
    kf
  }

  def create(width: Int, height: Int, showLoading: Boolean): KojoFrame = {
    incrementInstanceCount()
    val kf = new KojoFrame(width, height, showLoading)
    kf.show()
    kf
  }
}

class KojoFrame private (width: Int, height: Int, showLoading: Boolean) {
  if (Utils.isLinux) {
    System.setProperty("sun.java2d.xrender", "false")
  }

  val kojoCtx = new KojoCtx // context needs to be created right up front to set user language

  def show(): Unit = {
    Utils.runInSwingThreadAndWait {
      updateDefaultFonts(12 + kojoCtx.screenDpiFontDelta)
      loadLookAndFeel()
      kojoCtx.lookAndFeelReady()

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

      val frame = new JFrame("Kojo Canvas")

      frame.setLayout(new BorderLayout)
      frame.add(spriteCanvas, BorderLayout.CENTER)
      frame.add(statusBar, BorderLayout.SOUTH)

      kojoCtx.frame = frame
      kojoCtx.canvas = spriteCanvas

      spriteCanvas.setPreferredSize(new Dimension(width, height))
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
      frame.setExtendedState(Frame.NORMAL)
      frame.pack()
      frame.setLocationRelativeTo(null)
      frame.setVisible(true)
    }

    if (showLoading) {
      Utils.runInSwingThreadAndWait {
        val b = builtins
        import b._
        import CanvasAPI._
        import TurtleAPI._

        cleari()
        val fig = Picture.image("/images/splash.png")
        drawCentered(fig)
      }
      Thread.sleep(700)
    }
  }

  private def updateDefaultFonts(size: Int) = {
    val plain = new Font("SansSerif", 0, size)
    UIManager.put("ColorChooser.font", plain)
    UIManager.put("FileChooser.font", plain)
    UIManager.put("Panel.font", plain)
    UIManager.put("TextPane.font", plain)
    UIManager.put("OptionPane.font", plain)
    UIManager.put("OptionPane.messageFont", plain)
    UIManager.put("OptionPane.buttonFont", plain)
    UIManager.put("EditorPane.font", plain)
    UIManager.put("ScrollPane.font", plain)
    UIManager.put("TabbedPane.font", plain)
    UIManager.put("ToolBar.font", plain)
    UIManager.put("ProgressBar.font", plain)
    UIManager.put("Viewport.font", plain)
    UIManager.put("TitledBorder.font", plain)
    UIManager.put("Button.font", plain)
    UIManager.put("RadioButton.font", plain)
    UIManager.put("ToggleButton.font", plain)
    UIManager.put("ComboBox.font", plain)
    UIManager.put("CheckBox.font", plain)
    UIManager.put("Menu.font", plain)
    UIManager.put("Menu.acceleratorFont", plain)
    UIManager.put("PopupMenu.font", plain)
    UIManager.put("MenuBar.font", plain)
    UIManager.put("MenuItem.font", plain)
    UIManager.put("MenuItem.acceleratorFont", plain)
    UIManager.put("CheckBoxMenuItem.font", plain)
    UIManager.put("RadioButtonMenuItem.font", plain)
    UIManager.put("Label.font", plain)
    UIManager.put("Table.font", plain)
    UIManager.put("TableHeader.font", plain)
    UIManager.put("Tree.font", plain)
    UIManager.put("Tree.rowHeight", plain.getSize + 5)
    UIManager.put("List.font", plain)
    UIManager.put("TextField.font", plain)
    UIManager.put("PasswordField.font", plain)
    UIManager.put("TextArea.font", plain)
    UIManager.put("ToolTip.font", plain)
  }

  def builtins = {
    net.kogics.kojo.lite.Builtins.instance
  }

  def loadLookAndFeel(): Unit = {
    if (Utils.isMac) {
      // use the system look and feel
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
      UIManager.getLookAndFeelDefaults.put("defaultFont", new FontUIResource("Arial", Font.PLAIN, 12))
    }
    else {
      UIManager.getInstalledLookAndFeels.find {
        _.getName == "Nimbus"
      }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }
    }
  }
}
