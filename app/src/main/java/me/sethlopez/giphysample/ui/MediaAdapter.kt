package me.sethlopez.giphysample.ui

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.giphy.sdk.core.models.Media
import kotlinx.android.synthetic.main.view_holder_media_item.view.*
import me.sethlopez.giphysample.R
import java.util.*

class MediaAdapter : PagedListAdapter<Media, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_holder_media_item -> MediaItemViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.view_holder_media_item -> (holder as MediaItemViewHolder).bind(getItem(position)!!)
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.view_holder_media_item

    companion object {
        @JvmStatic
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean = oldItem == newItem
        }
    }

    class MediaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val gifImageView: ImageView = view.gif
        private val tagsTextView: TextView = view.tags

        fun bind(media: Media) {
            // load the gif into the ImageView
            val colorRes = COLORS[Random().nextInt(COLORS.size)]
            val colorDrawable = ColorDrawable(ContextCompat.getColor(itemView.context, colorRes))
            val image = media.images.fixedWidth
            GlideApp.with(itemView)
                .asGif()
                .placeholder(colorDrawable)
                .override(image.width, image.height)
                .load(image.gifUrl)
                .into(gifImageView)
            // join the tags and set them as the TextView value
            tagsTextView.text = media.tags?.joinToString(separator = " ") { "#$it" }
        }

        companion object {
            private val COLORS = listOf(
                R.color.blue,
                R.color.purple,
                R.color.red,
                R.color.teal,
                R.color.yellow
            )

            fun create(parent: ViewGroup): MediaItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.view_holder_media_item, parent, false)
                return MediaItemViewHolder(view)
            }
        }
    }
}