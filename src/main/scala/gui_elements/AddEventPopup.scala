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
            width_=(300)
            height_=(500)
            title = "Add new event"
            alwaysOnTop_=(true)
            scene = new Scene(300, 500) {
                val defFont = new Font(12)

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
                            endTimeCBoxHours.items_=(
                                new ObservableBuffer ++= genHours.filter(_ > startTimeCBoxHours.getValue())
                            )
                        else if this.getValue().isEqual(endTimeDatePicker.getValue()) then
                            endTimeDatePicker.value_=(endTimeDatePicker.getValue().plusDays(1))
                            endTimeCBoxHours.items_=(
                                new ObservableBuffer ++= genHours
                            )
                        else if this.getValue().isBefore(endTimeDatePicker.getValue()) then
                            endTimeCBoxHours.items_=(
                                new ObservableBuffer ++= genHours
                            )
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
                            endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours.filter(_ > startTimeCBoxHours.getValue()))
                    )
                }   
                val startTimeCBoxHours = new ComboBox[String] {
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
                        if startTimeDatePicker.getValue().isEqual(endTimeDatePicker.getValue()) && this.getValue() != "23"  then
                            endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours.filter(_ > this.getValue()))
                        else if startTimeDatePicker.getValue().isEqual(endTimeDatePicker.getValue()) then
                            endTimeDatePicker.value_=(endTimeDatePicker.getValue().plusDays(1))
                            endTimeCBoxHours.items_=(new ObservableBuffer ++= genHours)
                            endTimeCBoxHours.value_=("00")
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

                val extrainfoTxtField = new TextField {
                    promptText = "Add info (optional)"
                    font = defFont
                    prefWidth = 140
                    layoutX = 100
                    layoutY = 247
                }
                val popupRootPane = new Pane {
                    children += new Label("Name:") {
                    layoutX = 10
                    layoutY = 20
                    font = defFont
                    }
                    children += new Label("Additional info:") { 
                    layoutX = 10
                    layoutY = 250
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
                    children += new Label(":"){
                        layoutX = 168
                        layoutY = 80
                    }
                    children += new Label(":"){
                        layoutX = 168
                        layoutY = 142
                    }
                    children ++= List(nameTxtField, 
                                    startTimeCBoxHours, 
                                    startTimeCBoxMinutes, 
                                    startTimeDatePicker, 
                                    extrainfoTxtField,
                                    endTimeCBoxHours,
                                    endTimeCBoxMinutes,
                                    endTimeDatePicker
                                    )
                }
                root = popupRootPane
            }
            resizable_=(false)
        }
        addEventPopup
    end genNewPopup

end WindowGenerator

