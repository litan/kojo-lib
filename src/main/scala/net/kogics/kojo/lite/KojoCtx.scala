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

import java.awt.Color
import java.awt.Cursor
import java.awt.Font
import java.awt.Toolkit
import java.util.prefs.Preferences
import javax.swing.plaf.FontUIResource
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.UIManager

import net.kogics.kojo.core.DelegatingSpriteListener
import net.kogics.kojo.core.SpriteListener
import net.kogics.kojo.lite.action.FullScreenBaseAction
import net.kogics.kojo.lite.action.FullScreenCanvasAction
import net.kogics.kojo.lite.action.FullScreenSupport
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.util.Utils

class KojoCtx extends core.KojoCtx {
  val prefs = Preferences.userRoot().node("Kojolite-Prefs")
  var frame: JFrame = _
  var canvas: SpriteCanvas = _
  @volatile var fps = 50 // gets reset on clear
  @volatile var screenDPI = Toolkit.getDefaultToolkit.getScreenResolution
  var statusBar: StatusBar = _
  Utils.kojoCtx = this

  val activityListener = new DelegatingSpriteListener
  def setActivityListener(l: SpriteListener): Unit = {
    activityListener.setRealListener(l)
  }

  lazy val screenSize = Toolkit.getDefaultToolkit.getScreenSize
  lazy val baseFontSize = System.getProperty("kojo.baseFont.size").toInt
  lazy val hiDpiFontIncrease = System.getProperty("kojo.hidpi.font.increase").toInt

  lazy val screenDpiFontDelta: Int = {
    // alternative approach
    // (baseFontSize * screenDPI / 96.0 - baseFontSize).round.toInt
    val delta1 = screenSize.width match {
      case n if n <= 1440 => 0
      case n if n <= 1680 => 2
      case n if n <= 1920 => 4
      case n if n <= 2560 => 6
      case n if n <= 2880 => 6
      case n if n <= 3840 => 6
      case _              => 6
    }
    val delta = Utils.appProperty("font.increase") match {
      case Some(d) => d.toInt + delta1
      case None    => delta1
    }
    System.setProperty("kojo.hidpi.font.increase", delta.toString)
    delta
  }

  def lookAndFeelReady() = {
    val defaults = UIManager.getLookAndFeelDefaults
    val defaultFontSize = defaults.get("defaultFont").asInstanceOf[FontUIResource].getSize
    System.setProperty("kojo.baseFont.size", defaultFontSize.toString)
    if (screenDpiFontDelta > 0) {
      def changeFontSize(key: String, delta: Int): Unit = {
        val f = defaults.get(key).asInstanceOf[FontUIResource]
        // if we use f.getName below, the 'Hindi' Language menu item does not show up right
        val f2 = new FontUIResource(Font.SANS_SERIF, f.getStyle, f.getSize + delta)
        defaults.put(key, f2)
      }
      val fontsToChange = List("defaultFont")
      fontsToChange.foreach(changeFontSize(_, screenDpiFontDelta))
    }
  }
  def menuReady(m: JMenu) = {
    //    if (screenDpiFontDelta > 0) {
    //      val f = m.getFont
    //      val f2 = new Font(f.getName, f.getStyle, f.getSize + screenDpiFontDelta)
    //      m.setFont(f2)
    //    }
  }

  type ActionLike = FullScreenBaseAction
  def fullScreenCanvasAction(): ActionLike = FullScreenCanvasAction(this)
  def updateMenuItem(mi: JCheckBoxMenuItem, action: ActionLike) = FullScreenSupport.updateMenuItem(mi, action)

  def baseDir: String = getLastLoadStoreDir + "/"

  @volatile var lastLoadStoreDir = prefs.get("lastLoadStoreDir", "")
  def getLastLoadStoreDir = lastLoadStoreDir
  def setLastLoadStoreDir(dir: String): Unit = {
    lastLoadStoreDir = dir
    prefs.put("lastLoadStoreDir", lastLoadStoreDir)
  }

  @volatile var _lastColor =
    new Color(Integer.parseInt(prefs.get("lastColor", Integer.toString(Color.red.getRGB()))), true)
  def lastColor: Color = _lastColor
  def lastColor_=(c: Color): Unit = {
    _lastColor = c
    prefs.put("lastColor", Integer.toString(_lastColor.getRGB()))
  }

  def knownColors = staging.KColor.knownColors
  def knownColor(name: String): Color = staging.KColor.knownColorsMap(name)

  def knownColors2 = doodle.ColorMap.knownColors.keys.toList
  def knownColor2(name: String) = doodle.ColorMap.knownColors(name)

  def readInput(prompt: String): String = ""
  @volatile var astStopPhase = "typer"
  def setAstStopPhase(phase: String): Unit = {
    astStopPhase = phase
  }

  def showStatusText(text: String): Unit = {
    statusBar.showText(text)
  }

  def showStatusCaretPos(line: Int, col: Int): Unit = {
    statusBar.showCaretPos(line, col)
  }

  def showAppWaitCursor(): Unit = {
    val gp = frame.getGlassPane()
    gp.setVisible(true)
    gp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
  }

  def hideAppWaitCursor(): Unit = {
    val gp = frame.getGlassPane()
    gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
    gp.setVisible(false)
  }

  def activateDrawingCanvas() = Utils.runInSwingThread {
    canvas.activate()
  }

  def repaintCanvas(): Unit = {
    canvas.repaint()
  }
}
