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
import javax.swing.GroupLayout.Alignment
import scalafx.geometry.Pos
import scalafx.scene.text.Font
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import scalafx.scene.control.TabPane.TabClosingPolicy




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
                    children = List(new Button)
                }
                val rightPane = new FlowPane(Orientation.Vertical, 0, 3) {
                    val label1 = new Label {
                        text = "JAN 2023 | WEEK  1"
                        font = new Font(50)
                    }
                    val prevButton = new Button() {
                        graphic_=(new ImageView(new Image("file:leftarrow.png")))
                    }
                    val nextButton = new Button {
                        graphic_=(new ImageView(new Image("file:rightarrow.png")))
                    }
                    val topPane = new FlowPane(Orientation.Horizontal, 10, 0) {
                        alignment_=(Pos.Center)
                        prefWidth_=(rootWidth * 0.75 - 13)
                        prefHeight_=(rootHeigth*0.10)
                        children = List(prevButton, label1, nextButton)
                        background = Background.fill(Color.DarkTurquoise)
                    } 
                    val weeklyTab = new Tab {
                        text = "Weekly"
                    }
                    val dailyTab = new Tab{
                        text = "Daily"
                    }
                    val botPane = new TabPane {
                        tabs = List(weeklyTab, dailyTab)
                        tabClosingPolicy_=(TabClosingPolicy.Unavailable)
                        maxHeight_=(500)
                        background = Background.fill(Color.BlanchedAlmond)
                    }    
                    prefWidth_=(rootWidth * 0.75 - 7)
                    border_=(Border.stroke(Color.Cyan))
                    children = List(topPane, botPane)
                }


                val fullPane = new FlowPane(5, 0) {
                    border_=(Border.stroke(Color.Black))
                    background = Background.fill(Color.BlanchedAlmond)
                    children = List(leftPane, rightPane)
                }
                root = fullPane
                
                //resizable_=(false)
            }
            
        }
    

end MainGUI