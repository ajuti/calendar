package calendar_classes

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import java.time.*

class Event(private val name: String, private val startingTime: LocalDateTime, private val endingTime: LocalDateTime, stringTags: String = "", private var extraInfo: String = ""):

  private var bannerColor: Option[ColorTag] = None

  private val tags: Map[String, Tag] = Map[String, Tag]()
  if stringTags.nonEmpty then stringTags.split(", ").foreach(addTag(_))

  def getName = this.name

  def getWeek =
    var offSet: Int = 0
    startingTime.getDayOfWeek match
      case DayOfWeek.MONDAY     =>
      case DayOfWeek.TUESDAY    => offSet = 1
      case DayOfWeek.WEDNESDAY  => offSet = 2
      case DayOfWeek.THURSDAY   => offSet = 3
      case DayOfWeek.FRIDAY     => offSet = 4
      case DayOfWeek.SATURDAY   => offSet = 5
      case DayOfWeek.SUNDAY     => offSet = 6
    end match

    1 + ((startingTime.getDayOfYear - offSet - 1) / 7)
  end getWeek

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