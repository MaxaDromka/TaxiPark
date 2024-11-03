package com.example.taxipark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrdersActivity : AppCompatActivity() {

    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView)

        // Пример списка заказов (замените это на реальные данные из базы данных)
        val ordersList = listOf(
            Order(1, 1, 1, "Ул. Ленина, д.1", "Ул. Пушкина, д.2", "Выполнен"),
            Order(2, 2, 2, "Ул. Чехова, д.3", "Ул. Гоголя, д.4", "Отменен"),
            // Добавьте остальные заказы...
            Order(3, 3, 3, "Ул. Толстого, д.5", "Ул. Достоевского, д.6", "Выполнен")
            // и т.д.
        )

        ordersAdapter = OrdersAdapter(ordersList)
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersRecyclerView.adapter = ordersAdapter
    }
}