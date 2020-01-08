package Core;

import Controller._client;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class _func {
    public static void Print(Object str){
        System.out.println(str);
    }
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    public static ImageView getImage(String name, double w, double h) {
        if(!name.contains(".")) name = "src/tmp/Images/"+name+".png";
        else name = "src/tmp/Images/"+name;
        File df = new File(Path.of(URLDecoder.decode(Path.of(name).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
        String path = "tmp/Images/logo.png";
        if(df.exists()) path = df.toURI().toString();
        //System.out.println(path);
        ImageView icon = new ImageView(new Image(path));
        icon.setFitHeight(h);
        icon.setFitWidth(w);
        return icon ;
    }
    public static boolean isNull(String str){
        if(str == null) return true ;
        if(str.trim().equals("")) return true ;
        return str.length() == 0;
    }
    public static boolean isValid(String type, String value){
        if (isNull(value)) return false;
        String regex = "";
        if(type.equals("phone")) regex ="^(0|\\+212)[56]( ?[0-9]{2}){4}$";
        else if(type.equals("email")) regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if(!regex.equals("")){
            Pattern pat = Pattern.compile(regex);
            return pat.matcher(value).matches();
        }
        return true ;
    }
    public static RotateTransition Rotate(Node node){
        RotateTransition rotate = new RotateTransition();
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setByAngle(360);
        rotate.setCycleCount(Transition.INDEFINITE);
        rotate.setDuration(Duration.millis(400));
        rotate.setNode(node);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.play();
        return rotate;
    }
    public static void Fade(Node node,double duration,double from,double to,int nbr){
        FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
        ft.setFromValue(from);
        ft.setToValue(to);
        ft.setCycleCount(nbr);
        ft.setAutoReverse(true);
        ft.play();
    }
    public static void Translate(Node node,double from,double duration){
        TranslateTransition st = new TranslateTransition(Duration.millis(duration),node);
        st.setAutoReverse(false);
        st.setCycleCount(1);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.setFromX(from);
        st.setToX(0);
        st.play();
    }
}
