package gui_elements

import scalafx.application.JFXApp3
import scalafx.scene._
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color._
import scalafx.Includes._
import scalafx.scene.paint._
import scalafx.scene.text.Text
import scalafx.scene.layout.GridPane
import scalafx.geometry.Insets
import scalafx.scene.effect.DropShadow
import scalafx.scene.control._
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.layout.Region
import scalafx.scene.layout.Border
import scalafx.scene.layout.FlowPane
import scalafx.scene.layout.BorderPane
import scalafx.geometry.Orientation
import scalafx.scene.layout.Background


object MainGUI extends JFXApp3:
    override def start(): Unit = 
        stage = new JFXApp3.PrimaryStage {
            title = "Calendar"
            val rootWidth = 1280
            val rootHeigth = 720
            scene = new Scene(rootWidth, rootHeigth) {
                val leftPane = new FlowPane {
                    maxWidth_=(rootWidth*0.25)
                    prefHeight_=(rootHeigth)

                    background = Background.fill(Color.Crimson)
                }
                val rightPane = new FlowPane(Orientation.Vertical, 0, 3) {
                    val label = new Label("huh")
                    prefWidth_=(rootWidth * 0.74)
                    val button = new Button("click")

                    border_=(Border.stroke(Color.Cyan))
                    children = List(label, button)
                }

                val fullPane = new FlowPane(5, 0)
                fullPane.border_=(Border.stroke(Color.Black))
                fullPane.children = List(leftPane, rightPane)
                
                root = fullPane
                resizable_=(false)
            }
            
        }
    

end MainGUI