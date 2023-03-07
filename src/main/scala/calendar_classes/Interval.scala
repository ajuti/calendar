package calendar_classes

import java.time.LocalDateTime

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

    def length: Int = this.start.compareTo(this.end)

    // checks whether two intervals intersect each other
    def intersects(interval: Interval): Boolean = 
        this.contains(interval) || interval.contains(this) || this.overlaps(interval) || interval.overlaps(this)
    
    override def toString(): String = this.start.toString() +  " - " + this.end.toString()

end Interval