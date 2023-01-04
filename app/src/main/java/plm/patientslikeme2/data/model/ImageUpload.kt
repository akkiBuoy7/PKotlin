package plm.patientslikeme2.data.model

import com.google.gson.annotations.SerializedName

data class ImageUpload(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data(),
)

data class Data(
    @SerializedName("uploaded_image") var uploadedImage: UploadedImage? = UploadedImage()
)

data class UploadedImage(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("image_content_type") var imageContentType: String? = null,
    @SerializedName("image_file_name") var imageFileName: String? = null,
    @SerializedName("image_file_size") var imageFileSize: Int? = null,
    @SerializedName("image_updated_at") var imageUpdatedAt: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null
)