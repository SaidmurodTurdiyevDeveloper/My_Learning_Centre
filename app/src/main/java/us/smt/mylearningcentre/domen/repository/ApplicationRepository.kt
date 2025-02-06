package us.smt.mylearningcentre.domen.repository

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.ApplicationFormData
import us.smt.mylearningcentre.data.model.CreateApplicationFormData
import us.smt.mylearningcentre.util.ResponseResult

interface ApplicationRepository {
    fun createApplicationToJoinClub(data:CreateApplicationFormData):Flow<ResponseResult<Boolean>>
    fun acceptApplication(application: ApplicationFormData):Flow<ResponseResult<Boolean>>
    fun rejectApplication(application: ApplicationFormData):Flow<ResponseResult<Boolean>>
    fun getClubApplications(clubId:String):Flow<ResponseResult<List<ApplicationFormData>>>
    fun getMyApplication(applicationFormId:String):Flow<ResponseResult<ApplicationFormData>>
}