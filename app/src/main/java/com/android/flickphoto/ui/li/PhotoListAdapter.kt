package com.android.flickphoto.ui.li

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.flickphoto.R
import com.android.flickphoto.databinding.GridViewItemBinding
import com.android.flickphoto.models.Photo

class PhotoListAdapter (private val displayPhoto:(Photo)->Unit) : ListAdapter<Photo,PhotoListAdapter.PhotoListViewHolder>(PhotoDiffUtil()){


    class PhotoListViewHolder private constructor(private val binding:GridViewItemBinding, val displayPhoto:(Photo)->Unit):RecyclerView.ViewHolder(binding.root){
        companion object{
            fun getInstance(parent:ViewGroup,displayPhoto: (Photo) -> Unit):PhotoListViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val photoItemBinding = DataBindingUtil.inflate<GridViewItemBinding>(inflater, R.layout.grid_view_item,parent,false)
                return PhotoListViewHolder(photoItemBinding,displayPhoto)
            }
        }
        fun bind(photo:Photo){
            binding.photo = photo
            binding.viewHolder = this
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        return PhotoListViewHolder.getInstance(parent,displayPhoto)

    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }


}

class PhotoDiffUtil:DiffUtil.ItemCallback<Photo>(){
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        //data class compares each of the property (inside the model) to see if they are equal
        return oldItem == newItem
    }

}