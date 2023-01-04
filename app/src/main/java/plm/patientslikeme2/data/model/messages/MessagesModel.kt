package plm.patientslikeme2.data.model.messages

data class MessagesResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: Data? = Data(),
    var meta: Meta? = Meta(),
)

data class Meta(
    var page: Int? = null,
    var per_page: Int? = null,
    var total_entries: Int? = null,
)

data class Data(
    var messages: ArrayList<Messages> = arrayListOf(),
    var last_page: Boolean? = null,
)

data class Sender(
    var id: Int? = null,
    var login: String? = null,
    var first_name: String? = null,
    var last_name: String? = null,
    var avatar_url: String? = null,
    var primary_condition_id: Int? = null,
)

data class Messages(
    var id: Int? = null,
    var body_html: String? = null,
    var sender_id: Int? = null,
    var recipient_id: Int? = null,
    var reply_to_message_id: Int? = null,
    var created_at: String? = null,
    var read: Boolean? = null,
    var is_self_user_message: Boolean? = null,
    var blocked_by_other_participant: Boolean? = null,
    var image_urls: ArrayList<String> = arrayListOf(),
    var body_markdown: String? = null,
    var sender: Sender? = Sender(),
    var preview_text: String? = null,
    var other_participant: Sender? = Sender(),
)

data class MessagesThreadResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: ThreadData? = ThreadData(),
    var meta: Meta? = Meta(),
)

data class ThreadMessages(
    var id: Int? = null,
    var body_html: String? = null,
    var sender_id: Int? = null,
    var recipient_id: Int? = null,
    var created_at: String? = null,
    var read: Boolean? = null,
    var image_urls: ArrayList<String> = arrayListOf(),
    var body_markdown: String? = null,
    var sender: Sender? = Sender(),
    var is_self_user_message: Boolean? = null,
    var reply_to_message_id: String? = null,
    var blocked_by_other_participant: String? = null,
)

data class OtherParticipant(
    var id: Int? = null,
    var login: String? = null,
    var first_name: String? = null,
    var last_name: String? = null,
    var avatar_url: String? = null,
    var primary_condition_id: Int? = null,
    var email_verified: Boolean? = null,
    var follower: Boolean? = null,
    var community_leader: Boolean? = null,
    var blocked_by_other_participant: Boolean? = null,
    var deleted: Boolean? = null,
)

data class ThreadData(
    var subject: String? = null,
    var last_page: Boolean? = null,
    var messages: ArrayList<ThreadMessages> = arrayListOf(),
    var other_participant: OtherParticipant? = OtherParticipant(),
)

data class ReplyMessageRequest(
    var body_html: String? = null,
    var sender_id: String? = null,
    var recipient_id: String? = null,
    var reply_to_message_id: String? = null,
    var image_urls: ArrayList<String> = arrayListOf(),
)

data class NewMessageRequest(
    var body_html: String? = null,
    var sender_id: String? = null,
    var recipient_id: String? = null,
    var image_urls: ArrayList<String> = arrayListOf(),
)

data class MessageUnreadCountResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: UnreadData? = UnreadData(),
)

data class UnreadData(
    var unread_count: Int? = null,
)

data class NewMessageResponse(
    var success: Boolean? = null,
    var message: String? = null,
    var data: SendData? = SendData(),
)

data class SendData(
    var message: Message? = Message(),
)

data class Message(
    var id: Int? = null,
    var subject: String? = null,
    var body_html: String? = null,
    var sender_user_id: Int? = null,
    var recipient_user_id: Int? = null,
    var created_at: String? = null,
    var read: Boolean? = null,
    var recipient_deleted: Boolean? = null,
    var sender_deleted: Boolean? = null,
    var spam: Boolean? = null,
    var replied: Boolean? = null,
    var message_type: String? = null,
    var reply_to_message_id: Int? = null,
    var root_message_id: Int? = null,
    var admin_deleted: Boolean? = null,
    var owner_id: String? = null,
    var recipient_archived: Boolean? = null,
    var bulk_message_tag: String? = null,
    var body_markdown: String? = null,
    var body_burned_ears: ArrayList<String> = arrayListOf(),
    var sent_from_ore: Boolean? = null,
    var archived_hide_reply_button: Boolean? = null,
    var metadata: String? = null,
    var has_primary_action_button: Boolean? = null,
    var first_read_at: String? = null,
    var reply_to_id: String? = null,
    var image_urls: ArrayList<String> = arrayListOf(),
)