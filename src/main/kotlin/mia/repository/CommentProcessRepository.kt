package mia.repository

import mia.entity.CommentProcess
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentProcessRepository:  JpaRepository<CommentProcess, Long> {

    fun findCommentProcessByCommentId(commentId: String): CommentProcess?
    fun existsByCommentId(commentId: String): Boolean

}