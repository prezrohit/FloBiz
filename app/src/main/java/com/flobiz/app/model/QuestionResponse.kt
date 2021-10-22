package com.flobiz.app.model

class QuestionResponse(private val items: List<Question>) {
	override fun toString(): String {
		return "QuestionResponse(questionList=$items)"
	}
}

class Question(
	private val tags: List<String>,
	private val owner: Owner,
	private val isAnswered: Boolean,
	private val viewCount: Int,
	private val acceptedAnswerId: Int,
	private val answerCount: Int,
	private val score: Int,
	private val creationDate: String,
	private val questionId: String,
	private val contentLicense: String,
	private val link: String,
	private val title: String

) {
	override fun toString(): String {
		return "Question(tags=$tags, owner=$owner, isAnswered=$isAnswered, viewCount=$viewCount, acceptedAnswerId=$acceptedAnswerId, answerCount=$answerCount, score=$score, creationDate='$creationDate', questionId='$questionId', contentLicense='$contentLicense', link='$link', title='$title')"
	}
}

class Owner(
	private val reputation: Int,
	private val userId: String,
	private val userType: String,
	private val acceptRate: Int,
	private val profileImage: String,
	private val displayName: String,
	private val link: String

) {
	override fun toString(): String {
		return "Owner(reputation=$reputation, userId='$userId', userType='$userType', acceptRate=$acceptRate, profileImage='$profileImage', displayName='$displayName', link='$link')"
	}
}
