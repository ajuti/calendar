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
import scalafx.collections._
import java.time._
import calendar_classes.Event
import calendar_classes.Interval
import scala.math._
import scalafx.scene.input.KeyCode.S
import java.{util => ju}
import java.time.format.DateTimeParseException
import gui_elements.MainGUI.popupOpen
import scala.languageFeature.existentials

object WindowGenerator:

    case class EmptyNameException(description: String) extends Exception(description)
    case class EmptyTagFieldException(description: String) extends Exception(description)
    case class FieldTooLongException(description: String) extends Exception(description)
    case class EndTimeBeforeStartException(description: String) extends Exception(description)

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

    def genNewPopupFromDrag(start: (Double, Double), end: (Double, Double), addingDay: Boolean = false, shift15: Boolean = false) =
        val week = calendar1.getCurrentWeek.getInterval
        val day = calendar1.getCurrentDay.getInterval
        val startForDay = day.start.withHour(((start._2 - 30) / 35).floor.toInt).withMinute(((start._2 - 30) % 35 / 8.75).floor.toInt * 15)
        val startForWeek = week.start.plusDays(((start._1 - 47) / 130).toLong).withHour(((start._2 - 30) / 35).floor.toInt).withMinute(((start._2 - 30) % 35 / 8.75).floor.toInt * 15)
                
        try
            val endForDay = day.start.withHour(((end._2 - 30) / 35).floor.toInt).withMinute(((end._2 - 30) % 35 / 8.75).floor.toInt * 15).plusMinutes(if shift15 then 15 else 0)
            val endForWeek = week.start.plusDays(((start._1 - 47) / 130).toLong).withHour(((end._2 - 30) / 35).floor.toInt).withMinute(((end._2 - 30) % 35 / 8.75).floor.toInt * 15).plusMinutes(if shift15 then 15 else 0)   
            if addingDay then
                genNewPopup(startForDay, endForDay, isDragging = true)
            else    
                genNewPopup(startForWeek, endForWeek, isDragging = true)
        catch
            case e: DateTimeException => 
                if addingDay then
                    genNewPopup(startForDay, startForDay.plusDays(1).withHour(0).withMinute(0), isDragging = true)
                else    
                    genNewPopup(startForWeek, startForWeek.plusDays(1).withHour(0).withMinute(0), isDragging = true)

    end genNewPopupFromDrag

    def genNewPopupForEditing(event: Event) = 
        genNewPopup(event.getInterval.start, editing = true, event = Some(event))
    end genNewPopupForEditing

    def genNewPopupFromClick(x: Double, y: Double, addingDay: Boolean = false) =   
        val week = calendar1.getCurrentWeek.getInterval
        val day = calendar1.getCurrentDay.getInterval
        if addingDay then
            genNewPopup(day.start.withHour(((y - 30) / 35).floor.toInt).withMinute(((y - 30) % 35 / 8.75).floor.toInt * 15))
        else    
            genNewPopup(week.start.plusDays(((x - 47) / 130).toLong).withHour(((y - 30) / 35).floor.toInt).withMinute(((y - 30) % 35 / 8.75).floor.toInt * 15))
    end genNewPopupFromClick

    def updatePanes(tags: Buffer[String] = allTags) = 
        calendar1.getCurrentDay.updateEvents()
        calendar1.getCurrentWeek.updateEvents()
        weekEvents.children.clear()
        dayEvents.children.clear()
        weekBannerEvents.children.clear()
        dayBannerEvents.children.clear()
        CreateEventPane.initializeWeek(calendar1.getCurrentWeek.getEvents.filter(x => scanTags(x)))
        CreateEventPane.initializeDay(calendar1.getCurrentDay.getEvents.filter(x => scanTags(x)))
        for i <- weekBannerEvents.children.indices do
            weekBannerEvents.children(i).layoutY = 2 + i * 20
        for i <- dayBannerEvents.children.indices do
            dayBannerEvents.children(i).layoutY = 2 + i * 20

    def genNewPopup(initDate: LocalDateTime = LocalDateTime.now(), initEnd: LocalDateTime = LocalDateTime.now(), editing: Boolean = false, event: Option[Event] = None, isDragging: Boolean = false): Stage =
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
                    endTimeCBoxMinutes.value_=(startTimeCBoxMinutes.getValue())
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
                    editable = false
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
                    editable = false
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
                        try 
                            if this.getValue().isAfter(startTimeDatePicker.getValue()) then
                                endTimeCBoxHours.items = genHours
                                endTimeCBoxMinutes.items = genMinutes
                            else if this.getValue().isEqual(startTimeDatePicker.getValue()) then
                                handleTimeChange(startTimeCBoxHours.getValue(), startTimeCBoxMinutes.getValue())
                            else
                                throw EndTimeBeforeStartException("End date cannot be before start date")
                        catch
                            case e: EndTimeBeforeStartException => 
                                errorLabelTime.text = "End date can't be\nbefore start date"
                                errorLabelTime.visible = true
                                this.value = startTimeDatePicker.getValue()
                    )
                    onMousePressed = () =>
                        errorLabelTime.visible = false
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
                            if initDate.getMinute() != 0 then
                                initDate.getMinute().toString()
                            else 
                                "00"
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
                    value_=(
                        if editing then
                            if event.get.getInterval.`end`.getMinute() != 0 then
                                event.get.getInterval.`end`.getMinute().toString()
                            else
                                "00"
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
                        text = 
                            if event.get.getInfo != "!empty!" then event.get.getInfo else ""
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
                    prefWidth = 25
                    layoutX = 215
                    layoutY = 173
                    onAction = () =>
                        try 
                            val tags = tagsTxtField.text.value.trim()
                            if tags.isEmpty() then throw EmptyTagFieldException("The text field was empty")
                            if tags.length() > 10 then throw FieldTooLongException("Tags can't be longer than 10 characters")
                            tagsList.items.get().add(tagsTxtField.text.value.trim())
                        catch
                            case e: EmptyTagFieldException =>
                                errorLabelTags.text = "can't be empty"
                                errorLabelTags.visible = true
                            case e: FieldTooLongException =>
                                errorLabelTags.text = "max 10 chars"
                                errorLabelTags.visible = true
                        tagsList.scrollTo(tagsList.items.get().size() - 1)
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
                            try
                                this.items.get().remove(this.selectionModel.get().getSelectedIndex())
                            catch
                                case e: IndexOutOfBoundsException => println("Nothing to remove")
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
                val errorLabelTags = new Label {
                    text = "can't be empty"
                    font = new Font(11) {
                        textFill = Color.Red
                    }
                    layoutX = 10
                    layoutY = 190
                    visible = false
                }

                val saveEvent = new Button {
                    text = 
                        if editing then "Save changes" else "Add event"
                    layoutX = 
                        if editing then 30 else 50
                    layoutY = 360
                    onAction = () =>
                        errorLabelTime.visible = false
                        errorLabelName.visible = false
                        errorLabelTags.visible = false
                        tempPaneWeek.prefHeight = 0.0
                        tempPaneDay.prefHeight = 0.0
                        try
                            val eventName = nameTxtField.text.getValue
                            if eventName == "" then throw EmptyNameException("Name field was empty")
                            val eventTime = Interval(LocalDateTime.parse(startTimeDatePicker.getValue().toString() + "T" + startTimeCBoxHours.getValue() + ":" + startTimeCBoxMinutes.getValue()),
                                             LocalDateTime.parse(endTimeDatePicker.getValue().toString() + "T" + endTimeCBoxHours.getValue() + ":" + endTimeCBoxMinutes.getValue()))
                            val tagsToBeAdded = tagsList.items.get().mkString("-")
                            val extraInformation = if extrainfoTxtField.text.get() != "!empty!" then extrainfoTxtField.text.get() else ""
                            
                            if editing then
                                val edited = event.get
                                edited.setName(eventName)
                                edited.setNewInterval(eventTime)
                                edited.removeAllTags()
                                tagsToBeAdded.split('-').foreach(x => if x.nonEmpty then edited.addTag(x))
                                if extraInformation != "!empty!" then edited.addInfo(extraInformation) else ""
                                edited.setColor(colPicker.getValue())

                                updatePanes()
                            else
                                val freshE = new Event(
                                    eventName,
                                    eventTime,
                                    tagsToBeAdded,
                                    extraInformation,
                                    Some(colPicker.getValue())
                                )
                                calendar1.addEvent(freshE)    
                                updatePanes()
                            close()
                            popupOpen = false
                        catch
                            case e: NullPointerException => 
                                errorLabelTime.text = "Incorrect time"
                                errorLabelTime.visible = true
                            case e: DateTimeParseException => 
                                errorLabelTime.text = "Incorrect time"
                                errorLabelTime.visible = true
                            case e: EmptyNameException => errorLabelName.visible = true
                        calendar1.upload()
                        updateCheckBoxes()
                        updatePanes()

                }
                val cancelEvent = new Button {
                    text = "Cancel"
                    layoutX = 
                        if editing then 127 else 150
                    layoutY = 360
                    onAction = () =>
                        tempPaneWeek.prefHeight = 0.0
                        tempPaneDay.prefHeight = 0.0
                        close()
                        popupOpen = false
                        calendar1.upload()
                        updateCheckBoxes()
                        updatePanes()
                }
                val deleteEvent = new Button {
                    text = "Delete"
                    layoutX = 185
                    layoutY = 360
                    disable = !editing
                    visible = editing
                    onAction = () =>
                        calendar1.deleteEvent(event.get)
                        updatePanes()
                        close()
                        popupOpen = false
                        calendar1.upload()
                        updateCheckBoxes()
                        updatePanes()
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
                                    errorLabelTime,
                                    deleteEvent,
                                    errorLabelTags
                                    )
                    if editing then
                        val hour = event.get.getInterval.`end`.getHour()
                        endTimeCBoxHours.value = if hour >= 10 then hour.toString() else s"0${hour}"
                        if !sameDay(event.get.getInterval) then 
                            endTimeCBoxHours.items = genHours
                        updateEndTimeHours()
                    else if !startTimeDatePicker.getValue().isEqual(endTimeDatePicker.getValue()) then
                        endTimeCBoxHours.items = genHours
                        endTimeCBoxHours.value = "00"
                    else
                        handleTimeChange(startTimeCBoxHours.getValue(), startTimeCBoxMinutes.getValue())
                        if startTimeCBoxMinutes.getValue != "45" then
                            endTimeCBoxHours.value = endTimeCBoxHours.items.get().drop(1).headOption.getOrElse("23")
                        else
                            endTimeCBoxHours.value = endTimeCBoxHours.items.get().headOption.getOrElse("23")
                    if isDragging then
                        endTimeCBoxHours.value = if initEnd.getHour() >= 10 then initEnd.getHour().toString() else s"0${initEnd.getHour()}"
                        endTimeCBoxMinutes.value = if initEnd.getMinute() >= 10 then setStartMinsFromNull(initEnd) else s"00"
                        if endTimeCBoxHours.value.get() == "00" then
                            endTimeDatePicker.value = endTimeDatePicker.getValue().plusDays(1)
                }
                root = popupRootPane
            }
            onCloseRequest = () => 
                popupOpen = false
                tempPaneDay.prefHeight = 0.0
                tempPaneWeek.prefHeight = 0.0
            resizable_=(false)
        }
        addEventPopup
    end genNewPopup

end WindowGenerator

