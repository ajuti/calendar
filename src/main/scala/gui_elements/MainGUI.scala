package gui_elements

import scalafx.application.JFXApp3
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
import scalafx.collections.ObservableBuffer.Add
import scalafx.collections.ObservableBuffer.Remove
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.input.MouseEvent
import scala.math._
import gui_elements.Notification._
import java.time._


object MainGUI extends JFXApp3:
    val calendar1 = new Calendar 
    var popupOpen = false  
    var primaryStage = new PrimaryStage
    def start(): Unit = 

        CreateEventPane.initializeWeek(calendar1.getCurrentWeek.getEvents.filter(x => scanTags(x)))
        CreateEventPane.initializeDay(calendar1.getCurrentDay.getEvents.filter(x => scanTags(x)))
        stage = new JFXApp3.PrimaryStage {
            title = "Calendar"
            val rootWidth = 1280
            val rootHeigth = 720
            scene = new Scene(rootWidth + 12, rootHeigth + 12) {
                
                val leftPane = new VBox {
                    prefWidth_=(rootWidth*0.25)
                    prefHeight_=(rootHeigth)

                    background = Background.fill(Color.DarkTurquoise)
                    children = List(paneOfMonthly, searchRootPane, tagsLabel, scrollCheckBoxes)
                    
                }
                val rightPane = new VBox {  
                    prefWidth_=(rootWidth * 0.75 + 12)
                    prefHeight_=(rootHeigth)
                    border = Border.stroke(Color.Black)
                    children = List(topPane, botPane)   // Children from TopPaneNextPrev, BotTabPane
                }
                val fullPane = new HBox {
                    background = Background.fill(Color.BlanchedAlmond)
                    children = List(leftPane, rightPane) // Children from above
                }
                val rootPane = new VBox {
                    children_=(List(menuBar, fullPane))
                }
                root = rootPane
                resizable_=(false)
            }
        }
        primaryStage = this.stage
        timeline.play()
            
    

end MainGUI