package com.kandyba.mygeneration.presentation.activities

import com.kandyba.mygeneration.models.presentation.calendar.Event

interface FragmentNavigator {

    fun openBottomSheetFragment(event: List<Event>)
    fun showStartAnimation(show: Boolean)
}