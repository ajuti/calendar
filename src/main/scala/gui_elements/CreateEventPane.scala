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

object CreateEventPane:

    def freshPane(c: Event): Pane = 
        val eventTime = c.getInterval
        
        new Pane {
            if sameDay(eventTime) then 
                prefHeight_=(eventTime.lengthInMinutes / 15 * 8.75)
                prefWidth = 125
                layoutY_=(30 + eventTime.start.getHour() * 35 + (eventTime.start.getMinute()/15) * 8.75)
            else
                prefHeight_=(20)
                prefWidth = eventTime.lengthInDays * 130 - 5
                layoutY_=(3)
            layoutX_=(47 + (eventTime.start.getDayOfWeek().getValue() - 1) * 130)
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
                eventToolTip.text = 
                    s"${c.getName}\n${eventTime.start.getHour()}:${if eventTime.start.getMinute() == 0 then "00" else eventTime.start.getMinute()}-${eventTime.end.getHour()}:${if eventTime.`end`.getMinute() == 0 then "00" else eventTime.`end`.getMinute()}\n${info}\n${tags}".trim()
                eventToolTip.show(this, e.getScreenX(), e.getScreenY() + 10)
            }
            onMouseExited = (e: MouseEvent) => {
                clickToEdit = false
                eventToolTip.hide()
                eventToolTip.text = null
            }
            onMouseClicked = (e: MouseEvent) => {
                if !popupOpen then
                    val editWindow = genNewPopupForEditing(this, c)
                    editWindow.show()
                    popupOpen = true

            }
        }
    end freshPane

    def initialize(events: Buffer[Event]) = 
        for i <- events yield
            freshPane(i)
    end initialize

    def addOnePane(event: Event) =
        val newP = freshPane(event)
        weekEvents.children.add(newP)
    end addOnePane

