package com.poa.kga

import android.content.Context

interface Rng {
    fun kp(context: Context, listA: ArrayList<String>, listB: ArrayList<String>)
}

class Rimp : Rng {
    override fun kp(
        context: Context,
        listA: ArrayList<String>,
        listB: ArrayList<String>
    ) {
        DTag.ptGod(context, listA, listB)
    }
}