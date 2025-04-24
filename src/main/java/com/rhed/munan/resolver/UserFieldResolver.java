package com.rhed.munan.resolver;

import com.rhed.munan.model.Course;
import com.rhed.munan.model.Purchase;
import com.rhed.munan.model.Comment;
import com.rhed.munan.model.User;
import com.rhed.munan.service.CourseService;
import com.rhed.munan.service.PurchaseService;
import com.rhed.munan.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserFieldResolver {

    private final CourseService courseService;
    private final PurchaseService purchaseService;
    private final CommentService commentService;

    @BatchMapping(typeName = "User", field = "courses")
    public Map<User, List<Course>> getCoursesBatch(List<User> users) {
        List<UUID> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        Map<UUID, List<Course>> coursesByUserId = courseService.getCoursesByCreatorIds(userIds);

        return users.stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> coursesByUserId.getOrDefault(user.getId(), List.of())
                ));
    }

    @SchemaMapping(typeName = "User", field = "purchases")
    public List<Purchase> getPurchases(User user) {
        return purchaseService.getPurchasesByUser(user.getId());
    }

    @SchemaMapping(typeName = "User", field = "comments")
    public List<Comment> getComments(User user) {
        return commentService.getCommentsByUser(user.getId());
    }
}