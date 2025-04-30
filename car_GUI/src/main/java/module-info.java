module agh.it.car_project_gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;


    opens agh.it.car_project_gui to javafx.fxml;
    exports agh.it.car_project_gui;
    exports car_simulation to junit;
}