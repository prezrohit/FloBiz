package com.flobiz.app.model

class QuestionResponse(val items: List<Question>) {
	override fun toString(): String {
		return "QuestionResponse(questionList=$items)"
	}
}

class Question(
	val tags: List<String>,
	val owner: Owner,
	val is_answered: Boolean,
	val view_count: Int,
	val accepted_answer_id: Int,
	val answer_count: Int,
	val score: Int,
	val creation_date: String,
	val question_id: String,
	val content_license: String,
	val link: String,
	val title: String

) {
	override fun toString(): String {
		return "Question(tags=$tags, owner=$owner, isAnswered=$is_answered, viewCount=$view_count, acceptedAnswerId=$accepted_answer_id, answerCount=$answer_count, score=$score, creationDate='$creation_date', questionId='$question_id', contentLicense='$content_license', link='$link', title='$title')"
	}
}

class Owner(
	val reputation: Int,
	val user_id: String,
	val user_type: String,
	val accept_rate: Int,
	val profile_image: String,
	val display_name: String,
	val link: String

) {
	override fun toString(): String {
		return "Owner(reputation=$reputation, userId='$user_id', userType='$user_type', acceptRate=$accept_rate, profileImage='$profile_image', displayName='$display_name', link='$link')"
	}
}
