// Borrowed from: https://github.com/underscoreio/doodle
package net.kogics.kojo.doodle

/** An angle in radians
  */
final class Angle(val toRadians: Double) {
  def +(that: Angle): Angle =
    Angle.radians(this.toRadians + that.toRadians)

  def -(that: Angle): Angle =
    Angle.radians(this.toRadians - that.toRadians)

  def *(m: Double): Angle =
    Angle.radians(this.toRadians * m)

  def /(m: Double): Angle =
    Angle.radians(this.toRadians / m)

  def >(that: Angle): Boolean =
    this.toRadians > that.toRadians

  def <(that: Angle): Boolean =
    this.toRadians < that.toRadians

  def sin: Double =
    math.sin(toRadians)

  def cos: Double =
    math.cos(toRadians)

  def normalize: Angle = {
    import scala.annotation.tailrec
    @tailrec
    def iterate(r: Double): Double =
      r match {
        case r if r < 0.0 =>
          iterate(r + Angle.TwoPi)
        case r if r > Angle.TwoPi =>
          iterate(r - Angle.TwoPi)
        case r => r
      }

    Angle(iterate(toRadians))
  }

  /** Angle as the proportion of a full turn around a circle */
  def toTurns: Double =
    this.toRadians / Angle.TwoPi

  def toDegrees: Double =
    (this.toRadians / Angle.TwoPi) * 360

  def toCanvas: String =
    this.normalize.toDegrees.toString

  override def toString: String =
    s"Angle(${toRadians.toString})"

  def copy(toRadians: Double = this.toRadians): Angle =
    new Angle(toRadians)

  override def equals(that: Any): Boolean =
    that.isInstanceOf[Angle] && that.asInstanceOf[Angle].toRadians == this.toRadians

  override def hashCode: Int =
    this.toRadians.hashCode
}

object Angle {
  val TwoPi = math.Pi * 2
  val zero = Angle(0.0)
  val one = Angle(TwoPi)

  def degrees(deg: Double): Angle =
    Angle(deg * TwoPi / 360.0)

  def radians(rad: Double): Angle =
    Angle(rad)

  /** A turn represents angle as a proportion of a full turn around a circle, with a full turn being 1.0
    */
  def turns(t: Double): Angle =
    Angle(t * TwoPi)

  def apply(radians: Double): Angle =
    if (radians.isNaN)
      new Angle(0.0)
    else
      new Angle(radians)
}
