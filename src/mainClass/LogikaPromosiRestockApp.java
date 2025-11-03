package mainClass;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LogikaPromosiRestockApp extends Application {

    // VARIABEL KONSTANTA SESUAI TUGAS
    private final double BATAS_KEUNTUNGAN_MIN = 500000.0;
    private final int TARGET_STOK_MIN = 10;
    
    // Deklarasi Global untuk akses mudah
    private TextField tfKeuntungan;
    private TextField tfStok;
    private TextField tfStokMin;
    private TextArea output;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Simulasi Bisnis: Logika Promosi & Restock Otomatis");

        // ==== 1. Judul & Subtitle ====
        Label title = new Label("💼 Analisis Bisnis Otomatis");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#065f46"));

        Label subtitle = new Label("Masukkan data atau pilih skenario di bawah untuk simulasi:");
        subtitle.setTextFill(Color.web("#374151"));

        // ==== 2. Panduan, Tombol Preset, dan Input Fields ====
        VBox topSection = createTopSection(); 
        
        // Button Hitung
        Button btnHitung = new Button("💡 Hitung Keputusan Bisnis");
        btnHitung.setMaxWidth(Double.MAX_VALUE);
        btnHitung.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-font-size: 14px; -fx-background-radius: 8;");

        // ==== 3. Bagian Output ====
        output = new TextArea();
        output.setEditable(false);
        output.setFont(Font.font("Consolas", 13));
        output.setPromptText("Hasil analisis akan muncul di sini...");
        output.setStyle("-fx-control-inner-background: #111827; -fx-text-fill: #e5e7eb; -fx-background-radius: 8;");
        output.setPrefHeight(250);

        // ==== 4. Layout Utama ====
        VBox root = new VBox(20, title, subtitle, topSection, btnHitung, output);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0fdf4;");

        // ==== 5. Logika Program (Menghubungkan Button) ====
        // Nama fungsi dipertahankan sebagai handleHitungAction agar sesuai standar JavaFX event handler
        btnHitung.setOnAction(e -> handleHitungAction());

        Scene scene = new Scene(root, 550, 700); 
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Membuat Panduan, Tombol Preset, dan Input Fields.
     */
    private VBox createTopSection() {
        // --- A. Panduan Skenario ---
        VBox guideBox = new VBox(5);
        guideBox.setPadding(new Insets(10));
        guideBox.setStyle("-fx-background-color: #e0f2f7; -fx-border-color: #a7d9ed; -fx-border-width: 1; -fx-background-radius: 8;");
        
        Label guideTitle = new Label("PANDUAN SKENARIO BISNIS:");
        guideTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        guideTitle.setTextFill(Color.web("#0e7490"));

        // Panduan mencerminkan BATAS_KEUNTUNGAN_MIN dan TARGET_STOK_MIN
        guideBox.getChildren().addAll(
            guideTitle,
            new Label("1. ✨ Optimal: Keuntungan > " + (int)BATAS_KEUNTUNGAN_MIN + " & Stok > " + TARGET_STOK_MIN),
            new Label("2. 📉 Promosi: Keuntungan < " + (int)BATAS_KEUNTUNGAN_MIN + " & Stok > " + TARGET_STOK_MIN),
            new Label("3. 📦 Restock: Keuntungan > " + (int)BATAS_KEUNTUNGAN_MIN + " & Stok < " + TARGET_STOK_MIN),
            new Label("4. 🚨 Kritis: Keuntungan < " + (int)BATAS_KEUNTUNGAN_MIN + " & Stok < " + TARGET_STOK_MIN)
        );
        
        // --- B. Tombol Preset ---
        HBox buttonBox = new HBox(10); 
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        
        Button btnOptimal = createPresetButton("✨ Optimal", 650000, 25, 10, "#4ade80");
        Button btnPromosi = createPresetButton("📉 Promosi", 300000, 25, 10, "#facc15");
        Button btnRestock = createPresetButton("📦 Restock", 650000, 5, 10, "#38bdf8");
        Button btnKritis = createPresetButton("🚨 Kritis", 300000, 5, 10, "#f43f5e");
        
        buttonBox.getChildren().addAll(btnOptimal, btnPromosi, btnRestock, btnKritis);
        
        // --- C. Input Fields ---
        tfKeuntungan = new TextField("650000");
        tfStok = new TextField("25");
        tfStokMin = new TextField("10");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        String labelInputStyle = "-fx-font-weight: bold; -fx-text-fill: #374151;";
        
        grid.add(new Label("Keuntungan Kotor (Rp):"), 0, 0);
        grid.add(tfKeuntungan, 1, 0);
        grid.add(new Label("Stok Saat Ini:"), 0, 1);
        grid.add(tfStok, 1, 1);
        grid.add(new Label("Stok Minimal (Target):"), 0, 2);
        grid.add(tfStokMin, 1, 2);

        grid.getChildren().stream()
            .filter(node -> node instanceof Label)
            .forEach(node -> node.setStyle(labelInputStyle));

        return new VBox(15, guideBox, buttonBox, grid); 
    }

    /**
     * Helper method untuk membuat tombol preset dan mengatur actionnya.
     */
    private Button createPresetButton(String text, double keuntungan, int stok, int stokMin, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 5 10;");
        
        btn.setOnAction(e -> {
            tfKeuntungan.setText(String.valueOf((int)keuntungan));
            tfStok.setText(String.valueOf(stok));
            tfStokMin.setText(String.valueOf(stokMin));
            output.setText(""); 
        });
        return btn;
    }

    /**
     * Logika utama untuk menghitung keputusan bisnis.
     */
    private void handleHitungAction() {
        try {
            double keuntunganKotor = Double.parseDouble(tfKeuntungan.getText());
            int stokSaatIni = Integer.parseInt(tfStok.getText());
            int stokMinimal = Integer.parseInt(tfStokMin.getText());
            
            // OPERATOR RELASI (>) SESUAI TUGAS (Point 2)
            boolean isTargetProfitTercapai = keuntunganKotor > BATAS_KEUNTUNGAN_MIN;
            
            // OPERATOR RELASI (<) DAN LOGIKA (||) SESUAI TUGAS (Point 3)
            // (stokSaatIni kurang dari stokMinimal) ATAU (isTargetProfitTercapai salah/false)
            boolean perluRestockCepat = (stokSaatIni < stokMinimal) || !isTargetProfitTercapai;

            StringBuilder sb = new StringBuilder();
            sb.append("=== HASIL ANALISIS ===\n");
            
            sb.append(String.format("Target Profit Min : Rp %,.0f%n", BATAS_KEUNTUNGAN_MIN));
            sb.append(String.format("Keuntungan Kotor  : Rp %,.0f%n", keuntunganKotor)); 
            sb.append(String.format("Stok Saat Ini     : %d unit%n", stokSaatIni));
            sb.append(String.format("Stok Minimal      : %d unit%n", stokMinimal));
            
            sb.append("---------------------------------\n");
            // OUTPUT Variabel Boolean (Point 4)
            sb.append("✅ isTargetProfitTercapai: " + (isTargetProfitTercapai ? "YA" : "TIDAK") + "\n");
            sb.append("✅ perluRestockCepat    : " + (perluRestockCepat ? "YA" : "TIDAK") + "\n\n");

            sb.append("💬 KEPUTUSAN BISNIS:\n");
            
            // Logika Implikasi Bisnis (Diambil dari kondisi turunan isTargetProfitTercapai dan perluRestockCepat)
            
            // Logika Asli (lebih ringkas dan akurat)
            boolean isStokAman = stokSaatIni >= stokMinimal;
            
            if (isTargetProfitTercapai && isStokAman) {
                // KONDISI 1: Keuntungan > Min DAN Stok >= Minimal
                sb.append("✨ KONDISI OPTIMAL. Bisnis stabil, keuntungan tercapai, dan stok mencukupi.\n");
            } else if (!isTargetProfitTercapai && isStokAman) {
                // KONDISI 2: Keuntungan <= Min DAN Stok >= Minimal
                sb.append("📉 PERLU PROMOSI. Keuntungan belum mencapai target. Stok aman, fokus pada strategi penjualan.\n");
            } else if (isTargetProfitTercapai && !isStokAman) {
                // KONDISI 3: Keuntungan > Min DAN Stok < Minimal
                sb.append("📦 PERLU RESTOCK. Penjualan bagus, tetapi stok menipis. Segera restock untuk menghindari kehabisan barang.\n");
            } else { // !isTargetProfitTercapai && !isStokAman
                // KONDISI 4: Kritis
                sb.append("🚨 KONDISI KRITIS! Keuntungan rendah dan stok menipis. Lakukan restock cepat dan strategi promosi agresif.\n");
            }

            output.setText(sb.toString());

        } catch (NumberFormatException ex) {
            output.setText("❌ Error: Pastikan semua input (Keuntungan, Stok, Minimal) berupa angka yang valid.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}