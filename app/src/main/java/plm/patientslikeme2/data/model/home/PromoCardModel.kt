package plm.patientslikeme2.data.model.home

import plm.patientslikeme2.utils.enum.ContextPromoCard

data class PromoCardModel(
    var id: Int,
    var short_description: String,
    var destination_url: String,
    var action_text: String,
    var mobile_theme: String,
    var context: PromoCardContext,
)

data class PromoCardResponse(
    var success: Boolean,
    var message: String,
    var data: PromoCardData
)

data class PromoCardData(
    var promo_cards: ArrayList<PromoCardModel>
)

data class PromoCardContext(
    var name: String,
    var group_id: Int,
    var discussion_id: Int,
    var post_id: Int
)

data class RemovePromoCard(
    var id: Int
)