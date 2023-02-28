package calendar_classes

import calendar_classes.service.GetWeek
import calendar_classes.{Week, Day}

import java.time.LocalDateTime
import scala.collection.mutable.Buffer

class Calendar:
  private val events = Buffer[Event]()
  private var currentDay = LocalDateTime.of(2021, 1, 1, 10, 00)
  private var currentWeek: Week = Week(this, getWeekIndex, getYear) //FIXME: whole year/week still needs to be fixed
  private var weekIndex = getWeekIndex

  def getWeekIndex = GetWeek.getWeek2(currentDay)

  def getYear = currentDay.getYear

  def getCurrentDay = currentDay.getDayOfMonth + currentDay.getMonth.toString

  def getCurrentWeek = this.currentWeek

  def getAllEvents = this.events

  def addEvent(event: Event) = events.addOne(event)

  def deleteEvent(event: Event) = events.remove(events.indexOf(event))

  def searchEvents(filter: String): Buffer[Event] =
    events.filter(x => x.getName == filter || x.getTags.contains(filter))
  end searchEvents

  def searchByTags(tag: Tag): Buffer[Event] =
    events.filter(x => x.getTags.contains(tag.tagName))

  def showNextWeek() =

    currentWeek = Week(this, this.getWeekIndex, this.getYear)

  def showPreviousWeek() = ???

  def showNextDay() = ???

  def showPreviousDay() = ???

end Calendar
