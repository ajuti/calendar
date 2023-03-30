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
import scalafx.scene.input.KeyCode.U

object CreateEventPane:
    
    def initialize(events: Buffer[Event]) = 
        for c <- events yield
            val eventTime = c.getInterval
            new Pane {
                if eventTime.start.getDayOfYear() == eventTime.`end`.getDayOfYear() then 
                    prefHeight_=((eventTime.`end`.getHour() + eventTime.`end`.getMinute() - eventTime.start.getHour() - eventTime.start.getMinute()) * 35)
                else
                    prefHeight_=(870 - (eventTime.start.getHour() - eventTime.start.getMinute()) * 35)
                prefWidth = 125
                layoutX_=(47 + (eventTime.start.getDayOfWeek().getValue() - 1) * 130)
                layoutY_=(30 + eventTime.start.getHour() * 35)
                background = Background.fill(Color.DodgerBlue)
                children += new Label {
                    text = c.getName + "\n" + eventTime.start.getHour() + ":" + eventTime.start.getMinute() + " - " + eventTime.`end`.getHour() + ":" + eventTime.`end`.getMinute()
                    font = new Font(15)
                    layoutX = 3
                } 
                border_=(Border.stroke(Color.Black))
            }
    end initialize

    def updateEventPanes(allPanes: Buffer[Pane]): Unit =
        for c <- allPanes do
        oneWeek.children += c
