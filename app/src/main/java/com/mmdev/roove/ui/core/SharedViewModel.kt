/*
 * Created by Andrii Kovalchuk
 * Copyright (c) 2020. All rights reserved.
 * Last modified 16.02.20 17:56
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.mmdev.roove.ui.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mmdev.business.cards.MatchedUserItem
import com.mmdev.business.conversations.ConversationItem
import com.mmdev.business.user.UserItem

/**
 * In general, you should strongly prefer passing only the minimal amount of data between destinations.
 * For example, you should pass a key to retrieve an object rather than passing the object itself,
 * as the total space for all saved states is limited on Android.
 * If you need to pass large amounts of data,
 * consider using a ViewModel as described in Share data between fragments.
 *
 * @see https://developer.android.com/guide/navigation/navigation-pass-data
 */

class SharedViewModel: ViewModel() {

	val matchedUserItemSelected: MutableLiveData<MatchedUserItem> = MutableLiveData()

	val userCardSelected: MutableLiveData<UserItem> = MutableLiveData()

	val conversationSelected: MutableLiveData<ConversationItem> = MutableLiveData()


	private val currentUser: MutableLiveData<UserItem> = MutableLiveData()

	fun getCurrentUser() = currentUser

	fun setCurrentUser(userItem: UserItem) {
		currentUser.value = userItem
	}

	fun setUserSelected(userItem: UserItem) {
		userCardSelected.value = userItem
	}

	fun setConversationSelected(conversationItem: ConversationItem) {
		conversationSelected.value = conversationItem
		//if we moved to user profile from conversation
//		if (userCardSelected.value?.userItem?.baseUserInfo?.userId != conversationItem.partner.userId) {
//			Log.wtf("mylogs_SharedViewModel", "card updated")
//			userCardSelected.value = MatchedUserItem(conversationItem.partner,
//			                                         conversationStarted = conversationItem.conversationStarted)
//		}
	}

	fun setMatchedUserItem(matchedUserItem: MatchedUserItem) {
		matchedUserItemSelected.value = matchedUserItem
	}



}