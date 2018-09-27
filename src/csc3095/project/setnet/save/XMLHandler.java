package csc3095.project.setnet.save;

import csc3095.project.setnet.main.Setnet;

import javafx.scene.control.Alert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

//http://blog.bdoughan.com/2010/10/jaxb-and-shared-references-xmlid-and.html
public class XMLHandler
{

    public static Setnet loadSetnetDataFromFile(File file)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(Setnet.class);
            Unmarshaller um = context.createUnmarshaller();

            return (Setnet) um.unmarshal(file);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Open error");
            alert.setHeaderText("Unable to open selected file");
            alert.setContentText("There was a problem when trying to open the file: " + file.getPath()  + ".\n\nPlease try again or try opening another file.");

            alert.showAndWait();
            return null;
        }
    }

    public static void saveSetnetDataToFile(File file, Setnet setnet)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(Setnet.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            m.marshal(setnet, file);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save error");
            alert.setHeaderText("Unable to save file at provided location");
            alert.setContentText("There was a problem when trying to save the file at: " + file.getPath() + ".\n\nPlease try again or saving at another location.");

            alert.showAndWait();
        }
    }
}
