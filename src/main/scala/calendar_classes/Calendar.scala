package calendar_classes

import calendar_classes.service.GetWeek
import calendar_classes.{Week, Day}

import java.time.LocalDateTime
import scala.collection.mutable.Buffer

class Calendar:
  private val events = Buffer[Event]()
  private var currentDay = Day(this, LocalDateTime.now())
  private var weekIndex = getWeekIndex
  private var yearIndex = currentDay.getLdt.getYear
  private var currentWeek: Week = Week(this, getWeekIndex, getYear)
  
  def getWeekIndex = GetWeek.getWeek2(currentDay.getLdt)

  def getWeek = this.currentWeek

  def getYear = yearIndex

  def getCurrentDay = currentDay.getLdt.getDayOfMonth + currentDay.getLdt.getMonth.toString

  def getCurrentWeek = this.currentWeek

  def getAllEvents = this.events

  def addEvent(event: Event): Unit = 
    events.addOne(event)
    if event.getWeek == this.currentWeek.getWeekNum then
      this.currentWeek.addEvent(event)
    if event.getStart.toLocalDate() == this.currentDay.getLdt.toLocalDate() then
      this.currentDay.addEvent(event)

  def deleteEvent(event: Event) = events.remove(events.indexOf(event))

  def searchEvents(filter: String): Buffer[Event] =
    events.filter(x => x.getName == filter || x.getTags.contains(filter))
  end searchEvents

  def searchByTags(tag: Tag): Buffer[Event] =
    events.filter(x => x.getTags.contains(tag.tagName))

  def showNextWeek() =
    if weekIndex == 53 || GetWeek.weeksInAYear(currentDay.getLdt) == 52 then
      weekIndex = 1
      yearIndex += 1
    else
      weekIndex = 53
    currentWeek = Week(this, weekIndex, yearIndex)
    currentDay = Day(this, currentDay.getLdt.plusDays(7))
  end showNextWeek

  def showPreviousWeek() = ???   

  def showNextDay() = ???

  def showPreviousDay() = ???

end Calendar
