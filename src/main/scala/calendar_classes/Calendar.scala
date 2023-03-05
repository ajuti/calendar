package calendar_classes

import calendar_classes.service.{GetWeek, DateAndWeekGen}
import calendar_classes.{Week, Day}

import java.time.LocalDateTime
import scala.collection.mutable.Buffer

class Calendar:
  private val events = Buffer[Event]() // stores all events of calendar
  
  // mutable variable for tracking day | Day object knows which events are scheduled for itself
  private var currentDay = Day(this, LocalDateTime.now())
  
  // mutable variable for tracking week | Week object also knows off of the events scheduled for that week
  private var currentWeek = Week(this, getWeekIndex, currentDay.getLdt.getYear)
  
  // Returns the index of the week (1 to 52 or 53 depending on leap years)
  // Utilizes GetWeek -serviceobject, which calculates week number, given the LocalDateTime
  def getWeekIndex = GetWeek.getWeek2(currentDay.getLdt)

  def getCurrentDay: LocalDateTime = currentDay.getLdt

  def getCurrentWeek: Week = this.currentWeek

  def getAllEvents: Buffer[Event] = this.events

  // Sets new Day and Week objects to the mutable variables keeping track of said values
  // Can be called with both Day and Week or just Week
  def setDayAndWeek(day: Day, week: Week) = 
    this.currentDay = day
    this.currentWeek = week
  end setDayAndWeek

  def setDayAndWeek(day: Day) = 
    this.currentDay = day
    this.currentWeek = DateAndWeekGen.newWeek(this, day)

  // Adds new event to list of all events as well as the current Day and Week if event is scheduled for current Day/Week
  def addEvent(event: Event): Unit = 
    events.addOne(event)
    if event.getWeek == this.currentWeek.getWeekNum then
      this.currentWeek.addEvent(event)
    if event.getStart.toLocalDate() == this.currentDay.getLdt.toLocalDate() then
      this.currentDay.addEvent(event)
  end addEvent

  // Deletes events from calendar and current Day/Week
  def deleteEvent(event: Event) = 
    events.remove(events.indexOf(event))
    if event.getWeek == this.currentWeek.getWeekNum then
      this.currentWeek.deleteEvent(event)
    if event.getStart.toLocalDate() == this.currentDay.getLdt.toLocalDate() then
      this.currentDay.deleteEvent(event)
  end deleteEvent

  // Filtering methods for string input and tag input
  // String, will give all events whose title contains or tags contain input filter

  // ByTags will only search with given tag:
  // E.g. given tag: Work, it will only show events tagged with Work, not events that
  // have "Work" in their title, but not Work as a tag 
  def searchEvents(filter: String): Buffer[Event] =
    events.filter(x => x.getName.contains(filter) || x.getTags.contains(filter))
  end searchEvents

  def searchByTags(tag: Tag): Buffer[Event] =
    events.filter(x => x.getTags.contains(tag.tagName))
  end searchByTags

  // Updates the internal state of calendar using setDayAndWeek
  def showNextWeek() =
    val newDayAndWeek = DateAndWeekGen.newDate(this, 7)
    setDayAndWeek(newDayAndWeek._1, newDayAndWeek._2)
  end showNextWeek

  def showPreviousWeek() = 
    val newDayAndWeek = DateAndWeekGen.newDate(this, -7)
    setDayAndWeek(newDayAndWeek._1, newDayAndWeek._2)
  end showPreviousWeek

  def showNextDay() = 
    val newDayAndWeek = DateAndWeekGen.newDate(this, 1)
    setDayAndWeek(newDayAndWeek._1, newDayAndWeek._2)
  end showNextDay

  def showPreviousDay() = 
    val newDayAndWeek = DateAndWeekGen.newDate(this, -1)
    setDayAndWeek(newDayAndWeek._1, newDayAndWeek._2)
  end showPreviousDay

end Calendar
