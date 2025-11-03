module AplikasiLogika {
    requires javafx.controls;
    requires javafx.graphics;
    
    // BARIS SOLUSI: 
    // Buka package 'mainClass' ke modul 'javafx.graphics' 
    // agar launcher JavaFX bisa mengakses class di dalamnya.
    opens mainClass to javafx.graphics; 
}