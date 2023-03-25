module nakoso {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires pdfbox;
	requires javafx.web;
	
	opens application to javafx.graphics, javafx.fxml;
}
