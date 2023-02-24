package net.kogics.kojo

import net.kogics.kojo.lite.KojoFrame

/** The English api can be accessed via this one-line import: import net.kogics.kojo.English.*, CanvasAPI.*, TurtleAPI.*
  */
object English {
  val frame = KojoFrame.create(width = 800, height = 600, showLoading = false)
  val builtins = frame.builtins
  val CanvasAPI = builtins.TSCanvas
  val TurtleAPI = builtins.Tw
}
