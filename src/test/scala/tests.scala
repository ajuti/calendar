import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import calendar_classes.{Calendar, ColorTag, Day, Event, Week}
import java.time.*

class EventTest extends AnyFlatSpec, Matchers:
  val startingTime = LocalDateTime.of(2023, 03, 27, 10, 00)
  val endingTime = LocalDateTime.of(2023, 01, 01, 16, 30)
  val subject = Event("work", startingTime, endingTime)

  "setColor" should "assign a ColorTag to an event" in {
    subject.setColor(ColorTag(10,10,10))
    subject.getColor.nonEmpty
  }
  "removeColor" should "remove a color from bannerColor" in {
    subject.removeColor()
    assert(subject.getColor.isEmpty)
  }
  "subject.tags" should "be empty if no tags have been given for initialization of the object" in {
    assert(subject.getTags.isEmpty)
  }
  "addTag()" should "add a string to buffer of all tags" in {
    subject.addTag("school")
    subject.addTag("gym")
  }
  "getTags" should "return all tags as a string" in {
    println(subject.getTags)
  }
  "addInfo and getInfo" should "add additional info to the event, and get it in a string" in {
    subject.addInfo("extra info about the event")
    println(subject.getInfo)
  }
  "getTime" should "return given event's timeframe" in {
    println(subject.getTime)
  }
  "getWeek" should "return correct week in integer" in {
    println(subject.getWeek)
  }
end EventTest


class CalendarTest extends AnyFlatSpec, Matchers:
  val subject = new Calendar
  val startingTime = LocalDateTime.of(2023, 01, 01, 10, 00)
  val endingTime = LocalDateTime.of(2023, 01, 01, 16, 30)

  "addEvent and deleteEvent" should "create and add an Event to the calendar, and remove the Event from list of all events" in {
    val myEvent = Event("chess", startingTime, endingTime,  "moro, shakki, aarni")
    subject.addEvent(myEvent)
    assert(subject.getAllEvents.size === 1)
    println(subject.getAllEvents.head)

    subject.deleteEvent(myEvent)
    assert(subject.getAllEvents.isEmpty)
  }
  val e1 = Event("yksi", startingTime, endingTime, "moro")
  val e2 = Event("kaksi", startingTime, endingTime, "moro")
  val e3 = Event("kolme", startingTime, endingTime, "yksi")
  val e4 = Event("neljä", startingTime, endingTime)

  "searchEvents" should "return correct list of events" in {
    subject.addEvent(e1)
    subject.addEvent(e2)
    subject.addEvent(e3)
    subject.addEvent(e4)

    println(subject.searchEvents("yksi"))
    println(subject.searchEvents("gaming"))
    println(subject.searchEvents("moro"))
    subject.deleteEvent(e1)
    println(subject.searchEvents("yksi"))
  }


end CalendarTest

