module nakoso {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires pdfbox;
	
	opens application to javafx.graphics, javafx.fxml;
}
