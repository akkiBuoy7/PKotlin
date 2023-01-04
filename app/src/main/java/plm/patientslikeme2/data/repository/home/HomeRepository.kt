package plm.patientslikeme2.data.repository.home

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.NetworkResource
import plm.patientslikeme2.data.api.network.Resource
import plm.patientslikeme2.data.di.Environment
import plm.patientslikeme2.data.di.LocalJson
import plm.patientslikeme2.data.di.LocalJson.COMMON
import plm.patientslikeme2.data.di.LocalJson.HOME_CONDITION_CARD
import plm.patientslikeme2.data.di.LocalJson.HOME_GOAL_TRACKING
import plm.patientslikeme2.data.di.LocalJson.HOME_PROMO_CARD
import plm.patientslikeme2.data.di.LocalJson.NOTIFICATION_LIST
import plm.patientslikeme2.data.di.LocalJson.USER_DEVICES
import plm.patientslikeme2.data.di.MyApplication.Companion.APP_MODE
import plm.patientslikeme2.data.model.action.CommonModel
import plm.patientslikeme2.data.model.home.*
import plm.patientslikeme2.data.model.login.UserDevices
import plm.patientslikeme2.data.model.messages.MessageUnreadCountResponse
import plm.patientslikeme2.utils.extensions.getLocalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context
) {

    fun getHomeAnnouncements(): LiveData<Resource<AnnouncementResponse>> {
        return object : NetworkResource<AnnouncementResponse>() {
            override fun createCall(): LiveData<Resource<AnnouncementResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(HOME_PROMO_CARD)
                } else {
                    apiServices.getHomeAnnouncements()
                }
            }
        }.asLiveData()
    }

    fun postDismissAnnouncements(): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(HOME_PROMO_CARD)
                } else {
                    apiServices.postDismissAnnouncements()
                }
            }
        }.asLiveData()
    }

    fun getHomePromoCardList(): LiveData<Resource<PromoCardResponse>> {
        return object : NetworkResource<PromoCardResponse>() {
            override fun createCall(): LiveData<Resource<PromoCardResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(HOME_PROMO_CARD)
                } else {
                    apiServices.getHomePromoCardList()
                }
            }
        }.asLiveData()
    }

    fun postExcludePromoCard(request: RemovePromoCard): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(COMMON)
                } else {
                    apiServices.postExcludePromoCard(request)
                }
            }
        }.asLiveData()
    }

    fun getHomeConditionCard(): LiveData<Resource<ConditionCardResponse>> {
        return object : NetworkResource<ConditionCardResponse>() {
            override fun createCall(): LiveData<Resource<ConditionCardResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(HOME_CONDITION_CARD)
                } else {
                    getLocalResponse(HOME_CONDITION_CARD)
                    //apiServices.getHomeConditionCard()
                }
            }
        }.asLiveData()
    }

    fun getHomeGoalTrackingList(): LiveData<Resource<GoalTrackingResponse>> {
        return object : NetworkResource<GoalTrackingResponse>() {
            override fun createCall(): LiveData<Resource<GoalTrackingResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(HOME_GOAL_TRACKING)
                } else {
                    getLocalResponse(HOME_GOAL_TRACKING)
                    //apiServices.getHomeGoalTrackingList()
                }
            }
        }.asLiveData()
    }

    fun getNotificationList(page: Int?, size: Int?): LiveData<Resource<NotificationResponse>> {
        return object : NetworkResource<NotificationResponse>() {
            override fun createCall(): LiveData<Resource<NotificationResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(NOTIFICATION_LIST)
                } else {
                    apiServices.getNotification(page, size)
                }
            }
        }.asLiveData()
    }

    fun putMarkAsReadNotification(id: Int?): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(COMMON)
                } else {
                    apiServices.putMarkAsReadNotification(id)
                }
            }
        }.asLiveData()
    }

    fun putDismissNotification(id: Int?): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(COMMON)
                } else {
                    apiServices.putDismissNotification(id)
                }
            }
        }.asLiveData()
    }

    fun postUserDevices(request: UserDevices): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(USER_DEVICES)
                } else {
                    apiServices.postUserDevices(request)
                }
            }
        }.asLiveData()
    }

    fun getUnreadConversationCount(): LiveData<Resource<MessageUnreadCountResponse>> {
        return object : NetworkResource<MessageUnreadCountResponse>() {
            override fun createCall(): LiveData<Resource<MessageUnreadCountResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.VIEW_MESSAGE_UNREAD_COUNT)
                } else {
                    apiServices.getUnreadConversationCount()
                }
            }
        }.asLiveData()
    }

    fun getUnreadNotificationCount(): LiveData<Resource<NotificationCountResponse>> {
        return object : NetworkResource<NotificationCountResponse>() {
            override fun createCall(): LiveData<Resource<NotificationCountResponse>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(LocalJson.VIEW_MESSAGE_UNREAD_COUNT)
                } else {
                    apiServices.getUnreadNotificationCount()
                }
            }
        }.asLiveData()
    }

    fun postUserLogout(): LiveData<Resource<CommonModel>> {
        return object : NetworkResource<CommonModel>() {
            override fun createCall(): LiveData<Resource<CommonModel>> {
                return if (APP_MODE == Environment.LOCAL) {
                    getLocalResponse(COMMON)
                } else {
                    apiServices.postUserLogout()
                }
            }
        }.asLiveData()
    }
}