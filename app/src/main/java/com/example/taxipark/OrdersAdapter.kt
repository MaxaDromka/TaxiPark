package com.example.taxipark

import Order
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapter(private val ordersList: List<Order>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderIdTextView: TextView = view.findViewById(R.id.orderIdTextView)
        val pickupLocationTextView: TextView = view.findViewById(R.id.pickupLocationTextView)
        val dropoffLocationTextView: TextView = view.findViewById(R.id.dropoffLocationTextView)
        val driverNameTextView: TextView = view.findViewById(R.id.driverNameTextView)
        val orderStatusTextView: TextView = view.findViewById(R.id.orderStatusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]

        holder.orderIdTextView.text = "Заказ ID: ${order.orderID}"
        holder.pickupLocationTextView.text = "Место подачи: ${order.pickupLocation}"
        holder.dropoffLocationTextView.text = "Место назначения: ${order.dropoffLocation}"

        // Предполагается, что у вас есть метод для получения имени водителя по его ID
        holder.driverNameTextView.text = "Водитель: ${getDriverName(order.driverID)}"

        holder.orderStatusTextView.text = "Статус: ${order.status}"
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    private fun getDriverName(driverID: Int): String {
        // Реализуйте логику получения имени водителя по его ID
        // Это может быть обращение к базе данных или к локальному списку водителей
        return "Имя Водителя" // Заглушка
    }
}