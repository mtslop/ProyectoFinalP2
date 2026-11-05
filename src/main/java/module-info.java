module co.uniquindio.edu.poo.ProyectoFinalP2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.uniquindio.edu.poo to javafx.graphics;
    opens co.uniquindio.edu.poo.controller to javafx.fxml;
    opens co.uniquindio.edu.poo.Model to javafx.base;

    exports co.uniquindio.edu.poo;
    exports co.uniquindio.edu.poo.controller;
}