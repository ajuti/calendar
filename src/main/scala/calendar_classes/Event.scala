package calendar_classes

import calendar_classes.service.GetWeek

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import java.time.*

class Event(private val name: String, private val startingTime: LocalDateTime, private val endingTime: LocalDateTime, stringTags: String = "", private var extraInfo: String = ""):

  private var bannerColor: Option[ColorTag] = None

  private val tags: Map[String, Tag] = Map[String, Tag]()
  if stringTags.nonEmpty then stringTags.split(", ").foreach(addTag(_))

  def getName = this.name

  def getWeek = GetWeek.getWeek2(startingTime)

  def getDay = startingTime.getDayOfYear

  def getTime: String = startingTime.toString + " - " + endingTime.toString

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

  def getInfo = this.extraInfo

  override def toString = s"${this.name}"