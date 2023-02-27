package calendar_classes

import java.awt.Color

class Tag(val tagName: String):
  override def toString = this.tagName
end Tag

case class ColorTag(r: Int, g: Int, b: Int) extends Color(r, g, b)


