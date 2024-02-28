package com.waans.mario_world.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waans.mario_world.core.databinding.ItemNewsBinding
import com.waans.mario_world.core.domain.model.News

class NewsAdapter(private val listNews: ArrayList<News>) : RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, title, headline, link, date, image) = listNews[position]
        holder.binding.tvNewsTitle.text = title
        holder.binding.tvNewsDate.text = date
        holder.binding.tvNewsDesc.text = headline
        holder.binding.ivBannerImage.setImageResource(image)
        holder.binding.root.setOnClickListener { onItemClickCallback.onItemClicked(link) }
    }

    override fun getItemCount(): Int = listNews.size

    class ListViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(link: String)
    }
}
