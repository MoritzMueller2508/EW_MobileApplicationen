package com.dhbw.tinf19ai.CoroniReisen

import android.view.View
import com.faskn.lib.PieChart
import com.faskn.lib.Slice

class tst {
    fun main(){

        var slices = ArrayList<Slice>()
        var slice = Slice(3.5F,0,"tst",null,null,null)
        slices.add(slice)

        val pieChart = PieChart(
                slices = slices,
                null,
                0f,
                80f
        ).build()



    }




}