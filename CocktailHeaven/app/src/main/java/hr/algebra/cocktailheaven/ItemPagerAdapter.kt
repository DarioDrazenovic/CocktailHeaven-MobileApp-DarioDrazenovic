package hr.algebra.cocktailheaven

import android.app.AlertDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.cocktailheaven.framework.startActivity
import hr.algebra.cocktailheaven.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class ItemPagerAdapter(private val context: Context, private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val ivRead = itemView.findViewById<ImageView>(R.id.ivRead)
        private val tvId = itemView.findViewById<TextView>(R.id.tvId)
        private val tvName1 = itemView.findViewById<TextView>(R.id.tvName)
        private val tvBreweryType = itemView.findViewById<TextView>(R.id.tvBreweryType)
        private val tvCity = itemView.findViewById<TextView>(R.id.tvCity)
        private val tvState = itemView.findViewById<TextView>(R.id.tvState)
        private val tvPostalCode = itemView.findViewById<TextView>(R.id.tvPostalCode)
        private val tvCountry = itemView.findViewById<TextView>(R.id.tvCountry)
        private val tvCreatedAt = itemView.findViewById<TextView>(R.id.tvCreatedAt)
        private val tvUpdatedAt = itemView.findViewById<TextView>(R.id.tvUpdatedAt)
        fun bind(item: Item) {
            Picasso.get()
                .load(R.drawable.cocktails)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvName1.text = item.name
            tvId.text = item.id
            tvBreweryType.text = item.brewery_type
            tvCity.text = item.city
            tvState.text = item.state
            tvPostalCode.text = item.postal_code
            tvCountry.text = item.country
            tvCreatedAt.text = item.created_at
            tvUpdatedAt.text = item.updated_at
            ivRead.setImageResource(if(item.read) R.drawable.green_flag else R.drawable.red_flag)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = ViewHolder(itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pager, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.ivRead.setOnClickListener{
            item.read = !item.read
            val uri = ContentUris.withAppendedId(COCKTAIL_HEAVEN_PROVIDER_URI, item._id!!)
            val values = ContentValues().apply {
                put(Item::read.name, item.read)
            }
            context.contentResolver.update(
                uri,
                values,
                null,
                null
            )
            notifyItemChanged(position)
        }
        holder.bind(item)
    }

    override fun getItemCount() = items.size
}