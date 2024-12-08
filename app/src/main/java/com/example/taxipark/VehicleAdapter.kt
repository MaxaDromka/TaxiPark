package com.example.taxipark

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class VehicleAdapter(context: Context, private val vehicles: List<HashMap<String, Any>>) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = vehicles.size

    override fun getItem(position: Int): Any = vehicles[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.adapter_item_autos, parent, false)

        val modelTextView = view.findViewById<TextView>(R.id.textViewAuto2)
        val licensePlateTextView = view.findViewById<TextView>(R.id.textViewAuto3)
        val statusPlateTextView = view.findViewById<TextView>(R.id.textViewAuto4)

        val vehicle = vehicles[position]
        modelTextView.text = "Модель: ${vehicle["Model"] as String}" // Uncomment and use this line
        licensePlateTextView.text = "Номерной знак: ${vehicle["LicensePlate"] as String}"
        statusPlateTextView.text = "Статус: ${vehicle["StatusAuto"] as String}" // Update this line accordingly


        return view

    }
}