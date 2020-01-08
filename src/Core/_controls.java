package Core;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

public class _controls {
    public static class NumberTextField extends TextField {
        private final NumberFormat nf;
        private ObjectProperty<BigDecimal> number = new SimpleObjectProperty<>();
        public final BigDecimal getNumber() {
            return number.get();
        }
        public final void setNumber(BigDecimal value) {
            number.set(value);
        }
        public ObjectProperty<BigDecimal> numberProperty() {
            return number;
        }
        public NumberTextField() {
            this(BigDecimal.ZERO);
        }
        public NumberTextField(BigDecimal value) {
            this(value, NumberFormat.getInstance());
            initHandlers();
        }

        public NumberTextField(BigDecimal value, NumberFormat nf) {
            super();
            this.nf = nf;
            initHandlers();
            setNumber(value);
        }

        private void initHandlers() {
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    parseAndFormatInput();
                }
            });
            focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue.booleanValue()) {
                        parseAndFormatInput();
                    }
                }
            });
            // Set text in field if BigDecimal property is changed from outside.
            numberProperty().addListener(new ChangeListener<BigDecimal>() {
                @Override
                public void changed(ObservableValue<? extends BigDecimal> obserable, BigDecimal oldValue, BigDecimal newValue) {
                    setText(nf.format(newValue));
                }
            });
        }

        /**
         * Tries to parse the user input to a number according to the provided
         * NumberFormat
         */
        private void parseAndFormatInput() {
            try {
                String input = getText();
                if (input == null || input.length() == 0) {
                    return;
                }
                Number parsedNumber = nf.parse(input);
                BigDecimal newValue = new BigDecimal(parsedNumber.toString());
                setNumber(newValue);
                selectAll();
            } catch (ParseException ex) {
                // If parsing fails keep old number
                setText(nf.format(number.get()));
            }
        }
    }
    public static VBox CenterBox(ImageView mainImage, GridPane grid){
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        mainImage.setStyle("-fx-cursor:hand;");
        grid.setAlignment(Pos.TOP_CENTER);
        //grid.setMinSize(200, 200);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);
        grid.setVgap(5);
        centerBox.getChildren().add(mainImage);
        centerBox.getChildren().add(grid);
        centerBox.getStyleClass().add("centerBox");
        return centerBox;
    }
    public static VBox RightBox(Node[] nodes, int width){
        VBox rightVBox = new VBox();
        rightVBox.setMaxWidth(width);
        rightVBox.setMinWidth(width);
        rightVBox.setFillWidth(true);
        for(Node node:nodes)rightVBox.getChildren().add(node);
        rightVBox.getStyleClass().add("rightVBox");
        ((TableView)nodes[1]).prefHeightProperty().bind(rightVBox.heightProperty());
        ((TableView)nodes[1]).prefWidthProperty().bind(rightVBox.widthProperty());
        return rightVBox;
    }
    public static HBox BottomBox(String text){
        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        Label lblText = new Label(text);
        lblText.setTextFill(Paint.valueOf("white"));
        bottomHBox.getChildren().add(lblText);
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        bottomHBox.getStyleClass().add("BottomBox");
        return bottomHBox;
    }
    public static VBox LeftBox(Button[] buttons){
        VBox leftVBox = new VBox();
        for(Button btn : buttons) btn.getStyleClass().add("left-btn");
        buttons[0].setText("Ajouter");
        buttons[0].setGraphic(_func.getImage("add",20,20));
        buttons[1].setText("Modifier");
        buttons[1].setGraphic(_func.getImage("edit",20,20));
        buttons[2].setText("Supprimer");
        buttons[2].setGraphic(_func.getImage("delete",20,20));
        buttons[3].setText("Actualiser");
        buttons[3].setGraphic(_func.getImage("refresh",20,20));
        leftVBox.getChildren().addAll(buttons);
        leftVBox.getStyleClass().add("LeftBox");
        return leftVBox;
    }
    public static BorderPane FillBorderPane(VBox right,HBox bottom,VBox left, VBox center,HBox top){
        BorderPane bp = new BorderPane();
        if(bottom != null) bp.setBottom(bottom);
        if(top != null) bp.setTop(top);
        if(right != null) bp.setRight(right);
        if(left != null) bp.setLeft(left);
        if(center != null) bp.setCenter(center);
        return bp ;
    }
    public static NumberTextField NumberField(String id){
        NumberTextField nf = new NumberTextField();
        nf.setId(id);
        return nf;
    }
}
