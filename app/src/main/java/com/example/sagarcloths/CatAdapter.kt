package com.example.sagarcloths

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CatAdapter(private val catList:ArrayList<CatModel>):RecyclerView.Adapter<CatAdapter.CatViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view:View =LayoutInflater.from(parent.context).inflate(R.layout.categories_data,parent,false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val currentItem=catList[position]
        holder.cateName.text=currentItem.Name
        Glide.with(holder.cateImg.context).load(currentItem.Purl).into(holder.cateImg)
    }

    override fun getItemCount(): Int {
        return catList.size
    }
    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cateImg: ImageView =itemView.findViewById(R.id.cateImg)
       var cateName: TextView =itemView.findViewById(R.id.cateName)
    }

}




