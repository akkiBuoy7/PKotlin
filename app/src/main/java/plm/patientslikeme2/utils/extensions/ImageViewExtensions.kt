package plm.patientslikeme2.utils.extensions

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import plm.patientslikeme2.BuildConfig
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.profile.Moderator
import plm.patientslikeme2.data.model.profile.ModeratorAvatar
import plm.patientslikeme2.utils.enum.NotificationImage
import plm.patientslikeme2.utils.usercontrol.AvatarGenerator
import plm.patientslikeme2.utils.usercontrol.HexagonImageView
import plm.patientslikeme2.utils.usercontrol.RoundishImageView

@BindingAdapter("imageUrl")
fun RoundishImageView.loadImage(url: String?) {
    if (url.isNullOrEmpty().not()) {
        Glide.with(this.context).load(url)
            .placeholder((ColorDrawable(ContextCompat.getColor(this.context, R.color.gray_dark_5))))
            .into(this)
    }
}

fun HexagonImageView.loadImage(name: String?, avatar_url: String?) {
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(this.context).load(avatar_url).error(name?.let {
        AvatarGenerator.avatarImage(this.context, 200, 1, it, getProfileColor(this.context))
    }).apply(requestOptions).into(this)
}

fun HexagonImageView.loadImage(avatar_url: String?) {
    Glide.with(this.context)
        .load(avatar_url)
        .error(getProfileColor(this.context))
        .into(this)
}

fun ImageView.loadIconImage(url: String?) {
    var imageUrl = url.toString()
    if (!imageUrl.contains(BuildConfig.BASE_URL)) {
        imageUrl = "${BuildConfig.BASE_URL}$url"
    }
    if (url.isNullOrEmpty().not()) {
        Glide.with(this.context)
            .load(imageUrl)
            .into(this)
    }
}

@BindingAdapter("loadRoundImage")
fun RoundishImageView.loadRoundImage(url: String?) {
    if (url.isNullOrEmpty().not()) {
        Glide.with(this.context)
            .load(url)
            .error(R.drawable.ic_round_image)
            .into(this)
    }
}

@BindingAdapter("loadModeratorImage")
fun HexagonImageView.loadModeratorImage(moderator: Moderator) {
    var imageUrl = ""
    var userInitial = ""
    when (moderator.avatar.type) {
        NotificationImage.avatar_url.toString() -> imageUrl = moderator.avatar.value
        NotificationImage.user_initial.toString() -> userInitial = moderator.avatar.value
    }
    if (imageUrl.isNotEmpty()) {
        Glide.with(this.context)
            .load(imageUrl)
            .into(this)
    } else if (userInitial.isNotEmpty()) {
        Glide.with(this.context)
            .load(
                AvatarGenerator.avatarImage(
                    this.context,
                    200,
                    1,
                    moderator.avatar.value,
                    getProfileColor(this.context)
                )
            )
            .into(this)
    } else {
        Glide.with(this.context)
            .load(R.drawable.ic_tile_blue)
            .into(this)
    }
}

@BindingAdapter("loadAvatarImage")
fun HexagonImageView.loadAvatarImage(moderator: ModeratorAvatar) {
    var imageUrl = ""
    var userInitial = ""
    when (moderator.type) {
        NotificationImage.avatar_url.toString() -> imageUrl = moderator.value
        NotificationImage.user_initial.toString() -> userInitial = moderator.value
    }
    if (imageUrl.isNotEmpty()) {
        Glide.with(this.context)
            .load(imageUrl)
            .into(this)
    } else if (userInitial.isNotEmpty()) {
        Glide.with(this.context)
            .load(
                AvatarGenerator.avatarImage(
                    this.context,
                    200,
                    1,
                    moderator.value,
                    getProfileColor(this.context)
                )
            )
            .into(this)
    } else {
        Glide.with(this.context)
            .load(R.drawable.ic_tile_blue)
            .into(this)
    }
}