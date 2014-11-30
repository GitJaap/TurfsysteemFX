

package gui.controllers.screens;

import gui.controllers.screens.ControlledScreen;
import java.io.File;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import database.*;
import javafx.util.Pair;

/** Main controller class sitting as a top layer to manage the screens of the program.
 *  
 *  @author (large part taken from oracle)
 */
public class ScreensController  extends StackPane {
    //Holds the screens to be displayed and references to the data and controller class 
    private HashMap<String, Pair<Node, ControlledScreen>> screens = new HashMap<>();
    private boolean animationPerformed = true;
    
    // Reference to store the data from the database
    DataInitializer init;
    public ScreensController(DataInitializer initIn) {
        super();
        init = initIn;
    }
    
    //returns the initializer reference
    public DataInitializer getInit(){
        return init;
    }

    //Add the screen to the collection
    public void addScreen(String name, Node screen, ControlledScreen controller) {
        Pair<Node, ControlledScreen> pair = new Pair<>(screen,controller);
        screens.put(name, pair);
    }

    //Returns the Node with the appropriate name
    public Node getScreen(String name) {
        return screens.get(name).getKey();
    }
    
    //returns the controller with the appropriate name
    public ControlledScreen getController(String name){
        return screens.get(name).getValue();
    }

    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenController = ((ControlledScreen) myLoader.getController());
            //set the screencontroller and DataInitializer
            myScreenController.setScreenParent(this);
            myScreenController.setDataInitializer(init);
           
            // now add the controller adress to the node so it is usable later in the program
            loadScreen.setUserData(myScreenController);
            addScreen(name, loadScreen, myScreenController);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //This method tries to displayed the screen with a predefined name.
    //First it makes sure the screen has been already loaded.  Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    // If there isn't any screen being displayed, the new screen is just added to the root.
    public boolean setScreen(final String name) {      
        if (screens.get(name) != null) { 
            //screen loaded
            if(animationPerformed){
                final DoubleProperty opacity = opacityProperty();

                if (!getChildren().isEmpty()) {    //if there is more than one screen
                    Timeline fade = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                            new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            getChildren().remove(0);                    //remove the displayed screen
                            getController(name).delayedInitialize();             //load the components of the specified screen
                            getChildren().add(0, getScreen(name));     //add the screen
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(400), new KeyValue(opacity, 1.0)));
                            fadeIn.play();
                        }
                    }, new KeyValue(opacity, 0.0)));
                    fade.play();

                } else {
                    setOpacity(0.0);
                    getController(name).delayedInitialize();     //still load the components of the specified screen
                    getChildren().add(getScreen(name));       //no one else been displayed, then just show
                    Timeline fadeIn = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                            new KeyFrame(new Duration(1250), new KeyValue(opacity, 1.0)));
                    fadeIn.play();
                }
                return true;
            }
            else{ // no animation wanted
                getChildren().remove(0);                    //remove the displayed screen
                getController(name).delayedInitialize();             //load the components of the specified screen
                getChildren().add(0, getScreen(name));     //add the screen 
                return true;
            }
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }


        /*Node screenToRemove;
         if(screens.get(name) != null){   //screen loaded
         if(!getChildren().isEmpty()){    //if there is more than one screen
         getChildren().add(0, screens.get(name));     //add the screen
         screenToRemove = getChildren().get(1);
         getChildren().remove(1);                    //remove the displayed screen
         }else{
         getChildren().add(screens.get(name));       //no one else been displayed, then just show
         }
         return true;
         }else {
         System.out.println("screen hasn't been loaded!!! \n");
         return false;
         }*/
    }

    //This method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
    
    public void setAnimation(boolean bool){
        animationPerformed = bool;
    }
}
