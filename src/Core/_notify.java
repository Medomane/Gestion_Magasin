package Core;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class _notify {
    public String title ;
    public String header ;
    public String messge ;
    public Alert.AlertType type ;
    public _notify(String title, String header, String message, Alert.AlertType type){
        this.title = title;
        this.header = header;
        this.messge = message;
        this.type = type;
    }
    public void alert(){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(messge);
        alert.setContentText(header);
        alert.showAndWait();
    }
    public boolean confirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(messge);
        alert.setContentText(header);
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() == ButtonType.OK;
    }

    public static void Show(String title, String header, String message, Alert.AlertType type){
        _notify not = new _notify(title,header,message,type);
        not.alert();
    }
    public static boolean Confirm(String title, String header, String message){
        _notify not = new _notify(title,header,message, Alert.AlertType.CONFIRMATION);
        return not.confirm();
    }
}
