package com.mmdev.meetapp.ui.cards.viewmodel

import androidx.lifecycle.ViewModel
import com.mmdev.domain.cards.usecase.AddToSkippedUseCase
import com.mmdev.domain.cards.usecase.GetPotentialUserCardsUseCase
import com.mmdev.domain.cards.usecase.HandlePossibleMatchUseCase
import com.mmdev.domain.core.model.User

/* Created by A on 20.07.2019.*/

/**
 * get users from firebase firestore
 * getAllUsers -> getSkipedUsers -> getLikedUsers -> getMatchedUsers -> mergeLikedSkipedMatched ->
 * -> create new list from getAllUsersCards list that does not contains mergedLikedSkipedMatched items
 * -> postValue into LiveData variable ... else return null and show loading bar in Fragment class
 * TODO: MAKE ASYNC DATA RETRIEVE AND FETCH DYNAMICALLY
 * TODO: convert likes -> matches on other user side
 */

class CardsViewModel(private val addToSkipped: AddToSkippedUseCase,
                     private val getPotentialUserCards: GetPotentialUserCardsUseCase,
                     private val handlePossibleMatch: HandlePossibleMatchUseCase):
		ViewModel(){

	fun addToSkipped(skippedUser: User) = addToSkipped.execute(skippedUser)
	fun getPotentialUserCards() = getPotentialUserCards.execute()
	fun handlePossibleMatch(likedUser: User) = handlePossibleMatch.execute(likedUser)

}

