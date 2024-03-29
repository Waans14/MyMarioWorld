package com.waans.mario_world.core.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.waans.mario_world.core.R
import com.waans.mario_world.core.databinding.ItemListCharacterBinding
import com.waans.mario_world.core.domain.model.Character
import com.waans.mario_world.core.utils.MediaPlayerManager
import com.waans.mario_world.core.utils.Utils.fadeVisibility
import com.waans.mario_world.core.utils.Utils.imageURL


class CharacterDiffCallback(oldCharacterList: List<Character>, newCharacterList: List<Character>) :
    DiffUtil.Callback() {
    private val mOldCharacterList: List<Character>
    private val mNewCharacterList: List<Character>

    init {
        mOldCharacterList = oldCharacterList
        mNewCharacterList = newCharacterList
    }

    override fun getOldListSize(): Int {
        return mOldCharacterList.size
    }

    override fun getNewListSize(): Int {
        return mNewCharacterList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldCharacterList[oldItemPosition].name === mNewCharacterList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCharacter: Character = mOldCharacterList[oldItemPosition]
        val newCharacter: Character = mNewCharacterList[newItemPosition]
        return oldCharacter.name.equals(newCharacter.name) && oldCharacter.isFavorite == newCharacter.isFavorite
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}

class CharacterAdapter(private val context: Context) : RecyclerView.Adapter<CharacterAdapter.ListViewHolder>() {

    private var listData = ArrayList<Character>()
    var onItemClick: ((Character) -> Unit)? = null

    fun setData(newListData: List<Character>?) {
        if (newListData == null) return

        val diffCallback = CharacterDiffCallback(listData, newListData)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)

        listData.clear()
        listData.addAll(newListData)

        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_character, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListCharacterBinding.bind(itemView)

        var isExpand: Boolean = false
        var prevHeight: Int = 0
        val soundManager = MediaPlayerManager(context)

        fun bind(character: Character) {
            with(binding) {
                Glide.with(itemView.context)
                    .load("$imageURL${if (isExpand) character.imageOpen else character.imageClose}")
                    .placeholder(R.drawable.ic_image)
                    .apply(RequestOptions().override(200, 200))
                    .skipMemoryCache(true)
                    .into(ivPhoto)

                tvName.text = character.name
                tvDesc.text = character.description

                cardContainer.setCardBackgroundColor(
                    Color.rgb(character.bgColorR, character.bgColorG, character.bgColorB)
                )

                btnExpand.setOnClickListener {
                    isExpand = !isExpand

                    Glide.with(itemView.context)
                        .load("$imageURL${if (isExpand) character.imageOpen else character.imageClose}")
                        .placeholder(R.drawable.ic_image)
                        .apply(RequestOptions().override(200, 200))
                        .skipMemoryCache(true)
                        .into(ivPhoto)

                    btnExpand.setImageResource(if (isExpand) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)

                    if(prevHeight == 0){ prevHeight =  cardContainer.measuredHeight }

                    val anim = ValueAnimator.ofInt(cardContainer.measuredHeight, if (isExpand) prevHeight + 200 else prevHeight)
                    anim.addUpdateListener { valueAnimator ->
                        val `val` = valueAnimator.animatedValue as Int
                        val layoutParams: ViewGroup.LayoutParams = cardContainer.layoutParams
                        layoutParams.height = `val`
                        cardContainer.layoutParams = layoutParams
                    }
                    anim.duration = 700
                    anim.start()

                    if (isExpand){
                        btnSeeDetail.fadeVisibility(View.VISIBLE, 700)
                        soundManager.startSound(R.raw.slide_down)
                    }else{
                        btnSeeDetail.fadeVisibility(View.GONE, 2000)
                        soundManager.startSound(R.raw.slide_up)
                    }
                }
            }
        }

        init {
            binding.btnSeeDetail.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}