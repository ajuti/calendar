import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import calendar_classes.{ColorTag, Event}

class EventTest extends AnyFlatSpec, Matchers:
  val subject = Event("work", "123")

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
