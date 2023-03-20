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




object MainGUI extends JFXApp3:
    override def start(): Unit = 
        stage = new JFXApp3.PrimaryStage {
            title = "Calendar"
            val rootWidth = 1280
            val rootHeigth = 720
            scene = new Scene(rootWidth, rootHeigth) {
                val leftPane = new FlowPane {
                    prefWidth_=(rootWidth*0.25)
                    prefHeight_=(rootHeigth)

                    background = Background.fill(Color.Crimson)
                    border_=(Border.stroke(Color.DarkOrange))
                    children = List(new Button)         // Children from 
                }
                val rightPane = new FlowPane(Orientation.Vertical, 0, 3) {  
                    prefWidth_=(rootWidth * 0.75 - 5)
                    border_=(Border.stroke(Color.Cyan))
                    children = List(topPane, botPane)   // Children from TopPaneNextPrev, BotTabPane
                }
                val fullPane = new FlowPane(0, 0) {
                    border_=(Border.stroke(Color.Black))
                    background = Background.fill(Color.BlanchedAlmond)
                    children = List(leftPane, rightPane) // Children from above
                }
                root = fullPane
                
                //resizable_=(false)
            }
        }
            
    

end MainGUI