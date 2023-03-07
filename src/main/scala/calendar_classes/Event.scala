package calendar_classes

import calendar_classes.service.GetWeek

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import java.time.*
import java.awt.Color

class Event(private val name: String, private var interval: Interval, stringTags: String = "", private var extraInfo: String = ""):

  // Keeps track of which color the event should be displayed with
  // ColorTag object can store Colorhexcode values, or RGB
  private var bannerColor: Option[Color] = None

  // Stores all Tags in a Map
  private val tags: Map[String, Tag] = Map[String, Tag]()
  if stringTags.nonEmpty then stringTags.split(", ").foreach(addTag(_))

  def setTime(newStartAndEnd: (LocalDateTime, LocalDateTime)) = ???

  def getInterval = this.interval

  def getName: String = this.name

  // String representation of the timeperiod of the event
  def getTime: String = interval.toString()

  def getColor: Option[Color] = this.bannerColor

  // Sets the color for this event
  def setColor(hexcode: String) = this.bannerColor = Some(Color.decode(hexcode))

  def setColor(r: Int, g: Int, b: Int) = this.bannerColor = Some(Color(r, g, b))

  def removeColor() = this.bannerColor = None

  // Adds a tag to this event, by creating one with given string as input and adding it to the Map of all tags
  def addTag(name: String) = this.tags.addOne((name -> Tag(name)))

  def removeTag(name: String) = this.tags.remove(name)

  def getTags = this.tags.keys.mkString(", ")

  def addInfo(info: String) = this.extraInfo = info

  def deleteInfo = this.extraInfo == ""

  def getInfo = this.extraInfo

  override def toString = s"${this.name}"