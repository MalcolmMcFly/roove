package com.mmdev.data.auth

import com.facebook.login.LoginManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mmdev.domain.auth.repository.AuthRepository
import com.mmdev.domain.core.model.User
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

//todo: save user locally

@Singleton
class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth,
                                             private val fbLogin: LoginManager,
                                             private val db: FirebaseFirestore): AuthRepository {

	companion object {
		private const val GENERAL_COLLECTION_REFERENCE = "users"
	}

	/**
	 * Observable which track the auth changes of [FirebaseAuth] to listen when an user is logged or not.
	 *
	 * @param firebaseAuth firebaseAuth instance.
	 * @return an [Observable] which emits every time that the [FirebaseAuth] state change.
	 */
	override fun isAuthenticated(): Observable<Boolean> {
		return Observable.create(ObservableOnSubscribe<Boolean>{ emitter ->
			val authStateListener = FirebaseAuth.AuthStateListener {
				auth -> if (auth.currentUser == null) emitter.onNext(false)
			else emitter.onNext(true)
			}
			auth.addAuthStateListener(authStateListener)
			emitter.setCancellable { auth.removeAuthStateListener(authStateListener) }
		}).subscribeOn(Schedulers.io())
	}

	override fun handleUserExistence(userId: String): Single<User> {
		return Single.create(SingleOnSubscribe<User> { emitter ->
			val ref = db.collection(GENERAL_COLLECTION_REFERENCE).document(userId)
			ref.get().addOnCompleteListener { task ->
				if (task.isSuccessful) {
					val document = task.result
					if (document!!.exists()) {
						val user = document.toObject(User::class.java)
						//saveProfile(applicationContext, mProfileModel)
						//profileViewModel!!.setProfileModel(mProfileModel)
						emitter.onSuccess(user!!)
					}
					else emitter.onError(Exception("User do not exist"))
				}
				else emitter.onError(Exception("task is not successful"))
			}
		}).subscribeOn(Schedulers.io())
	}


	override fun signInWithFacebook(token: String): Single<User> {
		val credential = FacebookAuthProvider.getCredential(token)
		return Single.create(SingleOnSubscribe<User> { emitter ->
			auth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
				if (task.isSuccessful && auth.currentUser != null) {
					val firebaseUser: FirebaseUser = auth.currentUser!!
					val photoUrl: String = firebaseUser.photoUrl.toString()
					val urls = ArrayList<String>()
					urls.add(photoUrl)
					val user = User(name = firebaseUser.displayName!!,
									city = "Kyiv",
									mainPhotoUrl = photoUrl,
									photoURLs = urls,
									userId = firebaseUser.uid)

					emitter.onSuccess(user)
				}
				else emitter.onError(task.exception!!)

			}
		}).subscribeOn(Schedulers.io())
	}

	override fun signUp(user: User): Completable {
		return Completable.create { emitter ->
			val ref = db.collection(GENERAL_COLLECTION_REFERENCE).document(user.userId)
			ref.set(user)
				.addOnSuccessListener { emitter.onComplete() }
				.addOnFailureListener { task -> emitter.onError(task) }
		}.subscribeOn(Schedulers.io())
	}

	private fun saveProfile(user: User) {

	}

	override fun logOut(){
		auth.signOut()
		fbLogin.logOut()
	}


}


