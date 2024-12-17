import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import daa.gv.turistenokapp.R

class SliderAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    // Подготовленный массив с бесконечным скроллингом
    private val extendedImages = listOf(images.last()) + images + listOf(images.first())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.imageView.setImageResource(extendedImages[position])
        holder.imageView.clipToOutline = true
    }

    override fun getItemCount(): Int = extendedImages.size

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
