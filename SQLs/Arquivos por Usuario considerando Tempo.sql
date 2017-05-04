SELECT GROUP_CONCAT(concat(commit.committer_id,':',DATE_FORMAT(commit.date_commiter_commit,'%Y-%d-%m')) SEPARATOR '##')
                FROM extract_github.commit_commit_file 
                INNER JOIN commit ON commit.sha = commit_commit_file.commit_sha 
                INNER JOIN commit_file ON commit_file.id = commit_commit_file.files_id
				INNER JOIN project_commit ON commit.sha = project_commit.commits_sha
                WHERE project_commit.project_id = 1234714
                GROUP BY commit_file.filename
                