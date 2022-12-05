package hr.algebra.cocktailheaven

import android.app.AlertDialog
import android.content.ContentUris
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

class ItemAdapter(private val context: Context, private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        fun bind(item: Item) {
            Picasso.get()
                .load(R.drawable.cocktails)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvItem.text = item.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = ViewHolder(itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            //startamo activity da otvori detalje itema
            context.startActivity<ItemPagerActivity>(ITEM_POSITION, position)
        }
        holder.itemView.setOnLongClickListener{
            AlertDialog.Builder(context).apply{
                setTitle(R.string.delete)
                setMessage(context.getString(R.string.sure) + " '${items[position].name}'?")
                setIcon(R.drawable.delete)
                setCancelable(true)
                setNegativeButton(R.string.cancel, null)
                setPositiveButton("OK") {_, _ -> deleteItem(position)}
                show()
            }
            true
        }
        holder.bind(items[position])
    }

    private fun deleteItem(position: Int) {
        val item = items[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(COCKTAIL_HEAVEN_PROVIDER_URI, item._id!!),
            null,
            null)
        items.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
}