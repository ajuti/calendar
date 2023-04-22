package calendar_classes

import java.time.LocalDateTime
import java.time.temporal.TemporalUnit
import java.time.temporal.ChronoUnit

// Class structure is mostly pulled from earlier O1-course exercise and refactored to use LocalDateTimes instead of Moments like in the original
class Interval(val start: LocalDateTime, val end: LocalDateTime):  
       
    // checks if this Interval contains given date
    def contains(date: LocalDateTime): Boolean =
        start.toLocalDate().equals(date.toLocalDate()) || (start.toLocalDate().isBefore(date.toLocalDate()) && date.toLocalDate().isBefore(end.toLocalDate()))
    end contains

    // checks if this Interval contains a whole another interval
    def contains(another: Interval): Boolean = 
        (another.start.toLocalDate().equals(this.start.toLocalDate()) || another.start.toLocalDate().isAfter(this.start.toLocalDate())) && (another.end.toLocalDate().equals(this.end.toLocalDate()) || another.end.toLocalDate().isBefore(this.end.toLocalDate()))
    end contains

    // checks if two Intervals overlap e.g. 1-5 overlaps with 3-7
    def overlaps(another: Interval): Boolean = this.contains(another.start) || another.contains(this.end) || this.contains(another.end)

    def lengthInHours: Long = this.start.until(this.`end`, ChronoUnit.HOURS)

    def lengthInDays: Long = this.`end`.getDayOfYear() - this.start.getDayOfYear() + 1

    def lengthInMinutes: Long = this.start.until(this.`end`, ChronoUnit.MINUTES)

    // checks whether two intervals intersect each other
    def intersects(interval: Interval): Boolean = 
        this.contains(interval) || interval.contains(this) || this.overlaps(interval) || interval.overlaps(this)

    // checks whether the event is 
    def sameDay = 
        start.getDayOfYear() == `end`.getDayOfYear() || `end`.getHour() == 0 && `end`.getMinute() == 0 && this.lengthInHours < 24

    
    override def toString(): String = this.start.toString() +  ";" + this.end.toString()

end Interval