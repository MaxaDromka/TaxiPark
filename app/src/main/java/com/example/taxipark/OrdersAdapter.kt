package com.example.taxipark

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class OrdersAdapter(private val context: Context, private val orders: List<HashMap<String, Any>>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderIdTextView: TextView = view.findViewById(R.id.orderIdTextView)
        val driverNameTextView: TextView = view.findViewById(R.id.driverNameTextView) // Updated ID for Driver Name
        val pickupLocationTextView: TextView = view.findViewById(R.id.pickupLocationTextView)
        val dropoffLocationTextView: TextView = view.findViewById(R.id.dropoffLocationTextView)
        val statusTextView: TextView = view.findViewById(R.id.statusTextView)
        //val autoTextView: TextView = view.findViewById(R.id.numberauto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapteritemorders, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        //holder.orderIdTextView.text = "Идентификатор заказа: ${order["OrderID"]}"
        holder.driverNameTextView.text = "Водитель: ${order["DriverName"]}"
        holder.pickupLocationTextView.text = "Место забора: ${order["PickupLocation"]}"
        holder.dropoffLocationTextView.text = "Место высадки: ${order["DropoffLocation"]}"
        holder.statusTextView.text = "Статус заказа: ${order["Status"]}"
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}