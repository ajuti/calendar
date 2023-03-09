package calendar_classes

import calendar_classes.service.GetWeek

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import java.time.*
import java.awt.Color

class Event(private val name: String, private var interval: Interval, stringTags: String = "!empty!", private var extraInfo: String = "!empty!", private var bannerColor: Option[Color] = None):

  // BannerColor keeps track of which color the event should be displayed with
  // ColorTag object can store Colorhexcode values, or RGB

  // Stores all Tags in a Map
  private val tags: Map[String, Tag] = Map[String, Tag]()
  if stringTags.nonEmpty then stringTags.split("-").foreach(addTag(_))

  def setTimeWithHours(start: Int, end: Int) = 
    interval = Interval(interval.start.plusHours(start), interval.`end`.plusHours(`end`))
  end setTimeWithHours

  def setTimeWithHours(hours: Int) = 
    interval = Interval(interval.start.plusHours(hours), interval.`end`.plusHours(hours))
  end setTimeWithHours

  def setTimeWithDays(days: Int) = 
    interval = Interval(interval.start.plusDays(days), interval.`end`.plusDays(days))
  end setTimeWithDays

  def setTimeWithLDT(date: LocalDateTime) =  
    interval = Interval(date, date.plusMinutes(interval.lengthInMinutes))

  def getInterval = this.interval

  def getName: String = this.name

  // String representation of the timeperiod of the event
  def getTime: String = interval.toString()

  def getColor: Option[Color] = this.bannerColor

  def getColorString: String =
    this.bannerColor match
      case None => "!empty!"
      case Some(color) => s"${color.getRed()}-${color.getGreen()}-${color.getBlue()}"
  end getColorString

  // Sets the color for this event
  def setColor(hexcode: String) = this.bannerColor = Some(Color.decode(hexcode))

  def setColor(r: Int, g: Int, b: Int) = this.bannerColor = Some(Color(r, g, b))

  def removeColor() = this.bannerColor = None

  // Adds a tag to this event, by creating one with given string as input and adding it to the Map of all tags
  def addTag(name: String) = this.tags.addOne((name -> Tag(name)))

  def removeTag(name: String) = this.tags.remove(name)

  def getTags = this.tags.keys.mkString(";")

  def addInfo(info: String) = this.extraInfo = info

  def deleteInfo = this.extraInfo == ""

  def getInfo = this.extraInfo

  override def toString = s"${this.name}"