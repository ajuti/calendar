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
import gui_elements.MainGUI.weekEventPanes
import scalafx.stage._
import scalafx.event._
import scalafx.scene.input.MouseEvent
import scalafx.collections.ObservableArray
import scalafx.collections.ObservableBuffer
import java.time._
import calendar_classes.Event
import calendar_classes.Interval

object WindowGenerator:
    def genHours: IndexedSeq[String] =
        for c <- 0 to 23 yield
            if c < 10 then
                (s"0$c")
            else
                (s"$c")
    end genHours

    def genMinutes: IndexedSeq[String] =
        for c <- 0 to 3 yield
            if c == 0 then
                (s"0$c")
            else
                (s"${c * 15}")
    end genMinutes

    def setStartMinsFromNull(): String =
        val mins = LocalDateTime.now().getMinute()
        if mins < 15 then 
            "00"
        else if mins < 30 then 
            "15"
        else if mins < 45 then 
            "30"
        else
            "45"
    end setStartMinsFromNull

    def genNewPopup: Stage =
        val addEventPopup = new Stage {
            width_=(270)
            height_=(430)
            title = "Add new event"
            alwaysOnTop_=(true)
            scene = new Scene(270, 430) {
                val defFont = new Font(12)
                
                def shiftDay() = 
                    endTimeDatePicker.value_=(endTimeDatePicker.getValue().plusDays(1))
                    endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours)
                    endTimeCBoxHours.value_=("00")
                    endTimeCBoxMinutes.items_=(new ObservableBuffer ++= genMinutes)
                end shiftDay


                val nameTxtField = new TextField {
                    promptText = "Event name"
                    prefWidth = 140
                    layoutX = 100
                    layoutY = 17
                }
                val startTimeDatePicker: DatePicker = new DatePicker {
                    layoutX = 100
                    layoutY = 47
                    prefWidth = 140     
                    value_=(calendar1.getCurrentDate.toLocalDate())
                    this.valueProperty().onChange(
                        if this.getValue().isEqual(endTimeDatePicker.getValue()) && startTimeCBoxHours.getValue() != "23" then
                            if startTimeCBoxMinutes.getValue() != "45" then
                                endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours.filter(_ >= startTimeCBoxHours.getValue()))
                            else
                                endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours.filter(_ > startTimeCBoxHours.getValue()))

                        else if this.getValue().isEqual(endTimeDatePicker.getValue()) then
                            endTimeDatePicker.value_=(endTimeDatePicker.getValue().plusDays(1))
                            endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours)
                            endTimeCBoxMinutes.items_=(new ObservableBuffer ++= genMinutes)

                        else if this.getValue().isBefore(endTimeDatePicker.getValue()) then
                            endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours)
                            endTimeCBoxMinutes.items_=(new ObservableBuffer ++= genMinutes)

                        else
                            endTimeDatePicker.value_=(this.getValue())
                        )
                }
                val endTimeDatePicker: DatePicker = new DatePicker {
                    layoutX = 100
                    layoutY = 110
                    prefWidth = 140
                    value_=(
                        if calendar1.getCurrentDate.getHour() != 23 then
                            calendar1.getCurrentDate.toLocalDate() 
                        else
                            calendar1.getCurrentDate.toLocalDate().plusDays(1)
                        )
                    this.valueProperty().onChange(
                        if this.getValue().isAfter(startTimeDatePicker.getValue()) then
                            endTimeCBoxHours.items_=(
                                new ObservableBuffer ++= genHours
                            )
                        else
                            endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours.filter(_ >= startTimeCBoxHours.getValue()))
                            endTimeCBoxMinutes.items_=(new ObservableBuffer ++= genMinutes.filter(_ > startTimeCBoxMinutes.getValue()))
                    )
                }   
                val startTimeCBoxHours: ComboBox[String] = new ComboBox[String] {
                    for c <- genHours do
                        +=(c)
                    end for
                    value_=(
                        if LocalDateTime.now().getHour() < 10 then
                            s"0${LocalDateTime.now().getHour().toString()}"
                        else
                            s"${LocalDateTime.now().getHour().toString()}"
                        )
                    layoutX = 100
                    layoutY = 74
                    prefWidth_=(65)
                    promptText_=("Hr")
                    this.valueProperty().onChange(
                        if startTimeDatePicker.getValue().isEqual(endTimeDatePicker.getValue()) then
                            if this.getValue() != "23" && startTimeCBoxMinutes.getValue() != "45" then
                                endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours.filter(_ >= this.getValue()))
                                endTimeCBoxMinutes.items_=(new ObservableBuffer ++= genMinutes.filter(_ > startTimeCBoxMinutes.getValue()))
                            else if this.getValue != "23" && startTimeCBoxMinutes.getValue() == "45" then
                                endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours.filter(_ > this.getValue()))
                            else if this.getValue == "23" && startTimeCBoxMinutes.getValue() != "45" then
                                endTimeCBoxMinutes.items_=(new ObservableBuffer ++= genMinutes.filter(_ > startTimeCBoxMinutes.getValue()))
                            else
                                shiftDay()
                        else
                            {}
                        )
                }   
                val startTimeCBoxMinutes = new ComboBox[String] {
                    for c <- 0 to 3 do
                        if c == 0 then
                            +=(s"0$c")
                        else
                            +=(s"${c * 15}")
                    value_=(setStartMinsFromNull())
                    layoutX = 175
                    layoutY = 74
                    prefWidth_=(65)
                    promptText_=("Min")
                    this.valueProperty().onChange(
                        if startTimeDatePicker.getValue().isEqual(endTimeDatePicker.getValue()) then 
                            if this.getValue() != "45" then
                                endTimeCBoxMinutes.items_=(new ObservableBuffer ++= genMinutes.filter(_ > this.getValue()))
                            else
                                if startTimeCBoxHours.getValue() == "23" then
                                    shiftDay()
                                else
                                    endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours.filter(_ > startTimeCBoxHours.getValue()))
                        else
                            endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours)
                            endTimeCBoxMinutes.items_=(new ObservableBuffer ++= genMinutes)
                    )   
                }  
                val endTimeCBoxHours: ComboBox[String] = new ComboBox[String] {
                    items_=(
                        if LocalDateTime.now().getHour() != 23 then
                            new ObservableBuffer ++= genHours.filter(_ > startTimeCBoxHours.getValue())
                        else
                            new ObservableBuffer ++= genHours
                        )
                    value_=(this.items.get.head)
                    layoutX = 100
                    layoutY = 137
                    prefWidth_=(65)
                    promptText_=("Hr")
                }   
                val endTimeCBoxMinutes = new ComboBox[String] {
                    for c <- 0 to 3 do
                        if c == 0 then
                            +=(s"0$c")
                        else
                            +=(s"${c * 15}")
                    value_=(setStartMinsFromNull())
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
                }
                val tagsTxtField = new TextField {
                    promptText = "Tag name"
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
                    onMouseClicked = (e: MouseEvent) => 
                        if e.getClickCount() == 2 then
                            this.items.get().remove(this.selectionModel.get().getSelectedIndex())
                }
                val colPicker = new ColorPicker {
                    layoutX = 100
                    layoutY = 257
                    value = Color.Transparent
                }
                val saveEvent = new Button {
                    text = "Add event"
                    layoutX = 50
                    layoutY = 370
                    onAction = () =>
                        val freshE = new Event(
                            nameTxtField.text.value,
                            Interval(LocalDateTime.parse(startTimeDatePicker.getValue().toString() + "T" + startTimeCBoxHours.getValue() + ":" + startTimeCBoxMinutes.getValue()),
                                     LocalDateTime.parse(endTimeDatePicker.getValue().toString() + "T" + endTimeCBoxHours.getValue() + ":" + endTimeCBoxMinutes.getValue())),
                                     tagsList.items.get().mkString("-"),
                                     extrainfoTxtField.text.get(),
                                     Some(colPicker.getValue())
                        )
                        calendar1.addEvent(freshE)
                }
                val cancelEvent = new Button {
                    text = "Cancel"
                    layoutX = 150
                    layoutY = 370
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
                                    cancelEvent
                                    )
                }
                root = popupRootPane
            }
            resizable_=(false)
        }
        addEventPopup
    end genNewPopup

end WindowGenerator

