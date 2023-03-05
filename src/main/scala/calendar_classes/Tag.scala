package calendar_classes

import scala.compiletime.ops.string

case class Tag(val tagName: String):
  override def toString = this.tagName
end Tag


