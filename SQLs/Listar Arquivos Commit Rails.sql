SELECT * FROM extract_github.commit_commit_file
INNER JOIN commit ON commit.sha = commit_commit_file.commit_sha
INNER JOIN project_commit ON commit.sha = project_commit.commits_sha
WHERE project_commit.project_id = 8514