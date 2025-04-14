package net.kogics.kojo.lite

import net.kogics.kojo.core.KojoCtx

trait KojoHandle {
  def kojoCtx: KojoCtx
  def builtins: Builtins
}
