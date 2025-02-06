package us.smt.mylearningcentre.domen.repository

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.ClubCategoryData
import us.smt.mylearningcentre.data.model.ClubData
import us.smt.mylearningcentre.data.model.ClubDetailsData
import us.smt.mylearningcentre.data.model.CreateUpdateClubData
import us.smt.mylearningcentre.util.ResponseResult

interface ClubRepository {
    fun getClubList(): Flow<ResponseResult<List<ClubData>>>
    fun getClubCategory(): Flow<ResponseResult<List<ClubCategoryData>>>
    fun createClubCategory(name: String): Flow<ResponseResult<Boolean>>
    fun sortClubListWithCategory(categories: List<String>): Flow<ResponseResult<List<ClubData>>>
    fun getClubDetail(id: String): Flow<ResponseResult<ClubDetailsData>>
    fun createClubDetail(data: CreateUpdateClubData): Flow<ResponseResult<Boolean>>
    fun updateClubDetail(data: ClubDetailsData): Flow<ResponseResult<Boolean>>
    fun sendNotificationToMembers(tokens: List<String>,title:String,body:String): Flow<ResponseResult<Boolean>>
}