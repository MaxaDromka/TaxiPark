package com.example.taxipark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapter(private val orders: List<HashMap<String, Any>>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val orderId: TextView = view.findViewById(R.id.orderIdTextView)
        //val driverId: TextView = view.findViewById(R.id.driverIdTextView)
        //val vehicleId: TextView = view.findViewById(R.id.vehicleIdTextView)
       // val pickupLocation: TextView = view.findViewById(R.id.pickupLocationTextView)
       // val dropoffLocation: TextView = view.findViewById(R.id.dropoffLocationTextView)
       // val status: TextView = view.findViewById(R.id.statusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
       // holder.orderId.text = order["OrderID"] as String
       // holder.driverId.text = order["DriverID"] as String
       // holder.vehicleId.text = order["VehicleID"] as String
       // holder.pickupLocation.text = order["PickupLocation"] as String
       // holder.dropoffLocation.text = order["DropoffLocation"] as String
       // holder.status.text = order["Status"] as String
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}