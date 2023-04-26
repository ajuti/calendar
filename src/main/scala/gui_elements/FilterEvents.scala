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
import calendar_classes.Event

def allTags = calendar1.getAllEvents.map(_.getTags.split(';').filterNot(x => x == "!empty!" || x == "")).flatten.distinct

def scanTags(event: Event) = 
    var tagFound = false
    for i <- event.getTagsList do
        if checkBoxes.filter(x => x.isSelected()).map(x => x.text.getValue()).contains(i) || i == "" then
            tagFound = true
    tagFound
end scanTags

def getCheckBoxes =
    for i <- allTags yield
        new CheckBox {
            text = i
            font = new Font(14)
            selected = true
            stylesheets += getClass().getResource("checkboxes.css").toExternalForm()
            onAction = () =>
                updatePanes(checkBoxes.filter(x => x.isSelected()).map(x => x.text.toString) += "")
        }
end getCheckBoxes
    
def updateCheckBoxes(): Unit = 
    checkBoxes.clear()
    getCheckBoxes.foreach(checkBoxes += _)
    paneOfCheckBoxes.children = checkBoxes
    for i <- paneOfCheckBoxes.children.indices do
        paneOfCheckBoxes.children(i).layoutY = (i / 3) * 25
        paneOfCheckBoxes.children(i).layoutX = (i % 3) * 100

end updateCheckBoxes

val checkBoxes: Buffer[CheckBox] = 
    getCheckBoxes

val paneOfCheckBoxes = new Pane {
    children = checkBoxes
    for i <- children.indices do
        children(i).layoutY = (i / 3) * 25
        children(i).layoutX = (i % 3) * 100
}

val scrollCheckBoxes = new ScrollPane {
    content = paneOfCheckBoxes
    prefHeight = 100
    border = Border.stroke(Color.Black)
}

val tagsLabel = new Label {
    text = "  Filter by tags"
    font = new Font(25)
}

