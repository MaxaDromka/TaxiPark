package com.example.taxipark

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookingsAdapter(private val context: Context, private val bookings: List<HashMap<String, Any>>) : RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookingIdTextView: TextView = view.findViewById(R.id.bookingIdTextView)
        val orderIdTextView: TextView = view.findViewById(R.id.orderIdTextView)
        val pickupLocationTextView: TextView = view.findViewById(R.id.pickupLocationTextView)
        val dropoffLocationTextView: TextView = view.findViewById(R.id.dropoffLocationTextView)
        val statusTextView: TextView = view.findViewById(R.id.statusTextView)
        val bookingDateTextView: TextView = view.findViewById(R.id.bookingDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapteritembookings, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]

        holder.bookingIdTextView.text = "Booking ID: ${booking["BookingID"]}"
        holder.orderIdTextView.text = "Order ID: ${booking["OrderID"]}"
        holder.pickupLocationTextView.text = "Pickup: ${booking["PickupLocation"]}"
        holder.dropoffLocationTextView.text = "Dropoff: ${booking["DropoffLocation"]}"
        holder.statusTextView.text = "Status: ${booking["Status"]}"
        holder.bookingDateTextView.text = "Date: ${booking["BookingDate"]}"
    }

    override fun getItemCount(): Int {
        return bookings.size
    }
}
