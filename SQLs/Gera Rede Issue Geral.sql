SELECT GROUP_CONCAT(concat(issue_comment.user_id,'(',user.login,')') SEPARATOR '##'), issue.id
                FROM extract_github.issue_issue_comment
                INNER JOIN issue ON issue.id = issue_issue_comment.issue_id
                INNER JOIN issue_comment ON issue_comment.id = issue_issue_comment.issueComments_id
				INNER JOIN project_issue ON issue.id = project_issue.issue_id
                INNER JOIN extract_github.user ON user.id = issue_comment.user_id
                WHERE project_issue.project_id = 1234714
                GROUP BY issue_issue_comment.issue_id
                