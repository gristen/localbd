package com.vojtechruzicka.javafxweaverexample;



import com.vojtechruzicka.javafxweaverexample.entity.BookEntity;
import com.vojtechruzicka.javafxweaverexample.service.BookServise;
import com.vojtechruzicka.javafxweaverexample.service.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static javafx.scene.control.ButtonType.CANCEL;

@Component
@FxmlView("main-stage.fxml")
public class MyController {

    private UserService userService;
    private BookServise bookServise;

    private ObservableList<BookEntity> bookArr = FXCollections.observableArrayList();
    @FXML
    private TextField TF_author;

    @FXML
    private TextField TF_kind;

    @FXML
    private TextField TF_pub;

    @FXML
    private TextField TF_title;

    @FXML
    private TextField TF_year;

    @FXML
    private Button bSave;

    @FXML
    private TableView<BookEntity> table;

    @FXML
    private TableColumn<BookEntity,String> tableColumn_author;

    @FXML
    private TableColumn<BookEntity,String> tableColumn_kind;

    @FXML
    private TableColumn<BookEntity,String> tableColumn_pub;

    @FXML
    private TableColumn<BookEntity,String> tableColumn_title;

    @FXML
    private TableColumn<BookEntity,String> tableColumn_year;
    @FXML
    private AnchorPane aPane;

    @FXML
    void bDelete(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            bookServise.deleteBook(table.getItems().get(selectedIndex).getId());
            table.getItems().remove(selectedIndex);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Никто не выбран");
            alert.setContentText("Пожалуйста выберите человека в таблице");
            alert.showAndWait();
        }

    }

    @FXML
    void onSave(ActionEvent event) {
        if (!(tableColumn_title.equals("")) && !tableColumn_author.equals("")){
            BookEntity book = new BookEntity();
            book.setBookname(tableColumn_title.getText());
            book.setAuthor(tableColumn_author.getText());
            book.setPublisher(tableColumn_pub.getText());
            book.setYear(tableColumn_year.getText());
            book.setKind(tableColumn_kind.getText());

            bookArr.add(bookServise.saveBook(book));
            tableColumn_title.setText("");
            tableColumn_author.setText("");
            tableColumn_pub.setText("");
            tableColumn_year.setText("");
            tableColumn_kind.setText("");
        }

    }


   //конструктор, используется для определения сервисного слоя
    public MyController(UserService userService, BookServise bookServise) {
        this.userService = userService;
        this.bookServise = bookServise;
    }

   //настройка программы в зависимости от роли
   private void isRol(int rol){
    if (rol == 0){
        aPane.setVisible(true);
    }
   }
    // создание диалогового окна логин/пароль
    private void loginDialog(){

        // Создание диалогового окна.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Окно логина");
        dialog.setHeaderText("Введите логин пароль для входа в программу");

        // Создание кнопок(OK, Cancel).
        ButtonType loginButtonType = new ButtonType("Вход", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, CANCEL);

        // Разметка окна через GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        //Поля ввода
        TextField username = new TextField();
        username.setPromptText("Логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Пароль");
        // надписи
        grid.add(new Label("Логин"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль"), 0, 1);
        grid.add(password, 1, 1);

        // Включить/выключить кнопку входа в зависимости от того, было ли введено имя пользователя.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Фокус на поле имени пользователя по умолчанию.
        Platform.runLater(() -> username.requestFocus());

        // Выполните некоторую проверку (используя лямбда-синтаксис Java 8).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Преобразование результата в пару "имя пользователя-пароль" при нажатии кнопки входа.
        // И смотрим на правильность ввода логина пароля
        // если правильно то узнаем роль


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()){
            if(userService.logIn(username.getText(), password.getText())){
                System.out.println("Username=" + username.getText() + ", Password=" + password.getText());
                isRol(userService.getRol(username.getText(), password.getText()));
            }


        }

/*
        result.ifPresent(usernamePassword -> {
            if(userService.logIn(usernamePassword.getValue(),usernamePassword.getKey()))
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });*/
    }
    @FXML
    private void initialize() {
        loginDialog(); // вызов диалогового окна логин/пароль

        bookServise.getAll().forEach(bookArr::add);

        tableColumn_author.setCellValueFactory(new PropertyValueFactory<BookEntity,String>("author"));
        tableColumn_title.setCellValueFactory(new PropertyValueFactory<BookEntity,String>("title"));
        tableColumn_pub.setCellValueFactory(new PropertyValueFactory<BookEntity,String>("pub"));
        tableColumn_year.setCellValueFactory(new PropertyValueFactory<BookEntity,String>("year"));
        tableColumn_kind.setCellValueFactory(new PropertyValueFactory<BookEntity,String>("kind"));
        table.setItems(bookArr);

        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue ) -> showBookEntityDetails(newValue));
    }

    private void showBookEntityDetails(BookEntity bookEntity) {
        if (bookEntity != null){
            tableColumn_title.setText(bookEntity.getBookname());
            tableColumn_author.setText(bookEntity.getAuthor());
            tableColumn_pub.setText(bookEntity.getPublisher());
            tableColumn_year.setText(bookEntity.getYear());
            tableColumn_kind.setText(bookEntity.getKind());
        } else {
            tableColumn_title.setText("");
            tableColumn_author.setText("");
            tableColumn_pub.setText("");
            tableColumn_year.setText("");
            tableColumn_kind.setText("");
        }
    }

}
