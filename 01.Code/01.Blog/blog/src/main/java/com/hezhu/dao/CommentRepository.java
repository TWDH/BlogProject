package com.hezhu.dao;

import com.hezhu.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /*根据创建时间，排序*/
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
