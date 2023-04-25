package gui_elements

import scalafx.scene._
import scalafx.Includes._
import scalafx.scene.paint._
import scalafx.geometry.Insets
import scalafx.scene.effect._
import scalafx.scene.control._
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.scene.text._
import scalafx.scene.image._
import scalafx.scene.control.TabPane._
import calendar_classes.Calendar
import scala.collection.mutable.Buffer
import scalafx.event.ActionEvent
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import java.time.format.DateTimeFormatter
import gui_elements.WindowGenerator._
import gui_elements.MainGUI._
import scalafx.scene.input.MouseEvent

val checkBoxes: Buffer[CheckBox] = 
    for i <- calendar1.getAllEvents.map(_.getTags.split(';').filterNot(x => x == "!empty!" || x == "")).flatten.distinct yield
        new CheckBox {
            text = i
            font = new Font(15)
            selected = true
        }

val paneOfCheckBoxes = new Pane {
    children = checkBoxes
    for i <- children.indices do
        children(i).layoutY = i * 25
}

val scrollCheckBoxes = new ScrollPane {
    content = paneOfCheckBoxes
    prefHeight = 100
    border = Border.stroke(Color.Black)
}

