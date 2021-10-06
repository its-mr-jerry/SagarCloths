package com.example.sagarcloths


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class CatAdapter(options: FirebaseRecyclerOptions<CatModel>) : FirebaseRecyclerAdapter<CatModel, CatAdapter.CatHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.categories_data,parent,false)
        return CatHolder(view)
    }

    override fun onBindViewHolder(holder: CatHolder, position: Int, model: CatModel) {

        Glide.with(holder.cateImg.context).load(model.Purl).into(holder.cateImg)
        holder.cateName.text=model.Name

    }
    class CatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cateImg:ImageView=itemView.findViewById(R.id.cateImg)
        val cateName:TextView=itemView.findViewById(R.id.cateName)
    }


}


