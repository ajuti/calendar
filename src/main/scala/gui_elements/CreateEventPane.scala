package gui_elements

import scalafx.scene.layout.FlowPane
import scalafx.scene._
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.text._
import scalafx.scene.image._
import scalafx.geometry._
import scalafx.scene.layout._
import scalafx.scene.paint._
import scala.collection.mutable.Buffer
import calendar_classes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.input.MouseEvent
import gui_elements.WindowGenerator.genNewPopupForEditing
import gui_elements.MainGUI.popupOpen
import gui_elements.WindowGenerator

object CreateEventPane:

    def freshWeeklyPane(c: Event): Pane = 
        val eventTime = c.getInterval
        
        new Pane {
            if eventTime.sameDay then 
                prefHeight_=(eventTime.lengthInMinutes / 15 * 8.75)
                prefWidth = 125
                layoutY_=(30 + eventTime.start.getHour() * 35 + (eventTime.start.getMinute()/15) * 8.75)
                layoutX_=(47 + (eventTime.start.getDayOfWeek().getValue() - 1) * 130)
            else
                prefHeight_=(20)
                prefWidth = eventTime.lengthInDays * 130 - 5
                layoutY_=(3)
                layoutX_=((eventTime.start.getDayOfWeek().getValue() - 1) * 130)
            background = Background.fill(c.getColor.getOrElse(Color.BlanchedAlmond))
            children += new Label {
                text = if sameDay(eventTime) && eventTime.lengthInMinutes >= 60 then
                            if c.getName.length() > 15 then 
                                (c.getName.substring(0, 15) + "...") + "\n" + eventTime.start.getHour() + ":" + (if eventTime.start.getMinute() == 0 then "00" else eventTime.start.getMinute()) + " - " + eventTime.`end`.getHour() + ":" + (if eventTime.end.getMinute() == 0 then "00" else eventTime.`end`.getMinute())
                            else
                                c.getName + "\n" + eventTime.start.getHour() + ":" + (if eventTime.start.getMinute() == 0 then "00" else eventTime.start.getMinute()) + " - " + eventTime.`end`.getHour() + ":" + (if eventTime.end.getMinute() == 0 then "00" else eventTime.end.getMinute())
                        else if !sameDay(eventTime) || (sameDay(eventTime) && eventTime.lengthInMinutes >= 30) then
                            c.getName
                        else
                            ""
                maxWidth = 120
                font = new Font(13)
                layoutX = 3
            }
            border_=(Border.stroke(Color.Black))
            
            onMouseEntered = (e: MouseEvent) => {
                clickToEdit = true
                val info = if c.getInfo != "!empty!" then c.getInfo else ""
                val tags = if c.getTags != "!empty!" then c.getTags else ""
                val startDate = s"${eventTime.start.getDayOfMonth()}.${eventTime.start.getMonthValue()}. "
                val endDate = s"${eventTime.end.getDayOfMonth()}.${eventTime.end.getMonthValue()}. "
                eventToolTip.text = 
                    s"${c.getName}\n${if !sameDay(eventTime) then startDate else ""}${eventTime.start.getHour()}:${if eventTime.start.getMinute() == 0 then "00" else eventTime.start.getMinute()} - ${if !sameDay(eventTime) then endDate else ""}${eventTime.end.getHour()}:${if eventTime.`end`.getMinute() == 0 then "00" else eventTime.`end`.getMinute()}${if info.nonEmpty then  "\n" + info else ""}${if tags.nonEmpty then "\n" + tags else ""}".trim()
                eventToolTip.show(this, e.getScreenX(), e.getScreenY() + 10)
            }
            onMouseExited = (e: MouseEvent) => {
                clickToEdit = false
                eventToolTip.hide()
                eventToolTip.text = null
            }
            onMouseClicked = (e: MouseEvent) => {
                if !popupOpen then
                    val editWindow = genNewPopupForEditing(c)
                    editWindow.show()
                    popupOpen = true

            }
        }
    end freshWeeklyPane

    def freshDailyPane(c: Event): Pane = 
        val eventTime = c.getInterval
        val startDate = s"${eventTime.start.getDayOfMonth()}.${eventTime.start.getMonthValue()}. "
        val endDate = s"${eventTime.end.getDayOfMonth()}.${eventTime.end.getMonthValue()}. "
        val info = if c.getInfo != "!empty!" then c.getInfo else ""
        val tags = if c.getTags != "!empty!" then c.getTags else ""
        
        new Pane {
            if sameDay(eventTime) then 
                prefHeight_=(eventTime.lengthInMinutes / 15 * 8.75)
                prefWidth = rootWidth * 0.74 - 60
                layoutY_=(30 + eventTime.start.getHour() * 35 + (eventTime.start.getMinute()/15) * 8.75)
            else
                prefHeight_=(20)
                prefWidth = rootWidth * 0.74 - 60
                layoutY_=(3)
            layoutX_=(50)
            background = Background.fill(c.getColor.getOrElse(Color.BlanchedAlmond))
            children += new Label {
                text = if sameDay(eventTime) && eventTime.lengthInMinutes >= 60 then
                            c.getName + "\n" + eventTime.start.getHour() + ":" + (if eventTime.start.getMinute() == 0 then "00" else eventTime.start.getMinute()) + " - " + eventTime.`end`.getHour() + ":" + (if eventTime.end.getMinute() == 0 then "00" else eventTime.end.getMinute())
                        else if !sameDay(eventTime) || (sameDay(eventTime) && eventTime.lengthInMinutes >= 30) then
                            s"${c.getName} ${if !sameDay(eventTime) then startDate else ""}${eventTime.start.getHour()}:${if eventTime.start.getMinute() == 0 then "00" else eventTime.start.getMinute()} - ${if !sameDay(eventTime) then endDate else ""}${eventTime.end.getHour()}:${if eventTime.`end`.getMinute() == 0 then "00" else eventTime.`end`.getMinute()}".trim()
                        else
                            ""
                font = new Font(13)
                layoutX = 3
            }
            border_=(Border.stroke(Color.Black))
            
            onMouseEntered = (e: MouseEvent) => {
                clickToEdit = true
                eventToolTip.text = 
                    s"${c.getName}\n${if !sameDay(eventTime) then startDate else ""}${eventTime.start.getHour()}:${if eventTime.start.getMinute() == 0 then "00" else eventTime.start.getMinute()} - ${if !sameDay(eventTime) then endDate else ""}${eventTime.end.getHour()}:${if eventTime.`end`.getMinute() == 0 then "00" else eventTime.`end`.getMinute()}\n${info}\n${tags}".trim()
                eventToolTip.show(this, e.getScreenX(), e.getScreenY() + 10)
            }
            onMouseExited = (e: MouseEvent) => {
                clickToEdit = false
                eventToolTip.hide()
                eventToolTip.text = null
            }
            onMouseClicked = (e: MouseEvent) => {
                if !popupOpen then
                    val editWindow = genNewPopupForEditing(c)
                    editWindow.show()
                    popupOpen = true

            }
        }
    end freshDailyPane

    def initializeWeek(events: Buffer[Event]): Unit = 
        for i <- events do
            if i.getInterval.sameDay then 
                weekEvents.children += freshWeeklyPane(i)
            else
                weekBannerEvents.children += freshWeeklyPane(i)
    end initializeWeek

    def initializeDay(events: Buffer[Event]): Unit =
        for i <- events do
            if i.getInterval.sameDay then 
                dayEvents.children += freshDailyPane(i)
            else
                dayBannerEvents.children += freshDailyPane(i)
    end initializeDay

    def addOneWeeklyPane(event: Event) =
        val newP = freshWeeklyPane(event)
        weekEvents.children.add(newP)
    end addOneWeeklyPane

    def addOneDailyPane(event: Event) = 
        val newP = freshDailyPane(event)
        dayEvents.children.add(newP)
    end addOneDailyPane

