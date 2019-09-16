import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.event.EventHandler;



class Airplane{
    String name;
    double x,y;
    double dx,dy;
    int capacity;
    double velosity;
    int timeDiv [] = new int [2];
    boolean stop = true;
    boolean raft = true;
    boolean cargo;
    Airport origin,destination;

    Airplane(String _name,double _velosity,int _t,int _tt,Airport _origin,Airport _destination,boolean _cargo,int _capacity) {
        this.name = _name;
        this.capacity = _capacity;
        this.velosity = _velosity;
        this.timeDiv[0] = _t;
        this.timeDiv[1] = _tt;
        this.origin = _origin;
        this.destination = _destination;
        this.dx = (destination.x - origin.x) * velosity / 6000;
        this.dy = (destination.y - origin.y) * velosity / 6000;
        this.x = origin.x;
        this.y = origin.y;
        this.cargo = _cargo;
    }
    String info(){
        String r = name + "  " + x +"  "+ y + "  " ;
        if(raft == true)
            r +=  origin.name + "  to  " + destination.name;
        else r += destination.name + "  to  " + origin.name;
        if(cargo == true)r += " is Cargo with "+ capacity + "Kg";
        else r += " is Passenger with " + capacity;
        return r;
    }
}


class Airport{
    String name;
    double x,y;
    Airport(String _name,double _x,double _y){
        this.name = _name;
        this.x = _x;
        this.y = _y;
    }

}


public class Project1 extends Application
{
    public static Airplane [] airplane = new Airplane [5];
    public static Airport [] airport = new Airport[4];
    public static void main(String[] args)
    {

        airport[0] = new Airport("Mehr-Abad",915,372);
        airport[1] = new Airport("Ankara",196.8,180);
        airport[2] = new Airport("Ahvaz",812,592.8);
        airport[3] = new Airport("Mashhad",1240.8,363.2);
        airplane[0] = new Airplane("Boeing-747",15,0,50,airport[0],airport[1],false,1500);
        airplane[1] = new Airplane("Antonov-A20",12.5,3,60,airport[3],airport[0],false,500);
        airplane[2] = new Airplane("Boeing-737",10,10,70,airport[2],airport[0],false,1000);
        airplane[3] = new Airplane("Antonov An-225",9,20,50,airport[1],airport[0],true,285000);
        airplane[4] = new Airplane("Xi'an Y-20",10,15,75,airport[0],airport[2],true,185000);
        launch(args);
    }


    public double yTouched ,xTouched;


    @Override
    public void start(Stage theStage)
    {

        theStage.setTitle( "Timeline Example" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( 1560, 800);
        root.getChildren().add( canvas );
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image aplanHome = new Image( "aplanHome.png" );
        Image aplanAway= new Image( "aplanAway.png" );
        Image aplanCargoHome [] = new Image[2];
        Image aplanCargoAway [] = new Image[2];
        aplanCargoHome[0] = new Image( "aplan_bar_right.png" );
        aplanCargoHome[1] = new Image( "aplan_bar_down.png" );
        aplanCargoAway[0] = new Image( "aplan_bar_left.png" );
        aplanCargoAway[1] = new Image( "aplan_bar_up.png" );
        Image space = new Image( "space.jpg" );
        final long startNanoTime = System.nanoTime();

        theScene.setOnMouseClicked(

                new EventHandler<MouseEvent>()
                {
                    public void handle(MouseEvent e) {
                        xTouched = e.getX();
                        yTouched = e.getY();
                    }
                });


        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 16);
        gc.setFont( theFont );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime) {
                gc.setFill(new Color(0.85, 0.85, 1.0, 1.0));
                gc.setFill(Color.BLUE);
                gc.drawImage(space, 0, 50);
                int stop = 0;
                long t = (currentNanoTime - startNanoTime) / 1000000000;
                for(int i = 0 ; i < 5 ; i++){
                    if (t % 100 == airplane[i].timeDiv[0]) {
                        airplane[i].x = airplane[i].origin.x;
                        airplane[i].y = airplane[i].origin.y;
                        airplane[i].stop = false;
                        airplane[i].raft = true;
                    }
                    if (t % 100 == airplane[i].timeDiv[1]) {
                        airplane[i].x = airplane[i].destination.x;
                        airplane[i].y = airplane[i].destination.y;
                        airplane[i].stop = false;
                        airplane[i].raft = false;
                    }
                    if (airplane[i].stop == false) {
                        if (airplane[i].raft == true) {
                            airplane[i].x += airplane[i].dx;
                            airplane[i].y += airplane[i].dy;
                            if (airplane[i].destination.x - 5 <= airplane[i].x && airplane[i].x <= airplane[i].destination.x + 5) {
                                airplane[i].stop = true;
                            }
                        } else {
                            airplane[i].x -= airplane[i].dx;
                            airplane[i].y -= airplane[i].dy;
                            if (airplane[i].origin.x - 5 <= airplane[i].x && airplane[i].x <= airplane[i].origin.x + 5) {
                                airplane[i].stop = true;
                            }
                        }
                        if (i < 3) {
                            if (airplane[i].raft == true)
                                gc.drawImage(aplanHome, airplane[i].x, airplane[i].y);
                            if (airplane[i].raft == false)
                                gc.drawImage(aplanAway, airplane[i].x, airplane[i].y);
                        } else {
                            if (airplane[i].raft == true)
                                gc.drawImage(aplanCargoHome[i - 3], airplane[i].x, airplane[i].y);
                            if (airplane[i].raft == false)
                                gc.drawImage(aplanCargoAway[i - 3], airplane[i].x, airplane[i].y);
                        }
                        if ( (xTouched >= airplane[i].x - 20 && xTouched <= airplane[i].x + 20)
                            && (yTouched >= airplane[i].y - 20 && yTouched <= airplane[i].y + 20)) {
                            gc.setFill(new Color(0.85, 0.85, 1.0, 1.0));
                            gc.fillRect(0, 0, 1560, 800);
                            gc.setFill(Color.BLUE);
                            String infoText = airplane[i].info();
                            gc.fillText(infoText, 20, 20);
                            gc.strokeText(infoText, 20, 20);
                            xTouched = 0;
                            yTouched = 0;
                        }
                    }

                }
                for(int i = 0; i < 5 ; i++){
                    if(airplane[i].stop == true){
                        stop++;
                    }
                }

                String text =  "Stoped Airplane: " + stop + " Working Airplane: " + (5 - stop);
                gc.setFill(new Color(0.85, 0.85, 1.0, 1.0));
                gc.fillRect(1000, 0, 500, 50);
                gc.setFill(Color.BLUE);
                gc.fillText(text, 1000, 20);
                gc.strokeText(text, 1000, 20);
            }
        }.start();
        theStage.show();
    }
}