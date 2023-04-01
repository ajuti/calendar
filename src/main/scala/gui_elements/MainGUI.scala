package gui_elements

//import gui_elements._

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




object MainGUI extends JFXApp3:
    val calendar1 = new Calendar
    val weekEventPanes = Buffer[Pane]()
    def start(): Unit = 
        
        weekEventPanes.appendAll(CreateEventPane.initialize(calendar1.getCurrentWeek.getEvents))
        stage = new JFXApp3.PrimaryStage {
            title = "Calendar"
            val rootWidth = 1280
            val rootHeigth = 720
            scene = new Scene(rootWidth, rootHeigth + 20) {
                val leftPane = new FlowPane {
                    prefWidth_=(rootWidth*0.25)
                    prefHeight_=(rootHeigth)

                    val showPop = new Button("Show Popup") {
                        onAction = () =>
                            //if addEventPopup.isShowing() then
                            addEventPopup.show()
                    }

                    background = Background.fill(Color.Crimson)
                    //border_=(Border.stroke(Color.DarkOrange))
                    children = List(showPop)         
                }
                val rightPane = new VBox {  
                    prefWidth_=(rootWidth * 0.75)
                    prefHeight_=(rootHeigth)
                    //maxWidth_=(rootWidth * 0.75 - 5)
                    //border_=(Border.stroke(Color.DarkKhaki))
                    children = List(topPane, botPane)   // Children from TopPaneNextPrev, BotTabPane
                }
                val fullPane = new HBox {
                    //border_=(Border.stroke(Color.Black))
                    background = Background.fill(Color.BlanchedAlmond)
                    children = List(leftPane, rightPane) // Children from above
                }
                val rootPane = new VBox {
                    children_=(List(menuBar, fullPane))
                }
                root = rootPane
                //resizable_=(false)
            }
        }
            
    

end MainGUI