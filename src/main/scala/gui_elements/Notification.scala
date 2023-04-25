package gui_elements

import scalafx.Includes._
import scalafx.scene._
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.util.Duration
import scalafx.animation.Timeline
import scala.collection.mutable.Buffer
import scalafx.animation.KeyFrame
import javafx.application.Platform
import gui_elements.MainGUI.calendar1
import java.time._
import java.time.temporal.ChronoUnit
import calendar_classes.Event
import gui_elements.MainGUI.primaryStage

object Notification:

    def showNotification(event: Event, now: Boolean = false) = 
        new Alert(AlertType.Information) {
            initOwner(primaryStage)
            title = "Event Notification" 
            headerText = s"Your event is starting ${if now then "now" else "soon"}!"
            if !now then
                contentText = s"${event.getName} starts in 60mins (${if0then00(event.getInterval.start, "hour")}:${if0then00(event.getInterval.start, "minute")})"
            else
                contentText = s"${event.getName} starting!"
        }.show()
    end showNotification

    val keyF = KeyFrame.apply(Duration(60000), onFinished = () => {
        calendar1.getAllEvents.foreach(
            x => 
                val tMinus60 = x.getInterval.start.minusMinutes(60).truncatedTo(ChronoUnit.MINUTES)
                val tMinus0 = x.getInterval.start.truncatedTo(ChronoUnit.MINUTES)
                if LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES) == tMinus60 then
                    showNotification(x)
                if LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES) == tMinus0 then
                    showNotification(x, true)
        )
    })

    val timeline = new Timeline {
        keyFrames += keyF
        cycleCount = Timeline.Indefinite
        jumpTo(Duration(LocalDateTime.now().getSecond() * 1000))
    }

end Notification
