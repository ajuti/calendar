package calendar_classes.service

import com.github.tototoshi.csv._
import java.io.File
import calendar_classes._
import scala.collection.mutable.Buffer
import java.{util => ju}
import java.io.FileNotFoundException
import java.io.IOException

class FileReader(filePath: String):


    val events = Buffer[Event]()

    def addAllEvents() = 
        try 
            val reader = CSVReader.`open`(new File(filePath))
            
            val parser = Parser()

            val it = reader.iterator
            
            try          
                var oneLine = it.next().mkString(",")
                while oneLine != null do
                    events.addOne(parser.toEventFromString(oneLine))
                    oneLine = it.next().mkString(",")    
                end while
            catch
                case e: ju.NoSuchElementException => println("Reading finished with exception")
                
            reader.close()
        catch
            case e: FileNotFoundException   => println("Couldn't find file")
            case e: IOException             => println("IOException when trying to read the file")
        
        events

    end addAllEvents

end FileReader

