package calendar_classes.service

import calendar_classes.{Week, Day, Calendar}
import java.time.LocalDateTime
import java.time.DayOfWeek
import calendar_classes.Interval

class DateAndWeekGen:

    def newDay(calendar: Calendar, offSet: Int): Day = 
        Day(calendar, genDayInterval(calendar.getCurrentDate.plusDays(offSet)))
    end newDay

    def newWeek(calendar: Calendar, oldWeek: Interval, offSet: Int): Week =
        Week(calendar, Interval(oldWeek.start.plusDays(offSet), oldWeek.`end`.plusDays(offSet)))
    end newWeek

    def genWeekInterval(date: LocalDateTime): Interval = 
        date.getDayOfWeek() match
            case DayOfWeek.MONDAY   => Interval(date.withHour(0).withMinute(0), date.plusDays(6).withHour(23).withMinute(59))
            case DayOfWeek.TUESDAY  => Interval(date.minusDays(1).withHour(0).withMinute(0), date.plusDays(5).withHour(23).withMinute(59))
            case DayOfWeek.WEDNESDAY => Interval(date.minusDays(2).withHour(0).withMinute(0), date.plusDays(4).withHour(23).withHour(59))
            case DayOfWeek.THURSDAY => Interval(date.minusDays(3).withHour(0).withMinute(0), date.plusDays(3).withHour(23).withMinute(59))
            case DayOfWeek.FRIDAY   => Interval(date.minusDays(4).withHour(0).withMinute(0), date.plusDays(2).withHour(23).withMinute(59))
            case DayOfWeek.SATURDAY => Interval(date.minusDays(5).withHour(0).withMinute(0), date.plusDays(1).withHour(23).withMinute(59))
            case DayOfWeek.SUNDAY   => Interval(date.minusDays(6).withHour(0).withMinute(0), date.withHour(23).withMinute(59)) 
    end genWeekInterval

    def genDayInterval(date: LocalDateTime): Interval = 
        Interval(date.withHour(0).withMinute(0), date.withHour(23).withMinute(59))
    end genDayInterval
        
end DateAndWeekGen