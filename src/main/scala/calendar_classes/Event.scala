package calendar_classes

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map

class Event(private val name: String, private val time: String, private val tags: Map[String, Tag] = Map[String, Tag](), private var extraInfo: String = ""):

  private var bannerColor: Option[ColorTag] = None

  def getName = this.name

  def getTime = this.time //FIXME: better formatting of the time later

  def getColor: Option[ColorTag] = this.bannerColor

  def setColor(colorTag: ColorTag) = this.bannerColor = Some(colorTag)

  def removeColor() = this.bannerColor = None

  def addTag(name: String) = this.tags.addOne((name -> Tag(name)))

  def removeTag(name: String) =
    if getTags.contains(name) then
      this.tags.remove(name)
    else ???
      //FIXME: throw exception probably

  def getTags = this.tags.keys.mkString(", ")

  def addInfo(info: String) = this.extraInfo = info