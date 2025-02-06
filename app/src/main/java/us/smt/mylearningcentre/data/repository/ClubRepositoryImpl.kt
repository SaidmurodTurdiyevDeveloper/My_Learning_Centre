package us.smt.mylearningcentre.data.repository

//import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import us.smt.mylearningcentre.data.database.local.room.dao.ClubCategoryDao
import us.smt.mylearningcentre.data.database.local.room.dao.ClubDao
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.data.database.remote.FireBaseHelper
import us.smt.mylearningcentre.data.model.ClubCategoryData
import us.smt.mylearningcentre.data.model.ClubData
import us.smt.mylearningcentre.data.model.ClubDetailsData
import us.smt.mylearningcentre.data.model.CreateUpdateClubData
import us.smt.mylearningcentre.data.model.toClubDataLs
import us.smt.mylearningcentre.domen.repository.ClubRepository
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClubRepositoryImpl @Inject constructor(
    private val localStorage: LocalStorage,
    private val categoryDb: ClubCategoryDao,
    private val clubDB: ClubDao
) : ClubRepository {
    companion object {
        const val name = "name"
        const val category = "category"
        const val presidentId = "presidentId"
        const val description = "description"
        const val membersIdList = "membersIdList"
    }

    override fun getClubList(): Flow<ResponseResult<List<ClubData>>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val result = fireBaseHelper.getAllData(
                collectionName = FireBaseHelper.collectionClub
            )
            val ls = result.map { document ->
                val data = document.data
                ClubDetailsData(
                    id = document.id,
                    name = data?.get(name) as? String ?: "",
                    category = data?.get(category) as? String ?: "",
                    presidentId = data?.get(presidentId) as? String ?: "",
                    description = data?.get(description) as? String ?: "",
                    members = (data?.get(membersIdList) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                )
            }
            clubDB.deleteAll()
            clubDB.insertAll(ls)
            emit(ResponseResult.Success(ls.toClubDataLs()))
        } catch (e: IOException) {
            val localLs = clubDB.getAll()
            emit(ResponseResult.Error(NetworkError.InternetConnection(localLs)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun getClubCategory(): Flow<ResponseResult<List<ClubCategoryData>>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val result = fireBaseHelper.getAllData(
                collectionName = FireBaseHelper.collectionClubCategory
            )
            val ls = result.map { document ->
                val data = document.data
                ClubCategoryData(
                    id = document.id,
                    name = data?.get(name) as? String ?: ""
                )
            }
            categoryDb.deleteAll()
            categoryDb.insertAll(ls)
            emit(ResponseResult.Success(ls))
        } catch (e: IOException) {
            val localLs = categoryDb.getAll()
            emit(ResponseResult.Error(NetworkError.InternetConnection(localLs)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun createClubCategory(name: String): Flow<ResponseResult<Boolean>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val map = mapOf(
                ClubRepositoryImpl.name to name
            )
            val id = firebase.addNewItem(
                mapData = map,
                collectionName = FireBaseHelper.collectionClubCategory
            )
            val newData = ClubCategoryData(
                id = id,
                name = name
            )
            categoryDb.insert(newData)
            emit(ResponseResult.Success(true))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun sortClubListWithCategory(categories: List<String>): Flow<ResponseResult<List<ClubData>>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val result = fireBaseHelper.getAllData(
                collectionName = FireBaseHelper.collectionClub
            )
            val ls = result.map { document ->
                val data = document.data
                ClubDetailsData(
                    id = document.id,
                    name = data?.get(name) as? String ?: "",
                    category = data?.get(category) as? String ?: "",
                    presidentId = data?.get(presidentId) as? String ?: "",
                    description = data?.get(description) as? String ?: "",
                    members = (data?.get(membersIdList) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                )
            }.filter {
                categories.contains(it.category)
            }
            emit(ResponseResult.Success(ls.toClubDataLs()))
        } catch (e: IOException) {
            val localLs = clubDB.getAll().filter {
                categories.contains(it.category)
            }
            emit(ResponseResult.Error(NetworkError.InternetConnection(localLs)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun getClubDetail(id: String): Flow<ResponseResult<ClubDetailsData>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val document = fireBaseHelper.getDatWithId(
                id = id,
                collectionName = FireBaseHelper.collectionClub
            )
            val data = document?.data
            val resultData = ClubDetailsData(
                id = document?.id ?: "",
                name = data?.get(name) as? String ?: "",
                category = data?.get(category) as? String ?: "",
                presidentId = data?.get(presidentId) as? String ?: "",
                description = data?.get(description) as? String ?: "",
                members = (data?.get(membersIdList) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            )
            emit(ResponseResult.Success(resultData))
        } catch (e: IOException) {
            val localData = clubDB.getById(id)
            emit(ResponseResult.Error(NetworkError.InternetConnection(localData)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun createClubDetail(data: CreateUpdateClubData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val map = mapOf(
                name to data.name,
                category to data.category,
                description to data.description,
                presidentId to localStorage.userId,
                membersIdList to listOf(localStorage.userId)
            )
            val id = firebase.addNewItem(
                mapData = map,
                collectionName = FireBaseHelper.collectionClub
            )
            localStorage.clubId = id
            val newData = ClubDetailsData(
                id = id,
                name = data.name,
                description = data.description,
                presidentId = localStorage.userId,
                category = data.category,
                members = listOf(localStorage.userId)
            )
            clubDB.insert(newData)
            emit(ResponseResult.Success(true))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun updateClubDetail(data: ClubDetailsData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val map = mapOf(
                name to data.name,
                category to data.category,
                description to data.description,
                presidentId to data.presidentId,
                membersIdList to data.members
            )
            firebase.updateItem(
                id = data.id,
                newMap = map,
                collectionName = FireBaseHelper.collectionTask
            )
            clubDB.update(data)
            emit(ResponseResult.Success(true))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }
//
//    private fun getAccessToken(context: Context): String {
//        val assetManager = context.assets
//        val inputStream = assetManager.open("service-account.json")
//        val credentials = GoogleCredentials.fromStream(inputStream)
//            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
//
//        credentials.refreshIfExpired()
//        return credentials.accessToken.tokenValue
//    }

    override fun sendNotificationToMembers(
        tokens: List<String>,
        title: String,
        body: String
    ): Flow<ResponseResult<Boolean>> = flow {
//        try {
//            val accessToken = getAccessToken(application) // OAuth 2.0 Token
//
//            val client = OkHttpClient()
//
//            tokens.forEach { token ->
//                val json = JSONObject().apply {
//                    put("message", JSONObject().apply {
//                        put("token", token)  // FCM token
//                        put("notification", JSONObject().apply {
//                            put("title", title)
//                            put("body", body)
//                        })
//                    })
//                }
//
//                val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
//
//                val request = Request.Builder()
//                    .url("https://fcm.googleapis.com/v1/projects/YOUR_PROJECT_ID/messages:send")
//                    .post(requestBody)
//                    .addHeader("Authorization", "Bearer $accessToken")
//                    .addHeader("Content-Type", "application/json")
//                    .build()
//
//                val response = withContext(Dispatchers.IO) {
//                    client.newCall(request).execute()
//                }
//
//                if (!response.isSuccessful) {
//                    throw IOException("Notification send error: ${response.message}")
//                }
//            }
//
//            emit(ResponseResult.Success(data = true)) // Hammasi muvaffaqiyatli bo‘lsa
//        } catch (e: IOException) {
//            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
//        } catch (e: Exception) {
//            emit(ResponseResult.Error(NetworkError.NetworkResult))
//        }
    }

}

