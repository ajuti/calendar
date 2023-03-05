package calendar_classes.service

import calendar_classes.{Week, Day, Calendar}

object DateAndWeekGen:

    def newDate(calendar: Calendar, offSet: Int): (Day, Week) = 
        val newDay = Day(calendar, calendar.getCurrentDay.plusDays(offSet))
        (
            newDay, Week(calendar, GetWeek.getWeek2(newDay.getLdt), newDay.getLdt.getYear())
        )
    end newDate

    def newWeek(calendar: Calendar, day: Day): Week =
        (
            Week(calendar, GetWeek.getWeek2(day.getLdt), day.getLdt.getYear())
        )
    end newWeek