package com.example.taxipark;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taxipark.DatabaseHelper.DatabaseHelper;

import java.util.List;

public class DriverActivity extends AppCompatActivity {

    private LinearLayout driversLayout; // LinearLayout для отображения водителей
    private Button addDriverButton; // Кнопка для добавления нового водителя
    private DatabaseHelper databaseHelper; // База данных

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers); // Убедитесь, что это правильный макет

        // Инициализация базы данных
        databaseHelper = new DatabaseHelper(this,null);

        // Инициализация элементов интерфейса
        driversLayout = findViewById(R.id.driversLayout);
        addDriverButton = findViewById(R.id.addDriverButton);

        // Отображение водителей
        displayDrivers();

        // Установка обработчика нажатия для кнопки добавления водителя
        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Логика для добавления нового водителя (например, открытие нового экрана)
            }
        });
    }

    private void displayDrivers() {
        driversLayout.removeAllViews(); // Очистка предыдущих записей

        // Получение списка водителей из базы данных
        List<Driver> drivers = databaseHelper.getAllDrivers();

        if (drivers.isEmpty()) {
            TextView noDriversTextView = new TextView(this);
            noDriversTextView.setText("Нет доступных водителей.");
            driversLayout.addView(noDriversTextView);
        } else {
            for (Driver driver : drivers) {
                // Создание текстового представления для каждого водителя
                TextView textView = new TextView(this);
                textView.setText("Имя: " + driver.getName() +
                        ", Лицензия: " + driver.getLicenseNumber() +
                        ", Телефон: " + driver.getPhoneNumber() +
                        ", Рейтинг: " + driver.getRating());
                driversLayout.addView(textView);
            }
        }
    }
}