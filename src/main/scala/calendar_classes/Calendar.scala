package calendar_classes

import java.time.LocalDateTime
import scala.collection.mutable.Buffer

class Calendar:
  private val events = Buffer[Event]()

  def getAllEvents = this.events

  def addEvent(event: Event) = events.addOne(event)

  def deleteEvent(event: Event) = events.remove(events.indexOf(event))

  def searchEvents(filter: String): Buffer[Event] =
    events.filter(x => x.getName == filter || x.getTags.contains(filter))
  end searchEvents

  def searchByTags(tag: Tag): Buffer[Event] =
    events.filter(x => x.getTags.contains(tag.tagName))

  def showNextWeek() = ???

  def showPreviousWeek() = ???

  def showNextDay() = ???

  def showPreviousDay() = ???


end Calendar

class Week(calendar: Calendar, private val weekNum: Int):
  private val events = calendar.getAllEvents.filter(x => x.getWeek == weekNum)

  override def toString = s"Week $weekNum"
end Week

class Day(calendar: Calendar, private val day: LocalDateTime):
  private val events = calendar.getAllEvents.filter(x => x.getDay == day.getDayOfYear)

  override def toString = s"${day.getDayOfMonth} of ${day.getMonth}"
end Day
