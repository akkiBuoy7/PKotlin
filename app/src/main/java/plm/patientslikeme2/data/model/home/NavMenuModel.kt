package plm.patientslikeme2.data.model.home

import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.global.FeatureFlagModel

data class NavMenuModel(
    var icon: Int,
    var title: Int,
    var type: Int
) {

    companion object {

        const val HEADER = 1
        const val EMPTY = 2
        const val SUB_HEADER = 3
        const val LOGOUT = 4

        fun getDrawerNavMenuList(feature: FeatureFlagModel): ArrayList<NavMenuModel> {
            val menuList: ArrayList<NavMenuModel> = arrayListOf()
            if (feature.android_followings) {
                menuList.add(NavMenuModel(R.drawable.ic_menu_following,
                    R.string.nav_menu_following, 1))
            }
            if (feature.android_my_conditions) {
                menuList.add(NavMenuModel(R.drawable.ic_menu_condition, R.string.nav_menu_my_conditions, 1))
            }
            if (feature.android_my_conditions) {
                menuList.add(NavMenuModel(R.drawable.ic_menu_treatment,
                    R.string.nav_menu_my_treatments,
                    1))
            }
            menuList.add(NavMenuModel(R.drawable.ic_menu_profile, R.string.nav_menu_personal_information, 1))
            menuList.add(NavMenuModel(R.drawable.ic_menu_settings, R.string.nav_menu_settings, 1))
            menuList.add(NavMenuModel(R.drawable.ic_menu_invite, R.string.nav_menu_invite_others, 1))
            menuList.add(NavMenuModel(0, 0, 2))
            menuList.add(NavMenuModel(0, R.string.nav_menu_crisis_resources, 3))
            menuList.add(NavMenuModel(0, R.string.nav_menu_my_account, 3))
            menuList.add(NavMenuModel(0, R.string.nav_menu_help_center, 3))
            menuList.add(NavMenuModel(0, R.string.nav_menu_privacy_policy, 3))
            menuList.add(NavMenuModel(0, R.string.nav_menu_terms_conditions, 3))
            menuList.add(NavMenuModel(0, 0, 2))
            menuList.add(NavMenuModel(0, R.string.nav_menu_logout, 4))

            return menuList
        }
    }
}
