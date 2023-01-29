package com.ghosh.covid_19_tracker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghosh.covid_19_tracker.databinding.CovidRvItemBinding

class CovidRvAdapter (var context: Context, var covidList: ArrayList<covidRvModel>):
RecyclerView.Adapter<CovidRvAdapter.covidViewHolder>(){

    inner class covidViewHolder(val adapterBinding: CovidRvItemBinding):RecyclerView.ViewHolder(adapterBinding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): covidViewHolder {
        val binding=CovidRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return covidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: covidViewHolder, position: Int) {
        var activity = MainActivity()
        holder.adapterBinding.idCasesRV.text=covidList[position].cases
        holder.adapterBinding.idRecoveredCasesRV.text=covidList[position].recovered
        holder.adapterBinding.idDeathsRV.text=covidList[position].deaths
        holder.adapterBinding.idTvState.text=covidList[position].state

        holder.adapterBinding.idCardViewRV.setOnClickListener{

            val intent= Intent(context,MainActivity::class.java)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return covidList.size
    }
}