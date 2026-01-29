# Blog API — Endpoints Reference

This document lists the project's HTTP endpoints, what they accept and what they return.

Authentication
- POST /auth/register
  - Auth: public
  - Request: JSON `RegisterRequest` ([src/main/java/blog/Dto/RegisterRequest.java](src/main/java/blog/Dto/RegisterRequest.java))
  - Response: 200 OK (empty)

- POST /auth/login
  - Auth: public
  - Request: JSON `LoginRequest` ([src/main/java/blog/Dto/LoginRequest.java](src/main/java/blog/Dto/LoginRequest.java))
  - Response: 200 OK JSON `AuthResponse` (contains JWT/token) ([src/main/java/blog/Dto/AuthResponse.java](src/main/java/blog/Dto/AuthResponse.java))

Posts
- POST /posts
  - Auth: required
  - Request: JSON `PostDto` ([src/main/java/blog/Dto/PostDto.java](src/main/java/blog/Dto/PostDto.java))
    - `content`, `description`, optional `file` (list of base64 strings)
  - Response: 200 OK JSON `PostResponseDto` ([src/main/java/blog/Dto/PostResponseDto.java](src/main/java/blog/Dto/PostResponseDto.java))
  - Notes: Creates a post for the current authenticated user and notifies that user's followers.

- GET /posts
  - Auth: required
  - Request: none
  - Response: 200 OK JSON array of `PostResponseDto`

- GET /posts/subscriptions
  - Auth: required
  - Request: none
  - Response: 200 OK JSON array of `PostResponseDto` — posts from users the current user follows

- GET /posts/{id}
  - Auth: required
  - Request: URL path param `id` (post id)
  - Response: 200 OK JSON `PostResponseDto`

- PUT /posts/{id}
  - Auth: required (must be post owner)
  - Request: `PostDto` (as request part)
  - Response: 200 OK `PostResponseDto`

- DELETE /posts/{id}
  - Auth: required (owner or admin)
  - Response: 200 OK (empty)

- POST /posts/{id}/like
  - Auth: required
  - Request: none
  - Response: 200 OK (empty)
  - Notes: Toggles like; creates a `LIKE` notification for the post owner when liked.

- POST /posts/{id}/comment
  - Auth: required
  - Request: JSON `CommentDto` ([src/main/java/blog/Dto/CommentDto.java](src/main/java/blog/Dto/CommentDto.java))
  - Response: 200 OK `CommentResponseDto` ([src/main/java/blog/Dto/CommentResponseDto.java](src/main/java/blog/Dto/CommentResponseDto.java))
  - Notes: Notifies post owner with a `COMMENT` notification.

- DELETE /posts/{postId}/comment/{commentId}
  - Auth: required (comment owner or admin)
  - Response: 200 OK (empty)

Users
- GET /users/{id}
  - Auth: required
  - Request: URL path param `id` (user id)
  - Response: 200 OK JSON `UserProfile` ([src/main/java/blog/Dto/UserProfile.java](src/main/java/blog/Dto/UserProfile.java)) — includes visible posts and follow state

- GET /users/me
  - Auth: required
  - Response: 200 OK JSON `UserDto` ([src/main/java/blog/Dto/UserDto.java](src/main/java/blog/Dto/UserDto.java))

- PUT /users/me
  - Auth: required
  - Request: JSON `UpdateUserDto` ([src/main/java/blog/Dto/UpdateUserDto.java](src/main/java/blog/Dto/UpdateUserDto.java))
  - Response: 200 OK (empty)

- POST /users/{id}/subscribe
  - Auth: required
  - Request: path param `id` (user to follow)
  - Response: 200 OK (empty)

- DELETE /users/{id}/unsubscribe
  - Auth: required
  - Request: path param `id` (user to unfollow)
  - Response: 200 OK (empty)

Notifications
- GET /notifications
  - Auth: required
  - Response: 200 OK JSON array `NotificationDto` ([src/main/java/blog/Dto/NotificationDto.java](src/main/java/blog/Dto/NotificationDto.java))

- POST /notifications/{id}/read
  - Auth: required
  - Response: 200 OK (empty)

- POST /notifications/{id}/unread
  - Auth: required
  - Response: 200 OK (empty)

- POST /notifications/{id}/delete
  - Auth: required
  - Response: 200 OK (empty)

- POST /notifications/markAllRead
  - Auth: required
  - Response: 200 OK (empty)

Reports
- POST /reports/user
  - Auth: required
  - Request: JSON `ReportRequest` ([src/main/java/blog/Dto/ReportRequest.java](src/main/java/blog/Dto/ReportRequest.java))
  - Response: 200 OK (empty)

- POST /reports/post
  - Auth: required
  - Request: JSON `ReportRequest`
  - Response: 200 OK (empty)

Admin (requires ROLE_ADMIN)
- GET /admin/users
  - Response: 200 OK JSON array `UserDto`

- GET /admin/posts
  - Response: 200 OK JSON array `PostResponseDto`

- POST /admin/posts/{id}/visible
  - Response: 200 OK (empty) — toggle post visibility

- GET /admin/reports
  - Response: 200 OK JSON array `ReportDto` ([src/main/java/blog/Dto/ReportDto.java](src/main/java/blog/Dto/ReportDto.java))

- DELETE /admin/users/{id}
  - Response: 200 OK (empty)

- DELETE /admin/users/{id}/ban
  - Response: 200 OK (empty)

- DELETE /admin/posts/{id}
  - Response: 200 OK (empty)

- DELETE /admin/comment/{id}
  - Response: 200 OK (empty)

- POST /admin/reports/{id}
  - Response: 200 OK (empty) — resolve report

- DELETE /admin/reports/{id}
  - Response: 200 OK (empty)

- GET /admin/analytics
  - Response: 200 OK JSON `AnalyticsDto` ([src/main/java/blog/Dto/AnalyticsDto.java](src/main/java/blog/Dto/AnalyticsDto.java))

Notes
- Authentication: JWT filter applied. `/auth/**` is public; all other endpoints require a valid JWT; `/admin/**` requires `ROLE_ADMIN`. See `src/main/java/blog/Security/SecurityConfig.java`.
- DTOs and model details: Refer to files in `src/main/java/blog/Dto/` and `src/main/java/blog/Model/` for exact field shapes.
- Media: Static media served under `/media/**` and `/avatars/**` (public)

If you want, I can:
- Generate a machine-readable OpenAPI JSON/yml file.
- Add example request/response payloads for key endpoints.
- Add Swagger/OpenAPI integration to expose interactive docs.
