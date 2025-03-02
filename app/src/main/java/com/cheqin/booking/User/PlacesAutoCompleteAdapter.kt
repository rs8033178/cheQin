package com.cheqin.booking.User

import Predictions
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cheqin.booking.R
import com.cheqin.booking.gcm.SharedPreference

class PlacesAutoCompleteAdapter(
    private val context: Context,
    private val placesRepo: PlacesRemoteRepo,
    private val sharedPreference: SharedPreference,
    private val isAreaSearch: Boolean = false
) : BaseAdapter(), Filterable {

    private var predictionList: MutableList<Predictions>? = null

    private var resultsList = listOf<String>()

    private var latitude: Double? = null
    private var longitude: Double? = null
    private var city: String? = null
    private var cityPlaceId: String? = null


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.drop_down_list_item, parent, false)
        }

        val name = view!!.findViewById<TextView>(R.id.text1)
        name.text = resultsList[position]

        if (position == resultsList.lastIndex && isAreaSearch) {
            name.typeface = Typeface.create(name.typeface, Typeface.ITALIC)
        } else {
            name.typeface = Typeface.create(name.typeface, Typeface.NORMAL)
        }

        return view
    }

    override fun getItem(position: Int): String = resultsList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = resultsList.size

    override fun getFilter(): Filter = CustomFilter()

    fun getPlaceId(pos: Int) = predictionList?.get(pos)?.place_id

    fun setSelectedCityData(
        city: String,
        latitude: Double,
        longitude: Double,
        cityPlaceId: String
    ) {
        this.city = city
        this.latitude = latitude
        this.longitude = longitude
        this.cityPlaceId = cityPlaceId
    }

    inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()

            if (constraint != null) {
                context as AppCompatActivity

                val browserKey = sharedPreference.getApiKey(context)
                val results: List<String>

                if (isAreaSearch) {
                    results = getAreaSuggestion(
                        constraint.toString(),
                        browserKey,
                        "$latitude,$longitude"
                    )
                } else {
                    results = getCitySuggestion(
                        constraint.toString(),
                        browserKey
                    )
                }
                filterResults.count = results.size
                filterResults.values = results

            }

            return filterResults

        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            if (results != null && results.count > 0) {
                resultsList = results.values as List<String>
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated()
            }

        }
    }

    private fun getAreaSuggestion(
        query: String,
        browserKey: String, location: String
    ): List<String> {

        val areaList = mutableListOf<String>()

        predictionList = placesRepo.getAreaSuggestions(
            query = query,
            browserKey = browserKey,
            location = location
        )?.toMutableList()

        val anywhereArea = Predictions(
            description = "Anywhere in ${city}",
            place_id = cityPlaceId
        )

        predictionList?.add(anywhereArea)

        predictionList?.forEach {
            areaList.add(it.description.split(",")[0])
        }

        return areaList
    }

    private fun getCitySuggestion(
        query: String,
        browserKey: String
    ): List<String> {

        val cityList = mutableListOf<String>()
        predictionList = placesRepo.getCitySuggestion(
            query,
            browserKey = browserKey,
            types = "(cities)"
        )?.toMutableList()

        predictionList?.forEach {
            cityList.add(it.description)
        }

        return cityList
    }

}