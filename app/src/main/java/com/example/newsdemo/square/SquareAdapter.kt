package com.example.newsdemo.square

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.newsdemo.main.model.MainArticleEntity
import com.example.newsdemo.databinding.ItemSquareBinding

class SquareAdapter : RecyclerView.Adapter<SquareAdapter.NewsAdapterViewHolder>() {

    inner class NewsAdapterViewHolder(val binding: ItemSquareBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<MainArticleEntity>() {
        override fun areItemsTheSame(oldItem: MainArticleEntity, newItem: MainArticleEntity): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: MainArticleEntity, newItem: MainArticleEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapterViewHolder {
        val binding =
            ItemSquareBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return NewsAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((MainArticleEntity) -> Unit)? = null

    override fun onBindViewHolder(holder: NewsAdapterViewHolder, position: Int) {
        val article = differ.currentList[position]
        with(holder) {

            //设置图片圆角角度
            //设置图片圆角角度
            val roundedCorners = RoundedCorners(16) //px

//通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            val options = RequestOptions().transform(CenterCrop(), roundedCorners)
            Glide.with(itemView.context).load(article.urlToImage).apply(options)
                .into(binding.imageView)


            // Glide.with(itemView.context)
            //     .load(article.urlToImage)
            //     .placeholder(R.drawable.placeholder_image)
            //     .into(binding.imageView)
            binding.textView.text = article.title
        }

        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (MainArticleEntity) -> Unit) {
        onItemClickListener = listener
    }
}