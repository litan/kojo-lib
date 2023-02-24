/*
 * Copyright (C) 2013 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo.lite.action

import java.awt.event.ActionEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.Frame
import java.awt.GraphicsEnvironment
import javax.swing.AbstractAction
import javax.swing.JCheckBoxMenuItem
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.WindowConstants

import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.lite.KojoCtx
import net.kogics.kojo.util.Utils

object FullScreenSupport {
  lazy val sdev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
  def isFullScreenOn = sdev.getFullScreenWindow != null

  def updateMenuItem(mi: JCheckBoxMenuItem, action: FullScreenBaseAction): Unit = {
    if (isFullScreenOn) {
      if (action.isFullScreen) {
        mi.setState(true)
        mi.setEnabled(true)
      }
      else {
        mi.setState(false)
        mi.setEnabled(false)
      }
    }
    else {
      mi.setState(false)
      mi.setEnabled(true)
    }
  }
}

class FullScreenBaseAction(key: String, fsComp: => JComponent, oldFrame: => JFrame) extends AbstractAction(key) {
  import FullScreenSupport._
  var fullScreenFrame: JFrame = _
  var fullScreen = false

  def isFullScreen = fullScreen

  def enterFullScreen(): Unit = {
    fullScreen = true
    fullScreenFrame = new JFrame
    fullScreenFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    fullScreenFrame.setUndecorated(true)
    fullScreenFrame.getContentPane.add(fsComp)
    sdev.setFullScreenWindow(fullScreenFrame)
    fullScreenFrame.validate()
    oldFrame.setVisible(false)

    val escComp = fullScreenFrame.getMostRecentFocusOwner()
    if (escComp != null) {
      escComp.addKeyListener(new KeyAdapter {
        override def keyPressed(event: KeyEvent): Unit = {
          if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escComp.removeKeyListener(this)
            leaveFullScreen()
          }
        }
      })
    }
  }

  def leaveFullScreen(): Unit = {
    fullScreen = false
    sdev.setFullScreenWindow(null)
    fullScreenFrame.setVisible(false)
    oldFrame.add(fsComp)
    oldFrame.setVisible(true)
    fsComp.revalidate()
  }

  // can also be called from the interp thread via the API
  def actionPerformed(e: ActionEvent) = Utils.runInSwingThreadAndWait {
    if (!isFullScreen) {
      if (!FullScreenSupport.isFullScreenOn) {
        enterFullScreen()
      }
    }
    else {
      leaveFullScreen()
    }
  }
}

object FullScreenCanvasAction {
  var instance: FullScreenCanvasAction = _
  def apply(kojoCtx: KojoCtx) = {
    if (instance == null) {
      instance = new FullScreenCanvasAction(kojoCtx.canvas, kojoCtx.frame)
    }
    instance
  }
}

class FullScreenCanvasAction(canvas: => SpriteCanvas, frame: => JFrame)
    extends FullScreenBaseAction(
      Utils.loadString("S_FullScreenCanvas"),
      canvas,
      frame
    ) {
  override def enterFullScreen(): Unit = {
    canvas.setFocusable(true) // make canvas work with frame.getMostRecentFocusOwner()
    super.enterFullScreen()
    canvas.activate()
  }
}
