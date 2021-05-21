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

package net.kogics.kojo.core

import java.awt.Color
import java.awt.geom.Point2D

import javax.swing.Action
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame

import javax.swing.JMenu

import net.kogics.kojo.doodle

trait KojoCtx {
  def activityListener: SpriteListener
  def setActivityListener(l: SpriteListener): Unit
  type ActionLike <: Action
  def fullScreenCanvasAction(): ActionLike
  def updateMenuItem(mi: JCheckBoxMenuItem, action: ActionLike): Unit
  def baseDir: String
  def frame: JFrame
  def getLastLoadStoreDir: String
  def setLastLoadStoreDir(dir: String): Unit
  def lastColor: Color
  def lastColor_=(c: Color): Unit
  def knownColors: List[String]
  def knownColor(name: String): Color
  def knownColors2: List[String]
  def knownColor2(name: String): doodle.Color
  def readInput(prompt: String): String
  def setAstStopPhase(phase: String): Unit
  def astStopPhase: String
  def showStatusText(text: String): Unit
  def showStatusCaretPos(line: Int, col: Int): Unit
  def showAppWaitCursor(): Unit
  def hideAppWaitCursor(): Unit
  def repaintCanvas(): Unit
  def screenDpiFontDelta: Int
  def baseFontSize: Int
  def hiDpiFontIncrease: Int
  def menuReady(m: JMenu): Unit
  def activateDrawingCanvas(): Unit

  var fps: Int
  var screenDPI: Int
}
