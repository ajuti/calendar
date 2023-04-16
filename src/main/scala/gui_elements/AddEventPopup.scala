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
import gui_elements.MainGUI.calendar1
import scala.collection.mutable.Buffer
import scalafx.stage._
import scalafx.event._
import scalafx.scene.input.MouseEvent
import scalafx.collections.ObservableArray
import scalafx.collections.ObservableBuffer
import java.time._
import calendar_classes.Event
import calendar_classes.Interval
import scala.math._
import scalafx.scene.input.KeyCode.S
import java.{util => ju}
import java.time.format.DateTimeParseException

object WindowGenerator:

    case class EmptyNameException(description: String) extends Exception(description)

    def genHours: ObservableBuffer[String] =
        val obsBuffer = new ObservableBuffer[String]()
        for c <- 0 to 23 do
            if c < 10 then
                obsBuffer += (s"0$c")
            else
                obsBuffer += (s"$c")
        obsBuffer
    end genHours

    def genMinutes: ObservableBuffer[String] =
        val obsBuffer = new ObservableBuffer[String]()
        for c <- 0 to 3 do
            if c == 0 then
                obsBuffer += (s"0$c")
            else
                obsBuffer += (s"${c * 15}")
        obsBuffer
    end genMinutes

    def setStartMinsFromNull(initDate: LocalDateTime = LocalDateTime.now()): String =
        val mins = initDate.getMinute()
        if mins < 15 then 
            "00"
        else if mins < 30 then 
            "15"
        else if mins < 45 then 
            "30"
        else
            "45"
    end setStartMinsFromNull

    def genNewPopupForEditing(pane: Pane, event: Event) = 
        genNewPopup(event.getInterval.start, true, Some(event), pane)
    end genNewPopupForEditing

    def genNewPopupFromClick(x: Double, y: Double) = 
        val week = calendar1.getCurrentWeek.getInterval
        genNewPopup(week.start.plusDays(((x - 47) / 130).toLong).withHour(((y - 30) / 35).floor.toInt).withMinute(((y - 30) % 35 / 8.75).floor.toInt * 15))

    def genNewPopup(initDate: LocalDateTime = LocalDateTime.now(), editing: Boolean = false, event: Option[Event] = None, existingPane: Pane = null): Stage =
        val addEventPopup = new Stage {
            width_=(270)
            height_=(430)
            title = if editing then "Edit event" else "Add new event"
            alwaysOnTop_=(true)
            scene = new Scene(270, 430) {
                val defFont = new Font(12)
                
                def shiftDay() = 
                    endTimeDatePicker.value_=(endTimeDatePicker.getValue().plusDays(1))
                    endTimeCBoxHours.items_=(genHours)
                    endTimeCBoxHours.value_=("00")
                    endTimeCBoxMinutes.items_=(genMinutes)
                end shiftDay
                
                // This is called only when the start date is the same as the end date
                def handleTimeChange(startHours: String, startMins: String): Unit = {
                    (startHours, startMins) match {
                        case ("23", "45") => shiftDay()
                        case ("23", _) => updateEndTimeMinutes()
                        case (_, "45") => updateEndTimeHours()
                        case (_, _) =>
                            updateEndTimeHours()
                            updateEndTimeMinutes(true)
                    }
                } 

                def updateEndTimeMinutes(bool: Boolean = false): Unit =
                    if startTimeCBoxHours.getValue() == endTimeCBoxHours.getValue() then
                        endTimeCBoxMinutes.items = genMinutes.filter(_ > startTimeCBoxMinutes.getValue())
                    else
                        endTimeCBoxMinutes.items = genMinutes
                end updateEndTimeMinutes

                def updateEndTimeHours(): Unit = 
                    if startTimeCBoxMinutes.getValue() != "45" then
                        endTimeCBoxHours.items = genHours.filter(_ >= startTimeCBoxHours.getValue())
                    else
                        endTimeCBoxHours.items = genHours.filter(_ > startTimeCBoxHours.getValue())

                end updateEndTimeHours

                val nameTxtField = new TextField {
                    promptText = "Event name"
                    prefWidth = 140
                    layoutX = 100
                    layoutY = 17
                    if editing then text = event.get.getName
                }
                val startTimeDatePicker: DatePicker = new DatePicker {
                    layoutX = 100
                    layoutY = 47
                    prefWidth = 140     
                    value_=(initDate.toLocalDate())
                    this.valueProperty().onChange(
                        if this.getValue().isBefore(endTimeDatePicker.getValue()) then
                            endTimeCBoxHours.items = genHours
                            endTimeCBoxMinutes.items = genMinutes
                        else
                            if this.getValue().isAfter(endTimeDatePicker.getValue()) then
                                endTimeDatePicker.value = startTimeDatePicker.getValue()
                            handleTimeChange(startTimeCBoxHours.getValue(), startTimeCBoxMinutes.getValue())
                    )
                }
                val endTimeDatePicker: DatePicker = new DatePicker {
                    layoutX = 100
                    layoutY = 110
                    prefWidth = 140
                    value_=(
                        if editing then
                            event.get.getInterval.`end`.toLocalDate()
                        else
                            if initDate.getHour() != 23 then
                                initDate.toLocalDate()
                            else    
                                initDate.toLocalDate().plusDays(1)
                        )
                    this.valueProperty().onChange(
                        if this.getValue().isAfter(startTimeDatePicker.getValue()) then
                            endTimeCBoxHours.items = genHours
                            endTimeCBoxMinutes.items = genMinutes
                        else
                            handleTimeChange(startTimeCBoxHours.getValue(), startTimeCBoxMinutes.getValue())
                    )
                }   
                val startTimeCBoxHours: ComboBox[String] = new ComboBox[String] {
                    for c <- genHours do
                        +=(c)
                    end for
                    value_=(
                        if initDate.getHour() < 10 then
                            s"0${initDate.getHour().toString()}"
                        else
                            s"${initDate.getHour().toString()}"
                        )
                    layoutX = 100
                    layoutY = 74
                    prefWidth_=(65)
                    promptText_=("Hr")
                    this.valueProperty().onChange(
                        if startTimeDatePicker.getValue().isEqual(endTimeDatePicker.getValue()) then
                            handleTimeChange(this.getValue(), startTimeCBoxMinutes.getValue())
                        else
                            endTimeCBoxHours.items = genHours
                            endTimeCBoxMinutes.items = genMinutes
                        )
                }   
                val startTimeCBoxMinutes = new ComboBox[String] {
                    for c <- 0 to 3 do
                        if c == 0 then
                            +=(s"0$c")
                        else
                            +=(s"${c * 15}")
                    value_=(
                        if editing then 
                            initDate.getMinute().toString()
                        else
                            setStartMinsFromNull(initDate)
                        )
                    layoutX = 175
                    layoutY = 74
                    prefWidth_=(65)
                    promptText_=("Min")
                    this.valueProperty().onChange(
                        if startTimeDatePicker.getValue().isEqual(endTimeDatePicker.getValue()) then 
                            handleTimeChange(startTimeCBoxHours.getValue(), this.getValue())    
                        else
                            endTimeCBoxHours.items = genHours
                            endTimeCBoxMinutes.items = genMinutes
                    )   
                }  
                val endTimeCBoxHours: ComboBox[String] = new ComboBox[String] {
                    layoutX = 100
                    layoutY = 137
                    prefWidth_=(65)
                    promptText_=("Hr")
                    this.valueProperty().onChange(
                        updateEndTimeMinutes()
                    )
                }   
                val endTimeCBoxMinutes = new ComboBox[String] {
                    /*for c <- 0 to 3 do
                        if c == 0 then
                            +=(s"0$c")
                        else
                            +=(s"${c * 15}") */
                    value_=(
                        if editing then
                            event.get.getInterval.`end`.getMinute().toString()
                        else
                            setStartMinsFromNull(initDate)
                        )
                    layoutX = 175
                    layoutY = 137
                    prefWidth_=(65)
                    promptText_=("Min")
                }   

                val extrainfoTxtField = new TextArea {
                    promptText = "Add info (optional)"
                    font = defFont
                    prefWidth = 140
                    prefHeight = 50
                    layoutX = 100
                    layoutY = 300
                    if editing then
                        text = event.get.getInfo
                }
                val tagsTxtField = new TextField {
                    promptText = "Tags (optional)"
                    font = defFont
                    prefWidth = 115
                    prefHeight = 27
                    layoutX = 100
                    layoutY = 173
                }
                val addTags = new Button {
                    text = "+"
                    prefWidth = 20
                    layoutX = 215
                    layoutY = 173
                    onAction = () =>
                        tagsList.items.get().add(tagsTxtField.text.value)
                }
                val tagsList = new ListView(List[String]()) {
                    prefWidth = 140
                    prefHeight = 50
                    layoutX = 100
                    layoutY = 200
                    items = new ObservableBuffer()
                    if editing && event.get.getTags != "!empty!" then
                        event.get.getTags.split(';').foreach(this.items.get().add(_))
                    onMouseClicked = (e: MouseEvent) => 
                        if e.getClickCount() == 2 then
                            this.items.get().remove(this.selectionModel.get().getSelectedIndex())
                }
                val colPicker = new ColorPicker {
                    layoutX = 100
                    layoutY = 257
                    value = 
                        if editing then
                            event.get.getColor.getOrElse(Color.BlanchedAlmond)
                        else
                            Color.BlanchedAlmond
                }
                val errorLabelName = new Label {
                    text = "event must have a name"
                    font = new Font(11) {
                        textFill = Color.Red
                    }
                    layoutX = 105
                    layoutY = 3
                    visible = false
                }
                val errorLabelTime = new Label {
                    text = "incorrect time"
                    font = new Font(11) {
                        textFill = Color.Red
                    }
                    layoutX = 10
                    layoutY = 125
                    visible = false
                }

                val saveEvent = new Button {
                    text = 
                        if editing then "Save changes" else "Add event"
                    layoutX = 50
                    layoutY = 360
                    onAction = () =>
                        errorLabelTime.visible = false
                        errorLabelName.visible = false
                        try
                            val eventName = nameTxtField.text.getValue

                            if eventName == "" then throw EmptyNameException("Name field was empty")

                            val freshE = new Event(
                                eventName,
                                Interval(LocalDateTime.parse(startTimeDatePicker.getValue().toString() + "T" + startTimeCBoxHours.getValue() + ":" + startTimeCBoxMinutes.getValue()),
                                         LocalDateTime.parse(endTimeDatePicker.getValue().toString() + "T" + endTimeCBoxHours.getValue() + ":" + endTimeCBoxMinutes.getValue())),
                                tagsList.items.get().mkString("-"),
                                extrainfoTxtField.text.get(),
                                Some(colPicker.getValue())
                            )
                            if editing then
                                calendar1.deleteEvent(event.get)
                                weekEvents.children -= existingPane
                            calendar1.addEvent(freshE)
                            if freshE.getInterval.intersects(calendar1.getCurrentWeek.getInterval) then
                                CreateEventPane.addOnePane(freshE)
                            close()
                        catch
                            case e: NullPointerException => errorLabelTime.visible = true
                            case e: DateTimeParseException => errorLabelTime.visible = true
                            case e: EmptyNameException => errorLabelName.visible = true

                }
                val cancelEvent = new Button {
                    text = "Cancel"
                    layoutX = 150
                    layoutY = 360
                    onAction = () =>
                        close()
                }
                val popupRootPane = new Pane {
                    children += new Label("Name:") {
                    layoutX = 10
                    layoutY = 20
                    font = defFont
                    }
                    children += new Label("Additional info:") { 
                    layoutX = 10
                    layoutY = 305
                    font = defFont
                    }
                    children += new Label("Start time:") {
                    layoutX = 10
                    layoutY = 50
                    font = defFont
                    }
                    children += new Label("End time:") {
                    layoutX = 10
                    layoutY = 112
                    font = defFont
                    }
                    children += new Label(":") {
                        layoutX = 168
                        layoutY = 80
                    }
                    children += new Label(":") {
                        layoutX = 168
                        layoutY = 142
                    }
                    children += new Label("Tags:") {
                        layoutX = 10
                        layoutY = 175
                    }
                    children += new Label("Color:") {
                        layoutX = 10
                        layoutY = 260
                    }
                    children ++= List(nameTxtField, 
                                    startTimeCBoxHours, 
                                    startTimeCBoxMinutes, 
                                    startTimeDatePicker, 
                                    extrainfoTxtField,
                                    endTimeCBoxHours,
                                    endTimeCBoxMinutes,
                                    endTimeDatePicker,
                                    tagsTxtField,
                                    addTags,
                                    tagsList,
                                    colPicker,
                                    saveEvent,
                                    cancelEvent,
                                    errorLabelName,
                                    errorLabelTime
                                    )
                    handleTimeChange(startTimeCBoxHours.getValue(), startTimeCBoxMinutes.getValue())
                    if editing then
                        endTimeCBoxHours.value = event.get.getInterval.`end`.getHour().toString()
                    else    
                        if endTimeCBoxHours.items.get().size() >= 2 then 
                            endTimeCBoxHours.value = endTimeCBoxHours.items.get().apply(1) 
                        else 
                            endTimeCBoxHours.value = "00"
                    endTimeCBoxMinutes.value = startTimeCBoxMinutes.getValue()
                }
                root = popupRootPane
            }
            resizable_=(false)
        }
        addEventPopup
    end genNewPopup

end WindowGenerator

